package com.example.strategybean.example.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.strategybean.example.ExampleStrategyBeanTestConfig;
import com.example.strategybean.example.message.request.EmailMessageRequest;
import com.example.strategybean.example.message.request.KtbMessageRequest;
import com.example.strategybean.example.message.request.NoOpMessageRequest;
import com.example.strategybean.example.message.request.PushMessageRequest;
import com.example.strategybean.example.message.request.RcsMessageRequest;
import com.example.strategybean.example.message.request.SmsMessageRequest;
import com.example.strategybean.exception.UnSupportedStrategyKeyException;

@SpringBootTest(classes = ExampleStrategyBeanTestConfig.class)
class MessageServiceStrategyBeanIntegrationTest {
    @Autowired
    private MessageService messageService;

    @Test
    void sendMessage() {
        Assertions.assertThat(messageService.send(new EmailMessageRequest())).isEqualTo("Email");
        Assertions.assertThat(messageService.send(new KtbMessageRequest())).isEqualTo("KTB");
        Assertions.assertThat(messageService.send(new SmsMessageRequest())).isEqualTo("SMS");
        Assertions.assertThat(messageService.send(new RcsMessageRequest())).isEqualTo("RCS");
        Assertions.assertThat(messageService.send(new PushMessageRequest())).isEqualTo("Push");
    }

    @Test
    void sendMessageNotMapped() {
        Assertions.assertThatThrownBy(() ->
            messageService.send(new NoOpMessageRequest()))
            .isInstanceOf(UnSupportedStrategyKeyException.class)
            .hasMessageContaining("No target bean found for key [NoOpMessageRequest]");
    }
}
