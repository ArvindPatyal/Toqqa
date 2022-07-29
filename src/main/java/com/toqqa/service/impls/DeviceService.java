package com.toqqa.service.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.toqqa.domain.Device;
import com.toqqa.domain.User;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.repository.DeviceRepository;
import com.toqqa.util.Constants;

@Service
public class DeviceService {

	private DeviceRepository deviceRepository;

	public DeviceService(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public Optional<Device> getByTokenAndUser(String token, User user) {
		return deviceRepository.findByTokenAndUser(token, user);
	}
	
	public Optional<Device> getByToken(String token) {
		return deviceRepository.findByToken(token);
	}

	public List<Device> getAllByUser(User user) {
		return deviceRepository.findAllByUser(user);
	}

	public void addDevice(User userObj, String deviceToken) {
		if (!getByTokenAndUser(deviceToken, userObj).isPresent()) {
			Device deviceObj = new Device();
			deviceObj.setToken(deviceToken);
			deviceObj.setUser(userObj);
			deviceRepository.saveAndFlush(deviceObj);
		}

	}
	
	public void deleteDevice(String deviceToken) {
		Optional<Device> deviceObj = getByToken(deviceToken);
		if (deviceObj.isPresent()) {
			deviceRepository.delete(deviceObj.get());
		}else {
    		throw new ResourceNotFoundException(Constants.MSG_NO_RESOURCE);
		}

	}
}
