package org.asu.chilll.power.repository;

import java.io.InputStream;
import java.util.Scanner;

//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.asu.chilll.power.repository.feature.LookupImportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class LookupImportRepositoryTests {

	@Autowired
	private LookupImportRepository repo;
	
//	@Test
	public void insertStoreItems() {
		//repo.insertStoreItems();
		
		System.out.println("DONE");
	}
}