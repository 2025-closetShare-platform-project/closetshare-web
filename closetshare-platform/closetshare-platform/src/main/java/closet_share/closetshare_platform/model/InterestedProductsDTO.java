package closet_share.closetshare_platform.model;

import jakarta.validation.constraints.NotNull;


public class InterestedProductsDTO {

    private Long seqId;

    @NotNull
    private Long userId;

    @NotNull
    private Long itemId;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(final Long seqId) {
        this.seqId = seqId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(final Long itemId) {
        this.itemId = itemId;
    }

}
