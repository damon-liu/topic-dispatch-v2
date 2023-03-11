package com.lhx.dispatch.v2.core.handler;

import com.lhx.dispatch.v2.entity.TopicBaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 3:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDefaultHandle {

    private Map<String, String> pathValMap;

    private Method method;

    private TopicBaseRequest request;
}
