package PizzaPazza.PizzaPazzaSecurity.security;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.model.exception.CreateTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expirationMs}")
    private Long jwtExpirations;



    public String creaToken(Utente utente){
        //PAYLOAD
        Claims claims = Jwts.claims().setSubject(utente.getUsername());
        claims.put("roles",utente.getRuolo());
        claims.put("firstname",utente.getNome());
        claims.put("lastname",utente.getCognome());
        Date dataCreazioneToken = new Date();
        Date dataScadenza = new Date(dataCreazioneToken.getTime()+ TimeUnit.MINUTES.toMillis(jwtExpirations));
        String username = utente.getUsername();


                //CREAZIONE TOKEN (claims,dataexpiration,tipologia,algoritmo e chiave)
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setExpiration(dataScadenza)
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }


    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(getSignedKey())
                    .parseClaimsJws(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean checkExpiration(Claims claims){
        try{
           return claims.getExpiration().after(new Date());
        }catch (Exception ex){
            throw new CreateTokenException("Errore nel controllo della scadenza del token");
        }
    }
    //Estrapola dal token l'username
    private Key getSignedKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


}
