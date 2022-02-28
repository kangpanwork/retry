package code;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeAspect implements Aspect {

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        TimeMap.start(target);
        return Aspect.super.before(target, method, args);
    }

    @Override
    public boolean after(Object target, Method method, Object[] args) {
        System.out.println("耗时：" + TimeMap.interval(target));
        return Aspect.super.after(target, method, args);
    }

    private static class TimeMap {
        private static Map<Object, Long> timeMap = new ConcurrentHashMap<>();

        private TimeMap(Object id) {
            Long beginTime = System.currentTimeMillis();
            timeMap.put(id, beginTime);
        }

        public static void start(Object id) {
            new TimeMap(id);
        }

        public static Long interval(Object id) {
            Long beginTime = timeMap.get(id);
            return System.currentTimeMillis() - beginTime;
        }

    }
}
