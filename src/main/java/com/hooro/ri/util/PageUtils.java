package com.hooro.ri.util;

import java.util.List;

@SuppressWarnings("rawtypes")
public class PageUtils {
    private Long iTotalRecords;//实际的行数
    private Long iTotalDisplayRecords;//过滤之后，实际的行数
    private Integer draw;//datatables传过来的参数 原样返回
	private List aaData;//返回实体
	
    public PageUtils() {
		
	}
	
	public PageUtils(Long iTotalRecords, Long iTotalDisplayRecords, Integer draw, List aaData) {

		super();
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.draw = draw;
		this.aaData = aaData;
	}

	public Long getiTotalRecords() {
	
		return iTotalRecords;
	}
	
	public void setiTotalRecords(Long iTotalRecords) {
	
		this.iTotalRecords = iTotalRecords;
	}
	
	public Long getiTotalDisplayRecords() {
	
		return iTotalDisplayRecords;
	}
	
	public void setiTotalDisplayRecords(Long iTotalDisplayRecords) {
	
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	
	public Integer getDraw() {
	
		return draw;
	}
	
	public void setDraw(Integer draw) {
	
		this.draw = draw;
	}
	
	public List getAaData() {
	
		return aaData;
	}
	
	public void setAaData(List aaData) {
	
		this.aaData = aaData;
	}
	
	
}
