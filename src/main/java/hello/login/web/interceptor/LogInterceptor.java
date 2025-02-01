package hello.login.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * 로그 인터셉터
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    // 로그아이디는 상수로 따로 뺀다..
    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        // 로그는 afterCompletion 에 남길 예정이라 request 에 uuid를 넘겨준다.
        request.setAttribute(LOG_ID,uuid);

        // 실행될 핸들러가 컨트롤러의 메서드인지 확인.
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
        }
        log.info("REQUEST [{}][{}][{}]",uuid,requestURI,handler);

        // true 이므로 다음으로 넘어간다.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("RESPONSE [{}][{}][{}]",logId,requestURI,handler);

        // 예외가 있는 경우 로그를 남긴다.
        if (ex != null) {
            // 에러로그는 중괄호가 필요없다.
            log.error("after Completion ERROR!!", ex);
        }

    }
}
