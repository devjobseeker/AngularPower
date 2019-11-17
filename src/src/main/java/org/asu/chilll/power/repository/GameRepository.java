package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.pk.GameProgressPK;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GameRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public GameProgress createGameProgress(GameProgress progress) {
		progress.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		progress.setCreateDate(date);
		progress.setCreateTime(date.getTime());
		progress.setLastUpdateDate(date);
		progress.setLastUpdateTime(date.getTime());
		em.persist(progress);
		return progress;
	}
	
	public GameProgress updateGameProgress(GameProgress progress) {
		GameProgressPK pk = new GameProgressPK();
		pk.setChildId(progress.getChildId());
		pk.setGrade(progress.getGrade());
		pk.setGameId(progress.getGameId());
		GameProgress dbProgress = em.find(GameProgress.class, pk);
		if(dbProgress != null) {
			dbProgress.setGameStatus(progress.getGameStatus());
			dbProgress.setNextListIndex(progress.getNextListIndex());
			dbProgress.setCurrentGroupIndex(progress.getCurrentGroupIndex());
			dbProgress.setTotalCorrectCount(progress.getTotalCorrectCount());
			dbProgress.setTotalTrialCount(progress.getTotalTrialCount());
			dbProgress.setRepetitionCount(progress.getRepetitionCount());
			dbProgress.setNumOfBoxes(progress.getNumOfBoxes());
			Date date = new Date();
			dbProgress.setLastUpdateDate(date);
			dbProgress.setLastUpdateTime(date.getTime());
			em.merge(dbProgress);
			return dbProgress;
		}
		return null;
	}
	
	public GameProgress fetchGameProgress(String childId, String grade, String gameId) {
		GameProgressPK pk = new GameProgressPK();
		pk.setChildId(childId);
		pk.setGrade(grade);
		pk.setGameId(gameId);
		return em.find(GameProgress.class, pk);
	}
	
//	public GameRawData createRawData(GameRawData data) {
//		data.setRawUid(UUID.randomUUID().toString());
//		Date date = new Date();
//		data.setCreateDate(date);
//		data.setCreateTime(date.getTime());
//		data.setYear(Calendar.getInstance().get(Calendar.YEAR));
//		em.persist(data);
//		return data;
//	}
	
	//fetch list of game progress order by createtime
	public List<GameProgress> fetchListGameProgress(String childId, String grade, List<String> gameIds) {
		return em.createNamedQuery("GameProgress.fetchListGameProgress", GameProgress.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("gameIds", gameIds)
				.getResultList();
	}
	
	public List<GameProgress> fetchGameProgressByChildIdAndGrade(String childId, String grade){
		return em.createNamedQuery("GameProgress.fetchGameProgressByChildIdAndGrade", GameProgress.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.getResultList();
	}
}