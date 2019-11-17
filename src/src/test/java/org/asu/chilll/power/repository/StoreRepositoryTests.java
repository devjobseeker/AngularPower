package org.asu.chilll.power.repository;

import java.util.List;

import org.asu.chilll.power.entity.lookup.LookupStoreItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class StoreRepositoryTests {
	
	@Autowired
	private StoreRepository repo;
	
//	@Test
//	public void fetchPurchasedItems() {
//		List<LookupStoreItem> results = repo.fetchPurchasedItems("aaa", "K");
//		System.out.println("========================================");
//		System.out.println(results.size());
//	}
}
