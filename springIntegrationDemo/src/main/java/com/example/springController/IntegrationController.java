package com.example.springController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springModel.Address;
import com.example.springModel.Student;
import com.example.springService.IntegrationGateway;

@RestController
@RequestMapping("/integrate")
public class IntegrationController {

	@Autowired
	private IntegrationGateway integrationGateway;
	
	@GetMapping("/identity/{name}")
	public String getMessageFromIntegrationService(@PathVariable("name") String name) {
		return integrationGateway.sendMessage(name);
	}
	
	@PostMapping("/studentService")
	public String getMessageFromStudentService(@RequestBody Student student) {
		return integrationGateway.processStudentDetails(student);
	}
	
	@PostMapping("/student")
	public void processStudent(@RequestBody Student student) {
		integrationGateway.processPayload(student);
	}
	
	@PostMapping("/address")
	public void processAddress(@RequestBody Address address) {
		integrationGateway.processPayload(address);
	}
	
	@PostMapping("/student1")
	public void processStudent1(@RequestBody Student student) {
		integrationGateway.processRecipient(student);
	}
	
	@PostMapping("/studentFilter")
	public void processStudentFilter(@RequestBody Student student) {
		integrationGateway.process(student);
	}
	
	@PostMapping("/addressFilter")
	public void processAddressFilter(@RequestBody Address address) {
		integrationGateway.process(address);
	}
}
