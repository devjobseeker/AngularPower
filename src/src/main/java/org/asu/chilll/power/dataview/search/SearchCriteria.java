package org.asu.chilll.power.dataview.search;

import java.util.List;

public class SearchCriteria {
	private int pageIndex;
	private int pageSize;
	private List<String> filters;
	private List<String> sorts;
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<String> getFilters() {
		return filters;
	}
	public void setFilters(List<String> filters) {
		this.filters = filters;
	}
	public List<String> getSorts() {
		return sorts;
	}
	public void setSorts(List<String> sorts) {
		this.sorts = sorts;
	}
}
