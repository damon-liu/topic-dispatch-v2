## 一、项目介绍

topic-dispatch项目是仿springmvc部分核心原理来实现生产环境中复杂业务数据分发处理，可以简单的理解成手写一个简易的springmvc框架，目前已经实现了以下部分逻辑。

![image-20230311224547904](https://damon-study.oss-cn-shenzhen.aliyuncs.com/%20typora/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8Bimage-20230311224547904.png)

## 二、测试接口

请求地址：

http://127.0.0.1:9999/request/test1?topicUri=/test/basic/i20nbl/00001/post&topicCode=topicCode001&param1=one&param1=two

请求结果：

```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "code": 0,
        "msg": "success",
        "data": {
            "topicUri": "/test/basic/i20nbl/00001/post",
            "topicCode": "topicCode001",
            "param1": "one,two",
            "param2": null
        }
    }
}
```

## 三、核心部分

### 3.1 TopicDispatch.doDispatchTopic()方法

```java
public Object doDispatchTopic(TopicBaseRequest eq) throws Exception {
    TopicHandlerExecutionChain mappedHandler = null;
    boolean isException = false;
    Object obj;

    try {
        // 1.处理器执行链
        mappedHandler = this.getHandler(eq);
        Object handler = mappedHandler.getHandler();

        // 2.处理器适配器
        TopicHandlerAdapter adapter = this.getHandlerAdapter(handler);

        // 3.执行前置处理（拦截器前置处理）
        mappedHandler.applyPreHandle();

        // 4.处理器适配器处理请求
        obj = adapter.handle(handler);

        // 5.执行后置处理（拦截器后置处理）
        mappedHandler.applyPostHandle();
    } catch (Exception e) {
        isException = true;
        log.info(e.getMessage());
        return new TopicResult<>(-1, "fail", e.getMessage());
    } finally {
        // 执行整个请求完毕后置处理
        if (!isException && mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(null);
        }
    }

    // 返回结果集
    return new TopicResult<>(0, "success", obj);
}
```

## 写在最后

由于本人目前工作比较繁忙，无暇编写更详细的文档，请小伙伴们先自行阅读核心部分的代码，若对项目有更好建议者，请发送邮件至670682988@qq.com，如果觉得本项目比较nice,麻烦动动您发财的小手**start**一下，感谢！

