package kz.baltabayev.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class GlobalInterceptor extends EmptyInterceptor { // Deprecated
    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
