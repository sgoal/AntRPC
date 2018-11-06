package com.sgl.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import com.sgl.impl.HelloServiceImpl;
import com.sgl.impl.InterfaceManager;

/**
 * bio server
 * @author liuhuadong
 *
 */
public class Server extends Thread{
	static {
		HelloServiceImpl.register();
	}
    @Override
	public void run() {
    	ServerSocket serverSocket  = null;
		try {
			serverSocket  = new ServerSocket(9112);
			for(;;) {
				Socket socket =  serverSocket.accept();
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				
				//interface->method->parameters->args
				String interfaceName = inputStream.readUTF();
				String methodName = inputStream.readUTF();
				
				Class[] parameterTypes = (Class[]) inputStream.readObject();
				Object[] args = (Object[]) inputStream.readObject();
				
				Class<?> implClass = InterfaceManager.getInstance().findClass(interfaceName);

				Method method =  implClass.getMethod(methodName, parameterTypes);
				Object res =  method.invoke(implClass.newInstance(), args);
				
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(res);
				
				outputStream.flush();
				outputStream.close();
				inputStream.close();

				socket.close();
			}
			
		} catch (IOException | ClassNotFoundException | NoSuchMethodException 
				| SecurityException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}finally {
			try {
				if(serverSocket!=null) {
				serverSocket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	super.run();
		
	}
}
