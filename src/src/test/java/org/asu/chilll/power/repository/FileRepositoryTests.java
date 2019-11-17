package org.asu.chilll.power.repository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.asu.chilll.power.entity.GameFileData;
import org.asu.chilll.power.entity.feature.AudioRecordTestFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class FileRepositoryTests {
	@Autowired
	private FileRepository repo;
	
//	@Test
//	public void convertBlobToFile() {
//		//List<GameFileData> gameFiles = new ArrayList<GameFileData>();
//		//gameFiles = repo.fetchFilesByChildId("test444");
//		String fileUid = "1970828c-bbd5-45b7-9128-093d6db7a501";
//		GameFileData gameFile = repo.fetchFile(fileUid);
//		//gameFiles.add(gameFile);
//		//for(GameFileData file: gameFiles) {
//			byte[] bytes = gameFile.getFileContent();
//			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//			try {
//				
//				String fileName = gameFile.getChildId() + "_" + gameFile.getFileName();
//				//FileOutputStream out = new FileOutputStream(new File("/Users/AllyPei/Desktop/test444/" + fileName + ".wav"));
//				FileOutputStream out = new FileOutputStream(new File("C:/Users/hjpei/Desktop/aaa/" + fileName + ".wav"));
//				IOUtils.copy(bis, out);
//				IOUtils.closeQuietly(bis);
//				IOUtils.closeQuietly(out);
//			}catch(Exception e) {
//				System.out.print("error");
//				System.out.print("===============");
//				e.printStackTrace();
//			}
//		//}
//		
//		//assertThat(gameFiles.size()).isEqualTo(1);
//	}
	
//	@Test
//	public void fetchAudioTestFile() {
//		String fileUid = "aa1232c2-0efa-4010-9ea0-c5cba1af1a7b";
//		AudioRecordTestFile file = repo.fetchAudioRecordTestFile(fileUid);
//		try {
//			//Blob blob = file.getFileContent();
//			byte[] bytes = file.getFileContent();
//			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//			String fileName = file.getFileName();
//			//FileOutputStream out = new FileOutputStream(new File("/Users/AllyPei/Desktop/test444/" + fileName + ".wav"));
//			FileOutputStream out = new FileOutputStream(new File("C:/Users/hjpei/Desktop/aaa/" + fileName + ".wav"));
//			IOUtils.copy(bis, out);
//			IOUtils.closeQuietly(bis);
//			IOUtils.closeQuietly(out);
//		}catch(Exception e) {
//			System.out.print("error");
//			System.out.print("===============");
//			e.printStackTrace();
//		}
//	}
}
