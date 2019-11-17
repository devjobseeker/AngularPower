package org.asu.chilll.power.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
    @NamedQuery(
        name = "GameFileData.fetchFilesByChildId",
        query = "SELECT f FROM GameFileData f where f.childId = :childId"
    )
})

@Entity
@Table(name = "game_file_data")
public class GameFileData {
	@Id
	@Column(name = "file_uid")
	private String fileUid;
	@Column(name = "game_id")
	private String gameId;
	@Column(name = "child_id")
	private String childId;
	private String experimenter;
	private String grade;
//	private Integer year;
	
	@Column(name = "relative_path")
	private String relativePath;	//relative path
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
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getExperimenter() {
		return experimenter;
	}
	public void setExperimenter(String experimenter) {
		this.experimenter = experimenter;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	//	public String getFilePath() {
//		return filePath;
//	}
//	public void setFilePath(String filePath) {
//		this.filePath = filePath;
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
