package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.StudentProfile;
import org.asu.chilll.power.entity.pk.StudentProfilePK;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StudentProfileRepository {
	@PersistenceContext
	private EntityManager em;
	
	public StudentProfile fetchStudentProfile(String childId, String grade) {
		StudentProfilePK pk = new StudentProfilePK();
		pk.setChildId(childId);
		pk.setGrade(grade);
		return em.find(StudentProfile.class, pk);
	}
	
	public StudentProfile createStudentProfile(StudentProfile profile) {
		profile.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		profile.setCreateDate(date);
		profile.setCreateTime(date.getTime());
		profile.setLastUpdateDate(date);
		profile.setLastUpdateTime(date.getTime());
		em.persist(profile);
		return profile;
	}
	
	public StudentProfile updateProfileIntro1(StudentProfile profile) {
		StudentProfilePK pk = new StudentProfilePK();
		pk.setChildId(profile.getChildId());
		pk.setGrade(profile.getGrade());
		StudentProfile dbProfile = em.find(StudentProfile.class, pk);
		if(dbProfile != null) {
			dbProfile.setPirateCharacter(profile.getPirateCharacter());
			dbProfile.setIntro1Completed(profile.getIntro1Completed());
			Date date = new Date();
			dbProfile.setLastUpdateDate(date);
			dbProfile.setLastUpdateTime(date.getTime());
			em.merge(dbProfile);
		}
		return profile;
	}
	
	public StudentProfile updateProfileIntro2(StudentProfile profile) {
		StudentProfilePK pk = new StudentProfilePK();
		pk.setChildId(profile.getChildId());
		pk.setGrade(profile.getGrade());
		StudentProfile dbProfile = em.find(StudentProfile.class, pk);
		if(dbProfile != null) {
			dbProfile.setPirateCharacter(profile.getPirateCharacter());
			dbProfile.setIntro2Completed(profile.getIntro2Completed());
			Date date = new Date();
			dbProfile.setLastUpdateDate(date);
			dbProfile.setLastUpdateTime(date.getTime());
			em.merge(dbProfile);
		}
		return profile;
	}
	
	public StudentProfile updateProfileDayComplete(StudentProfile profile) {
		StudentProfilePK pk = new StudentProfilePK();
		pk.setChildId(profile.getChildId());
		pk.setGrade(profile.getGrade());
		StudentProfile dbProfile = em.find(StudentProfile.class, pk);
		if(dbProfile != null) {
			dbProfile.setDay1Completed(profile.getDay1Completed());
			dbProfile.setDay2Completed(profile.getDay2Completed());
			Date date = new Date();
			dbProfile.setLastUpdateDate(date);
			dbProfile.setLastUpdateTime(date.getTime());
			em.merge(dbProfile);
		}
		return profile;
	}
	
	public StudentProfile updateTotalCoins(StudentProfile profile) {
		StudentProfilePK pk = new StudentProfilePK();
		pk.setChildId(profile.getChildId());
		pk.setGrade(profile.getGrade());
		StudentProfile dbProfile = em.find(StudentProfile.class, pk);
		if(dbProfile != null) {
			dbProfile.setTotalCoins(profile.getTotalCoins());
			Date date = new Date();
			dbProfile.setLastUpdateDate(date);
			dbProfile.setLastUpdateTime(date.getTime());
			em.merge(dbProfile);
		}
		return profile;
	}
}
