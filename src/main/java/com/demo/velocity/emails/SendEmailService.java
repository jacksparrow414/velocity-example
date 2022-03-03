package com.demo.velocity.emails;

import com.demo.velocity.entity.Order;
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
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    /**
     * https://velocity.apache.org/engine/2.3/configuration.html#resource-management
     *
     * https://velocity.apache.org/engine/2.3/configuration.html#configuration-examples
     * 注意最后一段话
     * Node that the three names 'file', 'class', and 'jar' are merely for your convenience and sanity.
     * They can be anything you want - they are just used to associate a set of properties together.
     * However, it is recommended that you use names that give some hint of the function
     *
     * 说明可以将file,class,jar的位置替换成其他，所以下面的写法是resource.loader.classpath.class，这里使用classpath
     */
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
        context.put("user", User.builder().name("jack").build());
        Template t = ve.getTemplate(SendEmailUtil.obtainTemplateRealPath("registerSuccess"));
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        javaMailSender.send(buildMessage("Register Success Email", writer.toString()));
        return true;
    }

    public boolean sendOrderDetailEmail() {
        VelocityContext context = new VelocityContext();
        Order order = new Order();
        order.setCustomerName("jack");
        List<String> items = Arrays.asList("猪肉", "牛肉", "鱼肉");
        order.setItems(items);
        order.setPaymentAmount(BigDecimal.valueOf(78.365));
        order.setPaymentTime(LocalDateTime.now());
        order.setDeliveryMethod("顺丰");
        context.put("order", order);
        context.put("header", "OrderDetail");
        Template template = ve.getTemplate(SendEmailUtil.obtainTemplateRealPath("orderDetail"));
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        javaMailSender.send(buildMessage("OrderDetail Email", writer.toString()));
        return true;
    }

    @SneakyThrows
    private MimeMessage buildMessage(String subject, String emailContent) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("jacksparrow414@163.com");
        helper.setTo("jacksparrow414@163.com");
        helper.setSubject(subject);
        message.setText(emailContent, Charset.defaultCharset().name(), "html");
        return message;
    }
}
