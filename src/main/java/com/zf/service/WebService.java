package com.zf.service;

import com.zf.executor.ExecutorEvent;
import com.zf.executor.ExecutorInfo;
import com.zf.executor.ExecutorStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class WebService {

    @Autowired
    private ApplicationContext applicationContext;

    private AtomicInteger atomic = new AtomicInteger(0);

    public void executor(ExecutorInfo info) {
        int executeId = atomic.incrementAndGet();
        info.setExecuteId(executeId);
        info.setStatus(ExecutorStatus.STATUS1);
        applicationContext.publishEvent(new ExecutorEvent(info));
        //executorCenter.executorCenter(1);
    }

}
