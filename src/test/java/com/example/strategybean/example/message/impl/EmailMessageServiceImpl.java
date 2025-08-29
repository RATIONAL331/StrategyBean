package com.example.strategybean.example.message.impl;

import org.springframework.stereotype.Service;

import com.example.strategybean.example.message.MessageService;
import com.example.strategybean.example.message.request.MessageRequest;

@Service
public class EmailMessageServiceImpl implements MessageService {
    @Override
    public String send(MessageRequest request) {
        return "Email";
    }
}
