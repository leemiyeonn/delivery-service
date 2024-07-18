package org.delivery.api.domain.token.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperIfs;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper implements TokenHelperIfs {

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) {
        // accessTokenPlusHour 이후의 시간 계산
        var expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);

        // LocalDateTime을 Date 타입으로 변환
        var expiredAt = Date.from(
                expiredLocalDateTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        // 비밀 키를 이용해 HMAC-SHA 키 생성
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // JWT 토큰 생성
        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 키 설정
                .setClaims(data) // 데이터 클레임 설정
                .setExpiration(expiredAt) // 만료 시간 설정
                .compact(); // 토큰 빌드

        // TokenDto 객체 생성 및 반환
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {
        // refreshTokenPlusHour 이후의 시간 계산
        var expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);

        // LocalDateTime을 Date 타입으로 변환
        var expiredAt = Date.from(
                expiredLocalDateTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        // 비밀 키를 이용해 HMAC-SHA 키 생성
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // JWT 토큰 생성
        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 키 설정
                .setClaims(data) // 데이터 클레임 설정
                .setExpiration(expiredAt) // 만료 시간 설정
                .compact(); // 토큰 빌드

        // TokenDto 객체 생성 및 반환
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {
        // 비밀 키를 이용해 HMAC-SHA 키 생성
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // JWT 파서 생성
        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            // 토큰 검증 및 파싱
            var result = parser.parseClaimsJws(token);
            // 클레임 맵 반환
            return new HashMap<String, Object>(result.getBody());

        } catch (Exception e) {
            if (e instanceof SignatureException) {
                // 토큰 서명이 유효 하지 않을 때
                throw new ApiException(TokenErrorCode.INVALID_TOKEN, e);
            } else if (e instanceof ExpiredJwtException) {
                // 토큰이 만료 되었을 때
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN, e);
            } else {
                // 그 외의 에러
                throw new ApiException(TokenErrorCode.TOKEN_EXCEPTION, e);
            }
        }
    }
}
