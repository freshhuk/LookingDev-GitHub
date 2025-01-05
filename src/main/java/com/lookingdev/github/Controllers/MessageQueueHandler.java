package com.lookingdev.github.Controllers;

import com.lookingdev.github.Domain.Enums.QueueAction;
import com.lookingdev.github.Domain.Models.MessageStatus;
import com.lookingdev.github.Services.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueHandler {

    private final MessageService messageService;

    @Autowired
    public MessageQueueHandler(MessageService messageService){
        this.messageService = messageService;
    }

    @RabbitListener(queues = "APIStatusQueue")
    public void listenAPIStatus(@Payload MessageStatus messageStatus){
        if(messageStatus.getAction().equals(QueueAction.INIT_DB)){
            messageService.initDatabase();
        } else if(messageStatus.getAction().equals(QueueAction.GET_GIT_DEV)){
            messageService.getGitHubUsers(messageStatus);
        }
    }
}
