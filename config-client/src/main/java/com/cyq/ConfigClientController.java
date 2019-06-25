package com.cyq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientController {

    @Autowired
    Environment environment;

//    @Value("${foo}")
//    String foo;

    @RequestMapping("/hi")
    public String hi(){
//        System.out.println(foo);
//        System.out.println(environment.getProperty("spring.datasource.url"));
//        System.out.println(environment.getProperty("wbfc.datasource.master.url"));
        return environment.getProperty("foo") + ", "+ environment.getProperty("foo1") + " Welcome !";
    }
}
