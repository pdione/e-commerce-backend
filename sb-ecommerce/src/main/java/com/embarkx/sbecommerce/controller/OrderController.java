package com.embarkx.sbecommerce.controller;

import com.embarkx.sbecommerce.payload.request.OrderDTO;
import com.embarkx.sbecommerce.payload.request.OrderRequestDTO;
import com.embarkx.sbecommerce.service.OrderService;
import com.embarkx.sbecommerce.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paynentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paynentMethod,
                                                  @RequestBody OrderRequestDTO orderRequestDTO){

        String emailId = authUtil.loggedInEmail();
        OrderDTO order = orderService.placeOrder(emailId,
                orderRequestDTO.getAddressId(),
                paynentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
                );

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
