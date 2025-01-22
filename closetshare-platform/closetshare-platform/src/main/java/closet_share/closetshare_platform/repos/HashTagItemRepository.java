package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.HashTag;
import closet_share.closetshare_platform.domain.HashTagItem;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.model.HashTagItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HashTagItemRepository extends JpaRepository<HashTagItem, Long> {

    HashTagItem findFirstByItemId(Item item);

    List<HashTagItem> findHashTagItemByItemId_SeqId(Long seqId);

    HashTagItem findFirstByHashtagId(HashTag hashTag);

    List<HashTagItem> findHashTagItemDTOByItemId_SeqId(@Param("itemId") Long itemId);

}
