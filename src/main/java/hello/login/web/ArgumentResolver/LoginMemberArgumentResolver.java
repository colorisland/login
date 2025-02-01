package hello.login.web.ArgumentResolver;

import hello.login.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static hello.login.web.SessionConst.LOGIN_MEMBER;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    // 해당 파라미터를 지원하는 건지 확인.
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        // 파라미터가 로그인 애노테이션을 가지고 있는지 확인.
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        // 파라미터 타입이 Member 인지 확인.
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    // 들어온 파라미터 정보를 가지고 작업하기.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        // getNativeRequest 메서드로 httpServletRequest 추출.
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(LOGIN_MEMBER);
    }
}
