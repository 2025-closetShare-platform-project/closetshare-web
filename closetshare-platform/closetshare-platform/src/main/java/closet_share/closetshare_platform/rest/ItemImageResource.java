package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.ItemImageDTO;
import closet_share.closetshare_platform.service.ItemImageService;
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
@RequestMapping(value = "/api/itemImages", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemImageResource {

    private final ItemImageService itemImageService;

    public ItemImageResource(final ItemImageService itemImageService) {
        this.itemImageService = itemImageService;
    }

    @GetMapping
    public ResponseEntity<List<ItemImageDTO>> getAllItemImages() {
        return ResponseEntity.ok(itemImageService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<ItemImageDTO> getItemImage(
            @PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(itemImageService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createItemImage(
            @RequestBody @Valid final ItemImageDTO itemImageDTO) {
        final Long createdSeqId = itemImageService.create(itemImageDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateItemImage(@PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final ItemImageDTO itemImageDTO) {
        itemImageService.update(seqId, itemImageDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteItemImage(@PathVariable(name = "seqId") final Long seqId) {
        itemImageService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
