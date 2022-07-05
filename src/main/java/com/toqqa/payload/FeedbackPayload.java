package com.toqqa.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FeedbackPayload {

    @NotNull
    @Length(min = 1, max = 200)
    private String title;

    @NotNull
    @Length(min = 1, max = 2000)
    private String description;
}