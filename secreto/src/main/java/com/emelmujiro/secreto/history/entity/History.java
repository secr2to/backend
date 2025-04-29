package com.emelmujiro.secreto.history.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String matchingResult;

    @Column(columnDefinition = "LONGTEXT")
    private String reasoningResult;
}
