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
		} else if ("reset".equalsIgnoreCase(device.getCommand())) {
			switchReset();
		} else {
			switchOff();
		}
	}
	private void switchOn() {
			try {
				mqttClient.publish("ON");
			} catch (MqttException e) {
				e.printStackTrace();
			}
	}
	
	private void switchOff() {
		try {
			mqttClient.publish("OFF");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	private void switchReset() {
		try {
			mqttClient.publish("RESET");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
