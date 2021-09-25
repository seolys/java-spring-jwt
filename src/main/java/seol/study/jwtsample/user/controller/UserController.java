package seol.study.jwtsample.user.controller;

import java.util.Optional;
import javassist.bytecode.DuplicateMemberException;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seol.study.jwtsample.user.dto.UserDto;
import seol.study.jwtsample.user.entity.User;
import seol.study.jwtsample.user.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 회원가입.
	 */
	@PostMapping("/signup")
	public ResponseEntity<User> signup(@Valid @RequestBody UserDto userDto) throws DuplicateMemberException {
		return ResponseEntity.ok(userService.signup(userDto));
	}

	/**
	 * USER, ADMIN 접근가능.
	 */
	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<User> getMyUserInfo() {
		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
	}

	/**
	 * ADMIN만 접근가능.
	 */
	@GetMapping("/user/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> getUserInfo(@PathVariable String username) {
		Optional<User> userWithAuthorities = userService.getUserWithAuthorities(username);
		if (userWithAuthorities.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(userWithAuthorities.get());
	}
}
