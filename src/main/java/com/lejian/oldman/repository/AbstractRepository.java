package com.lejian.oldman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractRepository<Bo,Entity> {


    protected abstract JpaRepository getDao();

    protected abstract Entity convertBo(Bo bo);

    protected abstract Bo convertEntity(Entity entity);

}
