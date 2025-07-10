package llf.llf.common;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtUtil {
    // 密钥必须大于等于32位
// 自动生成安全密钥
   private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+";
    // private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24小时有效期
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build() 
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
