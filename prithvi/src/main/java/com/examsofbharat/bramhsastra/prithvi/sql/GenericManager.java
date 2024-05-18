package com.examsofbharat.bramhsastra.prithvi.sql;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class GenericManager<T, ID> {

    public CrudRepository<T, ID> crudRepository;

    public void setCrudRepository(CrudRepository<T, ID> crudRepository) {
        this.crudRepository = crudRepository;
    }

    public <S extends T> S save(S var1) {
        return crudRepository.save(var1);
    }

    public <S extends T> Iterable<S> saveAll(Iterable<S> var1) {
        return crudRepository.saveAll(var1);
    }

    public Optional<T> findById(ID var1) {
        return crudRepository.findById(var1);
    }

    public boolean existsById(ID var1) {
        return crudRepository.existsById(var1);
    }

    public Iterable<T> findAll() {
        return crudRepository.findAll();
    }

    public Iterable<T> findAllById(Iterable<ID> var1) {
        return crudRepository.findAllById(var1);
    }

    public long count() {
        return crudRepository.count();
    }

    public void deleteById(ID var1) {
        crudRepository.deleteById(var1);
    }

    public void delete(T var1) {
        crudRepository.delete(var1);
    }

    public void deleteAll(Iterable<? extends T> var1) {
        crudRepository.deleteAll(var1);
    }

    public void deleteAll() {
        crudRepository.deleteAll();
    }
}
