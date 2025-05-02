package com.emelmujiro.secreto.feed.entity;

import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "feed")
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(length = 3000)
    private String content;

    private Integer heartCount;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private FeedType feedType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder.Default
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<FeedHeart> feedHeartList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<FeedReply> feedReplyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User user;
}
