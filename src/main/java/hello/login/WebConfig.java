package hello.login;

import hello.login.web.ArgumentResolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 인터셉트 설정 클래스에 추가.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") // 필터랑 url 패턴 문법이 좀 다르다.
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 인터셉터 타는 걸 제외할 패턴.

        // 로그인 체크 인터셉터.
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") // 필터랑 url 패턴 문법이 좀 다르다.
                // 인터셉터를 사용하면 url 패턴 체크를 인터셉터 등록할 때 하므로 역할이 분리된다.(인터셉터 로직에서 신경쓸 필요없음)
                .excludePathPatterns("/",
                        "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error"); // 인터셉터 타는 걸 제외할 패턴.
    }

    // ArgumentResolver 등록.
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    //    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 필터 체인에서 순서정하기.
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 url에 대해 적용.

        return filterRegistrationBean;
    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); // 필터 체인에서 순서정하기.
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 url에 대해 적용.

        return filterRegistrationBean;
    }
}
