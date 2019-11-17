package org.asu.chilll.power.dataview.search;

import java.util.List;

import org.asu.chilll.power.dataview.UserDataView;

public class PageResult {
	private List<UserDataView> users;
	private int pageIndex;
	private int pageSize;
	private int totalCount;
	private List<String> sorts;
	private List<String> filters;
	public List<UserDataView> getUsers() {
		return users;
	}
	public void setUsers(List<UserDataView> users) {
		this.users = users;
	}
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
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<String> getSorts() {
		return sorts;
	}
	public void setSorts(List<String> sorts) {
		this.sorts = sorts;
	}
	public List<String> getFilters() {
		return filters;
	}
	public void setFilters(List<String> filters) {
		this.filters = filters;
	}
}
