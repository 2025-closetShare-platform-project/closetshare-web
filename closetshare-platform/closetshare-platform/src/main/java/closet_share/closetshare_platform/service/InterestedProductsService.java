package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.InterestedProductId;
import closet_share.closetshare_platform.domain.InterestedProducts;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.InterestedProductsDTO;
import closet_share.closetshare_platform.repos.InterestedProductsRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InterestedProductsService {

    private final InterestedProductsRepository interestedProductsRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public InterestedProductsService(
            final InterestedProductsRepository interestedProductsRepository,
            final UserRepository userRepository, final ItemRepository itemRepository) {
        this.interestedProductsRepository = interestedProductsRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

//    public List<InterestedProductsDTO> findAll() {
//        final List<InterestedProducts> interestedProductses = interestedProductsRepository.findAll(Sort.by("seqId"));
//        return interestedProductses.stream()
//                .map(interestedProducts -> mapToDTO(interestedProducts, new InterestedProductsDTO()))
//                .toList();
//    }
//
//    public InterestedProductsDTO get(final Long seqId) {
//        return interestedProductsRepository.findById(seqId)
//                .map(interestedProducts -> mapToDTO(interestedProducts, new InterestedProductsDTO()))
//                .orElseThrow(NotFoundException::new);
//    }

    public void create(final InterestedProductsDTO interestedProductsDTO) {
        final InterestedProducts interestedProducts = new InterestedProducts();
        mapToEntity(interestedProductsDTO, interestedProducts);
        interestedProductsRepository.save(interestedProducts);
    }

    public void enrollInterestedProducts(User user, Item item) {
        // 복합 키 생성
        InterestedProductId interestedProductId = new InterestedProductId(user.getSeqId(), item.getSeqId());

        // LectureUser 객체 생성
        InterestedProducts interestedProducts = new InterestedProducts();
        interestedProducts.setSeqId(interestedProductId);
        interestedProducts.setUserId(user);
        interestedProducts.setItemId(item);

        // 데이터베이스에 저장
        interestedProductsRepository.save(interestedProducts);
    }

//    public void update(final Long seqId, final InterestedProductsDTO interestedProductsDTO) {
//        final InterestedProducts interestedProducts = interestedProductsRepository.findById(seqId)
//                .orElseThrow(NotFoundException::new);
//        mapToEntity(interestedProductsDTO, interestedProducts);
//        interestedProductsRepository.save(interestedProducts);
//    }

//    public void delete(final Long seqId) {
//        interestedProductsRepository.deleteById(seqId);
//    }

//    private InterestedProductsDTO mapToDTO(final InterestedProducts interestedProducts,
//            final InterestedProductsDTO interestedProductsDTO) {
//        interestedProductsDTO.setSeqId(interestedProducts.getSeqId());
//        interestedProductsDTO.setUserId(interestedProducts.getUserId() == null ? null : interestedProducts.getUserId().getSeqId());
//        interestedProductsDTO.setItemId(interestedProducts.getItemId() == null ? null : interestedProducts.getItemId().getSeqId());
//        return interestedProductsDTO;
//    }

    private InterestedProducts mapToEntity(final InterestedProductsDTO interestedProductsDTO,
            final InterestedProducts interestedProducts) {
        final User userId = interestedProductsDTO.getUserId() == null ? null : userRepository.findById(interestedProductsDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        interestedProducts.setUserId(userId);
        final Item itemId = interestedProductsDTO.getItemId() == null ? null : itemRepository.findById(interestedProductsDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("itemId not found"));
        interestedProducts.setItemId(itemId);
        InterestedProductId interestedProductId = new InterestedProductId(interestedProductsDTO.getUserId(), interestedProductsDTO.getItemId());
        interestedProducts.setSeqId(interestedProductId);
        return interestedProducts;
    }

    public boolean toggleLike(final InterestedProductsDTO interestedProductsDTO) {
        InterestedProductId id = new InterestedProductId(interestedProductsDTO.getUserId(), interestedProductsDTO.getItemId());
        Optional<InterestedProducts> existingRecord = interestedProductsRepository.findById(id);

        if (existingRecord.isPresent()) {
            // 이미 존재하면 삭제
            interestedProductsRepository.delete(existingRecord.get());
            return false; // 좋아요 취소 상태
        } else {
            final InterestedProducts interestedProducts = new InterestedProducts();
            mapToEntity(interestedProductsDTO, interestedProducts);
            interestedProductsRepository.save(interestedProducts);
            return true; // 좋아요 상태
        }
    }

    public boolean isLiked(Long itemId, Long userId) {
        InterestedProductId id = new InterestedProductId(userId, itemId);
        return interestedProductsRepository.existsById(id);
    }
}
