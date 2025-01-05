package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    // 바로 home.html로 가게 수정.
//    @GetMapping("/")
//    public String home() {
//        return "home";
//    }

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) Member loginMember, Model model) {

        // SessionAttribute 라는 이름에서 알 수 있듯이, 세션조회 + LOGIN_MEMBER 항목 조회 기능을 합쳐서 Member 객체를 반환한다.
        // 존재하는 회원인지 조회.
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인홈으로 이동.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 세션을 통해 회원찾기. key: loginMember
        HttpSession session = request.getSession(false);

        // 세션에 데이터가 없으면 비로그인 홈으로 이동.
        if(session==null){
            return "home";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 존재하는 회원인지 조회.
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인홈으로 이동.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // SessionManager 에 저장된 회원 정보 조회.
        // request 에 저장된 sessionId 값을 쿠키에서 찾아와서 서버 세션에서 Member 객체를 조회한다.
        Member loginMember = (Member) sessionManager.getSession(request);

        // 존재하는 회원인지 조회.
        if (loginMember == null) {
            return "home";
        }

        // 로그인 성공 시.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // 로그인 처리까지 되는 home
//    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required=false) Long memberId, Model model) {
        // 비로그인 상태.
        if (memberId == null) {
            return "home";
        }

        // 존재하는 회원인지 조회.
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        // 로그인 성공 시.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}