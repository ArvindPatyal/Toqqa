package com.toqqa.util;

import com.toqqa.constants.VerificationStatusConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static final String NO_SME_FOUND = "No Sme found";
    public static final String NO_AGENT_FOUND = "No Agent found";
    public static final String INVALID_REQUEST = "INVALID REQUEST";
    public static final String APPROVAL_REQUEST_DELETED = "Approval Request Deleted";
    public static final String REQUEST_APPROVED = "Request Approved";
    public static final String REQUEST_DECLINED = "Request Declined";
    public static final String RECENT_ORDERS_RETURNED = "Recent orders returned";
    public static final String NO_RECENT_ORDERS_FOUND = "No recent orders found";
    public static final List VerificationStatus = new ArrayList<>(Arrays.asList(VerificationStatusConstants.PENDING, VerificationStatusConstants.ACCEPTED, VerificationStatusConstants.DECLINED));
    public static final List SORT_ORDERS = new ArrayList(Arrays.asList("ASC", "DESC"));
    public static final String DASHBOARD_STATS = "Dashboard Stats";
    public static final String ORDER_LIST = "List All Orders";
    public static final String TOTAL_USERS = "Total registered users list";
    public static final String NEW_USERS_RETURNED = "New Users returned";
    public static final String TOTAL_USERS_COUNT_QUERY_BY_DATE = "select * from user_info  where cast(created_at as Date) between ?1 AND ?2";
    public static final String TOTAL_ORDERS_COUNT_QUERY_BY_DATE = "select * from order_info  where cast(modification_date as Date) between ?1 AND ?2";

    public static final String APPROVAL_STATUS = "Already changed approval status";

    public static final String NO_APPROVAL_STATUS = "No approval status found with this ID : ";

    public static final String ORDER_PLACED = "('PLACED')";
    public static final String ORDER_DELIVERED = "('DELIVERED')";
    public static final String ORDER_CANCELLED = "('CANCELLED')";
    public static final String APPROVAL_REQUESTS = "Approval requests returned";
    public static final String CANCEL_ORDERS_COUNT_QUERY_BY_DATE = "select * from order_info o WHERE cast(modification_date as Date)between ?1 AND ?2 AND  o.order_status = 'CANCELLED'";
    public static final String DELIVERED_ORDERS_COUNT_QUERY_BY_DATE = "select * from order_info o WHERE cast(modification_date as Date)between ?1 AND ?2 AND  o.order_status = 'DELIVERED'";
    public static final String NEW_ORDERS_COUNT_QUERY_BY_DATE = "select * from order_info o WHERE cast(modification_date as Date)between ?1 AND ?2 AND  o.order_status = 'PLACED'";
    public static final String TOTAL_ORDER_AMOUNT_DELIVERED_BY_DATE = "select sum(o.amount + o.shipping_fee) from order_info o where o.order_status IN" + ORDER_DELIVERED
            + " and cast(created_date as DATE) between ?1 AND ?2";
    public static final String TOP_4_NEW_APPROVAL_REQUEST = "SELECT * from verification_status v where v.status = 'PENDING' order by created_date DESC limit 4";
    public static final String VERIFICATION_STATUS = "('ACCEPTED'+'PENDING')";
    public static final String ALL_NEW_CUSTOMER = "select * from verification_status v WHERE cast(created_date as Date)between ?1 AND ?2 AND v.status IN" + VERIFICATION_STATUS + "AND v.role = 'CUSTOMER'";
    public static final String ALL_NEW_SMES = "select * from verification_status v WHERE cast(created_date as Date)between ?1 AND ?2 AND v.status IN" + VERIFICATION_STATUS + "AND v.role = 'SME'";
    public static final String ALL_NEW_AGENTS = "select * from verification_status v WHERE cast(created_date as Date)between ?1 AND ?2 AND v.status IN" + VERIFICATION_STATUS + "AND v.role = 'AGENT'";
}
