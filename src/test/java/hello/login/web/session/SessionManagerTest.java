package hello.login.web.session;

import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

public class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        // 1. 세션 생성
        Member member = new Member();
        member.setLoginId("aaa@gmail.com");
        member.setName("name");
        member.setPassword("test!");

        // 원래 내장 웹서버인 톰캣이 만들어주는건데 스프링이 목데이터로 HttpServletResponse 제공해준다.
        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(member,response);

        // 2. 요청에 응답 쿠키 저장(원래는 클라이언트에서 하는 것.)
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); // mySessionId = 123-123-124142-123

        // 3. 세션 조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        // 4. 세션 만료
        sessionManager.expireSession(request);
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();
    }
}
