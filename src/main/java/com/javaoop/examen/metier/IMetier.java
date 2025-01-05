package com.javaoop.examen.metier;

import java.util.List;

public interface IMetier<T> {
    List<T> findAll();

    T findById(Integer id);

    void save(T t);

    void update(Integer id, T t);

    void delete(Integer id);

    T search(String keyword);

    Class<T> getType();
}
