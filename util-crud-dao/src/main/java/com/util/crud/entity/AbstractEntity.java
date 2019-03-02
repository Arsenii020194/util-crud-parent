package com.util.crud.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
public class AbstractEntity<T> {
    private T id;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
