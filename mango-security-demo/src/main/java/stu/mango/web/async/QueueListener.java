package stu.mango.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 消息队列监听器
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    private final MockQueue mockQueue;

    private final DeferredResultHolder deferredResultHolder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public QueueListener(MockQueue mockQueue, DeferredResultHolder deferredResultHolder) {
        this.mockQueue = mockQueue;
        this.deferredResultHolder = deferredResultHolder;
    }

    /**
     * 用于监听Spring容器初始化完毕的事件，即当系统启动后监听模拟消息队列的completeOrder的值
     *
     *
     * @param contextRefreshedEvent Spring容器初始化完成事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(this::listenOrder).start();
    }

    /**
     * 模拟监听消息队列
     */
    private void listenOrder() {
        while (true) {
            if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {
                String orderNumber = mockQueue.getCompleteOrder();
                logger.info("返回订单处理结果：" + orderNumber);

                // 将处理结果放入DeferredResult中
                deferredResultHolder.getMap().get(orderNumber).setResult("place order success");

                mockQueue.setCompleteOrder(null); // mock queue 处理
            } else { // 还有待处理的订单消息
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
