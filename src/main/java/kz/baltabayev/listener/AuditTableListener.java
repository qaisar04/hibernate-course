package kz.baltabayev.listener;

import kz.baltabayev.entity.Audit;
import kz.baltabayev.entity.Operation;
import org.hibernate.event.spi.*;

import java.io.Serializable;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Operation.DELETE);
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Operation.INSERT);
        return false;
    }

    private void auditEntity(AbstractPreDatabaseOperationEvent event, Operation operation) {
        if (event.getEntity().getClass() != Audit.class) {
            Audit audit = Audit.builder()
                    .entityId((Serializable) event.getId())
                    .entityName(event.getEntity().getClass().getSimpleName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().persist(audit);
        }
    }
}
