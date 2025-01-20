package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserIdIgnoreCase(String userId);

    boolean existsByUserPhoneNumber(String userPhoneNumber);

    Optional<User> findByUserId(String userId);
}
