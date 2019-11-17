package org.asu.chilll.power.entity.feature;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audio_record_test_file")
public class AudioRecordTestFile {
	@Id
	@Column(name = "file_uid")
	private String fileUid;
	@Column(name = "relative_path")
	private String relativePath;
//	@Column(name = "device_id")
//	private String deviceId;
	
//	@Column(name = "file_length")
//	private Long fileLength;
//	@Column(columnDefinition = "MEDIUMBLOB", name = "file_content")
//	private byte[] fileContent;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "file_type")
	private String fileType;
	
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "create_time")
	private Long createTime;
	public String getFileUid() {
		return fileUid;
	}
	public void setFileUid(String fileUid) {
		this.fileUid = fileUid;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	//	public String getDeviceId() {
//		return deviceId;
//	}
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}
//	public Long getFileLength() {
//		return fileLength;
//	}
//	public void setFileLength(Long fileLength) {
//		this.fileLength = fileLength;
//	}
//	public byte[] getFileContent() {
//		return fileContent;
//	}
//	public void setFileContent(byte[] fileContent) {
//		this.fileContent = fileContent;
//	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
}