package com.since.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.since.entity.Account;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: token生成工具类
 * @author: SY_zheng
 * @create: 2019-05-05
 */
public class JwtUtils {
    /**
     * 私钥
     */
    private static final String SECRET = "JKKLJOoasdlfjbc!~!#";

    /**
     * token过期时间为10天
     */
    private static final int CALENDAR_FIELD = Calendar.DATE;
    private static final int CALENDAR_INTERVAL = 10;


    /**
     * 生成token令牌，可根据实际进行修改，符合自己业务
     * @param account
     * @return
     */
    public static String createToken(Account account){
        try {
            Date date = new Date();
            // 设置过期时间
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
            Date expiresDate = nowTime.getTime();

            Map<String, Object> map = new HashMap<>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");

            // 设置token
            String token = JWT.create()
                    // header
                    .withHeader(map)
                    // payload
                    .withClaim("iss", "Service")
                    .withClaim("aud", "web")
                    // id
                    .withClaim("accountId", null == account.getAccountId() ? null : account.getAccountId().toString())
                    // 账号
                    .withClaim("Account", null == account.getAccount()? null: account.getAccount())
                    .withClaim("authority", null == account.getAuthority()? null: account.getAuthority().toString())
                    // 当前时间
                    .withIssuedAt(date)
                    // 过期时间
                    .withExpiresAt(expiresDate)
                    .sign(Algorithm.HMAC256(SECRET));
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密Token
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
        return jwt.getClaims();

    }

    /**
     * 根据Token获取user_id
     * @param token
     * @return user_id
     */
    public static Long getUserId(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim userIdClaim = claims.get("accountId");
        return Long.valueOf(userIdClaim.asString());
    }

    /**
     * 根据token获取account
     * @param token
     * @return
     */
    public static String getAccount(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim accountClaim = claims.get("Account");
        return accountClaim.asString();
    }
}