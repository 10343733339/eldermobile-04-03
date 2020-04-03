package com.hooro.ri.util;

import java.util.List;



/**
 * bootstrap-table 分页
 * @ClassName: PageUtil
 * @author ty
 * @date 2017-5-15 上午11:35:29
 */
@SuppressWarnings("rawtypes")
public class PageUtil1 {
	private Long total;// 总记录数
	private List rows;// 当前页数
	
	public PageUtil1() {
		
	}
	
	public PageUtil1(Long total, List rows) {

		super();
		this.total = total;
		this.rows = rows;
	}



	public Long getTotal() {
	
		return total;
	}
	
	public void setTotal(Long total) {
	
		this.total = total;
	}
	
	public List getRows() {
	
		return rows;
	}
	
	public void setRows(List rows) {
	
		this.rows = rows;
	}
	
	
}
