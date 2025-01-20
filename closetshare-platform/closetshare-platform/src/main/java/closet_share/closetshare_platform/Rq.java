package closet_share.closetshare_platform;

import closet_share.closetshare_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private Boolean isLogin;
    private User user;
    private closet_share.closetshare_platform.domain.User siteUser;
    private final UserService userService;

    public closet_share.closetshare_platform.domain.User getSiteUser() {
        if (isLogout()) return null;

        if (siteUser == null) {
            // entityManager 객체로 프록시 객체 얻기
            siteUser = userService.findByUserId(getUser().getUsername()).get();
        }

        return siteUser;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public boolean isLogin() {
        if (isLogin == null) getUser();

        return isLogin;
    }

    private User getUser() {
        if (isLogin == null) {
            user = Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .filter(authentication -> authentication.getPrincipal() instanceof User)
                    .map(authentication -> (User) authentication.getPrincipal())
                    .orElse(null);

            isLogin = user != null;
        }

        return user;
    }
}