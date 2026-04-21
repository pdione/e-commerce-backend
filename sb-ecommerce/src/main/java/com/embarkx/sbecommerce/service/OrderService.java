package com.embarkx.sbecommerce.service;

import com.embarkx.sbecommerce.payload.request.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paynentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
