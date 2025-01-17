package hello.login.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("log filter doFilter");

        // 그냥 ServletRequest 는 자주 사용하는 HttpServletRequest 의 부모이다.
        // 우리는 HttpServletRequest 를 이용하니까 다운캐스팅 해준다.
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            // {} 부분이 치환이 된다.
            log.info("REQUEST [{}] [{}]", uuid, requestURI);
            // doFilter 로 필터체인을 안넘기면 서블릿이 진행이 안된다.
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}] [{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
