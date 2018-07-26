package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 */

@RunWith(SpringJUnit4ClassRunner.class) //junit启动时加载springIOC容器
@ContextConfiguration({"classpath:spring/spring-dao.xml"})//告诉junit spring配置文件
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource //依赖注入的注解
    private SeckillDao seckillDao;


    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        System.out.println(killTime);
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount" + updateCount);
    }

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getseckillName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        /**
         *  List<Seckill> queryAll(int offset,int limit);
         *  java没有保存形参的表述：
         *      queryAll(int offset,int limit) -> queryAll(arg0, arg1)
         *  传一个参数没有问题，但是多个参数应该告诉MyBatis，哪个位置应该是哪个参数
         *  这样在xml中提取sql语句中 #{offset} 的参数时，MyBatis 才能找到该参数
         *  怎么做：修改接口,加上@Param("真正的形参")
         *      List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
         */
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for(Seckill seckill : seckills) {
            System.out.println(seckill);
        }

    }
}