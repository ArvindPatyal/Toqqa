package com.toqqa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

	public static final int MIN_DISTANCE = 6000;

	public static final String MSG_DATA_PROCESSED = "Data processed successfully.";

	public static final String ERR_NO_CURRENT_ADDRESS = "No address has beeen set as a current address.";

	public static final String ERR_USER_NOT_SME = "Current user is not having role SME.";

	public static final String TOTAL_ORDER_AMOUNT_DELIVERED_QUERY_BY_DATE = "select sum(o.amount) from order_info o where o.sme_id =?1"
			+ " and o.order_status='DELIVERED' and cast(created_date as DATE) between ?2 AND ?3";

	public static final String ORDER_COUNT_QUERY_BY_DATE_AND_STATUS = "select count(o.id) from order_info o where o.sme_id =?1 and "
			+ "o.order_status=?2 and cast(created_date as DATE) between ?3 AND ?4";

	public static final String TOTAL_ORDER_ITEM_COUNT_QUERY_BY_DATE = "select sum(i.quantity) from order_item i inner join order_info o on i.order_id = o.id where o.sme_id =?1 and o.order_status='DELIVERED'\n"
			+ "and cast(created_date as DATE) between ?2 AND ?3";

	public static final String MSG_NO_BUSINESS_LOGO = "There is no business logo found for sme.";

	public static final String ORDER_CONSTANT = "ORD-";

	public static final String AGENT_CONSTANT = "AGT-";

	public static final String INVOICE_CONSTANT = "INV-";
	
	public static final String CUSTOMER_NOTIFICATION_TITLE = "Order";

	public static final String CUSTOMER_NOTIFICATION_MESSAGE = "Your order is %s ";
	
	public static final String SELLER_NOTIFICATION_TITLE = "Order";

	public static final String SELLER_NOTIFICATION_MESSAGE = "A new order has been placed with product %s ";
	
	public static final String SELLER__PRODUCT_NOTIFICATION_TITLE = "Product";

	public static final String SELLER_PRODUCT_NOTIFICATION_MESSAGE = "Product with low quantity are %s ";
	
	public static final int PRODUCT_LOW_COUNT = 5;



}
