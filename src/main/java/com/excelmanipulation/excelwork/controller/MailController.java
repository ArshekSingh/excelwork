package com.excelmanipulation.excelwork.controller;


import com.excelmanipulation.excelwork.response.Response;
import com.excelmanipulation.excelwork.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MailController {

    @Autowired
    private SendMailService sendMailService;

    @PostMapping("/sendMailWithAttachment")
    public Response sendMailWithAttachment() {
        return sendMailService.sendEmailWithAttachment();
    }
}
