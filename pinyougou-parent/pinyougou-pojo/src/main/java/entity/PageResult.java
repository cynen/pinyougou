package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 分页结果.
 * total: 全部数据
 * rows: 当前页的数据列表.
 * @author myth
 *
 */
public class PageResult implements Serializable{
	
	private long total;
	
	private List rows;

	public PageResult() {
		super();
	}

	public PageResult(long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
