package com.example.demo.controller;
//
//import com.example.demo.model.AdminModel;
//import com.example.demo.model.ChatMsg;
//import com.example.demo.repo.AdminRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class chatControllers {
//
//
//	@Autowired
//	private AdminRepository adminRepo;
//	
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public chatControllers(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/private-message")
//    public void sendPrivateMessage(ChatMsg chatMsg) {
//    	
//        System.out.println("Looking for admin with name: " + chatMsg.getReceiver());
//
//AdminModel admin   = adminRepo.findByName(chatMsg.getReceiver());   
//        	System.out.println(admin.getName());
//
//        if (admin != null) {
//            System.out.println("Admin found: " + admin.getEmail());
//
//            messagingTemplate.convertAndSendToUser(admin.getName(), "/queue/messages", chatMsg);
//            messagingTemplate.convertAndSendToUser(chatMsg.getSender(), "/queue/messages", chatMsg); // Acknowledge to sender
//            
//            System.out.println("message sebd ");
//        } else {
//            System.out.println("Admin not found: " + chatMsg.getReceiver());
//        }
//    }
//
//}

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.ChatMsg;

@Controller
public class chatControllers {

    private final SimpMessagingTemplate messagingTemplate;

    public chatControllers(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload ChatMsg message) {
        System.out.println("Message received: " + message.getContent());

        // Send the message to both users involved in the chat
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages", message);
        messagingTemplate.convertAndSendToUser(message.getSender(), "/queue/messages", message);
        System.out.println("messge recirve" + message.getReceiver());
        System.out.println("messge send" + message.getSender());

    }
}
