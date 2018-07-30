package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 直接写类，没有必要写接口
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class); //.class拿到字节码

    public Seckill getSeckill(long seckillId) {
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                //并没有实现内部序列化操作
                //get->byte[] -> 反序列化 -> Object(Seckill)
                //采用自定义序列化
                //protostuff : pojo
                byte[] bytes = jedis.get(key.getBytes()); //转化成字节流
                //从缓存中重新获取
                if(bytes != null) {
                    Seckill seckill = schema.newMessage(); //空对象
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema); //将bytes 反序列化到seckill中
                    //seckill 被反序列化
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        //set Object[Seckill] -> 序列化 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));//默认的大小
                //超市缓存
                int timeout = 60*60; //一小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
