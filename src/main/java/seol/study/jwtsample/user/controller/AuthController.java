package seol.study.jwtsample.user.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seol.study.jwtsample.common.jwt.JwtFilter;
import seol.study.jwtsample.common.jwt.TokenProvider;
import seol.study.jwtsample.user.dto.LoginDto;
import seol.study.jwtsample.user.dto.TokenDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	/**
	 * 로그인
	 * @param loginDto
	 * @return
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		Authentication authentication = getAuthenticationManager().authenticate(authenticationToken); // CustomUserDetailsService#loadUserByUsername
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.createToken(authentication);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

		return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
	}

	private AuthenticationManager getAuthenticationManager() {
		AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
		return authenticationManager;
	}
}
