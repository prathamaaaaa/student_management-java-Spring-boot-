package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.demo.model.ChatMessage;

import java.security.Principal;

@Controller
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

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload ChatMessage message, Principal principal) {
        String recipient = message.getRecipient();
        messagingTemplate.convertAndSendToUser(recipient, "/queue/messages", message);
    }
}
