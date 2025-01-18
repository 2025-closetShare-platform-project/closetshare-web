package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.model.ItemImageDTO;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.service.ItemImageService;
import closet_share.closetshare_platform.util.CustomCollectors;
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
@RequestMapping("/itemImages")
public class ItemImageController {

    private final ItemImageService itemImageService;
    private final ItemRepository itemRepository;

    public ItemImageController(final ItemImageService itemImageService,
            final ItemRepository itemRepository) {
        this.itemImageService = itemImageService;
        this.itemRepository = itemRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("itemIdValues", itemRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Item::getSeqId, Item::getTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("itemImages", itemImageService.findAll());
        return "admin/itemImage/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("itemImage") final ItemImageDTO itemImageDTO) {
        return "admin/itemImage/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("itemImage") @Valid final ItemImageDTO itemImageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/itemImage/add";
        }
        itemImageService.create(itemImageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("itemImage.create.success"));
        return "redirect:/admin/itemImages";
    }

    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
        model.addAttribute("itemImage", itemImageService.get(seqId));
        return "admin/itemImage/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("itemImage") @Valid final ItemImageDTO itemImageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/itemImage/edit";
        }
        itemImageService.update(seqId, itemImageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("itemImage.update.success"));
        return "redirect:/admin/itemImages";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Long seqId,
            final RedirectAttributes redirectAttributes) {
        itemImageService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("itemImage.delete.success"));
        return "redirect:/admin/itemImages";
    }

}
