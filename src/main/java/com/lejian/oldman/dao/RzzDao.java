package com.lejian.oldman.dao;

import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.RzzEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RzzDao extends JpaRepository<RzzEntity,Long>,JpaSpecificationExecutor<RzzEntity> {

    @Query(value = "select r.type,count(r.type) as count from rzz_oldman r left join oldman o on o.oid=r.oid where r.type in (?1) and if(LENGTH(?5)>0,area_custom_one=?5,if(LENGTH(?4)>0,area_village=?4,if(LENGTH(?3)>0,area_town=?3,if(LENGTH(?2)>0,area_country=?2,1=1)))) group by r.type",nativeQuery = true)
    List<Map<String,Object>> getMapCount(List<Integer> selectValue, String areaCountry, String areaTown, String areaVillage, String areaCustomOne);

    List<RzzEntity> findByOidIn(List<String> oidList);
}
