package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.BorrowDTO;
import closet_share.closetshare_platform.model.BorrowStatus;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.BorrowService;
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
@RequestMapping("/borrows")
public class BorrowController {

    private final BorrowService borrowService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BorrowController(final BorrowService borrowService, final UserRepository userRepository,
            final ItemRepository itemRepository) {
        this.borrowService = borrowService;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("borrowStatusValues", BorrowStatus.values());
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getSeqId, User::getUserId)));
        model.addAttribute("itemIdValues", itemRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Item::getSeqId, Item::getTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("borrows", borrowService.findAll());
        return "borrow/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("borrow") final BorrowDTO borrowDTO) {
        return "borrow/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("borrow") @Valid final BorrowDTO borrowDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "borrow/add";
        }
        borrowService.create(borrowDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("borrow.create.success"));
        return "redirect:/borrows";
    }

    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
        model.addAttribute("borrow", borrowService.get(seqId));
        return "borrow/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("borrow") @Valid final BorrowDTO borrowDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "borrow/edit";
        }
        borrowService.update(seqId, borrowDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("borrow.update.success"));
        return "redirect:/borrows";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Long seqId,
            final RedirectAttributes redirectAttributes) {
        borrowService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("borrow.delete.success"));
        return "redirect:/borrows";
    }

}
