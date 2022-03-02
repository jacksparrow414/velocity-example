package com.demo.velocity.emails;

import com.demo.velocity.entity.User;
import lombok.extern.java.Log;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.nio.charset.Charset;

@Service
@Log
public class SendEmailService {

    @PostConstruct
    public void initVelocity() {
        Velocity.init();
    }

    public boolean sendRegisterSuccessEmail() {
        VelocityContext context = new VelocityContext();
        context.put("user", User.builder().name("jack").build());
        StringWriter w = new StringWriter();
        Velocity.mergeTemplate(obtainRealTemplatePath("registerSuccess"), Charset.defaultCharset().name(), context, w);
        log.info(w.toString());
        return true;
    }

    private String obtainRealTemplatePath(String templateName) {
        return getClass().getClassLoader().getResource("templates/emails/" + templateName + ".vm").getPath();
    }
}
