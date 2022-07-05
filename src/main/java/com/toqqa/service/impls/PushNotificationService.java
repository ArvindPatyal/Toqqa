package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Device;
import com.toqqa.domain.User;
import com.toqqa.dto.PushNotificationRequestDto;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.service.UserService;
import com.toqqa.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushNotificationService {

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private UserService userService;
	
	@Async
	public void sendNotificationToCustomer(OrderStatusUpdatePayload orderStatusUpdatePayload, User user) {
		log.info("Invoked :: PushNotificationService :: sendNotificationToCustomer()");

		for (Device deviceObj : deviceService.getAllByUser(user)) {
			sendPushNotificationToToken(bindNotificationObject(Constants.CUSTOMER_NOTIFICATION_TITLE,
					String.format(Constants.CUSTOMER_NOTIFICATION_MESSAGE, orderStatusUpdatePayload.getOrderStatus()),
					deviceObj.getToken()));
		}
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
		sendNotficationData(deviceTokenList, resultMap, Constants.SELLER_NOTIFICATION_TITLE, Constants.SELLER_NOTIFICATION_MESSAGE);
	}

	@Async
	public void sendNotificationToSmeForProduct(OrderInfoBo orderInfoObj) {
		log.info("Invoked :: PushNotificationService :: sendNotificationToSmeForProduct()");

		Map<String, String> resultMap = new HashMap<>();
		List<Device> deviceTokenList = new ArrayList<>();

		for (OrderItemBo orderItemObj : orderInfoObj.getOrderItemBo()) {
			SmeBo smeObj = orderItemObj.getProduct().getSellerDetails();
			if (orderItemObj.getProduct().getUnitsInStock() < Constants.PRODUCT_LOW_COUNT) {

				if (resultMap.containsKey(smeObj.getUserId())) {
					String value = resultMap.get(smeObj.getUserId());
					value = value + " and " + orderItemObj.getProduct().getProductName();
					resultMap.put(smeObj.getUserId(), value);
				} else {
					resultMap.put(smeObj.getUserId(), orderItemObj.getProduct().getProductName());
					deviceTokenList = deviceService.getAllByUser(userService.getById(smeObj.getUserId()));
				}
			}
		}
		sendNotficationData(deviceTokenList, resultMap, Constants.SELLER__PRODUCT_NOTIFICATION_TITLE, Constants.SELLER_PRODUCT_NOTIFICATION_MESSAGE);
	}

	private void sendNotficationData(List<Device> deviceTokenList, Map<String, String> resultMap, String title,
			String notficationBody) {

		for (Map.Entry<String, String> entry : resultMap.entrySet()) {
			deviceTokenList = deviceService.getAllByUser(userService.getById(entry.getKey()));
			for (Device deviceObj : deviceTokenList) {
				sendPushNotificationToToken(bindNotificationObject(title,
						String.format(notficationBody, entry.getValue()), deviceObj.getToken()));
			}

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

}
