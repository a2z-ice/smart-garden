package com.mqtt.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class ManagedMQTTClient {
	private static MqttClient client;
	private static ManagedMQTTClient mqttManagedClient;
	private static MqttConnectOptions conOpts;
	private static Object lock = new Object();
	private ManagedMQTTClient() {
		
	}
	
	public static ManagedMQTTClient getMQTTClient(String brokerUrl, String clientId, MqttConnectOptions connectionOpts) throws MqttException, InterruptedException {
		
		if(mqttManagedClient == null && null == client) {
			synchronized (lock) {
				if(mqttManagedClient == null && null == client ) {
						client =  new MqttClient(brokerUrl, clientId);
						conOpts = connectionOpts;
						mqttManagedClient = new ManagedMQTTClient();
					}
				}
			}
		reconnect(client);
		return mqttManagedClient;
		}
		
	
	static private void reconnect(MqttClient client) throws InterruptedException {
		while (!client.isConnected()) {
			System.out.println("Attempting to connect...");
			try {
				client.connect(conOpts);
				System.out.println("Connected!!...");
			} catch (MqttException e) {
				System.out.println("Connection Failed !!! Rretring after 5 seconds");
				Thread.sleep(5000);
			}
		}
	}
	
	public void publish(String topicName, int qos, byte[] payload) throws InterruptedException {

		MqttTopic topic = client.getTopic(topicName);
		MqttMessage message = new MqttMessage(payload);
		message.setQos(qos);
		MqttDeliveryToken token;
		try {
			token = topic.publish(message);
			token.waitForCompletion();			
		} catch (MqttException e) {
			reconnect(client);
		}
	}
	public void disconnect() throws MqttException {
		client.disconnect();
	}
}
