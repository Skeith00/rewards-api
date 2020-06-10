package uoc.rewards.rewardsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uoc.rewards.rewardsapi.model.dto.request.AddDealRequest;
import uoc.rewards.rewardsapi.model.dto.response.DealResponse;
import uoc.rewards.rewardsapi.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/org")
public class OrgController {

    private DealService dealService;

    public OrgController(DealService userService) {
        this.dealService = userService;
    }

    @PostMapping("/deal/add")
    public ResponseEntity addDeal(@RequestHeader(value = "organisation") String orgEmail, @RequestBody AddDealRequest addDealRequest) {
        dealService.createDeal(addDealRequest, orgEmail);
        return ResponseEntity.ok().body(String.format("Deal '%s' created", addDealRequest.getName()));
    }

    @GetMapping("/deal")
    public ResponseEntity<List<DealResponse>> getDeals(@RequestHeader("organisation") String orgEmail) {
        List<DealResponse> deals = dealService.getDealsByOrg(orgEmail);
        return ResponseEntity.ok().body(deals);
    }
}
