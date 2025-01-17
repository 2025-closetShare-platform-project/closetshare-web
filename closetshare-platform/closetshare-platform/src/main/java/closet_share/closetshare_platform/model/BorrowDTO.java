package closet_share.closetshare_platform.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class BorrowDTO {

    private Long seqId;

    @NotNull
    private Integer rentalFee;

    @NotNull
    private LocalDateTime rentalDate;

    @NotNull
    private LocalDateTime returnDate;

    @NotNull
    private BorrowStatus borrowStatus;

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

    public Integer getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(final Integer rentalFee) {
        this.rentalFee = rentalFee;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(final LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(final LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowStatus getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(final BorrowStatus borrowStatus) {
        this.borrowStatus = borrowStatus;
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
