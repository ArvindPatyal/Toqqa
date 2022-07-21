package com.toqqa.util;

public class AdminConstants {
    public static final String USER_LIST_SORT_BY = "createdAt";
    public static final String NO_AGENT_DOCUMENTS_FOUND = "No agent documents found";
    public static final String NO_PROFILE_PICTURE_FOUND = "No profile picture found";
    public static final String NO_ID_PROOF_FOUND = "No id proof found";
    public static final String NO_BUSINESS_LOGO_FOUND = "No business logo found";
    public static final String NO_REGISTRATION_DOCUMENT_FOUND = "No registration document found";
    public static final String TOGGLE_USER_STATUS_CHANGED = "User status changes";
    public static final String NO_USER_FOUND_WITH_ID = "No User found with id : ";
    public static final String LIST_OF_USERS_RETURNED = "List of users Returned";
    public static final String RECENT_ORDERS_RETURNED = "Recent orders returned";
    public static final String NO_RECENT_ORDERS_FOUND = "No recent orders found";
    public static final String DASHBOARD_STATS = "Dashboard Stats";
    public static final String NEW_USERS_RETURNED = "New users returned";
    public static final String TOTAL_USERS_COUNT_QUERY_BY_DATE = "select * from user_info  where cast(created_at as Date) between ?1 AND ?2";
    public static final String TOTAL_ORDERS_COUNT_QUERY_BY_DATE = "select * from order_info  where cast(modification_date as Date) between ?1 AND ?2";
    public static final String ORDER_STATUS = "('DELIVERED')";
    public static final String TOTAL_ORDER_AMOUNT_DELIVERED_BY_DATE = "select sum(o.amount + o.shipping_fee) from order_info o where o.order_status IN" + ORDER_STATUS
            + " and cast(created_date as DATE) between ?1 AND ?2";

}
