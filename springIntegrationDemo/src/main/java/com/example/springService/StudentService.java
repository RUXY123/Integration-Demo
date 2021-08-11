package com.example.springService;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.example.springModel.Student;

@Component
public class StudentService {

	@ServiceActivator(inputChannel = "intg.student.objectToJson.channel", outputChannel = "intg.student.jsonToObject.channel")
	public Message<?> receiveMessage(Message<?> message) throws MessagingException {
		System.out.println("##############");
		System.out.println(message);
		System.out.println("##############");
		System.out.println("Object to json " + message.getPayload());
		return message;
	}
	
	@ServiceActivator(inputChannel = "intg.student.jsonToObject.fromTransformer.channel", outputChannel = "intg.student.jsonToObject.channel")
	public void processJsonToObject(Message<?> message) throws MessagingException {
		MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
		MessageBuilder.fromMessage(message);
		System.out.println("#############");
		System.out.println("Json to object " + message.getPayload());
		Student student = (Student) message.getPayload();
		Message<?> newMessage = MessageBuilder.withPayload(student.toString()).build();
		replyChannel.send(newMessage);
	}
	
	@ServiceActivator(inputChannel = "student.channel")
	public void receiveStudentMessage(Message<?> message) throws MessagingException {
		System.out.println("######## student.channel ########");
		System.out.println(message);
		System.out.println("################");
		System.out.println(message.getPayload());
	}
	
	@ServiceActivator(inputChannel = "student.channel.1")
	public void receiveStudentMessage1(Message<?> message) throws MessagingException {
		System.out.println("######## student.channel.1 ########");
		System.out.println(message);
		System.out.println("################");
		System.out.println(message.getPayload());
	}
	
	@ServiceActivator(inputChannel = "student.channel.2")
	public void receiveStudentMessage2(Message<?> message) throws MessagingException {
		System.out.println("######## student.channel.2 ########");
		System.out.println(message);
		System.out.println("################");
		System.out.println(message.getPayload());
	}
}
