package com.example.springConfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.filter.MessageFilter;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.HeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
import org.springframework.messaging.Message;

import com.example.springModel.Address;
import com.example.springModel.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfig {
	// @EnableIntegration Can be omitted, Use only if there are multiple configuration files. Helps in identification.
	/*
	 * @Bean public MessageChannel receiverChannel() { return new DirectChannel(); }
	 * 
	 * @Bean public MessageChannel replyChannel() { return new DirectChannel(); }
	 */
	
	@Bean
	@Transformer(inputChannel = "intg.student.gateway.channel", outputChannel = "intg.student.toConvertObject.channel")
	public HeaderEnricher enrichHeader() {
		Map<String, HeaderValueMessageProcessor<String>> headersToAdd = new HashMap<>();
		headersToAdd.put("header1", new StaticHeaderValueMessageProcessor<String>("Test Header 1"));
		headersToAdd.put("header2", new StaticHeaderValueMessageProcessor<String>("Test Header 2"));
		
		HeaderEnricher enricher = new HeaderEnricher(headersToAdd);
		return enricher;
	}
	
	@Bean
	@Transformer(inputChannel = "intg.student.toConvertObject.channel", outputChannel = "intg.student.objectToJson.channel")
	public ObjectToJsonTransformer objectToJsonTransofrmer() {
		return new ObjectToJsonTransformer(getMapper());
	}
	
	@Bean
	public Jackson2JsonObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		return new Jackson2JsonObjectMapper(mapper);
	}
	
	@Bean
	@Transformer(inputChannel = "intg.student.jsonToObject.channel", outputChannel = "intg.student.jsonToObject.fromTransformer.channel")  
	public JsonToObjectTransformer jsonToObjectTransofrmer() {
		return new JsonToObjectTransformer(Object.class);
	}
	
	@ServiceActivator(inputChannel="payload.router.channel")
	@Bean
	public PayloadTypeRouter router1() {
		//For routing based on payload
		PayloadTypeRouter router = new PayloadTypeRouter();
		router.setChannelMapping(Student.class.getName(), "student.channel");
		router.setChannelMapping(Address.class.getName(), "address.channel");
		return router;
	}
	
	@ServiceActivator(inputChannel="recipient.router.channel")
	@Bean
	public RecipientListRouter router2() {
		//For parallel routing
		RecipientListRouter router = new RecipientListRouter();
		router.addRecipient("student.channel.1");
		router.addRecipient("student.channel.2");
		return router;
	}
	
	@ServiceActivator(inputChannel="header.router.channel")
	@Bean
	public HeaderValueRouter router3() {
		//For routing based on header
		//Redirect from payload router to enricher. Where add 2 headers and then to router3 method.
		HeaderValueRouter router = new HeaderValueRouter("testHeader");
		router.setChannelMapping("student", "student.channel");
		router.setChannelMapping("address", "address.channel");
		return router;
	}
	
	@Bean
	public DirectChannel receiverChannel() {
		return new DirectChannel();
	}
	
	@Filter(inputChannel = "router.channel")
	@Bean
	public MessageFilter filter() {
		MessageFilter filter = new MessageFilter(new MessageSelector() {
			@Override
			public boolean accept(Message<?> message) {
				return message.getPayload() instanceof Student;
			}
		});
		filter.setOutputChannelName("student.channel");
		return filter;
	}
	
	/*
	 * @Bean public JedisConnectionFactory jedisConnectionFactory() {
	 * JedisConnectionFactory factory = new JedisConnectionFactory(); return
	 * factory; }
	 * 
	 * @Bean public RedisQueueMessageDrivenEndpoint consumerEndPoint() {
	 * RedisQueueMessageDrivenEndpoint endPoint = new
	 * RedisQueueMessageDrivenEndpoint("Redis-Queue", jedisConnectionFactory());
	 * endPoint.setOutputChannelName("receiverChannel"); return endPoint; }
	 */
	
	
}
