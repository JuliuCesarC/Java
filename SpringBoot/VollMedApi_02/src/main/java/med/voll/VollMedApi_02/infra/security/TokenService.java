package med.voll.VollMedApi_02.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.VollMedApi_02.domain.usuario.Usuario;

@Service
public class TokenService {

  private String Issuer = "API Voll.med";

  @Value("${api.security.token.secret}")
  private String secret;

  public String gerarToken(Usuario usuario) {
    try {
      var algoritmo = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer(Issuer)
          .withSubject(usuario.getLogin())
          .withExpiresAt(dataExpiracao())          
          .sign(algoritmo);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("erro ao gerar token jwt", exception);
    }
  }

  public String getSubject(String tokenJWT) {
    try {
      var algoritmo = Algorithm.HMAC256(secret);
      return JWT.require(algoritmo)
          .withIssuer(Issuer)
          .build()
          .verify(tokenJWT)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new RuntimeException("Token JWT inválido ou expirado!");
    }
  }

  private Instant dataExpiracao() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }

}
