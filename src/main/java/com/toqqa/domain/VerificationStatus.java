package com.toqqa.domain;

import com.toqqa.constants.VerificationStatusConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationStatus {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private VerificationStatusConstants status;
    private String role;
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date modificationDate;

}
