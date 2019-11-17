package org.asu.chilll.power.entity.feature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dummy_test_connection")
public class DummyTestConnection {
	@Id
	@Column(name = "test_id")
	private String testId;

	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
}