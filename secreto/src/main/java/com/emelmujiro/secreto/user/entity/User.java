package com.emelmujiro.secreto.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.feed.entity.FeedHeart;
import com.emelmujiro.secreto.feed.entity.FeedReply;
import com.emelmujiro.secreto.feed.entity.FeedReplyHeart;
import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.room.entity.RoomUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String oAuthProvider;
  
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
    private List<Notification> notificationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserLog> userLogList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private UserRole role;

    @Builder(builderMethodName = "oauthUserBuilder")
    public User(String oAuthProvider, String username, String nickname, String email, String profileUrl) {
        this.oAuthProvider = oAuthProvider;
        this.username = username;
        this.nickname = nickname;
        this.password = "===== OAUTH-USER =====";
        this.email = email;
        this.profileUrl = profileUrl;
        this.role = UserRole.ROLE_USER;
    }
}
