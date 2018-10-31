package com.zf.service;

import com.zf.executor.ExecutorCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class WebService {

    @Autowired
    private ExecutorCenter executorCenter;

    public void executor(){
        executorCenter.executorCenter(1);
    }

}
