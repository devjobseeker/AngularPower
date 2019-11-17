package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.StorePurchaseHistory;
import org.asu.chilll.power.entity.lookup.LookupStoreItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StoreRepository {
	@PersistenceContext
	private EntityManager em;
	
	public List<LookupStoreItem> fetchAllItems() {
		return em.createNamedQuery("LookupStoreItem.fetchAllItems", LookupStoreItem.class).getResultList();
	}
	
	public LookupStoreItem fetchItem(Integer itemId) {
		return em.find(LookupStoreItem.class, itemId);
	}
	
	public StorePurchaseHistory createItemPurchaseHistory(StorePurchaseHistory history) {
		history.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		history.setCreateDate(date);
		history.setCreateTime(date.getTime());
		em.persist(history);
		return history;
	}
	
	public Long fetchTotalCount() {
		return em.createNamedQuery("LookupStoreItem.fetchTotalCount", Long.class)
				.getSingleResult();
	}
	
	public List<LookupStoreItem> fetchPurchasedItems(String childId, String grade){
		String query = "select l from StorePurchaseHistory h inner join LookupStoreItem l on l.itemId = h.purchasedItemId "
				+ "where h.childId = :childId and h.grade = :grade";
		return em.createQuery(query, LookupStoreItem.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.getResultList();
	}
}