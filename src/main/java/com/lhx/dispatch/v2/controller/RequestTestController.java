package com.lhx.dispatch.v2.controller;

import com.lhx.dispatch.v2.core.TopicDispatch;
import com.lhx.dispatch.v2.entity.TopicBaseRequest;
import com.lhx.dispatch.v2.entity.TopicTestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/request")
@ResponseBody
public class RequestTestController {

    @Autowired
    private TopicDispatch topicDispatch;

    @GetMapping(value = "/test")
    public Object test1(@ModelAttribute TopicBaseRequest request) throws Exception{
        return topicDispatch.doDispatchTopic(request);
    }

    @GetMapping(value = "/test1")
    public Object test1(@ModelAttribute TopicTestRequest request) throws Exception{
        return topicDispatch.doDispatchTopic(request);
    }
}
