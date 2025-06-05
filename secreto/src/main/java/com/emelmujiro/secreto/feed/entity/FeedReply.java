package com.emelmujiro.secreto.feed.entity;

import java.util.ArrayList;
import java.util.List;

import com.emelmujiro.secreto.global.entity.base.TimestampedEntity;
import com.emelmujiro.secreto.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "feed_reply")
public class FeedReply extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentioned_user_id")
    private User mentionedUser;

    @Setter(value = AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private FeedReply parent;

    private boolean nestedReplyYn;
    private int nestedReplyCount;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FeedReply> nestedReplyList = new ArrayList<>();

    private int heartCount;

    @OneToMany(mappedBy = "feedReply", fetch = FetchType.LAZY)
    private List<FeedReplyHeart> feedReplyHeartList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replier_id")
    private User replier;

    @Setter(value = AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private boolean deletedYn;

    @Builder
    public FeedReply(Feed feed, User replier, String comment, FeedReply parent, User mentionedUser) {
        setFeed(feed);
        this.replier = replier;
        this.parent = parent;
        if (parent != null) {
            this.nestedReplyYn = true;
            this.parent.addNestedReply(this);
        }
        this.comment = comment;
        this.mentionedUser = mentionedUser;
    }

    public boolean delete() {
        if (this.deletedYn) return false;
        return this.deletedYn = true;
    }

    public void updateContent(String comment) {
        this.comment = comment;
    }

    public void addNestedReply(FeedReply reply) {
        if (reply == this) return;
        this.nestedReplyList.add(reply);
        ++nestedReplyCount;
        reply.setParent(this);
    }

    public void removeNestedReply(FeedReply reply) {
        if (reply == null) return;
        this.nestedReplyList.remove(reply);
        --nestedReplyCount;
    }

    public void addHeart(FeedReplyHeart heart) {
        ++heartCount;
        this.feedReplyHeartList.add(heart);
        heart.setFeedReply(this);
    }

    public void removeHeart(FeedReplyHeart heart) {
        --heartCount;
        this.feedReplyHeartList.remove(heart);
        heart.setFeedReply(null);
    }
}
