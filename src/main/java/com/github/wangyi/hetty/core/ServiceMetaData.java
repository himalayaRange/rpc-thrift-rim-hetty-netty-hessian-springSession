package com.github.wangyi.hetty.core;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 
 * ClassName: ServiceMetaData  
 * Function: Proxy class for Hessian services
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 下午1:25:33 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceMetaData {

	private Class<?> _apiClass; //接口类
	
	private Class<?> _objectClass;//实现类
	
	private HashMap<String,Method> _methodMap = new HashMap<String,Method>();
	
	/**
	 * 
	 * Creates a new instance of ServiceMetaData. 
	 * 
	 * @param apiClass the API interface
	 * @param overloadEnabled
	 */
	public ServiceMetaData(Class<?> apiClass,boolean overloadEnabled){
		_apiClass= apiClass;
		
		Method[] methodList = apiClass.getMethods();
		
		for(int i=0;i<methodList.length;i++){
			Method method =methodList[i];
			
			if(_methodMap.get(method.getName()) == null)
				_methodMap.put(method.getName(), methodList[i]);
			
			Class<?>[] param = method.getParameterTypes();
			String mangledName  = method.getName()+"__"+param.length;
			_methodMap.put(mangledName, methodList[i]);
			
			if(overloadEnabled){
				_methodMap.put(managledName(method, false), methodList[i]);
			}
		}
	}
	
	
	/**
	 * Returns the API class for current object
	 * 
	 * getAPIClassName:
	 */
	public String getAPIClassName(){
		
		return _apiClass.getName();
	}
	
	
	/**
	 * Returns the API class for current URLS
	 * 
	 * getObjectName:
	 */
	public String getObjectName(){
			if(_objectClass!=null)
				return _objectClass.getName();
			else
				return getAPIClassName();
	}
	
	/**
	 * sets the object API class
	 * 
	 * setObjectClass:
	 */
	public void setObjectClass(Class<?> objectAPI){
		_objectClass=objectAPI;
	}
	
	/**
	 * Returns the method by the mangled name
	 * 
	 * getMethod:
	 */
	public Method getMethod(String managledName){
		
		return (Method)_methodMap.get(managledName);
	}
	
	/**
	 * creates a unique managed method name based on the method name
	 * 
	 * @param method
	 * 
	 * @param isFull
	 * 
	 * @return
	 *
	 */
	public String managledName(Method method,boolean isFull){
		StringBuffer sb = new StringBuffer(); // 线程安全的
		
		sb.append(method.getName());
		
		Class<?>[] params = method.getParameterTypes();
		for(int i=0;i<params.length;i++){
			sb.append('_');
			sb.append(mangleClass(params[i], isFull));
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Mangles a classname.
	 */
	public static String mangleClass(Class<?> cl, boolean isFull) {
		String name = cl.getName();

		if (name.equals("boolean") || name.equals("java.lang.Boolean"))
			return "boolean";
		else if (name.equals("int") || name.equals("java.lang.Integer")
				|| name.equals("short") || name.equals("java.lang.Short")
				|| name.equals("byte") || name.equals("java.lang.Byte"))
			return "int";
		else if (name.equals("long") || name.equals("java.lang.Long"))
			return "long";
		else if (name.equals("float") || name.equals("java.lang.Float")
				|| name.equals("double") || name.equals("java.lang.Double"))
			return "double";
		else if (name.equals("java.lang.String")
				|| name.equals("com.caucho.util.CharBuffer")
				|| name.equals("char") || name.equals("java.lang.Character")
				|| name.equals("java.io.Reader"))
			return "string";
		else if (name.equals("java.util.Date")
				|| name.equals("com.caucho.util.QDate"))
			return "date";
		else if (InputStream.class.isAssignableFrom(cl) || name.equals("[B"))
			return "binary";
		else if (cl.isArray()) {
			return "[" + mangleClass(cl.getComponentType(), isFull);
		} else if (name.equals("org.w3c.dom.Node")
				|| name.equals("org.w3c.dom.Element")
				|| name.equals("org.w3c.dom.Document"))
			return "xml";
		else if (isFull)
			return name;
		else {
			int p = name.lastIndexOf('.');
			if (p > 0)
				return name.substring(p + 1);
			else
				return name;
		}
	}

	public String toString() {
		return getClass().getSimpleName() + "[" + _apiClass.getName() + "]";
	}
	
}
