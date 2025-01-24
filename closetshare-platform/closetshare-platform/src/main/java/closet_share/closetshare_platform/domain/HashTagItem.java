package closet_share.closetshare_platform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Setter
@Getter
@Entity
@Table(name = "HashTagItems")
@EntityListeners(AuditingEntityListener.class)
public class HashTagItem {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id_id", nullable = false)
    private Item itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hashtag_id_id", nullable = false)
    private HashTag hashtagId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(final Long seqId) {
        this.seqId = seqId;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(final Item itemId) {
        this.itemId = itemId;
    }

    public HashTag getHashtagId() {
        return hashtagId;
    }

    public void setHashtagId(final HashTag hashtagId) {
        this.hashtagId = hashtagId;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
