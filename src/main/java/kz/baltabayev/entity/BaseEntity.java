package kz.baltabayev.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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

    void getId();

    void setId(T id);
}
