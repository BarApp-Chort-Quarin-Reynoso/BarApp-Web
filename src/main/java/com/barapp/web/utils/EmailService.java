package com.barapp.web.utils;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:local.properties")
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    public void sendEmail(List<String> to, String templateId, Map<String, String> dynamicTemplateData)
            throws IOException {
        Email from = new Email("barapp@yopmail.com");
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        for (String recipient : to) {
            Email toEmail = new Email(recipient);
            personalization.addTo(toEmail);
        }
        for (Map.Entry<String, String> entry : dynamicTemplateData.entrySet()) {
            personalization.addDynamicTemplateData(entry.getKey(), entry.getValue());
        }
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }
}