package com.mqtt.example;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class SimpleMqttPublisher {
	public static MqttAsyncClient myClient;

	private static MqttConnectOptions conOpts = new MqttConnectOptions();
	private static String topicName = "led_4_cmd";
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
	
	public static void main(String[] args) throws MqttException, InterruptedException {
		String clientId = "desktop-java";
		MqttClient client = new MqttClient(brokerUrl, clientId);
		client.connect(conOpts);
		String message = "off";
		for(int i = 1; i < 1000; i ++) {
			if(i%2==0) {
				message = "ON";
			} else {
				message = "OFF";
			}
			System.out.println("message::" + message + " topic:" + topicName);
			publish(client, topicName, 1, (message).getBytes());
			Thread.sleep(8000);
		}
	    client.disconnect();
		
	}
	
	
	static private void publish(MqttClient client, String topicName, int qos, byte[] payload) throws MqttException {
	     MqttTopic topic = client.getTopic(topicName);
	     MqttMessage message = new MqttMessage(payload);
	     message.setQos(qos);
	     MqttDeliveryToken token = topic.publish(message);
	     token.waitForCompletion();
	 }
	
	 	 
}
