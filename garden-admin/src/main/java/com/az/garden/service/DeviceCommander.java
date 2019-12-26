package com.az.garden.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.stereotype.Service;

import com.az.garden.device.MqttSubscribeRetrySample;
import com.az.garden.domain.Device;

@Service
public class DeviceCommander implements DeviceCommandService {
	
	private MqttSubscribeRetrySample mqttClient;
	public DeviceCommander(MqttSubscribeRetrySample mqttClient) {
		this.mqttClient = mqttClient;
	}
	
	@Override
	public void sendCommandToDevice(Device device) {
		if("on".equalsIgnoreCase(device.getCommand())) {
			switchOn();
		} else {
			switchOff();
		}
	}
	private void switchOn() {
			try {
				mqttClient.publish("on");
			} catch (MqttException e) {
				e.printStackTrace();
			}
	}
	
	private void switchOff() {
		try {
			mqttClient.publish("off");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
