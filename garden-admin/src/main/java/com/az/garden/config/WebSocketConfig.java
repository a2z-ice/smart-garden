package com.az.garden.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Value("${broker.stomp.port}") 
	int stompBorkerPort;
	
	@Value("${broker.host}") 
	String brokerHost;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/garden").withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		 registry.setApplicationDestinationPrefixes("/app");
		 
		 registry.enableStompBrokerRelay("/topic")
		 .setRelayPort(stompBorkerPort)
		 .setRelayHost(brokerHost)
		 .setClientLogin("guest")
		 .setClientPasscode("guest");
	}

}
