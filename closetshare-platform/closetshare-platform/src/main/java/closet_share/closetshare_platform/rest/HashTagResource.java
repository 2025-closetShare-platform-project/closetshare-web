package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.HashTagDTO;
import closet_share.closetshare_platform.service.HashTagService;
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
@RequestMapping(value = "/api/hashTags", produces = MediaType.APPLICATION_JSON_VALUE)
public class HashTagResource {

    private final HashTagService hashTagService;

    public HashTagResource(final HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }

    @GetMapping
    public ResponseEntity<List<HashTagDTO>> getAllHashTags() {
        return ResponseEntity.ok(hashTagService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<HashTagDTO> getHashTag(@PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(hashTagService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createHashTag(@RequestBody @Valid final HashTagDTO hashTagDTO) {
        final Long createdSeqId = hashTagService.create(hashTagDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateHashTag(@PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final HashTagDTO hashTagDTO) {
        hashTagService.update(seqId, hashTagDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteHashTag(@PathVariable(name = "seqId") final Long seqId) {
        final ReferencedWarning referencedWarning = hashTagService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        hashTagService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
