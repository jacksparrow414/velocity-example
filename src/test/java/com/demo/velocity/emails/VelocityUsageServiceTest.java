package com.demo.velocity.emails;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author jacksparrow414
 * @date 2023/12/27
 */
@SpringBootTest
class VelocityUsageServiceTest {
    
    @Autowired
    private VelocityUsageService velocityUsageService;
    
    @Test
    void velocityEvaluateNoParseAndIncludeDirective() {
        velocityUsageService.velocityEvaluateNoParseAndIncludeDirective();
    }
    
    @Test
    void velocityEvaluateHasParseOrIncludeDirective() {
        velocityUsageService.velocityEvaluateHasParseOrIncludeDirective();
    }
    
    @Test
    void velocityMergeTemplate() {
        velocityUsageService.velocityMergeTemplate();
    }
}