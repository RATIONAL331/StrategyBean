package com.example.strategybean.example.message;

import com.example.strategybean.annotation.StrategyBean;
import com.example.strategybean.annotation.StrategyKey;
import com.example.strategybean.example.message.impl.EmailMessageServiceImpl;
import com.example.strategybean.example.message.impl.KTBMessageServiceImpl;
import com.example.strategybean.example.message.impl.PushMessageServiceImpl;
import com.example.strategybean.example.message.impl.RCSMessageServiceImpl;
import com.example.strategybean.example.message.impl.SMSMessageServiceImpl;
import com.example.strategybean.example.message.request.MessageRequest;

@StrategyBean(
    value = {
        @StrategyBean.Mapping(mappingKey = "SmsMessageRequest", targetClass = SMSMessageServiceImpl.class),
        @StrategyBean.Mapping(mappingKey = "EmailMessageRequest", targetClass = EmailMessageServiceImpl.class),
        @StrategyBean.Mapping(mappingKey = "RcsMessageRequest", targetClass = RCSMessageServiceImpl.class),
        @StrategyBean.Mapping(mappingKey = "KtbMessageRequest", targetClass = KTBMessageServiceImpl.class),
        @StrategyBean.Mapping(mappingKey = "PushMessageRequest", targetClass = PushMessageServiceImpl.class),
    }
)
public interface MessageService {
    String send(@StrategyKey("#root.class.simpleName") MessageRequest request);
}
