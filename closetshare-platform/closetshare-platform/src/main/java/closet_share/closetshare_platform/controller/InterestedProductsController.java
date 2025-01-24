package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.InterestedProductsDTO;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.InterestedProductsService;
import closet_share.closetshare_platform.util.CustomCollectors;
import closet_share.closetshare_platform.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/interestedProductss")
public class InterestedProductsController {

    private final InterestedProductsService interestedProductsService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public InterestedProductsController(final InterestedProductsService interestedProductsService,
            final UserRepository userRepository, final ItemRepository itemRepository) {
        this.interestedProductsService = interestedProductsService;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getSeqId, User::getUserId)));
        model.addAttribute("itemIdValues", itemRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Item::getSeqId, Item::getTitle)));
    }

//    @GetMapping
//    public String list(final Model model) {
//        model.addAttribute("interestedProductses", interestedProductsService.findAll());
//        return "admin/interestedProducts/list";
//    }

//    @GetMapping("/add")
//    public String add(
//            @ModelAttribute("interestedProducts") final InterestedProductsDTO interestedProductsDTO) {
//        return "admin/interestedProducts/add";
//    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("interestedProducts") final InterestedProductsDTO interestedProductsDTO) {
        return "admin/interestedProducts/add";
    }


//    @GetMapping("/add/{itemId}")
//    public String add(@PathVariable(name = "itemId") final Long itemId,
//            @ModelAttribute("interestedProducts") final InterestedProductsDTO interestedProductsDTO) {
//
//        return "admin/interestedProducts/add";
//    }

//    @PostMapping("/add")
//    public String add(
//            @ModelAttribute("interestedProducts") @Valid final InterestedProductsDTO interestedProductsDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "admin/interestedProducts/add";
//        }
//        interestedProductsService.create(interestedProductsDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("interestedProducts.create.success"));
//        return "redirect:/admin/interestedProductss";
//    }

//    @PostMapping("/add")
//    public ResponseEntity<Map<String, Object>> add(@RequestBody InterestedProductsDTO interestedProductsDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
////        if (bindingResult.hasErrors()) {
////            return "admin/interestedProducts/add";
////        }
//        interestedProductsService.create(interestedProductsDTO);
//        try {
//            Integer status = 1;
//            int warnCount = 3;
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("status", status);
//            response.put("warnCount", warnCount);
//            return ResponseEntity.ok(response);
//        } catch (IllegalStateException e) {
//            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Unexpected error"));
//        }
//    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getLikeStatus(@RequestParam Long itemId, @RequestParam Long userId) {
        boolean isLiked = interestedProductsService.isLiked(itemId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("status", isLiked ? "liked" : "unliked");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> toggleLike(@RequestBody InterestedProductsDTO interestedProductsDTO) {
        try {
            boolean isLiked = interestedProductsService.toggleLike(interestedProductsDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("status", isLiked ? "liked" : "unliked");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Unexpected error"));
        }
    }


    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
//        model.addAttribute("interestedProducts", interestedProductsService.get(seqId));
        return "admin/interestedProducts/edit";
    }

//    @PostMapping("/edit/{seqId}")
//    public String edit(@PathVariable(name = "seqId") final Long seqId,
//            @ModelAttribute("interestedProducts") @Valid final InterestedProductsDTO interestedProductsDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "admin/interestedProducts/edit";
//        }
//        interestedProductsService.update(seqId, interestedProductsDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("interestedProducts.update.success"));
//        return "redirect:/admin/interestedProductss";
//    }
//
//    @PostMapping("/delete/{seqId}")
//    public String delete(@PathVariable(name = "seqId") final Long seqId,
//            final RedirectAttributes redirectAttributes) {
//        interestedProductsService.delete(seqId);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("interestedProducts.delete.success"));
//        return "redirect:/admin/interestedProductss";
//    }

}
