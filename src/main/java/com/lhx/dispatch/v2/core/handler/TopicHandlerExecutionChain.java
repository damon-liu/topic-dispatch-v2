package com.lhx.dispatch.v2.core.handler;

import cn.hutool.core.util.ObjectUtil;
import com.lhx.dispatch.v2.core.interceptor.TopicHandlerInterceptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 3:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicHandlerExecutionChain {

    private Object handler;

    private TopicHandlerInterceptor[] interceptors;

    @Nullable
    private List<TopicHandlerInterceptor> interceptorList;

    public TopicHandlerExecutionChain(Object handler) {
        this.handler = handler;
    }

    public void applyPreHandle() throws Exception {
        TopicHandlerInterceptor[] interceptors = getInterceptors();
        if (interceptors != null && interceptors.length > 0) {
            for (int i = 0; i < interceptors.length; i++) {
                TopicHandlerInterceptor interceptor = interceptors[i];
                interceptor.preHandle(this.handler);
            }
        }
    }

    public void applyPostHandle() throws Exception {
        TopicHandlerInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtil.isEmpty(interceptors)) {
            for (int i = interceptors.length - 1; i >= 0; i--) {
                TopicHandlerInterceptor interceptor = interceptors[i];
                interceptor.postHandle(this.handler);
            }
        }
    }

    /**
     * 整个请求处理完毕回调方法
     *
     * @param ex       异常
     * @throws Exception
     */
    public void triggerAfterCompletion(Exception ex) throws Exception {
        TopicHandlerInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtil.isEmpty(interceptors)) {
            for (int i = interceptors.length - 1; i >= 0; i--) {
                TopicHandlerInterceptor interceptor = interceptors[i];
                interceptor.afterCompletion(this.handler, ex);
            }
        }
    }

    public TopicHandlerInterceptor[] getInterceptors() {
        if (this.interceptors == null && this.interceptorList != null) {
            this.interceptors = this.interceptorList.toArray(new TopicHandlerInterceptor[0]);
        }
        return this.interceptors;
    }
}
