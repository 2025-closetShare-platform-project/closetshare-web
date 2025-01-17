package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.domain.HashTag;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.model.HashTagItemDTO;
import closet_share.closetshare_platform.repos.HashTagRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.service.HashTagItemService;
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
@RequestMapping("/hashTagItems")
public class HashTagItemController {

    private final HashTagItemService hashTagItemService;
    private final ItemRepository itemRepository;
    private final HashTagRepository hashTagRepository;

    public HashTagItemController(final HashTagItemService hashTagItemService,
            final ItemRepository itemRepository, final HashTagRepository hashTagRepository) {
        this.hashTagItemService = hashTagItemService;
        this.itemRepository = itemRepository;
        this.hashTagRepository = hashTagRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("itemIdValues", itemRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Item::getSeqId, Item::getTitle)));
        model.addAttribute("hashtagIdValues", hashTagRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(HashTag::getSeqId, HashTag::getTagName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("hashTagItems", hashTagItemService.findAll());
        return "hashTagItem/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("hashTagItem") final HashTagItemDTO hashTagItemDTO) {
        return "hashTagItem/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("hashTagItem") @Valid final HashTagItemDTO hashTagItemDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "hashTagItem/add";
        }
        hashTagItemService.create(hashTagItemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("hashTagItem.create.success"));
        return "redirect:/hashTagItems";
    }

    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
        model.addAttribute("hashTagItem", hashTagItemService.get(seqId));
        return "hashTagItem/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("hashTagItem") @Valid final HashTagItemDTO hashTagItemDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "hashTagItem/edit";
        }
        hashTagItemService.update(seqId, hashTagItemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("hashTagItem.update.success"));
        return "redirect:/hashTagItems";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Long seqId,
            final RedirectAttributes redirectAttributes) {
        hashTagItemService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("hashTagItem.delete.success"));
        return "redirect:/hashTagItems";
    }

}
