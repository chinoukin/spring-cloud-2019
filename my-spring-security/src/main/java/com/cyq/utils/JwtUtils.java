package com.cyq.utils;

import com.cyq.CheckResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * maven依赖
 *
 * <dependency>
 * <groupId>io.jsonwebtoken</groupId>
 * <artifactId>jjwt</artifactId>
 * <version>0.9.0</version>
 * </dependency>
 */

/**
 * @author cyq
 */
public class JwtUtils {

    public static void main(String[] args) throws Exception {
        // 签发jwt(签名算法hmac256)
        System.out.println("签发jwt(签名算法hmac256):");
        String jwt = createJWT("123", "jwt_hmac256", 600);
        System.out.println(jwt);
        // 解析
        SecretKey secretKey = generalKey();
        Claims claims = parseJWT(jwt, secretKey);
        System.out.println(claims);


        // 签发jwt(签名算法SHA256withRSA)
        System.out.println("\n签发jwt(签名算法SHA256withRSA):");
        Map<String, Object> keyMaps = createRSAKey(2048);
        String jwt2 = createJWT2("123", "jwt_hmac256", 600, (RSAPrivateKey) keyMaps.get("privateKey"));
        //String jwt2 = createJWT2("456", "jwt_SHA256withRSA", 60, (RSAPrivateKey) keyMaps.get("privateKey"));
        System.out.println(jwt2);
        // 解析
        Claims claims2 = parseJWT(jwt2, (RSAPublicKey) keyMaps.get("publicKey"));
        System.out.println(claims2);

    }

    /**
     * 签发JWT
     * @param id
     * @param subject 可以是JSON数据 尽可能少
     * @param ttlMillis
     * @return String
     *
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;// HmacSha256

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Shanghai"));
        //Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)   // 主题
                .setIssuer("user")     // 签发者
                .setIssuedAt(Date.from(now.toInstant()))      // 签发时间
                .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            OffsetDateTime expTime = now.plusSeconds(ttlMillis);
            builder.setExpiration(Date.from(expTime.toInstant())); // 过期时间
        }
        return builder.compact();
    }

    /**
     * 签发JWT
     * @param id
     * @param subject 可以是JSON数据 尽可能少
     * @param ttlMillis
     * @return String
     *
     */
    public static String createJWT2(String id, String subject, long ttlMillis, RSAPrivateKey privateKey) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;// SHA256withRSA
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Shanghai"));

        // 设置有关oauth2的jwt claims
        Map<String, Object> map = new HashMap<>();
        map.put("authorities", new String[]{"ROLE_USER", "ROLE_ADMIN"});
        map.put("user_name", "zhangsan");
        map.put("client_id", "client");


        JwtBuilder builder = Jwts.builder()
                .setClaims(map)
                .setId(id)
                .setSubject(subject)   // 主题
                .setIssuer("user")     // 签发者
                .setIssuedAt(Date.from(now.toInstant()))      // 签发时间
                .signWith(signatureAlgorithm, privateKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            OffsetDateTime expTime = now.plusSeconds(ttlMillis);
            builder.setExpiration(Date.from(expTime.toInstant())); // 过期时间
        }
        return builder.compact();
    }

    /**
     * 生成没有加密的密钥对
     * @param keysize
     * @return
     * @throws Exception
     */
    public static Map<String, Object> createRSAKey(int keysize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("rsa");
        keyPairGen.initialize(keysize);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        System.out.println(new String(org.apache.commons.codec.binary.Base64.encodeBase64(publicKey.getEncoded())));
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("publicKey", publicKey);
        keyMap.put("privateKey", privateKey);
        return keyMap;
    }


    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decode("testjwt");
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     *
     * 解析JWT字符串
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt, Key key) throws Exception {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();
    }


    /**
     * 验证JWT
     * @param jwtStr
     * @return
     */
    public static CheckResult validateJWT(String jwtStr, Key key) {
        CheckResult checkResult = new CheckResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr, key);
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            checkResult.setErrCode("JWT_ERRCODE_EXPIRE");
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            e.printStackTrace();
            checkResult.setErrCode("JWT_ERRCODE_FAIL");
            checkResult.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            checkResult.setErrCode("JWT_ERRCODE_FAIL");
            checkResult.setSuccess(false);
        }
        return checkResult;
    }
}
