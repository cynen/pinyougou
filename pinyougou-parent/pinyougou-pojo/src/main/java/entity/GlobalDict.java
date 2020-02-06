package entity;

/**
 * 全局通用字典值.
 * @author myth
 *
 */
public interface GlobalDict {

	// 商品状态.
	public static final String GOODS_UNCHECK = "0"; // 未审核 
	public static final String GOODS_CHECKED = "1"; // 已审核
	public static final String GOODS_REFUSE = "2";  // 审核未通过
	public static final String GOODS_CLOSED = "3";  // 已关闭
	
	
	// 商家状态.
	public static final String SELLER_UNCHECK = "0"; // 未审核 状态
	public static final String SELLER_CHECKED = "1"; // 已审核 状态
	public static final String SELLER_REFUSE = "2"; // 审核不通过 状态
	public static final String SELLER_CLOSED = "3"; // 已关闭 状态
	
	// item启用状态.
	public static final String ITEM_DISENABLE = "0"; // item启用
	public static final String ITEM_ENABLE = "1"; // item不启用

	// item是否默认状态.
	public static final String ITEM_DEFAULT_FALSE = "0"; // item不是默认
	public static final String ITEM_DEFAULT_TRUE = "1"; // item是默认
}
