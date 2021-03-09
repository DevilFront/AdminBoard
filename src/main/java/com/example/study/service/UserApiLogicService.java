package com.example.study.service;


import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import com.example.study.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import sun.jvm.hotspot.memory.HeapBlock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderGroupApiLogicService orderGroupApiLogicService;

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    // 1.request data
    // 2. user 생성
    // 3. 생성된 데이터 -> userapiresponse return



    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {


        // 1 request data  가져오기
        UserApiRequest userApiRequest = request.getData();

        // 2 user 생성

        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(userApiRequest.getStatus())
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = userRepository.save(user);


        // 3. 생성된 데이터 -> userapiresponse return



        return Header.OK(response(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        // id -> repository getOne, getById;

        Optional<User> optional = userRepository.findById(id);

        // user -> userApiresponse return

        return optional.map( user -> response(user))
                .map(userApiResponse -> Header.OK(userApiResponse))
                .orElseGet(
                        () -> Header.Error("데이터 없음")
                );

    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        // 1. date
        UserApiRequest userApiRequest = request.getData();

        // 2. id -> user 데이터를 찾고
        Optional<User> optional = userRepository.findById(userApiRequest.getId());

        return optional.map(user -> {
            user.setAccount(userApiRequest.getAccount())
                    .setPassword(userApiRequest.getPassword())
                    .setStatus(userApiRequest.getStatus())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnregisteredAt());

            return user;
        })
          .map(user -> userRepository.save(user)) //update user
          .map(updateUser -> response(updateUser)) // newUser
                .map(userApiResponse -> Header.OK(userApiResponse))
                .orElseGet( () -> Header.Error("데이터없음"));

        // 3. userApiresponse



    }

    @Override
    public Header delete(Long id) {

        Optional<User> optional = userRepository.findById(id);

      return  optional.map(user -> {
            userRepository.delete(user);
            return Header.OK();

        }).orElseGet(() -> Header.Error("데이터 없음"));


    }



    private UserApiResponse response(User user){
        // user -> userapiresponse

        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(UserStatus.REGISTERED)
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        // Header + data return


//        return Header.OK(userApiResponse);
        return userApiResponse;
    };

    public Header<List<UserApiResponse>> search(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        List<UserApiResponse> userApiResponsesList = users.stream()
                .map(user -> response(user))
                .collect(Collectors.toList());


        // List<UserapiResponse>
        // Header<List<UserApiResponse>>


        Pagination pagination = Pagination.builder()
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .currentPage(users.getNumber())
                .currentElements(users.getNumberOfElements())
                .build();




        return Header.OK(userApiResponsesList,pagination);

    }

    public Header<UserOrderInfoApiResponse> orderInfo(Long id) {

        User user = userRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);


        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(orderGroup -> {
                   OrderGroupApiResponse orderGroupApiResponse = orderGroupApiLogicService.response(orderGroup).getData();
                   //
                  List<ItemApiResponse> itemApiResponseList = orderGroup.getOrderDetailList().stream()
                          .map(detail -> detail.getItem())
                          .map(item -> itemApiLogicService.response(item).getData())
                          .collect(Collectors.toList());

                  orderGroupApiResponse.setItemApiResponseList(itemApiResponseList);
                  return orderGroupApiResponse;
                })
                .collect(Collectors.toList());

            userApiResponse.setOrderGroupApiResponseList(orderGroupApiResponseList);

            UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                    .userApiResponse(userApiResponse)
                    .build();

            return Header.OK(userOrderInfoApiResponse);

    }


//    public Header<List<UserApiResponse>> search(Pageable pageable) {
//
//        Page<User> users = userRepository.findAll(pageable);
//
//        List<UserApiResponse> userApiResponsesList = users.stream()
//                .map(user -> response(user))
//                .collect(Collector.of(List))
//
//    };



}
