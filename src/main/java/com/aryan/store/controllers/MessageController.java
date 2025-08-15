package com.aryan.store.controllers;

import com.aryan.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @RequestMapping("/message")
    public Message message(){
        //return new Message("Hello Aryann how do u do");
        Message message = new Message();
        message.setMessage("Han bete kya haal chaal");
        return message;
    }
}
