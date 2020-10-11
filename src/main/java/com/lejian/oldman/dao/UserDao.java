package com.lejian.oldman.dao;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.entity.UserEntity;
import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserEntity,Long>,JpaSpecificationExecutor<UserEntity> {

    UserEntity findByUsernameAndPassword(String username, String password);

    UserEntity findByUsername(String username);
}
