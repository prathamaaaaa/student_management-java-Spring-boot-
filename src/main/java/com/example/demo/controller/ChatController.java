package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.ChatMessage;

import java.security.Principal;

@Controller
@RequestMapping("/all")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    // Constructor Injection
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;  // Sends the message to the topic
    }

    // Private Chat

    @MessageMapping("/private-messages")
    public void sendPrivateMessage(@Payload ChatMessage message, Principal principal) {
        String recipient = message.getRecipient();
        System.out.println("msg send private");
        messagingTemplate.convertAndSendToUser(recipient, "/queue/messages", message);
    }
}
