package com.toqqa.service.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.toqqa.domain.Device;
import com.toqqa.domain.User;
import com.toqqa.repository.DeviceRepository;

@Service
public class DeviceService {
	
	
	private DeviceRepository deviceRepository;
	
	public DeviceService(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}
	
	public Optional<Device> getByTokenAndUser(String token, User user) {
		return deviceRepository.findByTokenAndUser(token,user);
	}
	
	public List<Device> getAllByUser(User user) {
		return deviceRepository.findAllByUser(user);
	} 
		
	public void addDevice(User userObj, String deviceToken) {
		if(!getByTokenAndUser(deviceToken,userObj).isPresent()) {
		Device deviceObj = new Device(); 
		deviceObj.setToken(deviceToken);
		deviceObj.setUser(userObj);
		deviceRepository.saveAndFlush(deviceObj);
		}
		
	}
	
	

}
