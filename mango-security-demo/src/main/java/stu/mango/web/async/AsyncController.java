package stu.mango.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/order")
public class AsyncController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final MockQueue mockQueue;

    private final DeferredResultHolder deferredResultHolder;

    @Autowired
    public AsyncController(MockQueue mockQueue, DeferredResultHolder deferredResultHolder) {
        this.mockQueue = mockQueue;
        this.deferredResultHolder = deferredResultHolder;
    }

    /**
     * 模拟使用 DeferredResult 处理异步请求，主线程和逻辑处理副线程互相不知对方的存在，
     *      只关注消息队列，借助DeferredResult传递信息。（可用于分布式服务器）
     *
     * @return 包含经副线程 “填充” 返回给前端的结果
     */
    @GetMapping
    public DeferredResult<String> order() throws Exception {
        logger.info("main thread start");
        String orderNumber = RandomStringUtils.randomNumeric(8);

        //模拟将请求放入消息队列
        mockQueue.setPlaceOrder(orderNumber);

        DeferredResult<String> deferredResult = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber, deferredResult);

        logger.info("main thread end");

        return deferredResult;
    }

    /**
     * 使用 Callable 异步处理请求，副线程由主线程调起。(可用于单个服务器)
     *
     * @return 处理具体逻辑的线程
     */
    @GetMapping("/{id:\\d+}")
    public Callable<String> getOrderInfo(@PathVariable(name = "id") String id) {
        return () -> {
            logger.info("second thread start");
            Thread.sleep(1000);
            logger.info("second thread end");
            return id + "success";
        };
    }
}
