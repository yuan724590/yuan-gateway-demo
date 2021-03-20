package yuan.gateway.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import yuan.gateway.demo.entity.GatewayFilter;
import yuan.gateway.demo.entity.GatewayPredicate;
import yuan.gateway.demo.entity.GatewayRoute;
import yuan.gateway.demo.route.DynamicRouteServiceImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用接口 动态更改配置
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    /**
     * 增加路由
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRoute route) {
        try {
            RouteDefinition definition = assembleRouteDefinition(route);
            return dynamicRouteService.add(definition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succss";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        return dynamicRouteService.delete(id);
    }

    @PostMapping("/update")
    public String update(@RequestBody GatewayRoute route) {
        RouteDefinition definition = assembleRouteDefinition(route);
        return dynamicRouteService.update(definition);
    }

    private RouteDefinition assembleRouteDefinition(GatewayRoute route) {
        RouteDefinition definition = new RouteDefinition();
        List<PredicateDefinition> pdList = new ArrayList<>();
        definition.setId(route.getId());

        List<GatewayPredicate> gatewayPredicateList = route.getPredicates();
        for (GatewayPredicate gatewayPredicate : gatewayPredicateList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gatewayPredicate.getArgs());
            predicate.setName(gatewayPredicate.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        URI uri = UriComponentsBuilder.fromHttpUrl(route.getUri()).build().toUri();
        definition.setUri(uri);

        List<FilterDefinition> filters = new ArrayList<>();
        for (GatewayFilter filterDefinition : route.getFilters()) {
            FilterDefinition filterDefinition1 = new FilterDefinition();
            filterDefinition1.setArgs(filterDefinition.getArgs());
            filterDefinition1.setName(filterDefinition.getName());
            filters.add(filterDefinition1);
        }
        definition.setFilters(filters);
        return definition;
    }
}
