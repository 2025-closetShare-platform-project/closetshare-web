package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.Borrow;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.BorrowDTO;
import closet_share.closetshare_platform.repos.BorrowRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BorrowService(final BorrowRepository borrowRepository,
            final UserRepository userRepository, final ItemRepository itemRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public List<BorrowDTO> findAll() {
        final List<Borrow> borrows = borrowRepository.findAll(Sort.by("seqId"));
        return borrows.stream()
                .map(borrow -> mapToDTO(borrow, new BorrowDTO()))
                .toList();
    }

    public BorrowDTO get(final Long seqId) {
        return borrowRepository.findById(seqId)
                .map(borrow -> mapToDTO(borrow, new BorrowDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BorrowDTO borrowDTO) {
        final Borrow borrow = new Borrow();
        mapToEntity(borrowDTO, borrow);
        return borrowRepository.save(borrow).getSeqId();
    }

    public void update(final Long seqId, final BorrowDTO borrowDTO) {
        final Borrow borrow = borrowRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(borrowDTO, borrow);
        borrowRepository.save(borrow);
    }

    public void delete(final Long seqId) {
        borrowRepository.deleteById(seqId);
    }

    private BorrowDTO mapToDTO(final Borrow borrow, final BorrowDTO borrowDTO) {
        borrowDTO.setSeqId(borrow.getSeqId());
        borrowDTO.setRentalFee(borrow.getRentalFee());
        borrowDTO.setRentalDate(borrow.getRentalDate());
        borrowDTO.setReturnDate(borrow.getReturnDate());
        borrowDTO.setBorrowStatus(borrow.getBorrowStatus());
        borrowDTO.setUserId(borrow.getUserId() == null ? null : borrow.getUserId().getSeqId());
        borrowDTO.setItemId(borrow.getItemId() == null ? null : borrow.getItemId().getSeqId());
        return borrowDTO;
    }

    private Borrow mapToEntity(final BorrowDTO borrowDTO, final Borrow borrow) {
        borrow.setRentalFee(borrowDTO.getRentalFee());
        borrow.setRentalDate(borrowDTO.getRentalDate());
        borrow.setReturnDate(borrowDTO.getReturnDate());
        borrow.setBorrowStatus(borrowDTO.getBorrowStatus());
        final User userId = borrowDTO.getUserId() == null ? null : userRepository.findById(borrowDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        borrow.setUserId(userId);
        final Item itemId = borrowDTO.getItemId() == null ? null : itemRepository.findById(borrowDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("itemId not found"));
        borrow.setItemId(itemId);
        return borrow;
    }

}
