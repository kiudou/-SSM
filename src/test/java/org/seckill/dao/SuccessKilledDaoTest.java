package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class) //junit启动时加载springIOC容器
@ContextConfiguration({"classpath:spring/spring-dao.xml"})//告诉junit spring配置文件
public class SuccessKilledDaoTest {

    //注入Dao实现类依赖
    @Resource//依赖注入的注解
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        /**
         * 第一次:1
         *第二次：0 表示不允许多次插入，因为使用联合主键，而且xml中sql语句中有ignore，忽略报错
         */
        long id = 1001L;
        long phone = 13626801269L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1001L;
        long phone = 13626801269L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}