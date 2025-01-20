package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.ItemImage;
import closet_share.closetshare_platform.model.ItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    ItemImage findFirstByItemId(Item item);

//    List<ItemImage> findItemImagesByItemId(Item itemId);
    List<ItemImage> findItemImagesByItemId_SeqId(Long itemId);

}
