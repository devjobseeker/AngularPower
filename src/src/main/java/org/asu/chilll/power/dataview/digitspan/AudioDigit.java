package org.asu.chilll.power.dataview.digitspan;

import java.util.ArrayList;
import java.util.List;

public class AudioDigit {
	
	private List<Integer> digits;
	private int order;
	public AudioDigit() {
		this.digits = new ArrayList<Integer>();
	}
	public List<Integer> getDigits() {
		return digits;
	}
	public void setDigits(List<Integer> digits) {
		this.digits = digits;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
