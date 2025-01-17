package closet_share.closetshare_platform.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class HashTagDTO {

    private Long seqId;

    @NotNull
    @Size(max = 20)
    private String tagName;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(final Long seqId) {
        this.seqId = seqId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(final String tagName) {
        this.tagName = tagName;
    }

}
