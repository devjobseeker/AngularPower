package org.asu.chilll.power.controller.feature;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.asu.chilll.power.dataview.DocumentDataView;
import org.asu.chilll.power.enums.FileFolderName;
import org.asu.chilll.power.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class AudioRecordController {
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "/api/audiorecord", method = RequestMethod.POST)
	public @ResponseBody DocumentDataView sendAudioData(MultipartHttpServletRequest request, HttpSession session) {
		try {
			List<DocumentDataView> dvs = new ArrayList<DocumentDataView>();
			Iterator<String> itr = request.getFileNames();
			while(itr.hasNext()) {
				MultipartFile file = request.getFile(itr.next());
				DocumentDataView dv = new DocumentDataView();
				dv.setDocumentId(UUID.randomUUID().toString());
				dv.setFileLength(file.getSize());
				dv.setFileContent(file.getBytes());
				dv.setFileType(file.getContentType());
				dv.setFileName("RecordingTest");
				dv.setChildId("TEST");
				dv.setFolderName(FileFolderName.Audio_Recording_Test.toString());
				dvs.add(dv);
				
				dv = fileService.createFileOnLocal(dv);
				fileService.createAudioRecordTestFile(dv);
			}
			
			return dvs.get(0);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//return void
	@RequestMapping(value = "/api/audiorecord/download", method = RequestMethod.GET)
	public @ResponseBody void downloadAudioFile(HttpServletResponse response, @RequestParam("fileUid")String fileUid) {
		try{
			DocumentDataView file = fileService.fetchAudioRecordTestFile(fileUid);
			byte[] bytes = file.getFileContent();
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			response.setContentType("vnd.wave");
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getDocumentId() + ".wav");
			response.setHeader("Content-Length", String.valueOf(file.getFileLength()));
			FileCopyUtils.copy(bis, response.getOutputStream());
		}catch(Exception e) {
			e.printStackTrace(System.out);
			//return null;
		}
	}
	
	//return HttpEntity<byte[]>
	@RequestMapping(value = "/api/audiorecord/play", method = RequestMethod.GET)
	public @ResponseBody HttpEntity<byte[]> playAudioFile(@RequestParam("fileUid")String fileUid) {
		try{
			DocumentDataView file = fileService.fetchAudioRecordTestFile(fileUid);
			byte[] bytes = file.getFileContent();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("audio", "vnd.wave"));
			header.set("Content-Disposition", "inline; filename=" + file.getFileName());
			header.setContentLength(file.getFileLength());
			return new HttpEntity<byte[]>(bytes, header);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}