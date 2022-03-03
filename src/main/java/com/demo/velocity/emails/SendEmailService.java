package com.demo.velocity.emails;

import com.demo.velocity.entity.User;
import com.demo.velocity.utils.SendEmailUtil;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * 使用 VelocityEngine 出现 Unable to find resource解决方案
 * https://www.jianshu.com/p/bc2076c4578d
 * https://stackoverflow.com/questions/9051413/unable-to-find-velocity-template-resources
 *
 * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.email
 * 另外一个例子参考{@link ExampleService}
 */
@Service
@Log
public class SendEmailService {
    
    private static final VelocityEngine ve = new VelocityEngine();

    @Autowired
    private JavaMailSender javaMailSender;
    
    @PostConstruct
    public void initVelocity() {
        ve.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        ve.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }
    

    /**
     * 发送HTML文件格式邮件 使用MimeMessage
     * https://docs.spring.io/spring-framework/docs/5.3.16/reference/html/integration.html#mail-javamail-mime
     * @return
     */
    @SneakyThrows
    public boolean sendRegisterSuccessEmail() {
        VelocityContext context = new VelocityContext();
        context.put("user", User.builder().name("关皓").build());
        Template t = ve.getTemplate(SendEmailUtil.obtainTemplateRealPath("registerSuccess"));
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("jacksparrow414@163.com");
        helper.setTo("jacksparrow414@163.com");
        helper.setSubject("Register Success Email");
        message.setText(writer.toString(), Charset.defaultCharset().name(), "html");
        javaMailSender.send(message);
        return true;
    }
}
