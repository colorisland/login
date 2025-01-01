package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm
            , BindingResult bindingResult
            , HttpServletResponse httpServletResponse) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        // 로그인 결과.
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리.
        // 성공 후 id를 쿠키에 담아 반환.
        // 쿠키에 시간 정보를 주지 않으면 브라우저 종료 시 삭제됨. (세션 쿠키)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        httpServletResponse.addCookie(idCookie);
        // 로그인 성공 시 홈으로 리다이렉트.
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse) {
        expireCookie(httpServletResponse, "memberId");
        // 리다이렉트를 하지 않으면 경로가 계속 /logout 으로 유지되는 상태에서 홈 화면이 보인다.
        // 해당 컨트롤러에서 어떤 기능 수행 후 다른 화면으로 넘어가야 한다면 리다이렉트 사용하기.
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse httpServletResponse, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }
}
