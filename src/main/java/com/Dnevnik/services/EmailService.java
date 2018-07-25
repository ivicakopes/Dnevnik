package com.Dnevnik.services;

import com.Dnevnik.Models.EmailObject;

public interface EmailService {
	void sendSimpleMessage (EmailObject object);
	void sendTemplateMessage (EmailObject object) throws Exception;
	void sendMessageWithAttachment (EmailObject object, String pathToAttachment) throws Exception;

}
