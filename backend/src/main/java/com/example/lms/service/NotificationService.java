package com.example.lms.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationService {

    @Value("${twilio.accountSid}")
    private String twilioAccountSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    @Value("${sendgrid.apiKey}")
    private String sendGridApiKey;

    public void sendPushNotification(String token, String title, String body) {
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setToken(token)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSmsNotification(String to, String body) {
        Twilio.init(twilioAccountSid, twilioAuthToken);
        MessageCreator messageCreator = com.twilio.rest.api.v2010.account.Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioPhoneNumber),
                body
        );
        messageCreator.create();
    }

    public void sendEmailNotification(String to, String subject, String body) {
        Email from = new Email("no-reply@example.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
