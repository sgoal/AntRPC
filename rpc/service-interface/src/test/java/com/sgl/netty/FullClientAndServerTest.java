package com.sgl.netty;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = SpringJUnitWebConfigIntegrationTest.Config.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:context.xml")
public class FullClientAndServerTest {
	 
	@Test
	 public void helloTest1() {
	       System.out.println("test");
	 }

}
