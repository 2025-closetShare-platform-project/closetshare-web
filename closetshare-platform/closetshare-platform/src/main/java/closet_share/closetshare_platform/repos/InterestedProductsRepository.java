package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.InterestedProductId;
import closet_share.closetshare_platform.domain.InterestedProducts;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InterestedProductsRepository extends JpaRepository<InterestedProducts, InterestedProductId> {

    InterestedProducts findFirstByUserId(User user);

    InterestedProducts findFirstByItemId(Item item);



}
