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

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("interestedProductses", interestedProductsService.findAll());
        return "interestedProducts/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("interestedProducts") final InterestedProductsDTO interestedProductsDTO) {
        return "interestedProducts/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("interestedProducts") @Valid final InterestedProductsDTO interestedProductsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "interestedProducts/add";
        }
        interestedProductsService.create(interestedProductsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("interestedProducts.create.success"));
        return "redirect:/interestedProductss";
    }

    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
        model.addAttribute("interestedProducts", interestedProductsService.get(seqId));
        return "interestedProducts/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("interestedProducts") @Valid final InterestedProductsDTO interestedProductsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "interestedProducts/edit";
        }
        interestedProductsService.update(seqId, interestedProductsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("interestedProducts.update.success"));
        return "redirect:/interestedProductss";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Long seqId,
            final RedirectAttributes redirectAttributes) {
        interestedProductsService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("interestedProducts.delete.success"));
        return "redirect:/interestedProductss";
    }

}
