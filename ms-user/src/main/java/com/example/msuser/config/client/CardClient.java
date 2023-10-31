package com.example.msuser.config.client;

import com.example.msuser.model.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-card", url = "http://ms-card:8380/card")
public interface CardClient {

    @GetMapping("/cards/{username}")
    List<CardDto> getActiveCards(@PathVariable String username,
                                        @RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam (defaultValue = "20") int pageSize);
}
