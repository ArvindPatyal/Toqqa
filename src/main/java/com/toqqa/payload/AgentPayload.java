package com.toqqa.payload;

import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentPayload {

    private String agentUserId;
    private List<RoleConstants> roles;
}
