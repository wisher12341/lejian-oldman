package com.lejian.oldman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRepository<Bo,Entity> {


    public List<Bo> getAll() {
        List<Entity> entityList = getDao().findAll();
        return entityList.stream().map(this::convertEntity).collect(Collectors.toList());
    }

    protected abstract JpaRepository getDao();

    protected abstract Entity convertBo(Bo bo);

    protected abstract Bo convertEntity(Entity entity);
}
