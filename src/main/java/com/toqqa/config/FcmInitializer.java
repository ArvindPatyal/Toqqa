package com.toqqa.config;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FcmInitializer {

	@Value("${app.firebase-configuration-file}")
	private String fireBaseConfigPath;

	@PostConstruct
	public void initialize() {
		try {
			FirebaseOptions fireBaseOption = new FirebaseOptions.Builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(fireBaseConfigPath).getInputStream()))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(fireBaseOption);
			}

		} catch (Exception e) {
			log.error("Exception :: FcmInitializer :: initialize()" + e.getMessage());

		}
	}

}
