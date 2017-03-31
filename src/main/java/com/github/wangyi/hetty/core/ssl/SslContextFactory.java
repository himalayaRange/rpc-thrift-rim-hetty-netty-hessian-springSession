package com.github.wangyi.hetty.core.ssl;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.StringUtils;
import com.github.wangyi.hetty.conf.HettyConfig;
import com.github.wangyi.hetty.util.FileUtil;

/**
 * 
 * ClassName: SslContextFactory  
 * Function: Ssl山下文工厂
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 下午10:09:11 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class SslContextFactory {

	private static final String PROTOCOL="TLS";
	private static final SSLContext SERVER_CONTEXT;
	private static final HettyConfig hettyConfig=HettyConfig.getInstance();
	
	static{
		String algorithm  = Security.getProperty("ssl.KeyManagerFactory.algorithm");
		if(algorithm==null){
			algorithm = "SunX509";
		}
		SSLContext serverContext=null;
		try {
			if(!StringUtils.isEmpty(hettyConfig.getCertificateKeyFile())){
				Security.addProvider(new BouncyCastleProvider());
				
				//initalize the SSLContext to work with our key managers
				//SSL协议提供的服务：
				//1.认证用户和服务器，确保数据发送到正确的客户机和服务端
				//2.加密数据防止数据被窃取
				//3.维护数据的完整性，确保数据在传输过程中不被改变
				serverContext = SSLContext.getInstance(PROTOCOL);
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
				tmf.init(KeyStore.getInstance("JKS"));
				serverContext.init(new KeyManager[] { PEMKeyManager.instance },tmf.getTrustManagers(), null);
			}else{
				KeyStore ks = KeyStore.getInstance("JKS");
				FileInputStream fin = new FileInputStream(FileUtil.getFile(hettyConfig.getKeyStorePath()));
				ks.load(fin, hettyConfig.getKeyStorePassword().toCharArray());
				
				// Set up key manager factory to use our key store
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
				kmf.init(ks, hettyConfig.getKeyStorePassword().toCharArray());
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
                tmf.init(ks);
				// Initialize the SSLContext to work with our key managers.
				serverContext = SSLContext.getInstance(PROTOCOL);
				serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			}
			
		} catch (Exception e) {
			throw new Error("Failed to initialize the server-side SSLContext", e);
		}
		SERVER_CONTEXT=serverContext;
	}
	
	public static SSLContext getServerContext() {
		return SERVER_CONTEXT;
	}
	
	
}
