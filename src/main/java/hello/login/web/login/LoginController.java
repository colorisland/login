package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

    // 비로그인 사용자가 로그인 창으로 리다이렉트했다가 로그인 완료하고 나서 원래 접근하려했던 페이지로 다시 리다이렉트되는 코드.
    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute("loginForm") LoginForm loginForm
            , BindingResult bindingResult
            , @RequestParam(name = "redirectURL", defaultValue = "/") String redirectURL
            , HttpServletRequest request) {
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
        // 세션이 있으면 있는 세션을 반환하고 없으면 신규 세션 반환.
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관.
        // 이 과정에서 UUID를 내부에서 알아서 생성하고 response에 넣어주는 작업을 하나보다..
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        // 로그인 성공 시 홈으로 리다이렉트.
        return "redirect:"+redirectURL;
    }

//    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute("loginForm") LoginForm loginForm
            , BindingResult bindingResult
            , HttpServletRequest request) {
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
        // 세션이 있으면 있는 세션을 반환하고 없으면 신규 세션 반환.
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관.
        // 이 과정에서 UUID를 내부에서 알아서 생성하고 response에 넣어주는 작업을 하나보다..
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        // 로그인 성공 시 홈으로 리다이렉트.
        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute("loginForm") LoginForm loginForm
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
        // 세션 관리자를 통해 세션을 생성하고 회원 데이터 보관.
        sessionManager.createSession(loginMember,httpServletResponse);

        // 로그인 성공 시 홈으로 리다이렉트.
        return "redirect:/";
    }

//    @PostMapping("/login")
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
    public String logoutV3(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            // 세션에 저장된 데이터들이 다 날아감.
            session.invalidate();
        }
        // 리다이렉트를 하지 않으면 경로가 계속 /logout 으로 유지되는 상태에서 홈 화면이 보인다.
        // 해당 컨트롤러에서 어떤 기능 수행 후 다른 화면으로 넘어가야 한다면 리다이렉트 사용하기.
        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        sessionManager.expireSession(httpServletRequest);
        // 리다이렉트를 하지 않으면 경로가 계속 /logout 으로 유지되는 상태에서 홈 화면이 보인다.
        // 해당 컨트롤러에서 어떤 기능 수행 후 다른 화면으로 넘어가야 한다면 리다이렉트 사용하기.
        return "redirect:/";
    }

//    @PostMapping("/logout")
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
