package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private final ItemService itemService;
    private final UserRepository userRepository;

    public HomeController(final ItemService itemService, final UserRepository userRepository) {
        this.itemService = itemService;
        this.userRepository = userRepository;
    }


    @GetMapping("/admin")
    public String admin() {
        return "admin/home/index";
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("items", itemService.findAll());
        return "member/home/index";
    }


}
