package closet_share.closetshare_platform.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Getter
@Setter
@Entity
@Table(name = "InterestedProductses")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class InterestedProducts {

    @EmbeddedId
    private InterestedProductId seqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // InterestedProductId의 userId와 매핑
    @JoinColumn(name = "user_id_id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId") // InterestedProductId의 itemId와 매핑
    @JoinColumn(name = "item_id_id", nullable = false)
    private Item itemId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
}
