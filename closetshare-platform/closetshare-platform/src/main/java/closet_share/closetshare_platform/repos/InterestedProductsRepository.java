package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.InterestedProducts;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InterestedProductsRepository extends JpaRepository<InterestedProducts, Long> {

    InterestedProducts findFirstByUserId(User user);

    InterestedProducts findFirstByItemId(Item item);

}
