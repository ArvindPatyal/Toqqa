package com.toqqa.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentReferralBo {
    UserBo customerDetails;
    SmeBo sellerDetails;
    AgentBo agentDetails;
}
