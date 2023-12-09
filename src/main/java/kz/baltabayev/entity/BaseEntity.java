package kz.baltabayev.entity;

import java.io.Serializable;

//@Getter
//@Setter
//@MappedSuperclass
//public abstract class BaseEntity<T extends Serializable> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private T id;
//
//}

public interface BaseEntity<T extends Serializable> {

    T getId();

    void setId(T id);
}
