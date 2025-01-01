package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;

    // 바로 home.html로 가게 수정.
//    @GetMapping("/")
//    public String home() {
//        return "home";
//    }

    // 로그인 처리까지 되는 home
    @GetMapping("/")
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