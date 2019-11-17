package org.asu.chilll.power.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.asu.chilll.power.dataview.DocumentDataView;
import org.asu.chilll.power.entity.GameFileData;
import org.asu.chilll.power.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class FileServiceTests {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileRepository repo;
	
//	@Test
//	public void saveDataOnLocal() {
//		DateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmss");
//		Date datetime = new Date();
//		System.out.println("===================================================");
//		System.out.println(dateF.format(datetime));
//		
//		String fileUid = "1970828c-bbd5-45b7-9128-093d6db7a501";
//		GameFileData gameFile = repo.fetchFile(fileUid);
//		
//		System.out.println(System.getProperty("user.home"));
//		
//		if(gameFile != null) {
//			DocumentDataView dv = new DocumentDataView();
//			dv.setChildId(gameFile.getChildId());
//			dv.setDocumentId(gameFile.getFileUid());
//			dv.setFileContent(gameFile.getFileContent());
//			dv.setFileName(gameFile.getFileName());
//			dv.setFileLength(gameFile.getFileLength());
//			dv.setFileType(gameFile.getFileType());
//			
//			try {
//				fileService.createFileOnLocal(dv);
//			}catch(Exception e) {
//				e.printStackTrace(System.out);
//			}
//		}else {
//			System.out.print("File not exist!");
//		}
//	}
	
//	@Test
//	public void fetchAudioRecordingTestFile() {
//		String fileUid = "4f926c6c-62fd-468f-87dd-a54d1113cba8";
//		DocumentDataView dv = fileService.fetchAudioRecordTestFile(fileUid);
//	}
}