package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.HashTagItemDTO;
import closet_share.closetshare_platform.service.HashTagItemService;
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
@RequestMapping(value = "/api/hashTagItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class HashTagItemResource {

    private final HashTagItemService hashTagItemService;

    public HashTagItemResource(final HashTagItemService hashTagItemService) {
        this.hashTagItemService = hashTagItemService;
    }

    @GetMapping
    public ResponseEntity<List<HashTagItemDTO>> getAllHashTagItems() {
        return ResponseEntity.ok(hashTagItemService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<HashTagItemDTO> getHashTagItem(
            @PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(hashTagItemService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createHashTagItem(
            @RequestBody @Valid final HashTagItemDTO hashTagItemDTO) {
        final Long createdSeqId = hashTagItemService.create(hashTagItemDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateHashTagItem(@PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final HashTagItemDTO hashTagItemDTO) {
        hashTagItemService.update(seqId, hashTagItemDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteHashTagItem(@PathVariable(name = "seqId") final Long seqId) {
        hashTagItemService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
