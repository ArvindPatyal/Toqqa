package com.toqqa.service.impls;

import com.google.firebase.messaging.*;
import com.toqqa.bo.NotificationHistoryBo;
import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.NotificationRoles;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.Device;
import com.toqqa.domain.NotificationHistory;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.dto.NotificationDto;
import com.toqqa.dto.NotificationHistoryDto;
import com.toqqa.dto.PushNotificationRequestDto;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.repository.NotificationRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    public void sendNotificationToCustomer(OrderStatusUpdatePayload orderStatusUpdatePayload, User user) {
        log.info("Invoked :: PushNotificationService :: sendNotificationToCustomer()");
        for (Device deviceObj : deviceService.getAllByUser(user)) {
            sendPushNotificationToToken(bindNotificationObject(Constants.CUSTOMER_NOTIFICATION_TITLE,
                    String.format(Constants.CUSTOMER_NOTIFICATION_MESSAGE, orderStatusUpdatePayload.getOrderStatus()),
                    deviceObj.getToken()));
        }
        this.persistNotification(NotificationDto.builder()
                .title(Constants.CUSTOMER_NOTIFICATION_TITLE)
                .message(String.format(Constants.CUSTOMER_NOTIFICATION_MESSAGE, orderStatusUpdatePayload.getOrderStatus()))
                .topic(Constants.CUSTOMER_NOTIFICATION_TOPIC + " " + orderStatusUpdatePayload.getOrderStatus().name())
                .role(NotificationRoles.CUSTOMER)
                .user(user)
                .build());
    }

    @Async
    public void sendNotificationToCustomerApproval(VerificationStatus verificationStatus) {
        String title = verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED)? Constants.CUSTOMER_APPROVAL_TITLE:Constants.CUSTOMER_DECLINE_TITLE;
        String message = verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED) ?
                "Your Verification Request  for " + verificationStatus.getRole().name() + " is Approved"  :
                "Your Verification Request  for " + verificationStatus.getRole().name() + " is Declined";

        for (Device deviceObj : deviceService.getAllByUser(verificationStatus.getUser())) {
            sendPushNotificationToToken(bindNotificationObject(title,
                    message,
                    deviceObj.getToken()));
        }
        this.persistNotification(NotificationDto.builder()
                .title(title)
                .message(message)
                .topic(Constants.CUSTOMER_APPROVAL_TITLE)
                .role(NotificationRoles.CUSTOMER)
                .user(verificationStatus.getUser())
                .build());
    }


    @Async
    public void orderCancelToSme(User user) {
        for (Device deviceObj : deviceService.getAllByUser(user)) {
            sendPushNotificationToToken(bindNotificationObject(Constants.SELLER_NOTIFICATION_TITLE,
                    Constants.ORDER_CANCELLED,
                    deviceObj.getToken()));
        }
        this.persistNotification(NotificationDto.builder()
                .title(Constants.SELLER_NOTIFICATION_TITLE)
                .message(Constants.ORDER_CANCELLED)
                .topic(Constants.SELLER_NOTIFICATION_TOPIC)
                .role(NotificationRoles.SME)
                .user(user)
                .build());
    }

    @Async
    public void sendNotificationToCustomerForRating(User user) {
        for (Device deviceObj : deviceService.getAllByUser(user)) {
            sendPushNotificationToToken(bindNotificationObject(Constants.RATE_THE_ORDER,
                    "Your order is delivered Rate your order",
                    deviceObj.getToken()));
        }
        this.persistNotification(NotificationDto.builder()
                .title(Constants.RATE_THE_ORDER)
                .message(Constants.RATE_YOUR_ORDER)
                .topic(Constants.RATE_THE_ORDER_TOPIC)
                .role(NotificationRoles.CUSTOMER)
                .user(user)
                .build());
    }

    @Async
    public void sendNotificationToSmeForRating(User user) {
        for (Device deviceObj : deviceService.getAllByUser(user)) {
            sendPushNotificationToToken(bindNotificationObject("New rating received",
                    Constants.RATINGS_ARRIVED,
                    deviceObj.getToken()));
        }
        this.persistNotification(NotificationDto.builder()
                .title("New rating received")
                .message(Constants.RATINGS_ARRIVED)
                .topic(Constants.RATINGS_ARRIVED_TOPIC)
                .role(NotificationRoles.SME)
                .user(user)
                .build());
    }

    @Async
    public void sendNotificationToSmeForOrder(OrderInfoBo orderInfoObj) {
        log.info("Invoked :: PushNotificationService :: sendNotificationToSmeForOrder()");

        Map<String, String> resultMap = new HashMap<>();
        List<Device> deviceTokenList = new ArrayList<>();

        for (OrderItemBo orderItemObj : orderInfoObj.getOrderItemBo()) {
            SmeBo smeObj = orderItemObj.getProduct().getSellerDetails();
            if (resultMap.containsKey(smeObj.getUserId())) {
                String value = resultMap.get(smeObj.getUserId());
                value = value + " and " + orderItemObj.getProduct().getProductName();
                resultMap.put(smeObj.getUserId(), value);
            } else {
                resultMap.put(smeObj.getUserId(), orderItemObj.getProduct().getProductName());
                deviceTokenList = deviceService.getAllByUser(userService.getById(smeObj.getUserId()));
            }
        }
        sendNotficationData(deviceTokenList, resultMap, Constants.SELLER_NOTIFICATION_TITLE, Constants.NEW_ORDER_RECEIVED_TOPIC, Constants.SELLER_NOTIFICATION_MESSAGE);

    }

    @Async
    public void sendNotificationToSmeForProduct(OrderInfoBo orderInfoObj) {
        log.info("Invoked :: PushNotificationService :: sendNotificationToSmeForProduct()");

        Map<String, String> resultMap = new HashMap<>();
        List<Device> deviceTokenList = new ArrayList<>();
        for (OrderItemBo orderItemObj : orderInfoObj.getOrderItemBo()) {
            SmeBo smeObj = orderItemObj.getProduct().getSellerDetails();

            if (orderItemObj.getProduct().getUnitsInStock() <= Constants.PRODUCT_LOW_COUNT) {

                if (resultMap.containsKey(smeObj.getUserId())) {
                    String value = resultMap.get(smeObj.getUserId());
                    value = value + " and " + orderItemObj.getProduct().getProductName();
                    resultMap.put(smeObj.getUserId(), value);
                } else {
                    resultMap.put(smeObj.getUserId(), orderItemObj.getProduct().getProductName());
                    deviceTokenList = deviceService.getAllByUser(userService.getById(smeObj.getUserId()));
                }
                sendNotficationData(deviceTokenList, resultMap, Constants.SELLER__PRODUCT_NOTIFICATION_TITLE, Constants.LOW_STOCK,
                        Constants.SELLER_PRODUCT_NOTIFICATION_MESSAGE + " " + Constants.QUANTITY + " " + orderItemObj.getProduct().getUnitsInStock());
            }
        }
    }

    private void sendNotficationData(List<Device> deviceTokenList, Map<String, String> resultMap, String title, String topic,
                                     String notficationBody) {

        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            deviceTokenList = deviceService.getAllByUser(userService.getById(entry.getKey()));
            for (Device deviceObj : deviceTokenList) {
                sendPushNotificationToToken(bindNotificationObject(title,
                        String.format(notficationBody, entry.getValue()), deviceObj.getToken()));
            }
            this.persistNotification(NotificationDto.builder()
                    .title(title)
                    .message(String.format(notficationBody, entry.getValue()))
                    .topic(topic)
                    .role(NotificationRoles.SME)
                    .user(this.userService.getById(entry.getKey()))
                    .build());
        }
    }

    private PushNotificationRequestDto bindNotificationObject(String title, String messageBody, String token) {
        PushNotificationRequestDto notficationObject = new PushNotificationRequestDto();
        notficationObject.setTitle(title);
        notficationObject.setMessage(String.format(messageBody));
        notficationObject.setToken(token);
        return notficationObject;
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

    private void persistNotification(NotificationDto request) {
        NotificationHistory notificationHistory = new NotificationHistory();
        notificationHistory.setMessage(request.getMessage());
        notificationHistory.setTitle(request.getTitle());
        notificationHistory.setTopic(request.getTopic());
        notificationHistory.setRole(request.getRole());
        notificationHistory.setUser(request.getUser());
        this.notificationRepository.saveAndFlush(notificationHistory);
    }

    public Response notifications(NotificationHistoryDto notificationHistoryDto) {
        User user = this.authenticationService.currentUser();
        return new Response(this.notificationRepository
                .findByUserAndRole(Sort.by(Sort.Direction.DESC, "createdDate"), user, notificationHistoryDto.getNotificationFor())
                .stream().map(NotificationHistoryBo::new), Constants.LIST_OF_NOTIFICATIONS);
    }
}
