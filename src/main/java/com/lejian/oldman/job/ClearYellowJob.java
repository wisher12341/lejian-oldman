package com.lejian.oldman.job;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.OldmanRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 更新
 */
@Slf4j
@Component
public class ClearYellowJob {


    @Autowired
    private OldmanRepository oldmanRepository;

    public ClearYellowJob(){
        log.info("ClearYellowJob");
    }





    /**
     * 一天结束检查老人的状态， 黄灯->绿灯
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void checkOldManStatus(){
        log.info("清空黄灯老人");
        oldmanRepository.updateStatusYtoG();
    }

}
