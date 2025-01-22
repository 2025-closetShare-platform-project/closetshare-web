package closet_share.closetshare_platform.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HashTagItemDTO {

    private Long seqId;

    @NotNull
    private Long itemId;

    @NotNull
    private Long hashtagId;

    @Size(max = 20)
    private String tagName;

//    // 모든 필드를 받는 생성자 (JPQL에서 필요)
//    public void HashTagItemAndHashNameDTO(Long seqId, Long itemIdSeqId, Long hashtagIdSeqId, String tagName) {
//        this.seqId = seqId;
//        this.itemId = itemIdSeqId;
//        this.hashtagId = hashtagIdSeqId;
//        this.tagName = tagName;
//    }

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(final Long seqId) {
        this.seqId = seqId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(final Long itemId) {
        this.itemId = itemId;
    }

    public Long getHashtagId() {
        return hashtagId;
    }

    public void setHashtagId(final Long hashtagId) {
        this.hashtagId = hashtagId;
    }

    public String  getTagName() {
        return tagName;
    }
    public void  setTagName(final String tagName) {this.tagName = tagName;}

    public HashTagItemDTO() {

    }
}
