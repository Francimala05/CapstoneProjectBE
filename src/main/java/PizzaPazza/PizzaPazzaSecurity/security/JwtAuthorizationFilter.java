package PizzaPazza.PizzaPazzaSecurity.security;

import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomDetailsService customDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

String richiestaUrl = request.getRequestURI();
if(richiestaUrl.startsWith("/utente/")){
    filterChain.doFilter(request,response);
    return;
}
String jwt = parseJwt(request);
if(jwt != null && jwtUtil.validateToken(jwt)){
    String username = jwtUtil.getUsernameFromToken(jwt);
    UserDetails userDetails = customDetailsService.loadUserByUsername(username);
    if(userDetails != null){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

filterChain.doFilter(request,response);
    }


   private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(headerAuth != null && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        return null;
   }

}