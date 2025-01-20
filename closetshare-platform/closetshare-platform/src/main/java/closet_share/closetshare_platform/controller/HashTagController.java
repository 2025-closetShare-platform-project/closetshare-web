package closet_share.closetshare_platform.controller;

import closet_share.closetshare_platform.model.HashTagDTO;
import closet_share.closetshare_platform.service.HashTagService;
import closet_share.closetshare_platform.util.ReferencedWarning;
import closet_share.closetshare_platform.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/hashTags")
public class HashTagController {

    private final HashTagService hashTagService;

    public HashTagController(final HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("hashTags", hashTagService.findAll());
        return "admin/hashTag/list";
    }

//    @GetMapping("/add")
//    public String add(@ModelAttribute("hashTag") final HashTagDTO hashTagDTO) {
//        return "admin/hashTag/add";
//    }

//    @PostMapping("/add")
//    public String add(@ModelAttribute("hashTag") @Valid final HashTagDTO hashTagDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "admin/hashTag/add";
//        }
//        hashTagService.create(hashTagDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("hashTag.create.success"));
//        return "redirect:/admin/hashTags";
//    }

    @GetMapping("/add")
    public String addHastag(@ModelAttribute("hashTag") final HashTagDTO hashTagDTO,
                            Model model) {
        model.addAttribute("hasTags",hashTagService.findAll());


        return "admin/hashTag/hastag-add";
    }

    @PostMapping("/add")
    public String add(@RequestBody @Valid List<String> tags
           , final RedirectAttributes redirectAttributes) {

        tags.forEach(tag -> System.out.println("Received tag: " + tag));
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("hashTag.create.success"));
        return "redirect:/admin/hashTags";
    }


    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId, final Model model) {
        model.addAttribute("hashTag", hashTagService.get(seqId));
        return "admin/hashTag/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Long seqId,
            @ModelAttribute("hashTag") @Valid final HashTagDTO hashTagDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/hashTag/edit";
        }
        hashTagService.update(seqId, hashTagDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("hashTag.update.success"));
        return "redirect:/admin/hashTags";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Long seqId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = hashTagService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            hashTagService.delete(seqId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("hashTag.delete.success"));
        }
        return "redirect:/admin/hashTags";
    }

}
