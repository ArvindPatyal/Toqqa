package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.domain.User;

public interface InvoiceService {

    void generateInvoice(OrderInfoBo orderInfoBo, User user);

    String fetchInvoice(String orderId, String userId);
}
