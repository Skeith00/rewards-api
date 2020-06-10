package uoc.rewards.rewardsapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uoc.rewards.rewardsapi.model.dto.request.PaymentRequest;
import uoc.rewards.rewardsapi.model.dto.request.UserRequest;
import uoc.rewards.rewardsapi.model.dto.response.HistoricResponse;
import uoc.rewards.rewardsapi.model.dto.response.UserResponse;
import uoc.rewards.rewardsapi.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestHeader(value = "organisation") String orgEmail, @RequestBody UserRequest userCreationRequest) {
        userService.createUser(userCreationRequest, orgEmail);
        return ResponseEntity.ok().body(String.format("User %s created", userCreationRequest.getEmail()));
    }

    @PostMapping("/edit/{userId}")
    public ResponseEntity editUser(@RequestHeader(value = "organisation") String orgEmail,
                                  @PathVariable int userId,
                                  @RequestBody UserRequest userRequest) {
        userService.editUser(userId, userRequest, orgEmail);
        return ResponseEntity.ok().body(String.format("User %s created", userRequest.getEmail()));
    }

    @PostMapping("/pay/{userId}")
    public ResponseEntity pay(@RequestHeader(value = "organisation") String orgEmail,
                              @PathVariable int userId,
                              @RequestBody PaymentRequest paymentRequest) {

        userService.pay(userId, paymentRequest.getPrice(), paymentRequest.getDealId(), orgEmail);
        return ResponseEntity.ok().body(String.format("User %d payed %f", userId, paymentRequest.getPrice()));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestHeader("organisation") String orgEmail) {
        List<UserResponse> usersByOrg = userService.getUsersByOrg(orgEmail);
        return ResponseEntity.ok().body(usersByOrg);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUser(@RequestHeader("organisation") String orgEmail, @PathVariable int userId) {
        UserResponse user = userService.getUserByIdAndOrg(userId, orgEmail);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/delete/{userId}")
    public ResponseEntity deleteUsers(@RequestHeader("organisation") String orgEmail,
                                                 @PathVariable int userId) {
        userService.deleteUser(userId, orgEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/historic/{userId}")
    public ResponseEntity<List<HistoricResponse>> getHistoricUser(@RequestHeader("organisation") String orgEmail,
                                      @PathVariable int userId) {
        List<HistoricResponse> historic = userService.getHistoricByUserIdAndOrg(userId, orgEmail);
        return ResponseEntity.ok().body(historic);
    }

}
