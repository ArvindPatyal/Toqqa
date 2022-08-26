package com.toqqa.dto;

import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserDetailsDto {
    @NotNull
    /* @NotNull
    private int pageSize;*/
    private int pageNumber;
    private String sortOrder;
    private String sortKey;
    private List<RoleConstants> roles;
    private List<VerificationStatusConstants> status;
}
