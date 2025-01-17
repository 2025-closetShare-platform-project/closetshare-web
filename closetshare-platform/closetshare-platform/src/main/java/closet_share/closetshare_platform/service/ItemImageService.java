package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.ItemImage;
import closet_share.closetshare_platform.model.ItemImageDTO;
import closet_share.closetshare_platform.repos.ItemImageRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ItemImageService {

    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;

    public ItemImageService(final ItemImageRepository itemImageRepository,
            final ItemRepository itemRepository) {
        this.itemImageRepository = itemImageRepository;
        this.itemRepository = itemRepository;
    }

    public List<ItemImageDTO> findAll() {
        final List<ItemImage> itemImages = itemImageRepository.findAll(Sort.by("seqId"));
        return itemImages.stream()
                .map(itemImage -> mapToDTO(itemImage, new ItemImageDTO()))
                .toList();
    }

    public ItemImageDTO get(final Long seqId) {
        return itemImageRepository.findById(seqId)
                .map(itemImage -> mapToDTO(itemImage, new ItemImageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ItemImageDTO itemImageDTO) {
        final ItemImage itemImage = new ItemImage();
        mapToEntity(itemImageDTO, itemImage);
        return itemImageRepository.save(itemImage).getSeqId();
    }

    public void update(final Long seqId, final ItemImageDTO itemImageDTO) {
        final ItemImage itemImage = itemImageRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(itemImageDTO, itemImage);
        itemImageRepository.save(itemImage);
    }

    public void delete(final Long seqId) {
        itemImageRepository.deleteById(seqId);
    }

    private ItemImageDTO mapToDTO(final ItemImage itemImage, final ItemImageDTO itemImageDTO) {
        itemImageDTO.setSeqId(itemImage.getSeqId());
        itemImageDTO.setSrc(itemImage.getSrc());
        itemImageDTO.setUuid(itemImage.getUuid());
        itemImageDTO.setFilename(itemImage.getFilename());
        itemImageDTO.setItemId(itemImage.getItemId() == null ? null : itemImage.getItemId().getSeqId());
        return itemImageDTO;
    }

    private ItemImage mapToEntity(final ItemImageDTO itemImageDTO, final ItemImage itemImage) {
        itemImage.setSrc(itemImageDTO.getSrc());
        itemImage.setUuid(itemImageDTO.getUuid());
        itemImage.setFilename(itemImageDTO.getFilename());
        final Item itemId = itemImageDTO.getItemId() == null ? null : itemRepository.findById(itemImageDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("itemId not found"));
        itemImage.setItemId(itemId);
        return itemImage;
    }

}
