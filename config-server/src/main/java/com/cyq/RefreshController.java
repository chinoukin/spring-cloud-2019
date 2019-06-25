package com.cyq;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RefreshController {

    // 内网穿透
    // http://y2fcw5.natappfree.cc -> 127.0.0.1:8888
    // webhooks配置此刷新地址
    @RequestMapping("/refresh")
    @RefreshScope
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()){


            httpClient.execute(new HttpPost("http://hss8wt.natappfree.cc/actuator/bus-refresh"));
        }


    }
}
