package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.ItemDTO;
import closet_share.closetshare_platform.service.ItemService;
import closet_share.closetshare_platform.util.ReferencedException;
import closet_share.closetshare_platform.util.ReferencedWarning;
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
@RequestMapping(value = "/api/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemResource {

    private final ItemService itemService;

    public ItemResource(final ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(itemService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createItem(@RequestBody @Valid final ItemDTO itemDTO) {
        final Long createdSeqId = itemService.create(itemDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateItem(@PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final ItemDTO itemDTO) {
        itemService.update(seqId, itemDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteItem(@PathVariable(name = "seqId") final Long seqId) {
        final ReferencedWarning referencedWarning = itemService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        itemService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
