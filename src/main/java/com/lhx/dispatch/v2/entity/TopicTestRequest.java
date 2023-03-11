package com.lhx.dispatch.v2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicTestRequest extends TopicBaseRequest{

    private String param1;

    private Integer param2;
}
