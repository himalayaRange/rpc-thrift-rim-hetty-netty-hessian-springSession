package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote;

import java.lang.reflect.Method;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.client.ClientHelper;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.FSTSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.FastjsonSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.Hessian2Serializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.HessianSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.JavaSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.KryoSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.serialzer.ProtobufSerializer;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server.ServerHelper;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：基于Thrift的消息转换
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftMessageConvert implements MessageConvert<Request, Response>,InitializingBean{

	private static Logger LOG = LoggerFactory.getLogger(ThriftMessageConvert.class);
	
	private static final int EXCEPTION = -1;
	
	private Serializer  serializer = new KryoSerializer();
	
	private String serializerName="Kryo";
	
	private static Method getOurStackTraceMethod =null;
	
	static{
		try {
			getOurStackTraceMethod = Throwable.class.getDeclaredMethod("getOurStackTrace", new Class[]{});
			getOurStackTraceMethod.setAccessible(true);
			
		} catch (Exception e) {
			getOurStackTraceMethod = null;
		}
	}
	
	
	public void setSerializerName(String serializerName) {
		this.serializerName = serializerName;
	}

	public void initSerializer() {
		serializerName  = this.serializerName.toLowerCase();
		if(Objects.equals("kryo", serializerName) || StringUtils.isEmpty(serializerName)){
			//系统默认采用kryo，基于Java语言的高性能序列化工具
			serializer=new KryoSerializer();
			System.out.println("Sytem serializer Util : Kryo 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : Kryo 序列化,system default...");
			}
		}else if(Objects.equals("protobuf", serializerName)){
			serializer=new ProtobufSerializer();
			System.out.println("Sytem serializer Util : protobuf 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : protobuf 序列化...");
			}
		}else if(Objects.equals("fst", serializerName)){
			serializer=new FSTSerializer();
			System.out.println("Sytem serializer Util : FST 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : FST 序列化...");
			}
		}else if(Objects.equals("fastjson", serializerName)){
			serializer=new FastjsonSerializer();
			System.out.println("Sytem serializer Util : FASTJSON 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : FASTJSON 序列化...");
			}
		}else if(Objects.equals("hessian2", serializerName)){
			serializer=new Hessian2Serializer();
			System.out.println("Sytem serializer Util : Hessian2 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : Hessian2 序列化...");
			}
		}else if(Objects.equals("hessian", serializerName)){
			serializer=new HessianSerializer();
			System.out.println("Sytem serializer Util : Hessian 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : Hessian 序列化...");
			}
		}else if(Objects.equals("java", serializerName)){
			serializer=new JavaSerializer();
			System.out.println("Sytem serializer Util : java 序列化...");
			if(LOG.isInfoEnabled()){
				LOG.info("Sytem serializer Util : java 序列化...");
			}
		}
	}
	
	@Override
	public Request buildParameters(com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.Param param)
			throws com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.MessageConvertException {
		try {
			//[Order [stid=88888, name=三星盖乐世S7 edge, price=6888.0, picture=null, createTime=Mon Dec 26 14:08:30 CST 2016]]
			Request request = ClientHelper.newRequest(param.path);
			Object[] parameters = param.parameters;
			if(parameters!=null){
				request.setBody(serializer.getBytes(parameters));
			}
			return request;
		} catch (Throwable e) {
			throw new MessageConvertException("can not write parameters" , e);
		}
	}

	@Override
	public com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.Param readParameters(Request request)
			throws com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.MessageConvertException {
		try {
			Object[] parameters = null;
			if (request.isSetBody()) {
				parameters =(Object[]) serializer.getObject(request.getBody(),Object.class);
			}
			return new Param(request.getPath(), parameters);
		} catch (Throwable e) {
			throw new MessageConvertException("Can not read parameters.", e);
		}
	}

	@Override
	public Response buildReturn(Object data)
			throws com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.MessageConvertException {
		try {
			Response response = ServerHelper.newResponse();
			if (data != null) {
				response.setBody(serializer.getBytes(data));
			}
			return response;
		} catch (Throwable e) {
			throw new MessageConvertException("Can not write return data.", e);
		}
	}

	@Override
	public Object readReturn(Response response)
			throws com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.MessageConvertException {
		try {
			Object data = null;
			if (response.isSetBody()) {
				data = serializer.getObject(response.getBody(),Object.class);
			}
			return data;
		} catch (Throwable e) {
			throw new MessageConvertException("Can not read return data.", e);
		}
	}

	@Override
	public Response buildException(Throwable exception)
			throws com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.MessageConvertException {
		try {
			if(getOurStackTraceMethod!=null){
				//java默认不会填充异常堆栈，导致kryo序列化无法打印堆栈，调用这个会填充堆栈，并且没有内存拷贝
				try {
					getOurStackTraceMethod.invoke(exception);
				} catch (Exception e) {
					getOurStackTraceMethod = null;
				}
			}
			Response response = ServerHelper.newResponse();
			response.setBody(serializer.getBytes(exception));
			response.setStatus(EXCEPTION);
			return response;
		} catch (Exception e) {
			throw new MessageConvertException("Can not write exception.", e);
		}
	}

	@Override
	public Throwable readException(Response response)
			throws com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.MessageConvertException {
		if(EXCEPTION == response.getStatus()){
			try {
				return (Throwable) serializer.getObject(response.getBody(),Object.class);
			} catch (Throwable e) {
				throw new MessageConvertException("Can not read exception." , e);
			}
		}
		return null;
	}

	/***
	 * INIT序列化
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		initSerializer();			
	}

}
