package com.demo.velocity.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * @author jacksparrow414
 * @date 2022/3/2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendEmailUtil {
    
    public static String obtainTemplateRealPath(@NonNull String templateName) {
        return "templates/emails/" + templateName + ".vm";
    }
}
