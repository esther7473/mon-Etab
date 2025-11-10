package ci.ada.monetabv2new.security;

import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccountEntity> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }


        UserAccountEntity userAccount = user.get();


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (userAccount.getRole() != null) {
          
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userAccount.getRole().name()));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }

        log.debug("User found with username: {} with roles: {}", username, authorities);

        return new User(
                userAccount.getUsername(),
                userAccount.getPassword(),
                authorities  
        );
    }
}
