package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.BorrowDTO;
import closet_share.closetshare_platform.service.BorrowService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/borrows", produces = MediaType.APPLICATION_JSON_VALUE)
public class BorrowResource {

    private final BorrowService borrowService;

    public BorrowResource(final BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowDTO>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<BorrowDTO> getBorrow(@PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(borrowService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createBorrow(@RequestBody @Valid final BorrowDTO borrowDTO) {
        final Long createdSeqId = borrowService.create(borrowDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateBorrow(@PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final BorrowDTO borrowDTO) {
        borrowService.update(seqId, borrowDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteBorrow(@PathVariable(name = "seqId") final Long seqId) {
        borrowService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
