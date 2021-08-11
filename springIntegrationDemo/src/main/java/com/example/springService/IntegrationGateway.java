package com.example.springService;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.example.springModel.Student;

@MessagingGateway
public interface IntegrationGateway {

	@Gateway(requestChannel = "integration.gateway.channel")
	public String sendMessage(String message);
	
	@Gateway(requestChannel = "intg.student.gateway.channel")
	public String processStudentDetails(Student student);
	
	@Gateway(requestChannel = "payload.router.channel")
	public <T> void processPayload(T object);
	
	@Gateway(requestChannel = "recipient.router.channel")
	public <T> void processRecipient(T object);
	
	@Gateway(requestChannel = "header.router.channel")
	public <T> void processHeader(T object);
	
	@Gateway(requestChannel = "router.channel")
	public <T> void process(T object);
}
