package com.communityhub.community.hub_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {
   @Id
   @GeneratedValue(strategy = IDENTITY)
    private Long id;
   @NotEmpty
    private String text;
   @ManyToOne(fetch = LAZY)
   @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;
    private Instant createdDate;
   @ManyToOne(fetch = LAZY)
   @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
