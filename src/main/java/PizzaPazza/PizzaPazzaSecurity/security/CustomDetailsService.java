package PizzaPazza.PizzaPazzaSecurity.security;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Utente user = utenteRepository.findByUsername(username).orElseThrow();

        UserDetails dettagliUtente = User.builder().username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRuolo().name()).build();

        return dettagliUtente;
    }
}
