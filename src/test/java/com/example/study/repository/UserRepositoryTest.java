package com.example.study.repository;

//import com.example.study.StudyApplication;
import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {

//        User user = new User();
////        user.setId();
//        user.setAccount("TestUser03");
//        user.setEmail("TestUser01@gmail.com");
//        user.setPhoneNumber("010-1111-3333");
//        user.setCreatedAt(LocalDateTime.now());
//        user.setCreatedBy("TestUser03");
//
//        User newUser = userRepository.save(user);
//        System.out.println("newUser : " + newUser);

        String account = "Test03";
        String password = "Test03";
        String status = "REGISTERED";
        String email = "Test01@gmail.com";
        String phoneNUmber = "011-1111-3333";
        LocalDateTime registeredAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = new User();

        user.setAccount(account);
        user.setPassword(password);
//        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNUmber);
        user.setRegisteredAt(registeredAt);
//        user.setCreatedAt(createdAt);
//        user.setCreatedBy(createdBy);

        User newUser = userRepository.save(user);


    }

    @Test
    @Transactional
    public void read() {

//        Optional<User> user = userRepository.findByAccount("TestUser01");
//
//        user.ifPresent(selectUser -> {
//
//            selectUser.getOrderDetailList().stream().forEach(detail -> {
////                System.out.println(detail.getItemId());
//                Item item = detail.getItem();
//                System.out.println(item);
//            });

//            System.out.println("user" + selectUser);
//            System.out.println("email" + selectUser.getEmail());
//        });

        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("011-1111-2222");

        if(user != null) {
            user.getOrderGroupList().stream().forEach(orderGroup -> {

                System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ주문 묶음 ㅡㅡㅡㅡㅡㅡㅡ");
                System.out.println("수령인 :" + orderGroup.getRevName());
                System.out.println("수령지 : " + orderGroup.getRevAddress());
                System.out.println("총금액 :" + orderGroup.getTotalPrice());
                System.out.println("총수량 :" +  orderGroup.getTotalQuantity());

                System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ주문 상세 ㅡㅡㅡㅡㅡㅡㅡ");

                orderGroup.getOrderDetailList().forEach((orderDetail -> {
                    System.out.println("파트너사 이름 : " + orderDetail.getItem().getPartner().getName());
                    System.out.println("파트너사 카테고리 : " + orderDetail.getItem().getPartner().getCategory().getTitle());
                    System.out.println("주문 상품 : " + orderDetail.getItem().getName());
                    System.out.println("고객센터 번호 :" + orderDetail.getItem().getPartner().getCallCenter());
                    System.out.println("주문의 상태 : " + orderDetail.getStatus());
                    System.out.println("도착예정일자 : " + orderDetail.getArrivalDate());



                }));
            });
        }
    }

    @Test
    public void update() {

        Optional<User> user = userRepository.findById(2L);

        user.ifPresent(selectUser -> {
            selectUser.setAccount("aaaa");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("updatemethod");

            userRepository.save(selectUser);
        });


    }

    @Test
    public void delete() {

        // 1차적으로 지우기
        Optional<User> user = userRepository.findById(2L);



        user.ifPresent(selectUser -> {
            userRepository.delete(selectUser);

        });

        //사라졌는지 다시 한번 확인
        Optional<User> deleteUser = userRepository.findById(2L);
        if (deleteUser.isPresent()) {

            System.out.println("데이터 존재:" + deleteUser.get());
        }
        else{
            System.out.println("데이터 삭제 데이터 없음음");
       }

    }
}