package com.demo.velocity.emails;

import com.demo.velocity.entity.Order;
import com.demo.velocity.utils.SendEmailUtil;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.java.Log;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

/**
 * 两种方式：https://velocity.apache.org/engine/devel/developer-guide.html#using-velocity
 * 一种是直接使用字符串 + velocityEngine.evaluate
 * 另外一种是使用模板文件 + velocityEngine.mergeTemplate， 这种方式需要在初始化的时候配置resourceLoader
 *
 * velocity配置： https://velocity.apache.org/engine/devel/configuration.html#configuring-velocity
 * 默认配置文件位置：org/apache/velocity/runtime/defaults/velocity.properties
 * Any values specified before init() time will replace the default values.
 * Therefore, you only have to configure velocity with the values for the keys that you need to change,
 * and not worry about the rest
 * @author jacksparrow414
 * @date 2023/12/27
 */
@Log
@Component
public class VelocityUsageService {
    
    /**
     * 字符串中含有#parse或#include指令，需要配置resourceLoader
     * @return
     */
    public String velocityEvaluateHasParseOrIncludeDirective() {
        StringWriter writer = new StringWriter();
        String template = "<html>\n" + "## 这里要写resources下的全路径\n" + "    #parse(\"templates/emails/header.vm\")\n" + "<body>\n" + "Hi, $order.customerName<br>\n" + "## 使用正式语法\n" +
            "您在${order.paymentTime}时完成的订单详情如下\n" + "<table border=\"1\">\n" + "    <tr>\n" + "        <th>宝贝</th>\n" + "    </tr>\n" + "    #set($temList = $order.items)\n" +
            "    #foreach($item in $temList)\n" + "        <tr>\n" + "            <td>$item</td>\n" + "        </tr>\n" + "    #end\n" + "</table>\n" + "<br>\n" + "合计金额为：$order.paymentAmount<br>\n" +
            "配送方式为：$order.deliveryMethod\n" + "</body>\n" + "</html>";
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "class");
        velocityEngine.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
        velocityEngine.setProperty("resource.loader.class.cache", true);
        VelocityContext velocityContext = new VelocityContext();
        Order order = new Order();
        order.setCustomerName("jack");
        List<String> items = Arrays.asList("猪肉", "牛肉", "鱼肉");
        order.setItems(items);
        order.setPaymentAmount(BigDecimal.valueOf(78.365));
        order.setPaymentTime(LocalDateTime.now());
        order.setDeliveryMethod("顺丰");
        velocityContext.put("order", order);
        velocityContext.put("header", "OrderDetail");
//        velocityContext.put("name", "sparrow");
        velocityEngine.evaluate(velocityContext, writer, UUID.randomUUID().toString(), template);
        log.info(writer.toString());
        return writer.toString();
    }
    
    public String velocityEvaluateNoParseAndIncludeDirective() {
        StringWriter writer = new StringWriter();
        String template = "hello $name";
        VelocityEngine velocityEngine = new VelocityEngine();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", "sparrow");
        velocityEngine.evaluate(velocityContext, writer, UUID.randomUUID().toString(), template);
        log.info(writer.toString());
        return writer.toString();
    }
    
    /**
     * https://velocity.apache.org/engine/devel/developer-guide.html#resource-loaders
     * 通过velocityEngine.mergeTemplate或者template.merge 的方式，需要在初始化的时候配置ResourceLoader.
     * 如果.vm文件中含有#include或#parse指令，也需要配置ResourceLoader
     *
     * because the resource management system will also handle non-template reasources, specifically things that are loaded via the #include() directive
     */
    public String velocityMergeTemplate() {
        StringWriter writer = new StringWriter();
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "class");
        velocityEngine.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
        velocityEngine.setProperty("resource.loader.class.cache", true);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", "sparrow");
        // 方式一
//        Template template = velocityEngine.getTemplate(SendEmailUtil.obtainTemplateRealPath("registerSuccess"));
//        template.merge(velocityContext, writer);
        // 方式二
        velocityEngine.mergeTemplate(SendEmailUtil.obtainTemplateRealPath("registerSuccess"), Charset.defaultCharset().name(), velocityContext, writer);
        log.info(writer.toString());
        return writer.toString();
    }
}
