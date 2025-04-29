package com.emelmujiro.secreto.user.entity;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedHeart;
import com.emelmujiro.secreto.feed.entity.FeedReply;
import com.emelmujiro.secreto.feed.entity.FeedReplyHeart;
import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.room.entity.RoomUser;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String profileUrl;

    private String nickname;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FeedReplyHeart> feedReplyHeartList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FeedReply> feedReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FeedHeart> feedHeartList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<RoomUser> roomUserList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notificationList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserLog> userLogList = new ArrayList<>();
}
