package com.cyq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Collection;

@Configuration
@EnableAuthorizationServer
@Order(6)
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String secret = "{bcrypt}" + bCryptPasswordEncoder.encode("123");

        //添加客户端信息
        clients.inMemory()                  // 使用in-memory存储客户端信息
                .withClient("client")       // client_id
                .secret(secret)                   // client_secret
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")     // 该client允许的授权类型
                .scopes("app")                     // 允许的授权范围
                .redirectUris("http://www.baidu.com")
                .autoApprove(true)
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(60);

        // 只有authorization_code、和password方式获得的token才支持refresh_token
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        // jwtAccessTokenConverter
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("test-jwt.jks"), "test123".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("test-jwt"));

        return converter;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        JwtAccessTokenConverter converter = jwtAccessTokenConverter();
        TokenStore tokenStore = new JwtTokenStore(converter);
        endpoints.tokenStore(tokenStore).tokenEnhancer(converter).authenticationManager(authenticationManager);

        // 设置UserDetailsService,否则refrsh_token无法使用
        endpoints.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public String getPassword() {
                        if (s.equals("zhangsan")) {
                            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                            String pass = bCryptPasswordEncoder.encode("123");
                            return pass;
                        }
                        return null;
                    }

                    @Override
                    public String getUsername() {
                        if (s.equals("zhangsan")) {
                            System.out.println(s);
                            return "zhangsan";
                        }
                        return null;
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
            }
        });

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
}
