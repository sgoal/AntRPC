# AntRPC
迷你RPC

* ~~加入netty~~
* ~~支持同步和异步~~
* ~~加入zookepper，支持自动发现服务~~
* 支持HA
* ~~支持注解配置~~
* 加入Spring测试
* 处理客户端还没有找到服务的情况

# **How to  use**
## 1. 定义接口和实现类
```
public interface HelloService {
	public String sayHello(String name);
	public List<ServiceData> getData();	
}

```
通过使用`RpcServiceImpl`来标记一个服务，提供RPC。
```
@RpcServiceImpl(HelloService.class)
public class HelloServiceImpl implements HelloService {
	@Override
	public String sayHello(String name) {
		System.out.println("need hello to " + name);
		return "need hello to " + name;
	}
    @Override
	public List<ServiceData> getData() {
		return Arrays.asList(new ServiceData("test1"),new ServiceData("test2"));
	}
}
```

## 2. 配置扫描的package
使用`PackageScan`来标记需要扫描并注册服务的`Package`。
```
@PackageScan("com.sgl.impl")
public class PackageConfig {
}
```
启动服务器
```
NettyServer server = new NettyServer();
server.bind(9112);
```
## 3.同步接口的使用(不使用注册中心)
```
NettyClient client = new NettyClient();
client.connect("localhost", 9112);
HelloService service = client.createProxy(HelloService.class);
System.out.println(service.sayHello("baby coffe"));
```
## 4.异步接口的使用(不使用注册中心)
通过`RpcAsyncListener`来监听结果。
```
NettyClient client = new NettyClient();
client.connect("localhost", 9112);
RpcFutrue futrue =  client.createAsyncCall(HelloService.class, 
        "sayHello", new Object[] {"this is async call"});

futrue.addResultListener(new RpcAsyncListener() {
    @Override
    public void onResultArrived(RpcFutrue futrue) {
        try {
            Object res = futrue.get();
            System.out.println("async get res: " + res);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }
});
```

## 5.使用注册中心注册和发现服务
传入Zookeeper的地址，即完成了服务的注册。
```
NettyServer server = new NettyServer("127.0.0.1:4981");
server.bindAndRegister(9112);
```

客户端的话，传入Zookeeper地址，会通过`Watcher`监听可用的服务地址；注意，如果客户端还没有找到合适的服务，此时调用`RPC`会出错。
```
NettyClient client = new NettyClient("127.0.0.1:4981");
```
