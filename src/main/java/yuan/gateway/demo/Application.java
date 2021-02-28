package yuan.gateway.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import yuan.gateway.demo.filter.CustomGatewayFilter;
import yuan.gateway.demo.filter.GatewayRateLimitFilterByIp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
public class Application {

//通过代码设置映射
    @Bean
    public RouteLocator testRouteLocator(RouteLocatorBuilder builder) {
        //生成比当前时间早2天的UTC时间
        ZonedDateTime minusTime = LocalDateTime.now().minusDays(2).atZone(ZoneId.systemDefault());
        ZonedDateTime datetime1 = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault());
        ZonedDateTime datetime2 = LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault());
        return builder.routes()
//                /**
//                 * 请求时间在minusTime之前的都跳转到uri
//                 * 127.0.0.1:40004
//                 */
//                .route("before", r -> r.before(minusTime)
//                        .uri("http://www.baidu.com"))
//                /**
//                 * 请求时间在minusTime之后的都跳转到uri
//                 * 127.0.0.1:40004
//                 */
//                .route("after", r -> r.after(minusTime)
//                        .uri("http://www.baidu.com"))
//                .route("between", r -> r.between(datetime1, datetime2)
//                        .uri("http://www.baidu.com"))
                /**
                 * cookie中chocolate=aaa的(需要在postman中额外配置cookie)
                 */
                .route("cookie", r -> r.cookie("chocolate", "aaa")
                        .uri("http://www.baidu.com"))
                /**
                 * 在header中host值能够匹配上**.baidu.com的
                 */
                .route("host", r -> r.host("**.baidu.com")
                        .uri("http://www.baidu.com"))
                /**
                 * 请求类型为PUT,也可以设置为GET,POST
                 * PUT 127.0.0.1:40004?authToken=1
                 */
                .route("method", r -> r.method("PUT")
                        .uri("http://www.baidu.com"))
                /**
                 * 参数name=yuan
                 * 127.0.0.1:40004?authToken=1&name=yuan
                 */
                .route("query", r -> r.query("name","yuan")
                        .uri("http://baidu.com"))
                /**
                 * 访问http://127.0.0.1:40004/test/head
                 *  这里强制为请求增加了两个header头
                 */
                .route("add_request_header_route", r -> r.path("/test/head")
                    .filters(f -> {
                        f.addRequestHeader("X-Request", "aa");
                        f.addRequestParameter("hello", "ValueB");
                        //进行重试
                        f.retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR));
                        f.rewritePath("test", "bb");
                        return f;
                    })
                    .uri("http://127.0.0.1:40003/test/head"))
                /**
                 * 访问http://127.0.0.1:40004/foo/cache/sethelp/help.html
                 *  这里相当于去掉前缀foo，
                 * 直接访问http://www.baidu.com/cache/sethelp/help.html
                 */
                .route("rewritepath_route", r ->
                        r.path("/foo/**").filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/$\\{segment}"))
                                .uri("http://www.baidu.com"))
                /**
                 * 引入filter文件
                 * 127.0.0.1:40004/test1?authToken=1
                 */
                .route(r -> r.path("/test1")
                        .filters(f -> f.filter(new CustomGatewayFilter()))
                        .uri("http://localhost:8001/customFilter?name=sailor"))
                /**
                 * 限流
                 * 127.0.0.1:40004/testLimit?authToken=1
                 */
                .route(r -> r.path("/testLimit")
                        // 桶的最大容量（即能装载的token的最大数量）、每次token补充量、补充token的时间间隔, 请求一次减少一个,如果为空就禁止访问
                        .filters(f -> f.filter(new GatewayRateLimitFilterByIp(3, 1, Duration.ofSeconds(100))))
                        .uri("http://www.baidu.com"))
                /**
                 * 机器ip为127.0.0.1
                 * 127.0.0.1:40004?authToken=1
                 */
                .route("remoteaddr", r -> r.remoteAddr("127.0.0.1")
                        .uri("http://baidu.com"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
