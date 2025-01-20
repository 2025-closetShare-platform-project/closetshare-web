package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.Category;
import closet_share.closetshare_platform.model.Gender;
import closet_share.closetshare_platform.model.ItemDTO;
import closet_share.closetshare_platform.model.ItemImageDTO;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.ItemImageService;
import closet_share.closetshare_platform.service.ItemService;
import closet_share.closetshare_platform.util.CustomCollectors;
import closet_share.closetshare_platform.util.ReferencedWarning;
import closet_share.closetshare_platform.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemImageService itemImageService;
    private final ItemService itemService;
    private final UserRepository userRepository;

    //파일 폴더 위치
    private String uploadFolder = "/Users/gim-yeseul/Documents/2025-closetshare-platform/closetshare-web/closetshare-platform/upload";


    public ItemController(final ItemService itemService, final UserRepository userRepository,
                          final  ItemImageService itemImageService) {
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.itemImageService = itemImageService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("categoryValues", Category.values());
        model.addAttribute("genderValues", Gender.values());
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getSeqId, User::getUserId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("items", itemService.findAll());
        return "admin/item/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("item") final ItemDTO itemDTO) {
        return "admin/item/add";
    }


    //MultipartFile file controller
    @Transactional
    @PostMapping("/add")
    public String add(@ModelAttribute("item") @Valid final ItemDTO itemDTO,
                      @RequestPart(value = "file", required = true) @Valid MultipartFile[] file,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "admin/item/add";
//        }

//        String subCategoryName = String.valueOf(itemDTO.getSeqId());
        List<Map<String,Object>> fileUrl = itemImageService.saveFiles(file, uploadFolder);

        //DTO 저장
        Long itemSeqId = itemService.create(itemDTO);
        // 각 파일 정보를 저장
        //LONG, UUID 두개를 map 에 저장하기 위해, Object 사용
        for (Map<String, Object> fileInfo : fileUrl) {
            ItemImageDTO itemImageDTO = new ItemImageDTO();
            itemImageDTO.setFilename((String) fileInfo.get("origin")); // 원본 이름 UUID
            itemImageDTO.setUuid((UUID) fileInfo.get("uuid")); // UUID
            itemImageDTO.setItemId(itemSeqId); // 연결된 Item의 ID
            itemImageDTO.setSrc((String) fileInfo.get("src"));
            // 이미지 정보 저장
            itemImageService.create(itemImageDTO);
        }



        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item.create.success"));
        return "redirect:/admin/items";
    }

    @GetMapping("/detail/{seqId}")
    public String detail(@PathVariable(name = "seqId") final Long seqId,
                         Model model) {
        model.addAttribute("item", itemService.get(seqId));
        model.addAttribute("images",itemImageService.findByitemId(seqId));

        return "member/item/detail";
    }


    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
        model.addAttribute("item", itemService.get(seqId));
        return "admin/item/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("item") @Valid final ItemDTO itemDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/item/edit";
        }
        itemService.update(seqId, itemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item.update.success"));
        return "redirect:/admin/items";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Long seqId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = itemService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            itemService.delete(seqId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("item.delete.success"));
        }
        return "redirect:/admin/items";
    }

}
