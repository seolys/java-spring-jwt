package seol.study.jwtsample.user.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import seol.study.jwtsample.user.entity.Authority;
import seol.study.jwtsample.user.entity.User;
import seol.study.jwtsample.user.repository.UserRepository;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) {
		return userRepository.findOneWithAuthoritiesByUsername(username)
				.map(user -> createUser(username, user))
				.orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
	}

	private org.springframework.security.core.userdetails.User createUser(String username, User user) {
		if (!user.isActivated()) {
			throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
		}

		Set<Authority> authorities = user.getAuthorities();
		List<GrantedAuthority> grantedAuthorities = authorities.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				grantedAuthorities);
	}
}
