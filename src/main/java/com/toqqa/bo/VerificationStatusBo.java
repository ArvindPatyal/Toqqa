package com.toqqa.bo;

import com.toqqa.domain.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationStatusBo {
    private String id;
    private String status;
    private String role;
    private LocalDateTime createdDate;
    private UserBo userBo;
    private SmeBo smeBo;
    private AgentBo agentBo;

    public VerificationStatusBo(VerificationStatus verificationStatus, UserBo userBo,
                                SmeBo smeBo, AgentBo agentBo) {
        this.id = verificationStatus.getId();
        this.userBo = userBo;
        this.role = verificationStatus.getRole().getValue();
        this.status = verificationStatus.getStatus().toString();
        this.createdDate = verificationStatus.getCreatedDate();
        this.smeBo = smeBo;
        this.agentBo = agentBo;
    }

}
