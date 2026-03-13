package ma.aboulhoda.azure_101.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import ma.aboulhoda.azure_101.entity.AppUser;
import ma.aboulhoda.azure_101.service.UserService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthWeb {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @PostMapping("/auth/process")
  public String processLogin(@RequestParam String username,
      @RequestParam String password,
      HttpSession session,
      HttpServletRequest request,
      HttpServletResponse response) {

    Optional<AppUser> userOpt = userService.findByUsername(username);

    if (userOpt.isPresent()) {
      if (passwordEncoder.matches(password, userOpt.get().getPassword())) {
        logUserIn(userOpt.get(), request, response);
        return "redirect:/";
      } else {
        return "redirect:/login?error=true";
      }
    } else {
      // Save credentials in session and ask to register
      session.setAttribute("temp_user", username);
      session.setAttribute("temp_pass", password);
      return "redirect:/prompt-register";
    }
  }

  @GetMapping("/prompt-register")
  public String promptRegisterPage(HttpSession session) {
    if (session.getAttribute("temp_user") == null)
      return "redirect:/login";
    return "prompt-register";
  }

  @PostMapping("/auth/register-confirm")
  public String confirmRegistration(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    String username = (String) session.getAttribute("temp_user");
    String password = (String) session.getAttribute("temp_pass");

    if (username != null && password != null && userService.findByUsername(username).isEmpty()) {
      AppUser newUser = new AppUser();
      newUser.setUsername(username);
      newUser.setPassword(passwordEncoder.encode(password));
      userService.save(newUser);

      logUserIn(newUser, request, response);

      session.removeAttribute("temp_user");
      session.removeAttribute("temp_pass");
      return "redirect:/";
    }
    return "redirect:/login";
  }

  private void logUserIn(AppUser user, HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUsername(), null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);

    new HttpSessionSecurityContextRepository().saveContext(context, request, response);
  }
}