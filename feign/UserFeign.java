package com.mavericsystems.likeservice.feign;

import com.mavericsystems.likeservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeign {
    @GetMapping("/users/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId);
}
