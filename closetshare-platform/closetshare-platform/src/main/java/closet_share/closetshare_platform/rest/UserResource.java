package closet_share.closetshare_platform.rest;

import closet_share.closetshare_platform.model.UserDTO;
import closet_share.closetshare_platform.service.UserService;
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
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{seqId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "seqId") final Long seqId) {
        return ResponseEntity.ok(userService.get(seqId));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Long createdSeqId = userService.create(userDTO);
        return new ResponseEntity<>(createdSeqId, HttpStatus.CREATED);
    }

    @PutMapping("/{seqId}")
    public ResponseEntity<Long> updateUser(@PathVariable(name = "seqId") final Long seqId,
            @RequestBody @Valid final UserDTO userDTO) {
        userService.update(seqId, userDTO);
        return ResponseEntity.ok(seqId);
    }

    @DeleteMapping("/{seqId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "seqId") final Long seqId) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        userService.delete(seqId);
        return ResponseEntity.noContent().build();
    }

}
