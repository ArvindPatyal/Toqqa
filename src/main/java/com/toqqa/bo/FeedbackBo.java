package com.toqqa.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackBo {

    private String title;

    private String description;

    private  String Name;

    private String email;

    private String phoneNumber;
}
