package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.Category;
import closet_share.closetshare_platform.model.Gender;
import closet_share.closetshare_platform.model.ItemDTO;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.ItemService;
import closet_share.closetshare_platform.util.CustomCollectors;
import closet_share.closetshare_platform.util.ReferencedWarning;
import closet_share.closetshare_platform.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserRepository userRepository;

    public ItemController(final ItemService itemService, final UserRepository userRepository) {
        this.itemService = itemService;
        this.userRepository = userRepository;
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

    @PostMapping("/add")
    public String add(@ModelAttribute("item") @Valid final ItemDTO itemDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/item/add";
        }
        itemService.create(itemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item.create.success"));
        return "redirect:/admin/items";
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
