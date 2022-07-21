package com.toqqa.bo;

import com.toqqa.domain.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackBo {

    private String id;

    private String title;

    private String description;

    private  String Name;

    private String email;

    private String phoneNumber;

    public  FeedbackBo(Feedback feedback){

        this.id = feedback.getId();
        this.title = feedback.getTitle();
        this.description = feedback.getDescription();
    }
}
