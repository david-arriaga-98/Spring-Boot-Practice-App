package com.isac.ecommerce.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private static final String SENDGRID_API_KEY = "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";

    public int sendDynamicEmail(String subject,
                                Email to,
                                Map<String, String> dynamicTemplateData,
                                String templateId
    ) throws IOException {
        Email from = new Email("me@david-arriaga.xyz", "FluxCommerce");
        Mail mail = new Mail();
        DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
        personalization.addTo(to);
        mail.setFrom(from);
        mail.setSubject(subject);
        dynamicTemplateData.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);

        SendGrid sendGrid = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        Map<String, String> headers = response.getHeaders();
        String body = response.getBody();
        return response.getStatusCode();
    }

    private static class DynamicTemplatePersonalization extends Personalization {

        @JsonProperty(value = "dynamic_template_data")
        private Map<String, String> dynamicTemplateData;

        @JsonProperty("dynamic_template_data")
        public Map getDynamicTemplateData() {
            if (dynamicTemplateData == null) {
                return Collections.<String, String>emptyMap();
            }
            return dynamicTemplateData;
        }

        public void addDynamicTemplateData(String key, String value) {
            if (dynamicTemplateData == null)
                dynamicTemplateData = new HashMap<>();
            dynamicTemplateData.put(key, value);
        }

    }

}
