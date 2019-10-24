package com.cyq.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * 此类应配置在ResourceServer服务器上
 */
@Configuration
public class ResourceJwtConfig {

    public static final String public_cert = "public.cert";

//    @Autowired
//    @Qualifier("resourceJwtAccessTokenConverter")
//    private JwtAccessTokenConverter resourceJwtAccessTokenConverter;

    @Bean
    @Qualifier("tokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(resourceJwtAccessTokenConverter());
    }

    @Bean
    @Qualifier("resourceJwtAccessTokenConverter")//正常情况下AuthServer和ResouceServer是分开的，这里由于AuthServer也有一个，此处加Qualifier区分一下
    protected JwtAccessTokenConverter resourceJwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource =  new ClassPathResource(public_cert);

        String publicKey;
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        converter.setVerifierKey(publicKey);
        return converter;
    }
}
