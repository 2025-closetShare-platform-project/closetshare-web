package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.HashTag;
import closet_share.closetshare_platform.domain.HashTagItem;
import closet_share.closetshare_platform.model.HashTagDTO;
import closet_share.closetshare_platform.repos.HashTagItemRepository;
import closet_share.closetshare_platform.repos.HashTagRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import closet_share.closetshare_platform.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final HashTagItemRepository hashTagItemRepository;

    public HashTagService(final HashTagRepository hashTagRepository,
            final HashTagItemRepository hashTagItemRepository) {
        this.hashTagRepository = hashTagRepository;
        this.hashTagItemRepository = hashTagItemRepository;
    }

    public List<HashTagDTO> findAll() {
        final List<HashTag> hashTags = hashTagRepository.findAll(Sort.by("seqId"));
        return hashTags.stream()
                .map(hashTag -> mapToDTO(hashTag, new HashTagDTO()))
                .toList();
    }

    public HashTagDTO get(final Long seqId) {
        return hashTagRepository.findById(seqId)
                .map(hashTag -> mapToDTO(hashTag, new HashTagDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HashTagDTO hashTagDTO) {
        final HashTag hashTag = new HashTag();
        mapToEntity(hashTagDTO, hashTag);
        return hashTagRepository.save(hashTag).getSeqId();
    }

    public void update(final Long seqId, final HashTagDTO hashTagDTO) {
        final HashTag hashTag = hashTagRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hashTagDTO, hashTag);
        hashTagRepository.save(hashTag);
    }

    public void delete(final Long seqId) {
        hashTagRepository.deleteById(seqId);
    }

    private HashTagDTO mapToDTO(final HashTag hashTag, final HashTagDTO hashTagDTO) {
        hashTagDTO.setSeqId(hashTag.getSeqId());
        hashTagDTO.setTagName(hashTag.getTagName());
        return hashTagDTO;
    }

    private HashTag mapToEntity(final HashTagDTO hashTagDTO, final HashTag hashTag) {
        hashTag.setTagName(hashTagDTO.getTagName());
        return hashTag;
    }

    public ReferencedWarning getReferencedWarning(final Long seqId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final HashTag hashTag = hashTagRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        final HashTagItem hashtagIdHashTagItem = hashTagItemRepository.findFirstByHashtagId(hashTag);
        if (hashtagIdHashTagItem != null) {
            referencedWarning.setKey("hashTag.hashTagItem.hashtagId.referenced");
            referencedWarning.addParam(hashtagIdHashTagItem.getSeqId());
            return referencedWarning;
        }
        return null;
    }

}
