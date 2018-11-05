package com.zf.executor;

import java.util.List;

/**
 * Created by zhangfei on 2018/11/4.
 */
public class ExecutorInfo {

    public String executorId;

    public ExecutorStatus status;

    public List<String> executeCases;

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public ExecutorStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutorStatus status) {
        this.status = status;
    }

    public List<String> getExecuteCases() {
        return executeCases;
    }

    public void setExecuteCases(List<String> executeCases) {
        this.executeCases = executeCases;
    }

}
