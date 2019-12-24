package com.mqtt.example;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

// override 10s limit
public class MyConnOpts extends MqttConnectOptions {
    private int keepAliveInterval = 60;
    @Override
    public void setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }
    @Override
    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }
}