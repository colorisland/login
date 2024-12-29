package hello.login.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    // 바로 home.html로 가게 수정.
    @GetMapping("/")
    public String home() {
        return "home";
    }
}