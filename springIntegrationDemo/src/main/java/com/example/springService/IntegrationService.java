package com.example.springService;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class IntegrationService {
	
	/*
	 * @Autowired private JedisConnectionFactory factory;
	 */

	@ServiceActivator(inputChannel = "integration.gateway.channel")
	public void anotherMessage(Message<String> message) throws MessagingException {
		MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
		MessageBuilder.fromMessage(message);
		Message<String> newMessage = MessageBuilder.withPayload("Welcome, " + message.getPayload() + " to Spring Integration").build();
		replyChannel.send(newMessage);
	}
	
	@ServiceActivator(inputChannel = "inputChannel", outputChannel = "redisChannel")
	public Message<?> receiveFromService(Message<?> message){
		System.out.println("Received from service.");
		return message;
	}
	
	/*
	 * @ServiceActivator(inputChannel = "redisChannel") public void
	 * sendMessageToQueue(Message<?> message){ RedisQueueOutboundChannelAdapter
	 * adapter = new RedisQueueOutboundChannelAdapter("Redis-Queue", factory);
	 * adapter.handleMessage(message); }
	 */
	
	@ServiceActivator(inputChannel = "receiverChannel")
	public void receiveMessageFromQueue(Message<?> message){
		System.out.println("Received from redis queue " + message);
	}
}
