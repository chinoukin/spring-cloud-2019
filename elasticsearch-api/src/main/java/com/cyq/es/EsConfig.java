package com.cyq.es;

import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {


    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public RestHighLevelClient client(){
        RestHighLevelClient client=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.20.181",9200,"http")
                //这里如果要用client去访问其他节点，就添加进去
                )
        );
        return client;
    }

}
