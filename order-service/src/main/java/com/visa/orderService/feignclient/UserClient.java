package com.visa.orderService.feignclient;



import com.visa.lib.entity.Auth.UserAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "userService", url = "${USER_SERVICE_URL}")
public interface UserClient {

    @GetMapping(value = "api/manager/user/id/{id}")
    public UserAccount getUserById(@PathVariable("id") Integer id);

    @GetMapping(value = "api/manager/user/name/{name}")
    public UserAccount getUserByName(@PathVariable("name") String name);
}
