package com.toqqa.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AgentUpdate {


    private MultipartFile agentDocuments;

    /*private MultipartFile idProof;*/

    private MultipartFile agentProfilePicture;


}
