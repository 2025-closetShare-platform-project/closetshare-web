package closet_share.closetshare_platform.domain;

import java.io.Serializable;
import java.util.Objects;

public class InterestedProductId implements Serializable {

    private Long itemId;
    private Long userId;

    public InterestedProductId(Long userId, Long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    // 기본 생성자 (필수)
    public InterestedProductId() {
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterestedProductId that = (InterestedProductId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, itemId);
    }

}
