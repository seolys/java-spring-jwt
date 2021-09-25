package seol.study.jwtsample.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seol.study.jwtsample.user.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
