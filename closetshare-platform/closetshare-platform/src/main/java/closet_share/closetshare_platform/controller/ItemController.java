package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.Rq;
import closet_share.closetshare_platform.domain.ItemImage;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.*;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.HashTagItemService;
import closet_share.closetshare_platform.service.HashTagService;
import closet_share.closetshare_platform.service.ItemImageService;
import closet_share.closetshare_platform.service.ItemService;
import closet_share.closetshare_platform.util.CustomCollectors;
import closet_share.closetshare_platform.util.ReferencedWarning;
import closet_share.closetshare_platform.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.boot.jackson.JsonMixinModuleEntries;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.*;


@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemImageService itemImageService;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final HashTagService hashTagService;
    private final HashTagItemService hashTagItemService;
    private final Rq rq;

    //파일 폴더 위치
    private String uploadFolder = "/Users/gim-yeseul/Documents/2025-closetshare-platform/closetshare-web/closetshare-platform/upload";


    public ItemController(final ItemService itemService, final UserRepository userRepository,
                          final HashTagService hashTagService,
                          final HashTagItemService hashTagItemService,
                          final  ItemImageService itemImageService, Rq rq, JsonMixinModuleEntries jsonMixinModuleEntries) {
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.itemImageService = itemImageService;
        this.hashTagItemService = hashTagItemService;
        this.hashTagService = hashTagService;
        this.rq = rq;
        this.jsonMixinModuleEntries = jsonMixinModuleEntries;
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
    public String add(@ModelAttribute("item") final ItemDTO itemDTO,
                      @ModelAttribute("hastag") final HashTagDTO hashTagDTO,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        User user = rq.getSiteUser();
        if (user == null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("로그인 후 이용 가능"));
            return "redirect:/users/login";
        }
        else {
            model.addAttribute("users",user);
            model.addAttribute("hasTags",hashTagService.findAll());
            return "member/item/add";
        }
    }


    //MultipartFile file controller
    @Transactional
    @PostMapping("/add")
    public String add(        @ModelAttribute("item") final ItemDTO itemDTO,
//            @RequestPart("data") final ItemDTO itemDTO,                // JSON 데이터
                               @RequestParam("hashtags") String hashtags,              // 해시태그 JSON
                      @RequestPart(value = "file", required = true) @Valid MultipartFile[] file,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "admin/item/add";
//        }
//        ItemDTO itemDTO = new ItemDTO();
        User user = rq.getSiteUser();
        List<String> tagList = new ArrayList<>(Arrays.asList(hashtags.split("\\^")));
        itemDTO.setUserId(user.getSeqId());
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

        for (String tag : tagList) {
            Long hashTagId;
            Long exist = hashTagService.findIdByHashName(tag);
            if ( exist != null) {
                hashTagId = exist;
            }
            else {
                HashTagDTO hashTagDTO = new HashTagDTO();
                hashTagDTO.setTagName(tag);
                hashTagId = hashTagService.create(hashTagDTO);
            }
            HashTagItemDTO hashTagItemDTO = new HashTagItemDTO();
            hashTagItemDTO.setItemId(itemSeqId);
            hashTagItemDTO.setHashtagId(hashTagId);

            hashTagItemService.create(hashTagItemDTO);
        }


        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item이 등록되었습니다."));
        //return "redirect:/member/item/detail"+itemSeqId;
        return "redirect:/items/detail/"+itemSeqId;
    }

    @GetMapping("/detail/{seqId}")
    public String detail(@PathVariable(name = "seqId") final Long seqId,
                         Model model) {
        User user = rq.getSiteUser();
        ItemDTO item  = itemService.get(seqId);
        if (user != null) {
            boolean isAuthor =  item.getUserId().equals(user.getSeqId());
            model.addAttribute("isAuthor",isAuthor);
        }
        else {
            model.addAttribute("isAuthor",false);
        }
        model.addAttribute("item",item);
        model.addAttribute("images",itemImageService.findByitemId(seqId));
        model.addAttribute("users",user);
        model.addAttribute("hashtags",hashTagItemService.findByItemId(seqId));
        return "member/item/detail";
    }


    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model,
                       RedirectAttributes redirectAttributes) {
        ItemDTO itemDTO = itemService.get(seqId);

        Long AuthorId = itemDTO.getUserId();
        User sessionuser = rq.getSiteUser();

        if (!sessionuser.getSeqId().equals(AuthorId)) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("접근 권한이 없습니다."));
            return "redirect:/member/item/detail/"+seqId;
        }
        else {
            List<ItemImageDTO> itemImageDTOS = itemImageService.findByitemId(seqId);
            model.addAttribute("item", itemService.get(seqId));
            model.addAttribute("images",itemImageDTOS);
            model.addAttribute("hashtags",hashTagItemService.findByItemId(seqId));
            return "member/item/edit";
        }
    }

    @Transactional
    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("item") final ItemDTO itemDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        ItemDTO olditemDTO = itemService.get(seqId);
        itemDTO.setUserId(olditemDTO.getUserId());
        if (!itemDTO.getUserId().equals(rq.getSiteUser().getSeqId())) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("잘못된 접근 입니다."));
            return "redirect:/member/item/detail/"+seqId;
        }
else {
            itemDTO.setViewCount(olditemDTO.getViewCount());
            if (bindingResult.hasErrors()) {
                return "member/item/edit";
            }
            itemService.update(seqId, itemDTO);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item.update.success"));
            return "redirect:/items/detail/" + seqId;
        }

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

    private final JsonMixinModuleEntries jsonMixinModuleEntries;
}
