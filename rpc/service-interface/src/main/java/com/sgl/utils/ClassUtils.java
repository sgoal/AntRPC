package com.sgl.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {
	
	public static List<Class<?>> getAllClassByInterface(Class<?> c){
		return getAllClassByInterface(null, c);
	}
	
	public static List<Class<?>> getAllClassByInterface(Class<? extends Annotation> anno,
			Class<?> c){
		List<Class<?>> returnClasses = null;
		if(c.isInterface()) {
			String packageName = c.getPackage().getName();
			returnClasses = getClassFromPackage(packageName, anno);
		}
		return returnClasses;
	}
	
	public static List<Class<?>> getClassFromPackage(String packageName,
			Class<? extends Annotation> filterAnnotation){
		HashSet<Class<?>> set = new HashSet<>();
		boolean recursive= true;
		try {
			set.addAll(
			getDirsFromClassLoader(packageName,filterAnnotation,
					Thread.currentThread().getContextClassLoader()));
			set.addAll(
					getDirsFromClassLoader(packageName,filterAnnotation,ClassUtils.class.getClassLoader()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		HashSet<Class<?>> set = new HashSet<>();
		return new ArrayList<>(set);
	}
	
	private static List<Class<?>> getDirsFromClassLoader(String packageName,
			Class<? extends Annotation> filterAnnotation,ClassLoader loader) throws Exception{
		List<Class<?>> clazzs = new ArrayList<>();
		String packageDir = packageName.replace('.', '/');
		boolean recursive= true;
		Enumeration<URL> dirs =  loader.
				getResources(packageDir);
		if(!dirs.hasMoreElements()) {
			return clazzs;
		}
		while(dirs.hasMoreElements()) {
			URL url =dirs.nextElement() ;
			String protocol = url.getProtocol();
			switch (protocol) {
			case "jar":
				packageName = getClassesInJar(packageName, clazzs,
						recursive, url, filterAnnotation);
				
				break; 
			case "file":
				String filePath = URLDecoder.decode(url.getFile(), "utf-8");
				findAddClassesInPackagge(packageName, filePath, true, clazzs, filterAnnotation);
				break;
			default:
				break;
			}
			
		}
		return clazzs;
	}
	
	private static String getClassesInJar(String packageName, List<Class<?>> clazzs,
			boolean recursive, URL url, Class<? extends Annotation> filterAnnotation) {
		JarFile jar;
		try {
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				// 获取jar里的�?个实�? 可以是目�? 和一些jar包里的其他文�? 如META-INF等文�?
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (name.charAt(0) == '/') {
					name = name.substring(1);
				}
				if (name.startsWith(packageName)) {
					int idx = name.lastIndexOf('/');

					if (idx != -1) {
						packageName = name.substring(0, idx).replace('/', '.');
					}
					// 如果可以迭代下去 并且是一个包
					if ((idx != -1) || recursive) {
						if (name.endsWith(".class") && !entry.isDirectory()) {
							String className = name.substring(packageName.length() + 1, name.length() - 6);
							try {
								Class<?> clazz = Class.forName(packageName + '.' + className);
								if(filterAnnotation==null) {
									clazzs.add(clazz);
								}else if (clazz.isAnnotationPresent(filterAnnotation)) {
									clazzs.add(clazz);
								}
								
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packageName;
	}
	
	public static void findAddClassesInPackagge(String packageName, String filePath,
			boolean isRecursive, List<Class<?>> outClasses, 
			Class<? extends Annotation>filterAnnotation) {
		File dir = new File(filePath);
		if(!dir.exists()||!dir.isDirectory())
			return;
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return (isRecursive&&pathname.isDirectory())||pathname.getName().endsWith(".class");
			}
		});
		for(File file:files) {
			if(file.isDirectory()) {
				findAddClassesInPackagge(packageName+"."+file.getName(), file.getAbsolutePath(), 
						isRecursive, outClasses, filterAnnotation);
			}else {
				//去掉.class
				String className = file.getName().substring(0, file.getName().length()-6);
				try {
					Class<?> clazz = Class.forName(packageName+"."+className);
					if(filterAnnotation==null) {
						outClasses.add(clazz);
					}else if(clazz.isAnnotationPresent((Class<? extends Annotation>) filterAnnotation)) {
						outClasses.add(clazz);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		
	}
}
