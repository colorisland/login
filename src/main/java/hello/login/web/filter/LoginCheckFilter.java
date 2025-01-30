package hello.login.web.filter;

import hello.login.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 화이트리스트 말고는 다 필터에 걸림.
    private static final String[] whiteList={"/","/members/add","/login","/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("인증 체크 필터 시작{}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpRequest.getSession(false);

                // 세션이 없거나 해당 uuid 에 해당하는 사용자 정보가 없으면 미인증 사용자이다.
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인으로 redirect 했다가 성공하면 현재 페이지로 다시 한번 Redirect 하게 한다.
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    // 미인증 사용자는 여기서 끝! (중요한 코드)
                    return;
                }
            }
            // 다음 필터로 넘어가기. (이거 꼭 해줘야 한다. 안그러면 로직이 그냥 끝나버린다.)
            filterChain.doFilter(httpRequest, httpResponse);
        } catch (Exception e) {
            log.info("인증 체크 로직 실패 {}", requestURI);
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }

    /**
     * 화이트 리스트의 경우 인증체크 X
     * @param requestURI
     * @return
     */
    private boolean isLoginCheckPath(String requestURI) {
        // 패턴 매치되는지 확인. 이 함수 자주쓴다.
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
