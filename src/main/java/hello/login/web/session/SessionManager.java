package hello.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 * 클라이언트 - 쿠키아이디(SESSION_COOKIE_NAME) -> 세션아이디 -> 세션 값(회원정보) - 서버
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";

    // 동시성 문제 해결해주는 ConcurrentHashMap 사용.
    private static Map<String, Object> sessonStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * 세션 저장소에 sessionId와 보관할 값 저장
     * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {
        // 세션 아이디를 생성하고, 값을 세션에 저장.
        String sessionId = UUID.randomUUID().toString();
        sessonStore.put(sessionId, value);

        // 클라이언트에 저장할 쿠키 생성.
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);

        // HTTP Response에 저장.
        response.addCookie(cookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionId = findCookie(request,SESSION_COOKIE_NAME);
        if (sessionId == null) {
            return null;
        }
        // 찾은 세션아이디로 세션 값 조회해서 리턴.
        return sessonStore.get(sessionId.getValue());
    }

    /**
     * 세션 만료
     * @param request
     */
    public void expireSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie != null) {
            // 세션아이디를 저달해 해당 세션 삭제.
            sessonStore.remove(cookie.getValue());
        }
    }

    /**
     * 쿠키 이름으로 세션 아이디 조회
     * @param request
     * @param sessionCookieName
     * @return
     */
    private static Cookie findCookie(HttpServletRequest request, String sessionCookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(sessionCookieName))
                .findAny()// 병렬처리로 찾은 것중에 아무거나 리턴. findFirst 보다 빠르다.
                .orElse(null);
    }
}
