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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Collections;

@RestController
public class LoginController {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 登录
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/user/login")
    public String login(HttpServletResponse response) throws Exception {

        String username_pass_encode = "Basic " + Base64.getEncoder().encodeToString("admin:1".getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", username_pass_encode);

        //授权请求信息
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("client_id", Collections.singletonList("peapod-web"));
        map.put("response_type", Collections.singletonList("code"));
        map.put("redirect_url", Collections.singletonList("http://localhost:6800/user/loginSuccess"));

        //HttpEntity
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

        ResponseEntity<Object> responseEntity = restTemplate.exchange("http://localhost:6800/oauth/authorize", HttpMethod.POST, httpEntity, Object.class);

        String location = responseEntity.getHeaders().getLocation().toString();
        System.out.println(location);
        response.sendRedirect(location);

        // RestController不能使用return "redirect:/user/loginSuccess"
//        String reqParams = location.substring("http://localhost:6800/user/loginSuccess".length());
//        System.out.println(reqParams);
//        return "redirect:/user/loginSuccess";

        return null;
    }

    /**
     * 登录成功
     *
     * @param code 授权码
     * @return
     * @throws Exception
     */
    @RequestMapping("/user/loginSuccess")
    public ResponseEntity<OAuth2AccessToken> loginSuccess(@RequestParam("code") String code) throws Exception {
        String client_secret = "peapod-web:123456";

        client_secret = "Basic " + Base64.getEncoder().encodeToString(client_secret.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", client_secret);

        //授权请求信息
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("redirect_url", Collections.singletonList("http://localhost:6800/user/loginSuccess"));
        map.put("grant_type", Collections.singletonList("authorization_code"));
        map.put("code", Collections.singletonList(code));

        //HttpEntity
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);
        //获取 Token
        return restTemplate.exchange("http://localhost:6800/oauth/token", HttpMethod.POST, httpEntity, OAuth2AccessToken.class);

    }
}
