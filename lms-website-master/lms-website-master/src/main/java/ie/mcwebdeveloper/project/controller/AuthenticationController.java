package ie.mcwebdeveloper.project.controller;

import ie.mcwebdeveloper.project.UserSession;
import ie.mcwebdeveloper.project.models.User;
import ie.mcwebdeveloper.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    private UserSession userSession;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("title", "LMS - Login");
        if(userSession.isLoginFailed()) {
            model.addAttribute("message", "Username/password incorrect");
            userSession.setLoginFailed(false);
        }
        return "login.html";
    }

    @PostMapping("/login")
    public void login(String username, String password, HttpServletResponse response, Model model) throws Exception {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if(user.isPresent()) {
            userSession.setUser(user.get());
            response.sendRedirect("/");
        } else {
            userSession.setLoginFailed(true);
            response.sendRedirect("/login");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response, Model model) throws  Exception {
        userSession.setUser(null);
        model.addAttribute("logoutMessage", "You have logged out successfully.");
        response.sendRedirect("/");

    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("title", "LMS - Signup");
        if(userSession.isSignupFailed()) {
            model.addAttribute("message", "Username taken.");
            userSession.setSignupFailed(false);
        }
        return "signup.html";
    }

    @PostMapping("/signup")
    public void signup(User theUser, Model model, HttpServletResponse response) throws Exception {
        System.out.println(theUser.getUsername());
        if(userRepository.existsByUsername(theUser.getUsername())) {
            userSession.setSignupFailed(true);
            response.sendRedirect("/signup");
        } else {
            userRepository.save(theUser);
            userSession.setUser(theUser);
            response.sendRedirect("/");
        }
    }

}
