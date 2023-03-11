package com.lhx.dispatch.v2.core;

import com.lhx.dispatch.v2.anno.TopicController;
import com.lhx.dispatch.v2.anno.TopicModelAttribute;
import com.lhx.dispatch.v2.anno.TopicPathVariable;
import com.lhx.dispatch.v2.anno.TopicRequestMapping;
import com.lhx.dispatch.v2.core.security.Security;
import com.lhx.dispatch.v2.entity.TopicBaseRequest;
import com.lhx.dispatch.v2.entity.TopicResult;
import com.lhx.dispatch.v2.entity.TopicTestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TopicController
@TopicRequestMapping("/test")
@Component
@Slf4j
public class TopicTestController {

    @TopicRequestMapping("/three/{id}/{topicCode}")
    @Security()
    public Integer test2(@TopicPathVariable("id") String id, @TopicPathVariable("topicCode") String topicCode) {
        return 200;
    }

    @TopicRequestMapping("/basic/{productSn}/{modemSn}/post")
    @Security(value = "damon")
    public TopicResult<TopicTestRequest> basic(@TopicPathVariable("productSn") String productSn, @TopicPathVariable("modemSn") String modemSn,
                                               @TopicModelAttribute TopicTestRequest request) {
        Class<? extends TopicBaseRequest> clazz = request.getClass();
        List<Field> allFields = new ArrayList<>(100);
        allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        allFields.forEach(field -> {
            // 设置字段可访问， 否则无法访问private修饰的变量值
            field.setAccessible(true);
            try {
                // 获取字段名称
                String fieldName = field.getName();
                // 获取指定对象的当前字段的值
                Object fieldVal = field.get(request);
                log.info("{}: {}", fieldName, fieldVal);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return new TopicResult<>(0, "success", request);
    }

    @TopicRequestMapping("/one/{id}")
    @Security()
    public List<String> test(@TopicPathVariable("id") String id) {
        ArrayList<String> list = new ArrayList<>();
        list.add("damon");
        list.add("kangkang");
        return list;
    }

    @TopicRequestMapping("/two/{id}")
    @Security(value = "damon")
    public String test1(String topicCode, @TopicPathVariable("id") String id) {
        return "damon噢噢噢噢";
    }
}
