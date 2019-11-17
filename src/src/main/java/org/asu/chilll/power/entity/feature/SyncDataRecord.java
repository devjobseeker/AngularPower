package org.asu.chilll.power.entity.feature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.asu.chilll.power.entity.pk.SyncDataPK;

@NamedQueries({
    @NamedQuery(
        name = "SyncDataRecord.fetchSyncDataRecordList",
        query = "SELECT r FROM SyncDataRecord r where r.childId = :childId and r.grade = :grade and r.gameId = :gameId"
    ),
    @NamedQuery(
        name = "SyncDataRecord.fetchSyncDataRecordListByREDCapStatus",
        query = "SELECT r FROM SyncDataRecord r where r.redcapSyncStatus = :redcapSyncStatus order by r.childId, r.grade, r.category"
    )
})

@Entity
@IdClass(value = SyncDataPK.class)
@Table(name = "sync_data_record")
public class SyncDataRecord {
	private String uid;
	@Id
	@Column(name = "child_id", length = 20)
	private String childId;
	@Id
	@Column(name = "grade", length = 10)
	private String grade;
	@Id
	@Column(name = "game_id", length = 10)
	private String gameId;
	@Id
	@Column(length = 20)
	private String category;	//detail, summary, gameProgress, studentProfile
	@Column(name = "redcap_sync_status")
	private String redcapSyncStatus;	//Pending, Processing, Finished
	@Column(name = "redcap_timestamp")
	private Long redcapTimestamp;
	public SyncDataRecord() {
		
	}
	public SyncDataRecord(String childId, String grade, String gameId, String category, String uid) {
		this.childId = childId;
		this.grade = grade;
		this.gameId = gameId;
		this.category = category;
		this.uid = uid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRedcapSyncStatus() {
		return redcapSyncStatus;
	}
	public void setRedcapSyncStatus(String redcapSyncStatus) {
		this.redcapSyncStatus = redcapSyncStatus;
	}
	public Long getRedcapTimestamp() {
		return redcapTimestamp;
	}
	public void setRedcapTimestamp(Long redcapTimestamp) {
		this.redcapTimestamp = redcapTimestamp;
	}
}