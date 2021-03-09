package com.example.study.controller;

import com.example.study.model.network.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GetController {

        @RequestMapping(method = RequestMethod.GET, path = "/getMethod")
        public String getRequest(){
            return "ht getMethod";
        }

       @GetMapping("/header")
       public Header getHeader(){

            // {resultCode : OK, ~~ 제이슨형태"
            return Header.builder().resultCode("OK").description("OK").build();
       }

}
