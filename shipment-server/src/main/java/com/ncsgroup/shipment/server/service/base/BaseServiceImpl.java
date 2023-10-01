package com.ncsgroup.shipment.server.service.base;

import com.ncsgroup.shipment.server.repository.BaseRepository;


import java.util.List;

public class BaseServiceImpl<T> implements BaseService<T> {
    private final BaseRepository<T> repository;

    public BaseServiceImpl(BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T create(T t) {
        return repository.save(t);
    }

    @Override
    public T update(T t) {
        return repository.save(t);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public T get(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<T> list() {
        return repository.findAll();
    }
}
