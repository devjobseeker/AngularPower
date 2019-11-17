package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.GameFileData;
import org.asu.chilll.power.entity.feature.AudioRecordTestFile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FileRepository {

	@PersistenceContext
	private EntityManager em;
	
	public void createFiles(List<GameFileData> files) {
		for(GameFileData file: files) {
			file.setFileUid(UUID.randomUUID().toString());
			Date date = new Date();
			file.setCreateDate(date);
			file.setCreateTime(date.getTime());
			em.persist(file);
		}
	}
	
	public GameFileData createFile(GameFileData file) {
		if(file.getFileUid() == null || file.getFileUid().isEmpty()) {
			file.setFileUid(UUID.randomUUID().toString());
		}
		
		Date date = new Date();
		file.setCreateDate(date);
		file.setCreateTime(date.getTime());
		em.persist(file);
		return file;
	}
	
	public GameFileData fetchFile(String fileUid) {
		return em.find(GameFileData.class, fileUid);
	}
	
	public List<GameFileData> fetchFilesByChildId(String childId){
		return em.createNamedQuery("GameFileData.fetchFilesByChildId", GameFileData.class)
				.setParameter("childId", childId)
				.getResultList();
	}
	
	public AudioRecordTestFile createAudioRecordTestFile(AudioRecordTestFile file){
		if(file.getFileUid() == null || file.getFileUid().isEmpty()) {
			file.setFileUid(UUID.randomUUID().toString());
		}
		file.setFileName(file.getFileUid());
		Date date = new Date();
		file.setCreateDate(date);
		file.setCreateTime(date.getTime());
		em.persist(file);
		return file;
	}
	
	public AudioRecordTestFile fetchAudioRecordTestFile(String fileUid) {
		return em.find(AudioRecordTestFile.class, fileUid);
	}
}
