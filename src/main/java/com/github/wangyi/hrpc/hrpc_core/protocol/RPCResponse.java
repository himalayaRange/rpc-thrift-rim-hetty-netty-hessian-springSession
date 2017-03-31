package com.github.wangyi.hrpc.hrpc_core.protocol;
/**
 * 
 * ClassName: RPCResponse  
 * Function: HRPC response 返回包
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 上午11:41:19 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RPCResponse {

	private String requestId;
	
	private String error;
	
	private Object result;
	
	public boolean isError() {
	   return error != null;
	}

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
