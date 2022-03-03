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
    
    @Autowired
    private ExampleService exampleService;

    @PostMapping("registerSuccess")
    public String sendRegisterSuccessEmail() {
        return sendEmailService.sendRegisterSuccessEmail() ? "success" : "fail";
    }
    
    @PostMapping("example")
    public String sendExampleEmail() {
        return exampleService.sendEmailByVelocity() ? "success" : "fail";
    }

    @PostMapping("orderDetail")
    public String sendOrderDetailEmail() {
        return sendEmailService.sendOrderDetailEmail() ? "success" : "fail";
    }
}
