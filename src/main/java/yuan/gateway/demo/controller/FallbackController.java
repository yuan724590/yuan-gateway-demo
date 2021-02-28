package yuan.gateway.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FallbackController {

    /**
     * 回滚方法
     */
    @GetMapping("/fallback")
    public String fallback() {
        return "Fallback！aaaaaaaaaaaaaaaaaaa";
    }

//调用的方法
//    @GetMapping("/test/Hystrix")
//    public String index(@RequestParam("isSleep") boolean isSleep) throws InterruptedException {
//        log.info("issleep is " + isSleep);
//        //isSleep为true开始睡眠，睡眠时间大于Gateway中的fallback设置的时间
//        if (isSleep) {
//            TimeUnit.SECONDS.sleep(5);
//        }
//        return "No Sleep";
//    }

}
