package com.lhx.dispatch.v2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicResult<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public TopicResult(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
