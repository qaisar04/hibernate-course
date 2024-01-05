package kz.baltabayev.listener;

import kz.baltabayev.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class QaisarRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object o) {
        ((Revision) o).setUsername("qaisar");
    }
}
