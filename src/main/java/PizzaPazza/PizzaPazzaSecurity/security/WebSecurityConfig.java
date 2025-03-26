package PizzaPazza.PizzaPazzaSecurity.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class WebSecurityConfig {

    @Autowired
    CustomDetailsService customDetailsService;

    @Autowired
    JwtAuthorizationFilter filtroAutorizzazione;

    @Autowired
    CustomCorsConfigurationSource corsConfigurationSource;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }





    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource.corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/utente/insert").permitAll()
                        .requestMatchers("/utente/login", "/api/**").permitAll()
                        .requestMatchers("/utente/get/**").permitAll()
                        .requestMatchers("/api/ordini/asporto/utente/**").permitAll()
                        .requestMatchers("/api/ordini/domicilio/utente/**").permitAll()
                        .requestMatchers("/utente/auth/**").hasAuthority("USER")
                        .requestMatchers("/utente/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/images/**").permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(filtroAutorizzazione, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    }