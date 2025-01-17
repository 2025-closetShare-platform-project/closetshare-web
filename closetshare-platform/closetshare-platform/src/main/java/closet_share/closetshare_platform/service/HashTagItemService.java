package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.HashTag;
import closet_share.closetshare_platform.domain.HashTagItem;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.model.HashTagItemDTO;
import closet_share.closetshare_platform.repos.HashTagItemRepository;
import closet_share.closetshare_platform.repos.HashTagRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HashTagItemService {

    private final HashTagItemRepository hashTagItemRepository;
    private final ItemRepository itemRepository;
    private final HashTagRepository hashTagRepository;

    public HashTagItemService(final HashTagItemRepository hashTagItemRepository,
            final ItemRepository itemRepository, final HashTagRepository hashTagRepository) {
        this.hashTagItemRepository = hashTagItemRepository;
        this.itemRepository = itemRepository;
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTagItemDTO> findAll() {
        final List<HashTagItem> hashTagItems = hashTagItemRepository.findAll(Sort.by("seqId"));
        return hashTagItems.stream()
                .map(hashTagItem -> mapToDTO(hashTagItem, new HashTagItemDTO()))
                .toList();
    }

    public HashTagItemDTO get(final Long seqId) {
        return hashTagItemRepository.findById(seqId)
                .map(hashTagItem -> mapToDTO(hashTagItem, new HashTagItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HashTagItemDTO hashTagItemDTO) {
        final HashTagItem hashTagItem = new HashTagItem();
        mapToEntity(hashTagItemDTO, hashTagItem);
        return hashTagItemRepository.save(hashTagItem).getSeqId();
    }

    public void update(final Long seqId, final HashTagItemDTO hashTagItemDTO) {
        final HashTagItem hashTagItem = hashTagItemRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hashTagItemDTO, hashTagItem);
        hashTagItemRepository.save(hashTagItem);
    }

    public void delete(final Long seqId) {
        hashTagItemRepository.deleteById(seqId);
    }

    private HashTagItemDTO mapToDTO(final HashTagItem hashTagItem,
            final HashTagItemDTO hashTagItemDTO) {
        hashTagItemDTO.setSeqId(hashTagItem.getSeqId());
        hashTagItemDTO.setItemId(hashTagItem.getItemId() == null ? null : hashTagItem.getItemId().getSeqId());
        hashTagItemDTO.setHashtagId(hashTagItem.getHashtagId() == null ? null : hashTagItem.getHashtagId().getSeqId());
        return hashTagItemDTO;
    }

    private HashTagItem mapToEntity(final HashTagItemDTO hashTagItemDTO,
            final HashTagItem hashTagItem) {
        final Item itemId = hashTagItemDTO.getItemId() == null ? null : itemRepository.findById(hashTagItemDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("itemId not found"));
        hashTagItem.setItemId(itemId);
        final HashTag hashtagId = hashTagItemDTO.getHashtagId() == null ? null : hashTagRepository.findById(hashTagItemDTO.getHashtagId())
                .orElseThrow(() -> new NotFoundException("hashtagId not found"));
        hashTagItem.setHashtagId(hashtagId);
        return hashTagItem;
    }

}
