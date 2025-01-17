package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.HashTag;
import closet_share.closetshare_platform.domain.HashTagItem;
import closet_share.closetshare_platform.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HashTagItemRepository extends JpaRepository<HashTagItem, Long> {

    HashTagItem findFirstByItemId(Item item);

    HashTagItem findFirstByHashtagId(HashTag hashTag);

}
