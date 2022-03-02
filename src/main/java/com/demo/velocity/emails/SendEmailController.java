package com.demo.velocity.emails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

    @PostMapping("registerSuccess")
    public String sendRegisterSuccessEmail() {
        return sendEmailService.sendRegisterSuccessEmail() ? "success" : "fail";
    }
}
