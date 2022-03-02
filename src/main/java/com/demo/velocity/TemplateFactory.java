package com.demo.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Component;

@Component
public class TemplateFactory {

    public Template createTemplate(String templateName) {
        String templateLocation = getClass().getClassLoader().getResource("templates/emails/" + templateName + ".vm").getPath();
        return Velocity.getTemplate(templateLocation);
    }
}
