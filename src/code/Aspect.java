package code;

import java.lang.reflect.Method;

public interface Aspect {

    default boolean before(Object target, Method method, Object[] args) {
        return true;
    }

    default boolean after(Object target, Method method, Object[] args) {
        return true;
    }
}
