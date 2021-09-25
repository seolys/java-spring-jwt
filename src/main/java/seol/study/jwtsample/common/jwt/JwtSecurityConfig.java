package seol.study.jwtsample.common.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private TokenProvider tokenProvider;

	public JwtSecurityConfig(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void configure(HttpSecurity http) {
		JwtFilter customFilter = new JwtFilter(tokenProvider);
		// addFilterBefore: 지정된 필터 앞에 커스텀 필터를 추가 -> UsernamePasswordAuthenticationFilter 보다 먼저 filter 실행됨.
		// addFilterAfter: 지정된 필터 뒤에 커스텀 필터를 추가 -> UsernamePasswordAuthenticationFilter 실행된 후에 filter 실행됨.
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
