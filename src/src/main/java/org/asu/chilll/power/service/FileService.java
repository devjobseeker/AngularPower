package org.asu.chilll.power.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.asu.chilll.power.dataview.DocumentDataView;
import org.asu.chilll.power.entity.GameFileData;
import org.asu.chilll.power.entity.feature.AudioRecordTestFile;
import org.asu.chilll.power.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	@Autowired
	private FileRepository repo;
	
	@Value("${power.file.recording.folder}")
	private String fileRecordingFolder;
	
	@Value("${power.file.recording.prefix.enable}")
	private boolean filePathPrefixEnable;
	
	private final static String dateFormat = "yyyyMMddHHmmss";
	
	public void createFile(DocumentDataView file) {
		//gameId, childId, fileLength, fileContent, fileName, fileType
		GameFileData entity = new GameFileData();
		entity.setFileUid(file.getDocumentId());
		entity.setGameId(file.getGameId());
		entity.setChildId(file.getChildId());
		entity.setGrade(file.getGrade());
		entity.setExperimenter(file.getExperimenter());
		entity.setRelativePath(file.getRelativePath());
		entity.setFileName(file.getFileName());
		entity.setFileType(file.getFileType());
		
		repo.createFile(entity);
		file.setDocumentId(entity.getFileUid());
	}
	
//	public void createFiles(List<DocumentDataView> files) {
//		List<GameFileData> entities = new ArrayList<GameFileData>();
//		for(DocumentDataView file: files) {
//			GameFileData entity = new GameFileData();
//			entity.setGameId(file.getGameId());
//			entity.setExperimenter(file.getExperimenter());
//			entity.setChildId(file.getChildId());
//			entity.setFileLength(file.getFileLength());
//			entity.setFileContent(file.getFileContent());
//			entity.setFileName(file.getFileName());
//			entity.setFileType(file.getFileType());
//			
//			entities.add(entity);
//			//file.setDocumentId(entity.getFileUid());
//		}
//		repo.createFiles(entities);
//	}
	
	public DocumentDataView createAudioRecordTestFile(DocumentDataView file) {
		AudioRecordTestFile entity = new AudioRecordTestFile();
		
		//entity.setDeviceId(file.getDeviceId());
		//entity.setFileContent(file.getFileContent());
		//entity.setFileLength(file.getFileLength());
		entity.setRelativePath(file.getRelativePath());
		entity.setFileType(file.getFileType());
		entity = repo.createAudioRecordTestFile(entity);
		file.setDocumentId(entity.getFileUid());
		file.setFileName(entity.getFileName());
		return file;
	}
	
	public DocumentDataView fetchAudioRecordTestFile(String fileUid) {
		AudioRecordTestFile entity = repo.fetchAudioRecordTestFile(fileUid);
		if(entity != null) {
			DocumentDataView dv = new DocumentDataView();
			dv.setRelativePath(entity.getRelativePath());
			//dv.setFileContent(entity.getFileContent());
			dv.setDocumentId(entity.getFileUid());
			dv.setFileName(entity.getFileName());
			dv.setFileType(entity.getFileType());
			String fileAbosultePath;
			if(filePathPrefixEnable) {
				fileAbosultePath = System.getProperty("user.home") + File.separator + fileRecordingFolder;
			}else {
				fileAbosultePath = fileRecordingFolder;
			}
			try {
				File file = new File(fileAbosultePath + File.separator + dv.getRelativePath());
				byte[] fileContent = Files.readAllBytes(file.toPath());
				dv.setFileContent(fileContent);
				dv.setFileLength((long)fileContent.length);
				return dv;
			}catch(Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public DocumentDataView createFileOnLocal(DocumentDataView dv) {
		try{
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int day = Calendar.getInstance().get(Calendar.DATE);
			String date = year + "_" + month + "_" + day;
			String fileAbosultePath;
			if(filePathPrefixEnable) {
				fileAbosultePath = System.getProperty("user.home") + "/" + fileRecordingFolder;
			}else {
				fileAbosultePath = fileRecordingFolder;
			}
			
			String gameDirRelPath = dv.getFolderName() + "/" + date;
			File gameDir = new File(fileAbosultePath + "/" + gameDirRelPath);
			if(!gameDir.exists() || !gameDir.isDirectory()) {
				//create directories
				gameDir.mkdirs();
			}
			DateFormat dateF = new SimpleDateFormat(dateFormat);
			Date datetime = new Date();
			Long createTime = datetime.getTime();
			ByteArrayInputStream bis = new ByteArrayInputStream(dv.getFileContent());
			String fileRelPath = dv.getChildId() + "_" + "Grade" + dv.getGrade() + "_" + dateF.format(datetime) + "_" + createTime + "_" + dv.getFileName() + ".wav";
			String filePath = gameDir.getAbsolutePath() + "/" + fileRelPath;
			FileOutputStream out = new FileOutputStream(new File(filePath));
			IOUtils.copy(bis, out);
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(out);
			dv.setRelativePath(gameDirRelPath + "/" + fileRelPath);
			return dv;
		}catch(Exception e) {
			System.out.println("========================================== SAVE FILE ON LOCAL FAIL! ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
}