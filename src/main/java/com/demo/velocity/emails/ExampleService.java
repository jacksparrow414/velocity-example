package com.demo.velocity.emails;

import com.demo.velocity.entity.User;
import com.demo.velocity.utils.SendEmailUtil;
import java.io.StringWriter;
import java.nio.charset.Charset;
import javax.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

/**
 * @author jacksparrow414
 * @date 2022/3/2
 * 使用Velocity
 * 另外一个例子参考{@link SendEmailService}
 */
@Log
@Service
public class ExampleService {
    
    @PostConstruct
    public void initVelocity() {
        // 读取resource下的模板文件
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }
    
    public boolean sendEmailByVelocity() {
        VelocityContext context = new VelocityContext();
        context.put("user", User.builder().name("sparrow").build());
        StringWriter writer = new StringWriter();
        Velocity.mergeTemplate(SendEmailUtil.obtainTemplateRealPath("registerSuccess"), Charset.defaultCharset().name(), context, writer);
        log.info(writer.toString());
        return true;
    }
}
