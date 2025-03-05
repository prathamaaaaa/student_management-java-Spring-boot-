package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.ChatMsg;

//import com.example.demo.model.AdminModel;
//import com.example.demo.model.ChatMsg;
//import com.example.demo.repo.AdminRepository;
//
//@Controller
//public class chatControllers {
//	
//	@Autowired
//	private AdminRepository adminRepository;
//	
//    @MessageMapping("/chat")
//    @SendToUser("/topic/messages")
//    public ChatMsg sendMessage(@Payload ChatMsg chatMessage) {
//    	AdminModel admin = adminRepository.findByName(chatMessage.getReceiver());    	
//			
//    		return chatMessage;
//		
//    }
//    
//}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Controller
@RequestMapping("/all")
public class chatControllers {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMsg chatMessage) {
        String sender = chatMessage.getSender();
        String receiver = chatMessage.getReceiver();
        String[] sorted = {sender, receiver};
        Arrays.sort(sorted);
        String topic = "/topic/chat/" + sorted[0] + "-" + sorted[1];
        messagingTemplate.convertAndSend(topic, chatMessage);
    }
    
}