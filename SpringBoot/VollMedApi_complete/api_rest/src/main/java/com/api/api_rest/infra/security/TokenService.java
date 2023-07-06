package com.api.api_rest.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.api_rest.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

  private String ISSUER = "Api Voll Med";

  @Value("${api.security.token.secret}")
  private String secret;

  public String gerarToken(Usuario usuario) {
    try {
      var algoritimo = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer(ISSUER)
          .withSubject(usuario.getUsername())
          .withExpiresAt(dataExpiracao())
          .sign(algoritimo);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Erro ao gerar token jwt", exception);
    }
  }

  public String getSubject(String tokenJWT) {
    try {
      var algoritimo = Algorithm.HMAC256(secret);
      return JWT.require(algoritimo)
          .withIssuer(ISSUER)
          .build()
          .verify(tokenJWT)
          .getSubject();

    } catch (JWTVerificationException exception) {
      throw new RuntimeException("Token JWT invalido ou expirado.");
    }
  }

  private Instant dataExpiracao() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }

}
