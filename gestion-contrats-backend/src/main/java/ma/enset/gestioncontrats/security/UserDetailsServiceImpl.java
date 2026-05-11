package ma.enset.gestioncontrats.security;

import ma.enset.gestioncontrats.entities.AppUser;
import ma.enset.gestioncontrats.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Implémentation de UserDetailsService — Spring Security appelle cette classe
 * automatiquement lors de l'authentification pour charger l'utilisateur depuis la BDD.
 *
 * On convertit notre AppUser en UserDetails (objet Spring Security)
 * en mappant nos rôles String en GrantedAuthority.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable : " + username
                ));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword()) // déjà hashé en BDD
                .authorities(
                    appUser.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                )
                .accountExpired(false)
                .disabled(!appUser.isEnabled())
                .build();
    }
}
