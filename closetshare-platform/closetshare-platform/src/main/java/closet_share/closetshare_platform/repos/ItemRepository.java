package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findFirstByUserId(User user);

}
