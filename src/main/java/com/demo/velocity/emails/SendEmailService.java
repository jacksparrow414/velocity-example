package com.demo.velocity.emails;

import com.demo.velocity.entity.User;
import com.demo.velocity.utils.SendEmailUtil;
import lombok.extern.java.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringWriter;

/**
 * 使用 VelocityEngine 出现 Unable to find resource解决方案
 * https://www.jianshu.com/p/bc2076c4578d
 * https://stackoverflow.com/questions/9051413/unable-to-find-velocity-template-resources
 *
 * 另外一个例子参考{@link ExampleService}
 */
@Service
@Log
public class SendEmailService {
    
    private static final VelocityEngine ve = new VelocityEngine();
    
    @PostConstruct
    public void initVelocity() {
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }
    

    public boolean sendRegisterSuccessEmail() {
        VelocityContext context = new VelocityContext();
        context.put("user", User.builder().name("jack").build());
        Template t = ve.getTemplate(SendEmailUtil.obtainTemplateRealPath("registerSuccess"));
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        log.info(writer.toString());
        return true;
    }
}
