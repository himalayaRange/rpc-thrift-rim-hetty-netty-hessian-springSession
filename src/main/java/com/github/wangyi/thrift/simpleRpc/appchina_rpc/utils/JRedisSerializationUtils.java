/*
 * Copyright (c) 2013.
 *  游戏服务器核心代码编写人陈磊拥有使用权
 *  联系方式：E-mail:13638363871@163.com ;qq:502959937
 *  个人博客主页：http://my.oschina.net/chenleijava
 */

package com.github.wangyi.thrift.simpleRpc.appchina_rpc.utils;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import javolution.util.FastTable;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import java.io.*;

/**
 * @author wangyi </br>
 *         dcserver1.3 </br>
 *         Date:14-1-9 </br>
 *         Time:下午4:00 </br>
 *         Package:{@link springJredisCache}</br>
 *         Comment： 对象序列化工具类    序列化方案基于 FST - Fast Serialization
 *         https://github.com/flapdoodle-oss/de.flapdoodle.fast-serialization
 *         <p/>
 */
public class JRedisSerializationUtils {


    public JRedisSerializationUtils() {  }


    // Serialize
    //-----------------------------------------------------------------------

    //    In order to optimize object reuse and thread safety,
    // FSTConfiguration provides 2 simple factory methods to
    // obtain input/outputstream instances (they are stored thread local):
    //! reuse this Object, it caches metadata. Performance degrades massively
    //using createDefaultConfiguration()        FSTConfiguration is singleton

    /**
     * <p>Serializes an <code>Object</code> to a byte array for
     * storage/serialization.</p>
     *
     * @param obj the object to serialize to bytes
     * @return a byte[] with the converted Serializable
     * @throws JRedisCacheException (runtime) if the serialization fails
     */
    public static byte[] fastSerialize(Object obj) throws JRedisCacheException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        FSTObjectOutput out = null;
        try {
            // stream closed in the finally
            byteArrayOutputStream = new ByteArrayOutputStream(512);
            out = new FSTObjectOutput(byteArrayOutputStream);  //32000  buffer size
            out.writeObject(obj);
            out.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            throw new JRedisCacheException(ex);
        } finally {
            try {
                obj = null;
                if (out != null) {
                    out.close();    //call flush byte buffer
                    out = null;
                }
                if (byteArrayOutputStream != null) {

                    byteArrayOutputStream.close();
                    byteArrayOutputStream = null;
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
    // Deserialize
    //-----------------------------------------------------------------------

    /**
     * <p>Deserializes a single <code>Object</code> from an array of bytes.</p>
     *
     * @param objectData the serialized object, must not be null
     * @return the deserialized object
     * @throws IllegalArgumentException if <code>objectData</code> is <code>null</code>
     * @throws JRedisCacheException     (runtime) if the serialization fails
     */
    public static Object fastDeserialize(byte[] objectData) throws Exception {
        ByteArrayInputStream byteArrayInputStream = null;
        FSTObjectInput in = null;
        try {
            // stream closed in the finally
            byteArrayInputStream = new ByteArrayInputStream(objectData);
            in = new FSTObjectInput(byteArrayInputStream);
            return in.readObject();
        } catch (ClassNotFoundException ex) {
            throw new JRedisCacheException(ex);
        } catch (IOException ex) {
            throw new JRedisCacheException(ex);
        } finally {
            try {
                objectData = null;
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                    byteArrayInputStream = null;
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    /**
     * Kryo 的包装
     */
    private static class KryoHolder {
        private Kryo kryo;
        static final int BUFFER_SIZE = 1024;
        private Output output = new Output(BUFFER_SIZE, -1);     //reuse
        private Input input=new Input();
        KryoHolder(Kryo kryo) {
            this.kryo = kryo;
        }

        /**
         * @param kryo
         * @param clazz
         */
        private static void checkRegiterNeeded(Kryo kryo, Class<?> clazz) {
            kryo.register(clazz);
        }
    }


    interface KryoPool {

        /**
         * get o kryo object
         *
         * @return
         */
        KryoHolder get();

        /**
         * return object
         *
         * @param kryo
         */
        void offer(KryoHolder kryo);
    }


    //基于kryo序列换方案

    /**
     * 由于kryo创建的代价相对较高 ，这里使用空间换时间
     * 对KryoHolder对象进行重用
     * KryoHolder会出现峰值，应该不会造成内存泄漏哦
     */
    public static class KryoPoolImpl implements KryoPool {
        /**
         * default is 1500
         * online server limit 3K
         */
        // private static int DEFAULT_MAX_KRYO_SIZE = 1500;

        /**
         * thread safe list
         */
        private final FastTable<KryoHolder> kryoFastTable = new FastTable<KryoHolder>();


        /**
         *
         */
        private KryoPoolImpl() {

        }

        /**
         * @return
         */
        public static KryoPool getInstance() {
            return Singleton.pool;
        }

        /**
         * get o KryoHolder object
         *
         * @return
         */
        @Override
        public KryoHolder get() {
           // KryoHolder kryoHolder = kryoFastTable.pollFirst(); 低版本      // Retrieves and removes the head of the queue represented by this table
            KryoHolder kryoHolder = kryoFastTable.getFirst();
            return kryoHolder == null ? creatInstnce() : kryoHolder;
        }

        /**
         * create a new kryo object to application use
         *
         * @return
         */
        public KryoHolder creatInstnce() {
            Kryo kryo = new Kryo();
            kryo.setReferences(false);//
            return new KryoHolder(kryo);
        }

        /**
         * return object
         * Inserts the specified element at the tail of this queue.
         *
         * @param kryoHolder
         */
        @Override
        public void offer(KryoHolder kryoHolder) {
            kryoFastTable.addLast(kryoHolder);
        }

        /**
         * creat a Singleton
         */
        private static class Singleton {
            private static final KryoPool pool = new KryoPoolImpl();
        }
    }


    /**
     * 将对象序列化为字节数组
     *
     * @param obj
     * @return 字节数组
     * @throws JRedisCacheException
     */
    public static byte[] kryoSerialize(Object obj) throws JRedisCacheException {
        KryoHolder kryoHolder = null;
        if (obj == null) throw new JRedisCacheException("obj can not be null");
        try {
            kryoHolder = KryoPoolImpl.getInstance().get();
            kryoHolder.output.clear();  //rest outPut
            kryoHolder.kryo.writeClassAndObject(kryoHolder.output, obj);
            return kryoHolder.output.toBytes();
        } finally {
            KryoPoolImpl.getInstance().offer(kryoHolder);
            obj = null; //GC
        }
    }


    /**
     * 将字节数组反序列化为对象
     *
     * @param bytes 字节数组
     * @return object
     * @throws JRedisCacheException
     */
    public static Object kryoDeserialize(byte[] bytes) throws JRedisCacheException {
        KryoHolder kryoHolder = null;
        if (bytes == null) throw new JRedisCacheException("bytes can not be null");
        try {
            kryoHolder = KryoPoolImpl.getInstance().get();
            kryoHolder.input.setBuffer(bytes,0,bytes.length);//call it ,and then use input object   ,data will be reset
            return kryoHolder.kryo.readClassAndObject(kryoHolder.input);
        } finally {
            KryoPoolImpl.getInstance().offer(kryoHolder);
            bytes = null;
        }
    }



    //jdk原生序列换方案

    /**
     * @param obj
     * @return
     * @throws JRedisCacheException 
     */
    public static byte[] jserialize(Object obj) throws JRedisCacheException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new JRedisCacheException(e);
        } finally {
            if (oos != null)
                try {
                    oos.close();
                    baos.close();
                } catch (IOException e) {
                }
        }
    }

    /**
     * @param bits
     * @return
     * @throws JRedisCacheException 
     */
    public static Object jdeserialize(byte[] bits) throws JRedisCacheException {
        ObjectInputStream ois = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bits);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new JRedisCacheException(e);
        } finally {
            if (ois != null)
                try {
                    ois.close();
                    bais.close();
                } catch (IOException e) {
                }
        }
    }


    // 基于protobuffer的序列化方案

    /**
     * @param bytes       字节数据
     * @param messageLite 序列化对应的类型
     * @return
     * @throws JRedisCacheException
     */
    public static MessageLite protoDeserialize(byte[] bytes, MessageLite messageLite) throws JRedisCacheException {
        assert (bytes != null && messageLite != null);
        try {
            return messageLite.getParserForType().parsePartialFrom(CodedInputStream.newInstance(bytes), ExtensionRegistryLite.getEmptyRegistry());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param messageLite 序列化对应的类型
     * @return
     * @throws JRedisCacheException
     */
    public static byte[] protoSerialize(MessageLite messageLite) throws JRedisCacheException {
        assert (messageLite != null);
        return messageLite.toByteArray();
    }


}