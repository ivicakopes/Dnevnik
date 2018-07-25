package com.Dnevnik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Dnevnik.Models.EmailObject;
import com.Dnevnik.services.EmailService;

@RestController
@RequestMapping(path = "/")
@CrossOrigin("http://localhost:4200")
public class EmailController {
	@Autowired
	private EmailService emailService;

	private static String PATH_TO_ATTACHMENT = "d://temp//slika.jpg";

	@RequestMapping(method = RequestMethod.POST, value = ("/simpleEmail"))
	public String sendSimmpleMessage(@RequestBody EmailObject object) {
		if (object == null || object.getTo() == null || object.getText() == null) {
			return null;
		}
		emailService.sendSimpleMessage(object);
		return "You mail has been sent";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/templateEmail")
	public String sendTemplateMessage(@RequestBody EmailObject object) throws Exception {
		if (object == null || object.getTo() == null || object.getText() == null) {
			return null;
		}
		emailService.sendTemplateMessage(object);
		return "Your mail has been sent!";
	}
	@RequestMapping(method = RequestMethod.POST, value =
			"/emailWithAttachment")
			public String sendMessageWithAttachment(@RequestBody EmailObject
			object) throws Exception {
			if(object==null || object.getTo()==null ||
			object.getText()==null) {
			return null;
			}
			emailService.sendMessageWithAttachment(object,
			PATH_TO_ATTACHMENT);
			return "Your mail has been sent!";
			}

	
}