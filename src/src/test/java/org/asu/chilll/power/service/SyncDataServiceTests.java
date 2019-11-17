package org.asu.chilll.power.service;

import org.asu.chilll.power.service.feature.SyncDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SyncDataServiceTests {
	
	@Autowired
	private SyncDataService dataService;
	
//	@Test
//	public void syncDataTest() {
//		Long start = System.currentTimeMillis();
//		try {
//			dataService.syncData("DS");
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//		}
//		Long end = System.currentTimeMillis();
//		long responseTime = (end - start) / 1000;
//		System.out.println(responseTime + " seconds");
//	}
}