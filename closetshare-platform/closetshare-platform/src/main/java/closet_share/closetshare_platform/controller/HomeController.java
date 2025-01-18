package closet_share.closetshare_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/admin")
    public String admin() {
        return "admin/home/index";
    }

    @GetMapping("/")
    public String index(){
        return "member/home/index";
    }


}
