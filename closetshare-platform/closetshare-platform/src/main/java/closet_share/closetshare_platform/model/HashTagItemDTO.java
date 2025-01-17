package closet_share.closetshare_platform.model;

import jakarta.validation.constraints.NotNull;


public class HashTagItemDTO {

    private Long seqId;

    @NotNull
    private Long itemId;

    @NotNull
    private Long hashtagId;

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

}
