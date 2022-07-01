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

@Service
public class PushNotificationService {



	public void sendPushNotificationToToken(PushNotificationRequestDto request) {
		
		request.setTitle("");
		request.setMessage("");
		request.setToken("");
		try {
			sendMessageToToken(request);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessageToToken(PushNotificationRequestDto request) throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageToToken(request);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        //String jsonOutput = gson.toJson(message);
		String response = sendAndGetResponse(message);
	}

	private Message getPreconfiguredMessageToToken(PushNotificationRequestDto request) {
		System.out.println("token==" + request.getToken());
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
