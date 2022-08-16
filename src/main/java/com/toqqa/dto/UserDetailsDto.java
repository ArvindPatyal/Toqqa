package com.toqqa.dto;

import com.toqqa.constants.VerificationStatusConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserDetailsDto {
    /*@NotNull
    private int pageNumber;*/
    private List<VerificationStatusConstants> status;
   /* @NotNull
    private int pageSize;*/
}
