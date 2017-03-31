
package com.github.wangyi.hrpc.hrpc_core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.hrpc.hrpc_core.exception.RPCTimeoutException;
import com.github.wangyi.hrpc.hrpc_core.exception.ResponseException;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCRequest;
import com.github.wangyi.hrpc.hrpc_core.protocol.RPCResponse;
import java.util.concurrent.*;

/**
 * 
 * ClassName: RPCFuture  
 * Function: RPC 请求处理
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-24 下午4:44:10 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RPCFuture implements Future<Object> {
    private static final Logger logger = LoggerFactory.getLogger(RPCFuture.class);

    private CountDownLatch countDownLatch;
    //堆积的待执行callback
    private AsyncRPCCallback callback;

    private RPCRequest request;
    private RPCResponse response;
    private long startTime;

    /**
     * RPC 同步请求
     * Creates a new instance of RPCFuture. 
     * 
     * @param request
     */
    public RPCFuture(RPCRequest request) {
        countDownLatch = new CountDownLatch(1);
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * RPC 异步请求
     * Creates a new instance of RPCFuture. 
     * 
     * @param request
     * @param callback
     */
    public RPCFuture(RPCRequest request,AsyncRPCCallback callback) {
        countDownLatch = new CountDownLatch(1);
        this.callback=callback;
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response.getResult();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean awaitSuccess = false;
        try {
            awaitSuccess = countDownLatch.await(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!awaitSuccess) {
            throw new RPCTimeoutException();
        }
        long useTime = System.currentTimeMillis() - startTime;
        logger.info("request id: {} class: {} method: {} useTime {}ms",
                request.getRequestId(), request.getClassName(), request.getMethodName() , useTime);
        return response.getResult();
    }

    public void done(RPCResponse res) {
        response = res;
        countDownLatch.countDown();
        if (callback != null) {
            if (!response.isError()) {
                callback.success(response.getResult());
            } else {
                callback.fail(new ResponseException(response.getError()));
            }
        }
        long useTime = System.currentTimeMillis() - startTime;
       /* logger.info("has done requestId: {} class: {} method: {} useTime: {}",
                request.getRequestId(), request.getClassName(), request.getMethodName() , useTime);*/
    }


    @SuppressWarnings("unused")
	private void setCallback(AsyncRPCCallback callback) {
        this.callback = callback;
        if (isDone()) {
            if (!response.isError()) {
                callback.success(response.getResult());
            } else {
                callback.fail(new ResponseException(response.getError()));
            }
        }
    }

}
