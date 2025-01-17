package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.Borrow;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Borrow findFirstByUserId(User user);

    Borrow findFirstByItemId(Item item);

}
