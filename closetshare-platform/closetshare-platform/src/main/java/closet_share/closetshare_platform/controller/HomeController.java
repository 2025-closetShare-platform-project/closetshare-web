package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.Rq;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.HashTagService;
import closet_share.closetshare_platform.service.ItemService;
import com.sun.xml.messaging.saaj.soap.impl.ElementImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

 @Controller
public class HomeController {

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final Rq rq;  // Injecting Rq directly

    public HomeController(final ItemService itemService, final UserRepository userRepository,
                          final  Rq rq, HashTagService hashTagService) {
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.rq = rq;
        this.hashTagService = hashTagService;
    }

//    @ModelAttribute("user")
//    public Rq getSettings(@AuthenticationPrincipal Rq user) {
//       return user;
//    }


    @GetMapping("admin")
    public String admin() {
        return "admin/home/index";
    }

    @GetMapping("/")
    public String index(Model model){

        model.addAttribute("items", itemService.findAll());
        model.addAttribute("user", rq.getSiteUser());
        model.addAttribute("hashtags",hashTagService.findAll());// Adding Rq object to model
        return "member/home/index";
    }

    private final HashTagService hashTagService;
}
