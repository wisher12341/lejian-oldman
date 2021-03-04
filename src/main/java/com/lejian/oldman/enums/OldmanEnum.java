package com.lejian.oldman.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 老人属性 枚举
 */
public interface OldmanEnum extends BusinessEnum{


    /**
     * 老人服务状态（颜色）
     */
    @Getter
    @AllArgsConstructor
    enum Status implements OldmanEnum{
        GREEN(1,"正常"),
        YELLOW(2,"服务中"),
        RED(3,"异常");
        private Integer value;
        private String desc;
    }

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    enum Sex implements OldmanEnum{
        MAN(1,"男"),
        WOMAN(2,"女");
        private Integer value;
        private String desc;
    }


    /**
     * 政治面貌
     */
    @Getter
    @AllArgsConstructor
    enum Politics implements OldmanEnum{
        MASSES(1,"群众"),
        MEMBER(2,"中共党员"),
        OTHER(3,"其他");
        private Integer value;
        private String desc;
    }


    /**
     * 学历
     */
    @Getter
    @AllArgsConstructor
    enum Education implements OldmanEnum{
        PRIMARY(1,"小学"),
        JUNIOR(2,"初中"),
        HIGH(3,"高中"),
        SECONDARY(4,"中专"),
        JUNIOR_COLLEGE(5,"大专"),
        BACHELOR(6,"本科"),
        MASTER(7,"硕士"),
        DOCTOR(8,"博士"),
        ILLITERACY(9,"文盲");
        private Integer value;
        private String desc;
    }


    /**
     * 户口性质
     */
    @Getter
    @AllArgsConstructor
    enum HouseholdType implements OldmanEnum{
        LOCAL(1,"本地"),
        NO_LOCAL(2,"非本地"),
        SEPARATION(3,"人户分离");
        private Integer value;
        private String desc;
    }

    /**
     * 家庭结构
     */
    @Getter
    @AllArgsConstructor
    enum FamilyType implements OldmanEnum{
        CHUNLAO(1,"纯老"),
        DUJU(2,"独居"),
        SHUDU(3,"失独"),
        GULAO(4,"孤老"),
        YLYYL(5,"一老养一老"),
        SZRY(6,"三支人员"),
        OTHER(0,"其他"),
        ;
        private Integer value;
        private String desc;
    }

    /**
     * 居家养老服务类型
     */
    @Getter
    @AllArgsConstructor
    enum ServiceType implements OldmanEnum {
        CHX(1, "长护险") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(CHX);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(1,101,102,104);
            }
        },
        JTFW(2, "家庭服务") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(JTFW);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(2,101,103,104);
            }
        },
        JJYLFW(3, "居家养老服务") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(JJYLFW);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(3,102,103,104);
            }
        },
        CHX_JTFW(101, "长护险+家庭服务") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(CHX, JTFW);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(101);
            }
        },
        CHX_JJYLFW(102, "长护险+居家养老服务") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(CHX, JJYLFW);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(102);
            }
        },
        JTFW_JJYLFW(103, "家庭服务+居家养老服务") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(JTFW, JJYLFW);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(103);
            }
        },
        CHX_JTFW_JJYLFW(104, "家庭服务+家庭服务+居家养老服务") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(CHX, JTFW, JJYLFW);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(104);
            }
        },
        //todo 组合 开会确认 哪些会交叉
        RZZ(4,"认知症服務老人") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(RZZ);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(4);
            }
        },
        DB(5,"大病服務老人") {
            @Override
            public List<ServiceType> map() {
                return Lists.newArrayList(DB);
            }

            @Override
            public List<Integer> getSearchValue() {
                return Lists.newArrayList(5);
            }
        };
        private Integer value;
        private String desc;

        /**
         * 拆除组合
         *
         * @return
         */
        public abstract List<ServiceType> map();

        /**
         * 获取查询数据库的 list值
         *
         * @return
         */
        public abstract List<Integer> getSearchValue();
    }

    /**
     * 服务状态
     */
    @Getter
    @AllArgsConstructor
    enum ServiceStatus implements OldmanEnum{
        ZC(0,"正常"),
        DUJU(1,"转出"),
        SHUDU(2,"死亡"),
        GULAO(3,"临时暂停"),
        YLYYL(4,"长期暂停"),
        ;
        private Integer value;
        private String desc;
    }

}
