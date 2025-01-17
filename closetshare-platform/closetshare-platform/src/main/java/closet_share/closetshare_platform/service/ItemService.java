package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.Borrow;
import closet_share.closetshare_platform.domain.HashTagItem;
import closet_share.closetshare_platform.domain.InterestedProducts;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.ItemImage;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.ItemDTO;
import closet_share.closetshare_platform.repos.BorrowRepository;
import closet_share.closetshare_platform.repos.HashTagItemRepository;
import closet_share.closetshare_platform.repos.InterestedProductsRepository;
import closet_share.closetshare_platform.repos.ItemImageRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import closet_share.closetshare_platform.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;
    private final ItemImageRepository itemImageRepository;
    private final HashTagItemRepository hashTagItemRepository;
    private final InterestedProductsRepository interestedProductsRepository;

    public ItemService(final ItemRepository itemRepository, final UserRepository userRepository,
            final BorrowRepository borrowRepository, final ItemImageRepository itemImageRepository,
            final HashTagItemRepository hashTagItemRepository,
            final InterestedProductsRepository interestedProductsRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
        this.itemImageRepository = itemImageRepository;
        this.hashTagItemRepository = hashTagItemRepository;
        this.interestedProductsRepository = interestedProductsRepository;
    }

    public List<ItemDTO> findAll() {
        final List<Item> items = itemRepository.findAll(Sort.by("seqId"));
        return items.stream()
                .map(item -> mapToDTO(item, new ItemDTO()))
                .toList();
    }

    public ItemDTO get(final Long seqId) {
        return itemRepository.findById(seqId)
                .map(item -> mapToDTO(item, new ItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ItemDTO itemDTO) {
        final Item item = new Item();
        mapToEntity(itemDTO, item);
        return itemRepository.save(item).getSeqId();
    }

    public void update(final Long seqId, final ItemDTO itemDTO) {
        final Item item = itemRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(itemDTO, item);
        itemRepository.save(item);
    }

    public void delete(final Long seqId) {
        itemRepository.deleteById(seqId);
    }

    private ItemDTO mapToDTO(final Item item, final ItemDTO itemDTO) {
        itemDTO.setSeqId(item.getSeqId());
        itemDTO.setCategory(item.getCategory());
        itemDTO.setGender(item.getGender());
        itemDTO.setTitle(item.getTitle());
        itemDTO.setContent(item.getContent());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setViewCount(item.getViewCount());
        itemDTO.setUserId(item.getUserId() == null ? null : item.getUserId().getSeqId());
        return itemDTO;
    }

    private Item mapToEntity(final ItemDTO itemDTO, final Item item) {
        item.setCategory(itemDTO.getCategory());
        item.setGender(itemDTO.getGender());
        item.setTitle(itemDTO.getTitle());
        item.setContent(itemDTO.getContent());
        item.setPrice(itemDTO.getPrice());
        item.setViewCount(itemDTO.getViewCount());
        final User userId = itemDTO.getUserId() == null ? null : userRepository.findById(itemDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        item.setUserId(userId);
        return item;
    }

    public ReferencedWarning getReferencedWarning(final Long seqId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Item item = itemRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        final Borrow itemIdBorrow = borrowRepository.findFirstByItemId(item);
        if (itemIdBorrow != null) {
            referencedWarning.setKey("item.borrow.itemId.referenced");
            referencedWarning.addParam(itemIdBorrow.getSeqId());
            return referencedWarning;
        }
        final ItemImage itemIdItemImage = itemImageRepository.findFirstByItemId(item);
        if (itemIdItemImage != null) {
            referencedWarning.setKey("item.itemImage.itemId.referenced");
            referencedWarning.addParam(itemIdItemImage.getSeqId());
            return referencedWarning;
        }
        final HashTagItem itemIdHashTagItem = hashTagItemRepository.findFirstByItemId(item);
        if (itemIdHashTagItem != null) {
            referencedWarning.setKey("item.hashTagItem.itemId.referenced");
            referencedWarning.addParam(itemIdHashTagItem.getSeqId());
            return referencedWarning;
        }
        final InterestedProducts itemIdInterestedProducts = interestedProductsRepository.findFirstByItemId(item);
        if (itemIdInterestedProducts != null) {
            referencedWarning.setKey("item.interestedProducts.itemId.referenced");
            referencedWarning.addParam(itemIdInterestedProducts.getSeqId());
            return referencedWarning;
        }
        return null;
    }

}
