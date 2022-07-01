package com.toqqa.service.impls;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.toqqa.dto.PushNotificationRequestDto;
import com.toqqa.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushNotificationService {

	public PushNotificationRequestDto bindNotificationObject(String orderNo, String body, String token) {
		log.info("Invoked :: PushNotificationService :: bindNotificationObject()");

		// temporarily hardcoded till the time tokens are not saved in DB.
		token = "eHaKM0EiQNGxtOTrS6HnZE:APA91bEaEP64Lm0NKrumlestf8JcWS77HyFU_cdivHAn2g3mH-CHm4C1a3F6UY51bAbFcaYLe_4BM2huDBAhrdLd9Bnk02nJV7GdasCob0qKJFvU4QUMyMYEhtFqOXs72zxR5LumM-He";
		PushNotificationRequestDto notficationObject = new PushNotificationRequestDto();
		notficationObject.setTitle(Constants.CUSTOMER_NOTIFICAYION_TITLE);
		notficationObject.setMessage(String.format(Constants.CUSTOMER_NOTIFICAYION_MESSAGE, orderNo, body));
		notficationObject.setToken(token);
		return notficationObject;
	}

	public void sendPushNotificationToToken(PushNotificationRequestDto request) {
		log.info("Invoked :: PushNotificationService :: sendPushNotificationToToken() :: request" + request);

		try {
			String response = sendMessageToToken(request);
			log.info("Response :: PushNotificationService :: sendPushNotificationToToken() ::" + response);


		} catch (Exception e) {
			log.error("Exception in :: PushNotificationService :: sendPushNotificationToToken()"
					+ e.getLocalizedMessage());
		}
	}

	public String sendMessageToToken(PushNotificationRequestDto request) throws InterruptedException, ExecutionException {
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
