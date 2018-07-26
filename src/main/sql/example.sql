-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;

-- 使用数据库
USE seckill;

-- 创建秒杀库存表
CREATE TABLE seckill(
seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
seckill_name VARCHAR(120) NOT NULL COMMENT '商品名称',
number INT NOT NULL COMMENT '库存数量',
start_time TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT="秒杀库存表"

-- 初始化数据

INSERT INTO
    seckill(seckill_name,number,start_time,end_time)
VALUES
    ('1000秒杀iphone8',100,'2018-7-24 00:00:00','2018-8-1 00:00:00'),
    ('180秒杀mi',200,'2018-7-24 00:00:00','2018-8-1 00:00:00'),
    ('400秒杀honor',500,'2018-7-24 00:00:00','2018-8-1 00:00:00'),
    ('6000秒杀vivo',700,'2018-7-24 00:00:00','2018-8-1 00:00:00');
    
    
 -- 秒杀成功明细表
 
 
 -- 用户登录认证相关的信息
 
CREATE TABLE success_killed(
seckill_id BIGINT NOT NULL COMMENT '秒杀商品id',
user_phone BIGINT NOT NULL COMMENT '用户手机号',
state TINYINT NOT NULL DEFAULT -1 COMMENT '状态表示：-1：无效 0：成功 1：已付款',
create_time TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone), /*联合主键，防止用户重复秒杀*/
KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT="秒杀成功明细表"

-- 链接数据库的控制台

mysql -uroot -p

-- 为什么手写DDl
-- 记录每次上线的DDL修改
-- 上线V1.1
-- alter table seckill
-- drop index index_create_time
-- add index idx_c_s(start_time, create_time);

-- 上线V1.2
-- ddl 要求对手写ddl非常的熟悉
