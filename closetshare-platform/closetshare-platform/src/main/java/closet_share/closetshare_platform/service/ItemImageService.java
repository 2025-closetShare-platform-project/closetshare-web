package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.ItemImage;
import closet_share.closetshare_platform.model.ItemDTO;
import closet_share.closetshare_platform.model.ItemImageDTO;
import closet_share.closetshare_platform.repos.ItemImageRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.util.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ItemImageService {

    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;

    String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));


    public ItemImageService(final ItemImageRepository itemImageRepository,
                            final ItemRepository itemRepository, ItemService itemService) {
        this.itemImageRepository = itemImageRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    public List<ItemImageDTO> findAll() {
        final List<ItemImage> itemImages = itemImageRepository.findAll(Sort.by("seqId"));
        return itemImages.stream()
                .map(itemImage -> mapToDTO(itemImage, new ItemImageDTO()))
                .toList();
    }


    public List<ItemImageDTO> findByitemId(Long itemId) {
//        final ItemDTO item = itemService.get(itemId);
        final List<ItemImage> itemImages = itemImageRepository.findItemImagesByItemId_SeqId(itemId);
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




    public List<Map<String, Object>> saveFiles(MultipartFile[] files, String uploadFolder) {
        String item = "Item/";
//        String userseqId = subCategoryName;
        Path folderPath = Paths.get(uploadFolder +"/"+dateFolder+"/"+ item);
        List<Map<String, Object>> fileInfoList = new ArrayList<>();

        try {
            // 폴더 생성
            Files.createDirectories(folderPath);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 비어 있는 파일은 무시
                }

                Map<String, Object> originNameMap = new HashMap<>();
                String originName = file.getOriginalFilename();
                UUID uuid = uploadFileNameMake(); // UUID 생성 메서드 호출

                originNameMap.put("origin", originName); // 문자열에서 UUID 생성
                originNameMap.put("uuid", uuid);

                String filesrc = uuid + "_" + originName;
                //String fileUrl = uploadFolder + dateFolder + "/" + item + filesrc;
                String fileUrl = "/upload/" + dateFolder + "/" + item + filesrc;

                // 파일 저장 경로
                Path filePath = folderPath.resolve(filesrc);
                originNameMap.put("src",fileUrl);
                file.transferTo(filePath.toFile()); // 파일 저장
                System.out.println("File saved at: " + fileUrl);

                fileInfoList.add(originNameMap); // 각 파일의 정보를 리스트에 추가
            }
        } catch (Exception e) {
            System.out.println("Error while saving files: " + e.getMessage());
        }

        return fileInfoList;
    }


    //file name 중복 저장 방지를 위한 난수 생성
    private UUID uploadFileNameMake() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private final ItemService itemService;
}
