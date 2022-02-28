package code;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Predicate;

public class RetryInterceptor extends TimeAspect implements InvocationHandler {

    private final Object target;

    private final int retry;

    private Predicate<Object> condition;

    public RetryInterceptor(Object target, int retry) {
        this.target = target;
        this.retry = retry;
    }

    public <T> RetryInterceptor(Object target, Predicate<Object> condition, int retry) {
        this.target = target;
        this.retry = retry;
        this.condition = condition;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (before(target, method, args)) {
            if (condition != null && condition.test(target)) {
                try {
                    result = method.invoke(target, args);
                } catch (Throwable runtimeException) {
                    if (retry == 0) {
                        throw runtimeException;
                    }
                    for (int i = 0; i < retry; i++) {
                        try {
                            result = method.invoke(target, args);
                            // 执行没有异常赋值
                            i = retry;
                        } catch (Throwable runtimeException_) {
                            // 最后一次执行
                            if (retry - i == 1) {
                                throw runtimeException_;
                            }
                        }
                    }
                }
            }
        }
        if (after(target, method, args)) {
            return result;
        }
        return null;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.target.getClass().getInterfaces(), this);
    }
}
