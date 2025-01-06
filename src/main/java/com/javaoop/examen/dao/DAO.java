package com.javaoop.examen.dao;

import java.util.List;

public interface DAO<T> {
    List<T> findAll();
    T findById(Integer id);
    T save(T t);
    T update(Integer id , T t);
    void delete(Integer id);
}
