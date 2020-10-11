package com.lejian.oldman.repository;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.dao.UserDao;
import com.lejian.oldman.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractSpecificationRepository<UserBo,UserEntity> {

    @Autowired
    private UserDao userDao;

    @Override
    protected JpaRepository getDao() {
        return userDao;
    }

    @Override
    protected UserBo convertEntity(UserEntity userEntity) {
        if(userEntity==null){
            return null;
        }
        UserBo userBo = new UserBo();
        BeanUtils.copyProperties(userEntity,userBo);
        return userBo;
    }

    public UserBo getByUsernameAndPassword(String username, String password) {
        return convertEntity(userDao.findByUsernameAndPassword(username,password));
    }

    @Override
    protected UserEntity convertBo(UserBo userBo) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userBo,userEntity);
        return userEntity;
    }

    public UserBo getByUsername(String username) {
        return convertEntity(userDao.findByUsername(username));
    }
}
