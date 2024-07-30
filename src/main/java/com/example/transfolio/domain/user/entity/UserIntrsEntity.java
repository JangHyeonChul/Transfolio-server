package com.example.transfolio.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Table(name = "tr_member_intrs")
public class UserIntrsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_intrs_pid")
    private Long pid;

//    @NotNull
//    private String userId;
    @OneToOne
    @JoinColumn(name = "user_pid")
    private UserEntity user;


    @NonNull
    private String intrsLanguage;

    @NonNull
    private String intrsMajor;

    @NonNull
    private String intrsLiterature;

    @NonNull
    private String intrsCorporation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserIntrsEntity() {

    }
}
