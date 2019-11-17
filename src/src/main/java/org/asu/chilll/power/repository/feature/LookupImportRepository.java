package org.asu.chilll.power.repository.feature;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.lookup.LookupStoreItem;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LookupImportRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public Boolean insertStoreItems() {
		List<LookupStoreItem> li = new ArrayList<LookupStoreItem>();
		
		String dataFile = "storeItems.csv";
		String cvsSplitBy = ",";
		InputStream is = null;
		
		try {
			ClassPathResource resource = new ClassPathResource(dataFile);
			is = resource.getInputStream();
			@SuppressWarnings("resource")
			Scanner s = new Scanner(is).useDelimiter("\\A");
			s.nextLine();	//skip the header
			while(s.hasNext()) {
				String itemRow = s.nextLine();
				String[] item = itemRow.split(cvsSplitBy);
				LookupStoreItem lookup = new LookupStoreItem();
				lookup.setItemId(Integer.parseInt(item[0]));
				lookup.setItemName(item[1].replaceAll("\"", ""));
				lookup.setItemSrc(item[2].replaceAll("\"", ""));
				lookup.setPriceGrade1(Double.parseDouble(item[3]));
				lookup.setPriceGrade2(Double.parseDouble(item[4]));
				lookup.setPriceGrade3(Double.parseDouble(item[5]));
				lookup.setPriceGrade4(Double.parseDouble(item[6]));
				lookup.setPriceGrade5(Double.parseDouble(item[7]));
				lookup.setPriceGrade6(Double.parseDouble(item[8]));
				lookup.setPriceGradeK(Double.parseDouble(item[9]));
				lookup.setPriceLevel(Integer.parseInt(item[10]));
				
				li.add(lookup);
			}
			
			for(LookupStoreItem item: li) {
				em.persist(item);
			}
			em.close();
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}finally {
			try {
				if(is != null) {
					is.close();
				}
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		return true;
	}
}
