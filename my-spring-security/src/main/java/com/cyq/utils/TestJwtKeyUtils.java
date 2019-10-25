package com.cyq.utils;

import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class TestJwtKeyUtils {

    public static void main(String[] args) throws Exception {
        // test-jwt.jks文件密钥
        String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDpvUhlWYkzpHRjSo" +
                "se8wTifRx72BOhYlwpTMeITG5os6vpuRiT+U4F4vs2WQ0tfuitpTh6P1Vn+idVKV3ExV7tF5VKiTE6iWWUW" +
                "jU4DX8MibGmc2cKWoN+0dy2JJWEuUU1iH/Q8WuFp9FoMsZDtFZAoIE8/FeND5CxpN5zfHfehBYQ2yD95gh0DikX" +
                "taevmumAf2pcTcFnPD3cbNpAFqAyn9m/xIIz2O8StxptGcAX3trjAxSTVdO771ReToDSt3stE0LPA5utV2nrB" +
                "23tG956JFsec105PfIZm4nhLWvPPp7arGEP+oQ9lxHQcEeck8zQAZLai1r+y31shzhjxve9AgMBAAECggEAAIc" +
                "g5rmxCaDRcnf0oHlEBY7qxYzTXoXuV1mJCdHCXhIpzTlRyOPDPVgp4RGKahPjSqEY443JD5cNB7A0OgGIGVj4SVbN+" +
                "afdxtv9SscdBlY5RHr2PJu/uEWZDgA/JjGU6m4Ie9xPtdmPK4ER2Y1KtJKoOA8J05O5a+1HY1NgoewBVemi34PqZQS" +
                "F7JF30YWcxTfbAQpRv9lG9QkKoLoYqvyVAMWY40CkLkhvjLpSvG7tnh/rv5izexcPIloDUiS3BnCRxGxIjKD6AVDzpl" +
                "zQ4WeV606sBZBAKMBowGUd9yLUqvAMDzlHjNKwUdKiYr0QWScjnyZeWFWX5mOwvOK1AQKBgQD1RVvd2IjDDOayB+ZMvhJ" +
                "S58Dee2AIa3D1AkzQAJ/pmeinRuvp+Tq4BZMHIYMxLZ6uCX+sU4rBrWmwTk12DOZzcCOnz8LH1NzuXWEZKQuTSvg69yfefF" +
                "AyEXSCvw9MT+ibUhZeL8HcYGVY7reOvgXoivtYAkak3oB4yFGgaQI8zQKBgQDz9soC4kU182eQ2YKUCrv5e/xup0nDEjwN" +
                "gC515EPePJuKE3vHMQ3l4o+bqTae51843cVbPwQElIHLIsbMO2BOY7D6JTg6DTC8xs4scDsd1GuQxWhU0Tbg5JMhav" +
                "5dVHIHZMFCaEidTvF5ugR4OXPLrbHc8IBlV8A8BchmjQamsQKBgQC6FlyD7bSdoB0r99hgY1lNs23enuaWnMW/7nXO3pp1" +
                "RwOAXsvA4goN8pTBtUhvOHiK/MP0QBInreVhEZetuE3kSbGTsYrLyJGy/AAeccjJgPji7ABAyjqiMiIezMzTjlEh/" +
                "t7fqQZBqJVcKwnzb/LVoxTdiiSFFBePDBT6SpGbBQKBgFeYVcIhUi5j3dyxbGQRUprqfKGSaTakVnGWg5gKxJ+H7WTv" +
                "R/MmVtIiveNrJ1R4yMTXQ+RWRPAMDmpMa0qrSxH6U3INaRnW4Jf3XdGw3lkAizqo1qQVlnm7OGS4UOpbxrGG6KkaXn" +
                "A1BxjoxHASWNRZ4lHs/Hzmu/wRK3o+ypORAoGACeG/NSNtH38A+dhtIBNgHjddptsKDoIITN+GebdtZmSyXMfgFGJ" +
                "qDpJ+99txkly1YB1Nja/ZKA4Ks2PKkwUlr7sZPLS+2CVGhFgLwEbDbkO9POEbD8U4/LyysDPbdyaq1PuP0IB+IPDj" +
                "b+9c8ix4Xa3Aj24spfMessEZZrJAXcM=";
        byte[] decoded0 = Base64.decodeBase64(privateKeyStr);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded0));

        String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6b1IZVmJM6R0Y0qLHvME" +
                "4n0ce9gToWJcKUzHiExuaLOr6bkYk/lOBeL7NlkNLX7oraU4ej9VZ/onVSldxMVe" +
                "7ReVSokxOolllFo1OA1/DImxpnNnClqDftHctiSVhLlFNYh/0PFrhafRaDLGQ7RW" +
                "QKCBPPxXjQ+QsaTec3x33oQWENsg/eYIdA4pF7Wnr5rpgH9qXE3BZzw93GzaQBag" +
                "Mp/Zv8SCM9jvErcabRnAF97a4wMUk1XTu+9UXk6A0rd7LRNCzwObrVdp6wdt7Rve" +
                "eiRbHnNdOT3yGZuJ4S1rzz6e2qxhD/qEPZcR0HBHnJPM0AGS2ota/st9bIc4Y8b3" +
                "vQIDAQAB";
        byte[] decoded = Base64.decodeBase64(publicKeyStr);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));


        // 此jwt可用于实际开发需要
        String jwt = JwtUtils.createJWT2("3001", "My jwt use for devlop", 30*24*60*60, priKey);
        Claims claims = JwtUtils.parseJWT(jwt, pubKey);
        System.out.println(jwt);
        System.out.println(claims);
    }
}
