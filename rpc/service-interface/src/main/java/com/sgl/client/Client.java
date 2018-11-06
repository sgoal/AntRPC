package com.sgl.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sgl.rpcproxy.RpcRequest;

public class Client {
	
	static public Object connectAndGet(String host, int port, RpcRequest request) {
		try {
			Socket socket = new Socket(host,port);
			ObjectOutputStream outputStream = new ObjectOutputStream(
					socket.getOutputStream());
			outputStream.writeUTF(request.getInterfaceName());
			outputStream.writeUTF(request.getMethodName());
			outputStream.writeObject(request.getParameterTypes());
			outputStream.writeObject(request.getArgs());
			
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			Object res = inputStream.readObject();
			outputStream.close();
			inputStream.close();
			socket.close();
			return res;
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
