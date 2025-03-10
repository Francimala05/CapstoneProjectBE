package PizzaPazza.PizzaPazzaSecurity.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable);

        // Impostazione autorizzazioni sugli accessi
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/utente/insert").permitAll()
                                .requestMatchers("/utente/login","/api/**").permitAll()
                                .requestMatchers("/utente/auth/**").hasAuthority("USER")
                                .requestMatchers("/utente/admin/**").hasAuthority("ADMIN"))
                        .sessionManagement(custom->custom.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filtroAutorizzazione, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    }