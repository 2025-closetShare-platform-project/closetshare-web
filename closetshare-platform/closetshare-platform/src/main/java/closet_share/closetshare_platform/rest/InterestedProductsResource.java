package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.InterestedProductsDTO;
import closet_share.closetshare_platform.service.InterestedProductsService;
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
@RequestMapping(value = "/api/interestedProductss", produces = MediaType.APPLICATION_JSON_VALUE)
public class InterestedProductsResource {

    private final InterestedProductsService interestedProductsService;

    public InterestedProductsResource(final InterestedProductsService interestedProductsService) {
        this.interestedProductsService = interestedProductsService;
    }

    @GetMapping
    public ResponseEntity<List<InterestedProductsDTO>> getAllInterestedProductss() {
        return ResponseEntity.ok(interestedProductsService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<InterestedProductsDTO> getInterestedProducts(
            @PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(interestedProductsService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createInterestedProducts(
            @RequestBody @Valid final InterestedProductsDTO interestedProductsDTO) {
        final Long createdSeqId = interestedProductsService.create(interestedProductsDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateInterestedProducts(
            @PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final InterestedProductsDTO interestedProductsDTO) {
        interestedProductsService.update(seqId, interestedProductsDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteInterestedProducts(
            @PathVariable(name = "seqId") final Long seqId) {
        interestedProductsService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
