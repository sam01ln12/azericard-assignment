package com.example.mspayment.config.client;

import com.example.mspayment.model.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-card", url = "http://ms-card:8380/card")
public interface CardClient {
    @GetMapping("/cards/{username}")
    List<CardDto> getActiveCards(@PathVariable("username") String username,
                                 @RequestParam int pageNumber,
                                 @RequestParam int pageSize);
}
