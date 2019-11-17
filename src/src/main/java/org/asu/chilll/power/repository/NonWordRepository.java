package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailNonWord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class NonWordRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public DetailNonWord createDetailData(DetailNonWord data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
}