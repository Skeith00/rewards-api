package uoc.rewards.rewardsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uoc.rewards.rewardsapi.model.dto.request.UserCreationRequest;
import uoc.rewards.rewardsapi.model.dto.response.UserResponse;
import uoc.rewards.rewardsapi.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestHeader(value = "organisation") String orgId, @RequestBody UserCreationRequest userCreationRequest) {
        userService.createUser(userCreationRequest, Integer.parseInt(orgId));
        return ResponseEntity.ok().body(String.format("User %s created", userCreationRequest.getEmail()));
    }

    @PostMapping("/pay/{userId}")
    public ResponseEntity pay(@RequestHeader(value = "organisation") String orgId,
                              @PathVariable int userId,
                              @RequestParam double price,
                              @RequestParam(required = false) int dealId) {

        userService.pay(userId, price, dealId, Integer.parseInt(orgId));
        return ResponseEntity.ok().body(String.format("User %d payed %d", userId, price));
    }

    @GetMapping
    public ResponseEntity<Set<UserResponse>> getUsers(@RequestHeader("organisation") String orgId) {
        Set<UserResponse> usersByOrg = userService.getUsersByOrg(Integer.parseInt(orgId));
        return ResponseEntity.ok().body(usersByOrg);
    }
}
