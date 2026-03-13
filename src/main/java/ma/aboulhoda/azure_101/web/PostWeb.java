package ma.aboulhoda.azure_101.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ma.aboulhoda.azure_101.entity.AppUser;
import ma.aboulhoda.azure_101.service.PostService;
import ma.aboulhoda.azure_101.service.UserService;

@Controller
@RequiredArgsConstructor
public class PostWeb {

  private final PostService postService;
  private final UserService userService;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("posts", postService.findAllByOrderByScoreDesc());
    return "index";
  }

  @GetMapping("/posts/new")
  public String newPostPage() {
    return "new-post";
  }

  @PostMapping("/posts/create")
  public String createPost(@RequestParam("title") String title,
      @RequestParam("file") MultipartFile file,
      Principal principal) throws Exception {
    AppUser author = userService.findByUsername(principal.getName()).orElseThrow();
    postService.createPost(title, file, author);
    return "redirect:/";
  }

  @PostMapping("/posts/{id}/vote")
  public String vote(@PathVariable String id,
      @RequestParam("isUpvote") boolean isUpvote,
      Principal principal) {
    if (principal == null)
      return "redirect:/login"; // Must be logged in to vote

    AppUser user = userService.findByUsername(principal.getName()).orElseThrow();
    postService.vote(id, user, isUpvote);
    return "redirect:/";
  }

}
