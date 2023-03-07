package com.tuchuan.face_recognition_acs.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JWTUtils {
    private static long expire = 604800;
    private static String sercet = "TuChuanTuChuanTuChuanTuChuanTuChuan";

    public static String generateToken(String username)
    {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * expire);
        return Jwts.builder()
                .setHeaderParam("type","JWT")
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512,sercet)
                .compact();
    }

    public static Claims getClaimsBytoken(String token)
    {
        return Jwts.parser()
                .setSigningKey(sercet)
                .parseClaimsJws(token)
                .getBody();
    }
}
