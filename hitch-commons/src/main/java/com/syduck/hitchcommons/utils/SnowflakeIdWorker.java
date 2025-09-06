package com.syduck.hitchcommons.utils;

/*
* 雪花算法一个 ID = [ 符号位0 ]+[ 41位时间戳 ]+[ 5位机房 ]+[ 5位机器 ]+[ 12位序列 ]
*
* */
public class SnowflakeIdWorker {

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 机房id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /*
     * 工作机器ID(0~31)
     */
    private final long workerId;

    /**
     * 机房ID(0~31)
     */
    private final long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /*构造方法*/
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        //支持的最大机器id，结果是31
        long maxWorkerId = ~(-1L << workerIdBits);
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("工作机器的ID不能超过最大机器ID或者小于0", maxWorkerId));
        }
        /*
         * 支持的最大机房id，结果是31
         */
        long maxDatacenterId = ~(-1L << datacenterIdBits);
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("机房的ID不能超过最大ID或者小于0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /*方法*/
    public synchronized long nextId() {
        //获取时间戳
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过 这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        //序列在 最终id 里占的位数 即 同一毫秒内 一台机器可以生成2^12个id 也就是4096
        long sequenceBits = 12L;
        if (lastTimestamp == timestamp) {
            //sequenceMask等于4095
            long sequenceMask = ~(-1L << sequenceBits);
            //超过 4095 时会自动 “回到 0”
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出--只能等待下一毫秒
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        /*移位 并通过 或运算  拼到一起组成64位的最终ID*/

        // 自定义起始时间 表示 2015-01-01 00:00:00 UTC
        long twepoch = 1420041600000L;
        long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
        long datacenterIdShift = sequenceBits + workerIdBits;
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << sequenceBits)
                | sequence;
    }

    //
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    //当前时间的时间戳
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
