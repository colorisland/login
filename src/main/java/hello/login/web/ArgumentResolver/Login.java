package hello.login.web.ArgumentResolver;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 파라미터에 적용될 애노테이션.
@Target(ElementType.PARAMETER)
// 런타임 내내 살아있고 접근가능하도록 제한.
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
