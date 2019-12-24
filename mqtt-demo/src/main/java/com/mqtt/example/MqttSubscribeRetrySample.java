package com.mqtt.example;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscribeRetrySample implements MqttCallbackExtended {
	
	
	 String topic = "led_4_cmd";
	 private MqttAsyncClient sampleClient;
	 String clientId = "JavaAsyncSample-";
     int qos = 2;
     String broker = "tcp://localhost:1883";
     MemoryPersistence persistence = new MemoryPersistence();
	 
	public MqttSubscribeRetrySample() throws MqttException {

		sampleClient = new MqttAsyncClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("guest");
	     	connOpts.setPassword("guest".toCharArray());
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        connOpts.setMaxReconnectDelay(5000);
        sampleClient.setCallback(this);
        this.sampleClient.connect(connOpts);
//        Thread.sleep(1000);
//        sampleClient.subscribe(topic, qos);
	}

    public static void main(String[] args) throws MqttException {
    	MqttSubscribeRetrySample s = new MqttSubscribeRetrySample();
    }

    public void connectionLost(Throwable arg0) {
        System.err.println("connection lost");

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.err.println("delivery complete");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        System.out.println("message: " + new String(message.getPayload()));
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
    	// TODO Auto-generated method stub
    	try {
    		//Very important to resubcribe to the topic after the connection was (re-)estabslished. 
    		//Otherwise you are reconnected but you don't get any message
    		this.sampleClient.subscribe(this.topic, qos);
    	} catch (MqttException e) {
    		e.printStackTrace();
    	}		
    }
    
}