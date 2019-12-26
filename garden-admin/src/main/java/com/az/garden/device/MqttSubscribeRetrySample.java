package com.az.garden.device;


import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.az.garden.domain.Device;

@Component
@Scope("singleton")
public class MqttSubscribeRetrySample implements MqttCallbackExtended {


	String commandTopic = "led_4_cmd";
	String statusTopic = "led_4_status";
	private MqttAsyncClient sampleClient;
	String clientId = "JavaAsyncSample-";
	int qos = 2;

	@Value("${broker.host}") 
	String brokerHost;

	@Value("${broker.mqtt.port}")
	int brokerMqttPort;

	//     String broker = "tcp://localhost:1883";
	MemoryPersistence persistence = new MemoryPersistence();

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	public MqttSubscribeRetrySample() throws MqttException {
		/*
		 * String broker = "tcp://" + brokerHost + ":" + brokerMqttPort; sampleClient =
		 * new MqttAsyncClient(broker, clientId, persistence); MqttConnectOptions
		 * connOpts = new MqttConnectOptions(); connOpts.setUserName("guest");
		 * connOpts.setPassword("guest".toCharArray()); connOpts.setCleanSession(true);
		 * connOpts.setAutomaticReconnect(true); connOpts.setMaxReconnectDelay(5000);
		 * sampleClient.setCallback(this); this.sampleClient.connect(connOpts);
		 */}

	public void connectionLost(Throwable arg0) {
		System.err.println("connection lost");

	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.err.println("delivery complete");
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("topic: " + topic);
		System.out.println("message received: " + new String(message.getPayload()));
		Device device  = new Device();

		device.setCommand(new String(message.getPayload()));
		device.setName(topic);

		messagingTemplate.convertAndSend("/topic/status", device);
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		// TODO Auto-generated method stub
		try {
			//Very important to resubcribe to the topic after the connection was (re-)estabslished. 
			//Otherwise you are reconnected but you don't get any message
			String[] topics = {this.commandTopic,this.statusTopic};
			int [] qosCollection = {qos,qos};
			this.sampleClient.subscribe(topics, qosCollection);
		} catch (MqttException e) {
			e.printStackTrace();
		}		
	}
	
	public void publish(String command) throws MqttPersistenceException, MqttException {
		this.sampleClient.publish(this.commandTopic, command.getBytes(), 0, false);
	}
	
	@PostConstruct
	public void init() throws MqttException {
		String broker = "tcp://" + brokerHost + ":" + brokerMqttPort;
		sampleClient = new MqttAsyncClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setUserName("guest");
		connOpts.setPassword("guest".toCharArray());
		connOpts.setCleanSession(true);
		connOpts.setAutomaticReconnect(true);
		connOpts.setMaxReconnectDelay(5000);
		sampleClient.setCallback(this);
		this.sampleClient.connect(connOpts);		
	}

}