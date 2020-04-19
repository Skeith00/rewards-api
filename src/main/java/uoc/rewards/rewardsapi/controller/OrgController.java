package uoc.rewards.rewardsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uoc.rewards.rewardsapi.model.dto.request.AddDealRequest;
import uoc.rewards.rewardsapi.model.dto.request.UserCreationRequest;
import uoc.rewards.rewardsapi.model.dto.response.UserResponse;
import uoc.rewards.rewardsapi.service.DealService;
import uoc.rewards.rewardsapi.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/org")
public class OrgController {

    private DealService userService;

    public OrgController(DealService userService) {
        this.userService = userService;
    }

    @PostMapping("/deal/add")
    public ResponseEntity addDeal(@RequestHeader(value = "organisation") String orgId, @RequestBody AddDealRequest addDealRequest) {
        userService.createDeal(addDealRequest, Integer.parseInt(orgId));
        return ResponseEntity.ok().body(String.format("Deal '%s' created", addDealRequest.getName()));
    }
}
