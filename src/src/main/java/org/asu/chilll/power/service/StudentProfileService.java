package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.StudentProfileDataView;
import org.asu.chilll.power.entity.StudentProfile;
import org.asu.chilll.power.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileService {
	
	@Autowired
	private StudentProfileRepository profileRepo;
	
	public StudentProfileDataView fetchStudentProfile(String childId, String grade) {
		StudentProfile profile = profileRepo.fetchStudentProfile(childId, grade);
		if(profile == null) {
			//create
			profile = new StudentProfile();
			profile.setChildId(childId);
			profile.setGrade(grade);
			profile = profileRepo.createStudentProfile(profile);
		}
		
		StudentProfileDataView dv = new StudentProfileDataView();
		dv.setChildId(profile.getChildId());
		dv.setGrade(profile.getGrade());
		dv.setUid(profile.getUid());
		dv.setGrade(profile.getGrade());
		dv.setIntro1Completed(profile.getIntro1Completed());
		dv.setIntro2Completed(profile.getIntro2Completed());
		dv.setDay1Completed(profile.getDay1Completed());
		dv.setDay2Completed(profile.getDay2Completed());
		dv.setTotalCoins(profile.getTotalCoins());
		dv.setTotalRocks(profile.getTotalRocks());
		dv.setPirateCharacter(profile.getPirateCharacter());
		
		return dv;
	}
	
	public StudentProfileDataView updateIntro1(StudentProfileDataView dv) {		
		StudentProfile profile = new StudentProfile();
		profile.setChildId(dv.getChildId());
		profile.setGrade(dv.getGrade());
		profile.setIntro1Completed(dv.getIntro1Completed());
		profile.setPirateCharacter(dv.getPirateCharacter());
		profileRepo.updateProfileIntro1(profile);
		
		return dv;
	}
	
	public StudentProfileDataView updateIntro2(StudentProfileDataView dv) {		
		StudentProfile profile = new StudentProfile();
		profile.setChildId(dv.getChildId());
		profile.setGrade(dv.getGrade());
		profile.setIntro2Completed(dv.getIntro2Completed());
		profile.setPirateCharacter(dv.getPirateCharacter());
		profileRepo.updateProfileIntro2(profile);
		
		return dv;
	}
	
	public StudentProfileDataView updateDayComplete(StudentProfileDataView dv) {
		StudentProfile profile = new StudentProfile();
		profile.setChildId(dv.getChildId());
		profile.setGrade(dv.getGrade());
		profile.setDay1Completed(dv.getDay1Completed());
		profile.setDay2Completed(dv.getDay2Completed());
		profileRepo.updateProfileDayComplete(profile);
		
		return dv;
	}
	
}