package com.example.study.repository;


import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.OrderDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDetailRepositoryTest extends StudyApplicationTests {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void create(){

        OrderDetail orderDetail = new OrderDetail();

//        orderDetail.setOrderAt(LocalDateTime.now());
        orderDetail.setArrivalDate(LocalDateTime.now().plusDays(2));
        orderDetail.setStatus("WAITING");
        orderDetail.setQuantity(1);
        orderDetail.setTotalPrice(BigDecimal.valueOf(900000));
//        orderDetail.setOrderAt(LocalDateTime.now());
        orderDetail.setCreatedAt(LocalDateTime.now());
        orderDetail.setCreatedBy("AdminServer");
        //어떤 사람
//        orderDetail.setOrderGroupId(1L);
        // 어떤 상품
//        orderDetail.setItemId(1L);


        OrderDetail newOrderDetail = orderDetailRepository.save(orderDetail);

    }

    @Test
    public void read(){


    }

    @Test
    public void update(){

    }

    @Test
    public void delete(){

    }
}

