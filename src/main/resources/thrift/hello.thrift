	namespace java com.github.wangyi.thrift.rpc.service
	
	struct Date {
			1:i32 cachedYear,
			2:i64 cachedFixedDateJan1,
			3:i64 cachedFixedDateNextJan1
	}
	
	struct Order {
			1:i64 serialVersionUID,
			2:string orderNo,
			3:string orderName,
			4:i64 orderPrice,
			5:Date createTime
	}

	service HelloWorldIface {
		 	Order sendOrder(1:Order arg0),		        	
		 	string sayHello(1:string arg0)		        	
	}
