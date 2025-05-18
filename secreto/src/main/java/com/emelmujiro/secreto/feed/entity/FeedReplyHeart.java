package com.emelmujiro.secreto.feed.entity;

import com.emelmujiro.secreto.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "feed_reply_heart")
public class FeedReplyHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_heart_id")
    private Long id;

    @Setter(value = AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_reply_id")
    private FeedReply feedReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public FeedReplyHeart(FeedReply feedReply, User user) {
        this.feedReply = feedReply;
        this.user = user;
    }
}
