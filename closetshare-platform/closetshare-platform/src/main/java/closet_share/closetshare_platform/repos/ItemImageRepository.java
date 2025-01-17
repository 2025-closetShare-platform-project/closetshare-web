package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    ItemImage findFirstByItemId(Item item);

}
