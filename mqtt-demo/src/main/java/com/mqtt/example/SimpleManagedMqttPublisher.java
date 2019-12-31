package com.mqtt.example;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class SimpleManagedMqttPublisher {
	public static MqttAsyncClient myClient;

	private static MqttConnectOptions conOpts = new MqttConnectOptions();
	private static String topicCmd = "led_4_cmd";
	private static String topicStatus = "led_4_status";
	private static final String host = "localhost";
	private static final int port = 1883;
	private static final String brokerUrl = "tcp://" + host + ":" + port;
	private static final byte[] payload = "this payload was published on MQTT and read using AMQP".getBytes();

	static {
		// provide authentication if the broker needs it
		conOpts.setUserName("guest");
		conOpts.setPassword("guest".toCharArray());
		conOpts.setCleanSession(true);
		conOpts.setKeepAliveInterval(60);
	};

	public static void main(String[] args) throws InterruptedException, MqttException {
		String clientId = "desktop-java";
		ManagedMQTTClient client = ManagedMQTTClient.getMQTTClient(brokerUrl, clientId, conOpts);
		for(int i = 1; i < 1000; i ++) {
			String message = "off";
			if(i%2==0) {
				message = "ON";
			} else {
				message = "OFF";
			}
			System.out.println("message::" + message + " topic:" + topicCmd);
			client.publish(topicCmd, 1, (message).getBytes());
			client.publish(topicStatus, 1, (message).getBytes());
			Thread.sleep(8000);
			} 
		System.out.println("Finished operation...");
		client.disconnect();
		}
}
