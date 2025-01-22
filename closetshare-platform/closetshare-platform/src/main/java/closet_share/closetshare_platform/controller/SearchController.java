package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.Rq;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.ItemService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class SearchController {

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final Rq rq;// Injecting Rq directly

    public SearchController(final ItemService itemService, final UserRepository userRepository,
                          final  Rq rq) {
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.rq = rq;
    }


    @GetMapping("/search/result")
    public String searchResult(Model model, @RequestParam("search") String search)
    {
        model.addAttribute("items", itemService.findAll());
        model.addAttribute("user", rq.getSiteUser());
        return "member/home/index";
    }

    @PostMapping("/search")
    public String search(@RequestParam("search") String search, Model model,
                         RedirectAttributes redirectAttributes)
    {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty.");
        }
        redirectAttributes.addAttribute("search", search);
        return "redirect:/search/result";
    }

}
