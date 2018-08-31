package pl.oskarpolak.tradex.models.services;

import org.springframework.stereotype.Service;
import pl.smsapi.BasicAuthClient;
import pl.smsapi.OAuthClient;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.StatusResponse;
import pl.smsapi.exception.ClientException;
import pl.smsapi.exception.SmsapiException;

@Service
public class SmsService {

    public boolean sendSms(String number, String content){
        try {
            OAuthClient client = new OAuthClient("7O5nsvMxWu2CV7BwDSKRVz8gKKFdlCCPoyVvrr6H");

            StringBuilder stringBuilder = new StringBuilder();



            SmsFactory smsApi = new SmsFactory(client);
            String phoneNumber = number;
            SMSSend action = smsApi.actionSend()
                    .setText(content)
                    .setTo(phoneNumber);

            StatusResponse result = action.execute();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (SmsapiException e) {
            e.printStackTrace();
        }
        return true;
    }
}
