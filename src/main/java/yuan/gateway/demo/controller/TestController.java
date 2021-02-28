//package yuan.gateway.demo.controller;
//
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//@RestController
//public class TestController {
//
//
//    @GetMapping("/test/head")
//    public String testGatewayHead(HttpServletRequest request) {
//        String head = request.getHeader("X-Request");
//        String parameter = request.getParameter("hello");
//        return "return head info:" + head + parameter;
//    }
//}
