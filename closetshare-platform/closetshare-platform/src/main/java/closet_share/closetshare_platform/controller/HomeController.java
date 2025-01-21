package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.Rq;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.ItemService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class HomeController {

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final Rq rq;  // Injecting Rq directly

    public HomeController(final ItemService itemService, final UserRepository userRepository,
                          final  Rq rq) {
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.rq = rq;
    }

//    @ModelAttribute("user")
//    public Rq getSettings(@AuthenticationPrincipal Rq user) {
//       return user;
//    }


    @GetMapping("/admin")
    public String admin() {
        return "admin/home/index";
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("items", itemService.findAll());
        model.addAttribute("user", rq.getSiteUser());  // Adding Rq object to model
        return "member/home/index";
    }
}
