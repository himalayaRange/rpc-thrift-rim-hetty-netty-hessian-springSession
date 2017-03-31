package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.cluster;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：随机方式均衡，支持重新分配池的大小，暂不支持权重分配（可以通过实例数量的配置实现）
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RandomLoadBalance implements LoadBalance{

	private static final Logger LOG = LoggerFactory.getLogger(RandomLoadBalance.class);
	
	protected  boolean[] poolStatus;
		
	@Override
	public synchronized void allocat(int count) {
		boolean[] _poolsStatus=new boolean[count<=1?1:count];
		Arrays.fill(_poolsStatus, true); //将数据的值全部填充为true
		poolStatus=_poolsStatus;
	}

	@Override
	public void invalid(int index) {
		boolean[] _poolsStatus=poolStatus;
		if(index<_poolsStatus.length && _poolsStatus[index]){
			_poolsStatus[index]=false;
			if(LOG.isInfoEnabled()){
				LOG.info(this.toString());
			}
		}
	}

	@Override
	public void avaliable(int index) {
		boolean[] _poolsStatus = poolStatus;
		if(index < _poolsStatus.length && !_poolsStatus[index]){
			_poolsStatus[index] = true;
			if (LOG.isInfoEnabled()) {
				LOG.info(this.toString());
			}
		}		
	}

	@Override
	public int nextIndex() {
		boolean[] _poolsStatus=poolStatus;
		if(_poolsStatus.length<=1){
			return 0;
		}
		//有无效的负载均衡点，需要去除
		if(hasInvalid(_poolsStatus)){
			int[] availableIndexs =new int[_poolsStatus.length]; //可用位置样本
			int availableCount=0;
			for(int i=0;i<_poolsStatus.length;i++){
				if(!_poolsStatus[i]){
					availableIndexs[availableCount++]=i; //记录样本数组
				}
			}
			if(availableCount >0){
				int randomIndex=ThreadLocalRandom.current().nextInt(availableCount);
				return availableIndexs[randomIndex];
			}
		}
		//全部是有效的均衡点，随机产生
		return ThreadLocalRandom.current().nextInt(_poolsStatus.length);
	}

	@Override
	public boolean isAvaliable(int index) {
		boolean[] _poolsStatus=poolStatus;
		return index<_poolsStatus.length&&_poolsStatus[index];
	}

	@Override
	public int getCount() {
		return poolStatus.length;
	}

	@Override
	public int[] getAllInvalid() {
		boolean[] _poolsStatus=poolStatus;
		List<Integer> invalidIndexs = new LinkedList<Integer>();
		for(int index=0;index<_poolsStatus.length;index++){
			if(!_poolsStatus[index]){
				invalidIndexs.add(index);
			}
		}
		int[] result=new int[invalidIndexs.size()];
		int i=0;
		for(int index:invalidIndexs){
			result[i++]=index;
		}
		return result;
	}
	
	//是有有无效的均衡点
	protected boolean hasInvalid(boolean[] _poolsStatus){
		for(int i=0;i<_poolsStatus.length;i++){
			if(!_poolsStatus[i]){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + Arrays.toString(poolStatus) + "]";
	}
}
