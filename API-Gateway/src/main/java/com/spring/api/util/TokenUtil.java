package com.spring.api.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.api.enumeration.MemberRole;
import com.spring.api.enumeration.TokenType;

@Component("tokenUtil")
public class TokenUtil {
	final private String SECRET;
	final private String ISSUER;
	
	TokenUtil(@Value("${token.secret}") String SECRET, @Value("${token.issuer}") String ISSUER){
		this.SECRET = SECRET;
		this.ISSUER = ISSUER;
	}
	
	public JWTVerifier verify(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT
        		.require(Algorithm.HMAC512(SECRET))
                .withIssuer(ISSUER)
                .build();
        verifier.verify(token);
        
        return verifier;
    }
	
	public boolean isAccessTokenWithDecoding(String token) {
		String tokenType = JWT.decode(token).getClaim("token-type").asString();

        if (tokenType.equals(TokenType.MEMBER_ACCESS_TOKEN.getTokenType())) {
            return true;
        }
        
        return false;
	}
	
	public boolean isAccessTokenWithVerification(String token) {
		Algorithm algorithm = Algorithm.HMAC512(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        String tokenType = jwt.getClaim("token-type").asString();
        
        if (tokenType.equals(TokenType.MEMBER_ACCESS_TOKEN.getTokenType())) {
            return true;
        }
        
        return false;
	}
	
	public boolean isRefreshTokenWithDecoding(String token) {
		String tokenType = JWT.decode(token).getClaim("token-type").asString();

        if (tokenType.equals(TokenType.MEMBER_REFRESH_TOKEN.getTokenType())) {
            return true;
        }
        
        return false;
	}
	
	public boolean isRefreshTokenWithVerification(String token) {
		Algorithm algorithm = Algorithm.HMAC512(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        String tokenType = jwt.getClaim("token-type").asString();
        
        if (tokenType.equals(TokenType.MEMBER_REFRESH_TOKEN.getTokenType())) {
            return true;
        }
        
        return false;
	}
	
	public boolean isExpiredWithDecoding(String token) {
		Date expirationDate = JWT.decode(token).getExpiresAt();
        return expirationDate.before(new Date());
	}
	
	public boolean isExpiredTokenWithVerification(String token) {
		Algorithm algorithm = Algorithm.HMAC512(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        Date expirationDate = jwt.getExpiresAt();
        return expirationDate.before(new Date());
	}
	
	public String getMemberIDWithDecoding(String token) {
        return JWT.decode(token).getClaim("member-id").asString();
    }
	
	public String getMemberIDWithVerification(String token) {
		Algorithm algorithm = Algorithm.HMAC512(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim("member-id").asString();
    }
	
	public String getMemberRoleWithDecoding(String token) {
        return JWT.decode(token).getClaim("member-role").asString();
    }
	
	public String getMemberRoleWithVerification(String token) {
		Algorithm algorithm = Algorithm.HMAC512(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim("member-role").asString();
    }
	
	public String getTokenTypeWithDecoding(String token) {
        return JWT.decode(token).getClaim("token-type").asString();
    }
	
	public String getTokenTypeWithVerification(String token) {
        Algorithm algorithm = Algorithm.HMAC512(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim("token-type").asString();
    }

	public String createMemberAccessToken(String memberId, MemberRole memberRole) {
        return JWT.create()
                .withSubject(memberId)
                .withIssuer(ISSUER)
                .withAudience(memberId)
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(LocalDateTime.now().plusHours(TokenType.MEMBER_ACCESS_TOKEN.getTokenTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("member-id", memberId.toString())
                .withClaim("member-role", memberRole.toString())
                .withClaim("token-type",TokenType.MEMBER_ACCESS_TOKEN.getTokenType())
                .sign(Algorithm.HMAC512(SECRET));
    }
	
    public String createMemberRefreshToken(String memberId, MemberRole memberRole) {
        return JWT.create()
                .withSubject(memberId)
                .withIssuer(ISSUER)
                .withAudience(memberId)
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(LocalDateTime.now().plusHours(TokenType.MEMBER_REFRESH_TOKEN.getTokenTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("member-id", memberId.toString())
                .withClaim("member-role", memberRole.toString())
                .withClaim("token-type",TokenType.MEMBER_REFRESH_TOKEN.getTokenType())
                .sign(Algorithm.HMAC512(SECRET));
    }
    
    public long getRemainingTimeWithDecoding(String token) {
    	if(token==null) {
    		return 0;
    	}
    	
    	long remainingTime=JWT.decode(token).getExpiresAt().getTime()-new Date().getTime();
    	
    	if(remainingTime<0) {
    		remainingTime = 0;
    	}
    	 
    	return remainingTime;
    }
    
    public long getRemainingTimeWithVerification(String token) {
    	Algorithm algorithm = Algorithm.HMAC512(SECRET);
    	
    	if(token==null) {
    		return 0;
    	}
    	
    	long remainingTime=JWT.require(algorithm).build().verify(token).getExpiresAt().getTime()-new Date().getTime();
    	
    	if(remainingTime<0) {
    		remainingTime = 0;
    	}
    	 
    	return remainingTime;
    }
}
