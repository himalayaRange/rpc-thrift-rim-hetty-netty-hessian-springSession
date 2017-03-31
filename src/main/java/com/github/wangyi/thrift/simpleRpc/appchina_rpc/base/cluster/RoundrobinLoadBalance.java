package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.cluster;

import java.lang.ref.WeakReference;
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
 * 类说明：轮询方式负载均衡
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RoundrobinLoadBalance implements LoadBalance {

	private static final Logger LOG = LoggerFactory.getLogger(RoundrobinLoadBalance.class);
	
	protected RingItem[] items;
	
	protected volatile  RingItem current;
	
	protected final byte[] RING_LOCK = new byte[0];
	
	protected final byte[] CURRENT_LOCK=new byte[0];
	
	
	
	@Override
	public synchronized void allocat(int count) {
		if(this.items != null){
			throw new IllegalStateException("not allocated again...");
		}
		if (count <= 1) {
			RingItem item = new RingItem(0);
			item.next = new WeakReference<RingItem>(item);
			item.pre = new WeakReference<RingItem>(item);
			this.items = new RingItem[] { item };
			this.current = item;
		} else {
			RingItem[] items = new RingItem[count];
			for (int i = 0; i < count; i++) {
				items[i] = new RingItem(i);
			}
			int first = 0;
			int last = count - 1;
			for (int i = (first + 1); i < last; i++) {
				items[i].next = new WeakReference<RingItem>(items[i + 1]);
				items[i].pre = new WeakReference<RingItem>(items[i - 1]);
			}
			items[first].next = new WeakReference<RingItem>(items[first + 1]);
			items[first].pre = new WeakReference<RingItem>(items[last]);
			items[last].next = new WeakReference<RingItem>(items[first]);
			items[last].pre = new WeakReference<RingItem>(items[last - 1]);
			this.items = items;
			this.current = items[ThreadLocalRandom.current().nextInt(count)];
		}
	}

	@Override
	public void invalid(int index) {
		if(items.length>1){
			RingItem me=items[index];
			if(me.available){
				synchronized (RING_LOCK) {
					if(me.available){
						WeakReference<RingItem> myPreRef = me.pre;
						WeakReference<RingItem> myNextRef = me.next;
						RingItem myPre = myPreRef.get();
						RingItem myNext= myNextRef.get();
						if(myPre==me||myNext==me){
							return;//最后一个
						}
						myPre.next = myNextRef;
						myNext.pre = myPreRef;
						me.available = false;
						if (LOG.isInfoEnabled()) {
							LOG.info(this.toString());
						}
					}
				}
			}
		}
	}

	@Override
	public void avaliable(int index) {
		if (items.length > 1) {
			RingItem me = items[index];
			if (!me.available) {
				synchronized (RING_LOCK) {
					if (!me.available) {
						RingItem available = null;
						RingItem availableNext = null;
						for (int i = 0; i < items.length; i++) {
							RingItem item = items[i];
							if (item.available) {
								available = item;
								availableNext = available.next.get();
								break;
							}
						}
						me.pre = availableNext.pre;
						me.next = available.next;
						WeakReference<RingItem> meRef = new WeakReference<RingItem>(me);
						availableNext.pre = meRef;
						available.next = meRef; //最后连接这个防止跳跃
						me.available = true;
						if (LOG.isInfoEnabled()) {
							LOG.info(this.toString());
						}
					}
				}
			}
		}		
	}

	@Override
	public int nextIndex() {
		if(items.length>1){
			synchronized (CURRENT_LOCK) {
				current=current.next.get();
				return current.index;
			}
		}
		return current.index;
	}

	@Override
	public boolean isAvaliable(int index) {
		return items[index].available;
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public int[] getAllInvalid() {
		List<Integer> invalidIndexs = new LinkedList<Integer>();
		for (int index = 0; index < items.length; index++) {
			RingItem item = items[index];
			if (!item.available) {
				invalidIndexs.add(item.index);
			}
		}
		int[] result = new int[invalidIndexs.size()];
		int i = 0;
		for (int index : invalidIndexs) {
			result[i++] = index;
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		synchronized (RING_LOCK) {
			for (int i = 0; i < items.length; i++) {
				RingItem item = items[i];
				if (item.available) {
					sb.append("[").append(item.pre.get().index).append("<-").append(item.index).append("->").append(item.next.get().index).append("] ");
				} else {
					sb.append("[").append(item.index).append("] ");
				}
			}
		}
		return sb.toString();
	}
	
	protected static final  class RingItem{
		protected final int index;
		protected volatile WeakReference<RingItem> pre; //java的弱引用,防止内存泄露
		protected volatile WeakReference<RingItem> next;
		protected volatile boolean available = true;
		protected RingItem(int index) {
			this.index = index;
		}
	}
}
