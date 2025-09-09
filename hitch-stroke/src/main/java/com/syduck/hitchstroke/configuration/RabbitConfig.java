package com.syduck.hitchstroke.configuration;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//TODO:任务4.1-Rabbitmq配置
@Configuration
public class RabbitConfig {
    /**
     * 延迟时间 单位毫秒
     */
    private static final long DELAY_TIME = 1000 * 30;


    //行程超时队列
    public static final String STROKE_OVER_QUEUE = "STROKE_OVER_QUEUE";
    //行程死信队列
    public static final String STROKE_DEAD_QUEUE = "STROKE_DEAD_QUEUE";

    //行程超时队列交换器
    public static final String STROKE_OVER_QUEUE_EXCHANGE = "STROKE_OVER_QUEUE_EXCHANGE";

    //行程死信队列交换器
    public static final String STROKE_DEAD_QUEUE_EXCHANGE = "STROKE_DEAD_QUEUE_EXCHANGE";
    //行程超时交换器 ROUTINGKEY
    public static final String STROKE_OVER_KEY = "STROKE_OVER_KEY";

    //行程死信交换器 ROUTINGKEY
    public static final String STROKE_DEAD_KEY = "STROKE_DEAD_KEY";



    /**
     * 声明行程超时队列
     *
     * @return
     */
    @Bean
    public org.springframework.amqp.core.Queue strokeOverQueue() {
        //【重要配置】超时队列配置，死信队列的绑定在该方法中实现
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", STROKE_DEAD_QUEUE_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", STROKE_DEAD_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", DELAY_TIME);
        //args存着死信队列的绑定信息，如果超时，即>TTL，则消息进入死信队列，判断为超时
        return QueueBuilder.durable(STROKE_OVER_QUEUE).withArguments(args).build();
    }


    /**
     * 声明行程死信队列
     *
     * @return
     */
    @Bean
    public Queue strokeDeadQueue() {
        return new Queue(STROKE_DEAD_QUEUE, true);
    }

    /**
     * 创建行程超时队列交换器
     *
     * @return
     */
    @Bean
    DirectExchange strokeOverQueueExchange() {
        return new DirectExchange(STROKE_OVER_QUEUE_EXCHANGE, true, false);
    }

    /**
     * 创建行程死信队列交换器
     *
     * @return
     */
    @Bean
    DirectExchange strokeDeadQueueExchange() {
        return new DirectExchange(STROKE_DEAD_QUEUE_EXCHANGE, true, false);
    }



    /**
     * 行程超时队列绑定
     *
     * @return
     */
    @Bean
    Binding bindingStrokeOverDirect() {
        return BindingBuilder.bind(strokeOverQueue()).
                to(strokeOverQueueExchange()).
                with(STROKE_OVER_KEY);
    }

    /**
     * 行程死信队列绑定
     *
     * @return
     */
    @Bean
    Binding bindingStrokeDeadDirect() {
        return BindingBuilder.bind(strokeDeadQueue()).
                to(strokeDeadQueueExchange()).
                with(STROKE_DEAD_KEY);
    }



}
