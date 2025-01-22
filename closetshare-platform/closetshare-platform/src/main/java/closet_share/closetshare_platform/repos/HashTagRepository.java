package closet_share.closetshare_platform.repos;

import closet_share.closetshare_platform.domain.HashTag;
import closet_share.closetshare_platform.domain.HashTagItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Boolean existsHashTagByTagName(String hashtagName);

    @Query("SELECT h.seqId FROM HashTag h WHERE h.tagName = :tagName")
    Long findIdByTagName(@Param("tagName") String tagName);}
