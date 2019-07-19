package com.cyq.oauth.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Collections;

@RestController
public class OneStepLoginController {
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 登录
     * authorization_code方式
     * 正常在浏览器中访问/oauth/authorize会自动redirect到redirect_url上，这里是前后端分离的Rest登录接口，所以中间的
     * redirect不会触发
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/login2")
    public ResponseEntity<OAuth2AccessToken> login(HttpServletResponse response) throws Exception {
        // 获取授权码
        String username_pass_encode = "Basic " + Base64.getEncoder().encodeToString("admin:1".getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", username_pass_encode);

        //授权请求信息
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("client_id", Collections.singletonList("peapod-web"));
        map.put("response_type", Collections.singletonList("code"));
        map.put("redirect_url", Collections.singletonList("http://localhost:6800/loginSuccess"));

        //HttpEntity
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

        ResponseEntity<Object> responseEntity = restTemplate.exchange("http://localhost:6800/oauth/authorize", HttpMethod.POST, httpEntity, Object.class);

        String location = responseEntity.getHeaders().getLocation().toString();


        String reqParams = location.substring("http://localhost:6800/loginSuccess".length() + 1);
        String code = reqParams.split("=")[1];


        // 根据拿到的授权码获取access_token
        String client_secret = "peapod-web:123456";

        client_secret = "Basic " + Base64.getEncoder().encodeToString(client_secret.getBytes());
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", client_secret);

        //授权请求信息
        map = new LinkedMultiValueMap<>();
        map.put("redirect_url", Collections.singletonList("http://localhost:6800/loginSuccess"));
        map.put("grant_type", Collections.singletonList("authorization_code"));
        map.put("code", Collections.singletonList(code));

        //HttpEntity
        httpEntity = new HttpEntity(map, httpHeaders);
        //获取 Token
        return restTemplate.exchange("http://localhost:6800/oauth/token", HttpMethod.POST, httpEntity, OAuth2AccessToken.class);

    }

}
