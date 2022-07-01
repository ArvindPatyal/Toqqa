package com.toqqa.controller;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentPayload;
import com.toqqa.payload.AgentUpdate;
import com.toqqa.payload.Response;
import com.toqqa.service.AgentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @ApiOperation(value = "agent updation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PutMapping("/agentupdate")
    public Response<AgentBo> agentUpdate(@ModelAttribute @Valid AgentUpdate agentUpdate) {
        log.info("Invoked:: Agentcontroller:: agentUpdate");
        return new Response<AgentBo>(this.agentService.agentUpdate(agentUpdate), "success");
    }

    @ApiOperation(value = "Returns Agent data by given id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/fetchAgent/{id}")
    public Response<AgentBo> fetchAgent(@PathVariable("id") @Valid String id) {
        log.info("Invoked:: Agentcontroller:: fetchAgent");
        return new Response<AgentBo>(this.agentService.fetchAgent(id), "success");
    }

    @ApiOperation(value = "Returns users joined by reference")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/referralList")
    public Response fetchAgentReferralList(@RequestBody @Valid AgentPayload agentPayload) {
        log.info("Invoked :: AgentController :: fetchAgentReferenceList()");
        return this.agentService.fetchAgentReferralList(agentPayload);
    }
}
