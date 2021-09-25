package seol.study.jwtsample.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import seol.study.jwtsample.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@EntityGraph(attributePaths = "authorities") // EAGER로 authorities를 같이 조회한다.(LAZY 아님)
	Optional<User> findOneWithAuthoritiesByUsername(String username);
}
