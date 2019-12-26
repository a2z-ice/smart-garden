package com.az.garden.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.az.garden.domain.Device;

@Controller
public class GardenController {

	@MessageMapping("/admin.newUser")
	@SendTo("/topic/status")
	public Device newUser(@Payload Device device,SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", device.getName());
		return device;
	}
	
	@MessageMapping("/device.instruction")
	public Device switchOn(@Payload Device device) {
		System.out.println("calling device instruction..");
		
		return device;
	}
	
}
