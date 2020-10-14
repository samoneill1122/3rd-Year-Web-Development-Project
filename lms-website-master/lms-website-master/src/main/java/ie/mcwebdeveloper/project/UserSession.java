package ie.mcwebdeveloper.project;

import ie.mcwebdeveloper.project.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    private User user;
    private boolean loginFailed;
    private boolean signupFailed;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed) {
        this.loginFailed = loginFailed;
    }

    public boolean isSignupFailed() { return signupFailed; }

    public void setSignupFailed(boolean signupFailed) { this.signupFailed = signupFailed; }
}
