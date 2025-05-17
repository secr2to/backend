package com.emelmujiro.secreto.feed.entity;

import com.emelmujiro.secreto.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "feed_reply")
public class FeedReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentioned_user_id")
    private User mentionedUser;

    private Boolean nestedReplyYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private FeedReply parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FeedReply> nestedReplyList = new ArrayList<>();

    @Setter(value = AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @OneToMany(mappedBy = "feedReply", fetch = FetchType.LAZY)
    private List<FeedReplyHeart> feedReplyHeartList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replier_id")
    private User user;

    @Builder
    public FeedReply(Feed feed, User user, String comment, FeedReply parent, User mentionedUser) {
        this.feed = feed;
        this.user = user;
        this.parent = parent;
        if (parent != null) {
            this.nestedReplyYn = true;
            this.parent.nestedReplyList.add(this);
        }
        this.comment = comment;
        this.mentionedUser = mentionedUser;
    }
}
