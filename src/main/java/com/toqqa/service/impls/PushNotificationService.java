package com.toqqa.service.impls;

import com.google.firebase.messaging.*;
import com.toqqa.bo.NotificationHistoryBo;
import com.toqqa.constants.NotificationRoles;
import com.toqqa.constants.OrderStatus;
import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.NotificationHistory;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.dto.NotificationHistoryDto;
import com.toqqa.dto.PushNotificationRequestDto;
import com.toqqa.payload.Response;
import com.toqqa.repository.NotificationRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import com.toqqa.util.NotificationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class PushNotificationService {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private Helper helper;

    public void approvalNotification(VerificationStatus verificationStatus, User user) {
        String role = verificationStatus.getRole() == RoleConstants.SME ? "SELLER" : verificationStatus.getRole().name();
        String title = String.format(NotificationConstants.VERIFICATION_REQUEST_APPROVED_TITLE, role);
        String message = verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED) ?
                String.format(NotificationConstants.VERIFICATION_REQUEST_ACCEPT_MESSAGE, role, role) :
                NotificationConstants.VERIFICATION_REQUEST_DECLINE_MESSAGE;
        this.sendNotification(NotificationConstants.VERIFICATION_REQUEST_APPROVED_TOPIC, title,
                message, NotificationRoles.CUSTOMER, user);
    }

    public void ratingReceivedNotification(String message, User user) {
        this.sendNotification(NotificationConstants.RATING_NOTIFICATION_TOPIC,
                NotificationConstants.RATING_NOTIFICATION_TITLE,
                message,
                NotificationRoles.SME, user);
    }

    public void lowProductStockNotification(OrderInfo orderInfo) {
        orderInfo.getOrderItems().forEach(orderItem -> {
            if (orderItem.getProduct().getUnitsInStock() <= NotificationConstants.PRODUCT_LOW_COUNT) {
                this.sendNotification(NotificationConstants.PRODUCT_NOTIFICATION_TOPIC,
                        String.format(NotificationConstants.LOW_STOCK_NOTIFICATION_TITLE, orderItem.getProduct().getProductName()),
                        String.format(NotificationConstants.LOW_STOCK_NOTIFICATION_MESSAGE, orderItem.getProduct().getProductName(), orderItem.getProduct().getUnitsInStock()),
                        NotificationRoles.SME, this.userService.getById(orderInfo.getSme().getUserId()));
            }
        });
    }

    public void orderNotification(OrderInfo orderInfo, User user) {
        if (orderInfo.getOrderStatus() != OrderStatus.PLACED) {
            this.sendNotification(NotificationConstants.ORDER_NOTIFICATION_TOPIC,
                    orderInfo.getOrderTransactionId() + " " + orderInfo.getOrderStatus().getValue(),
                    NotificationConstants.NEW_ORDER_PLACED,
                    NotificationRoles.SME, user);
        } else if (orderInfo.getOrderStatus() != OrderStatus.CANCELLED) {
            this.sendNotification(NotificationConstants.ORDER_NOTIFICATION_TOPIC,
                    orderInfo.getOrderTransactionId() + " " + orderInfo.getOrderStatus().getValue(),
                    NotificationConstants.ORDER_CANCEL_NOTIFICATION,
                    NotificationRoles.SME, user);
        } else if (orderInfo.getOrderStatus() != OrderStatus.DELIVERED) {
            this.sendNotification(NotificationConstants.ORDER_NOTIFICATION_TOPIC,
                    orderInfo.getOrderTransactionId() + " " + orderInfo.getOrderStatus().getValue(),
                    String.format(NotificationConstants.ORDER_NOTIFICATION_MESSAGE, orderInfo.getOrderStatus().getValue()),
                    NotificationRoles.CUSTOMER, user);
        } else {
            this.sendNotification(NotificationConstants.RATING_NOTIFICATION_TOPIC,
                    orderInfo.getOrderTransactionId() + " " + orderInfo.getOrderStatus().getValue(),
                    NotificationConstants.ORDER_DELIVERED_MESSAGE,
                    NotificationRoles.CUSTOMER, user);
        }
    }

    @Async
    private void sendNotification(String topic, String title, String message, NotificationRoles role, User user) {
        this.deviceService.getAllByUser(user).forEach(device ->
        {
            sendPushNotificationToToken(new PushNotificationRequestDto(title, message, topic, device.getToken()));
            this.persistNotification(title, message, topic, role, user);
        });

    }

    private void persistNotification(String title, String message, String topic, NotificationRoles role, User user) {
        NotificationHistory notificationHistory = new NotificationHistory();
        notificationHistory.setMessage(message);
        notificationHistory.setTitle(title);
        notificationHistory.setTopic(topic);
        notificationHistory.setRole(role);
        notificationHistory.setUser(user);
        this.notificationRepository.saveAndFlush(notificationHistory);
    }


    private void sendPushNotificationToToken(PushNotificationRequestDto request) {
        log.info("Invoked :: PushNotificationService :: sendPushNotificationToToken() :: request:: " + request);

        try {
            String response = sendMessageToToken(request);
            log.info("Response :: PushNotificationService :: sendPushNotificationToToken() ::" + response);

        } catch (Exception e) {
            log.error("Exception in :: PushNotificationService :: sendPushNotificationToToken()"
                    + e.getLocalizedMessage());
        }
    }

    private String sendMessageToToken(PushNotificationRequestDto request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(request);
        return sendAndGetResponse(message);
    }

    private Message getPreconfiguredMessageToToken(PushNotificationRequestDto request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequestDto request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
                .setNotification(new Notification(request.getTitle(), request.getMessage()));
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder().setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }


    public Response notifications(NotificationHistoryDto notificationHistoryDto) {
        User user = this.authenticationService.currentUser();
        return new Response(this.notificationRepository
                .findByUserAndRole(Sort.by(Sort.Direction.DESC, "createdDate"), user, notificationHistoryDto.getNotificationFor())
                .stream().map(NotificationHistoryBo::new), Constants.LIST_OF_NOTIFICATIONS);
    }
}
