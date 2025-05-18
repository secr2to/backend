package com.emelmujiro.secreto.feed.entity;

import java.util.ArrayList;
import java.util.List;

import com.emelmujiro.secreto.global.entity.base.TimestampedEntity;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "feed")
public class Feed extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    private String title;

    @Column(length = 3000)
    private String content;

    private int heartCount;
    private int replyCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private FeedType feedType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "feed", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FeedHeart> feedHeartList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<FeedReply> feedReplyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(
        mappedBy = "feed",
        cascade = {CascadeType.MERGE, CascadeType.PERSIST},
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<FeedTagUser> tagUsers = new ArrayList<>();

    @OneToMany(
        mappedBy = "feed",
        cascade = {CascadeType.MERGE, CascadeType.PERSIST},
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<FeedImage> images = new ArrayList<>();

    private boolean deletedYn;

    @Builder
    public Feed(String title, String content, Room room, User author) {
        this.title = title;
        this.content = content;
        this.room = room;
        this.feedType = this.room == null ? FeedType.COMMUNITY : FeedType.INGAME;
        this.heartCount = 0;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addFeedImage(FeedImage image) {
        this.images.add(image);
        image.setFeed(this);
    }

    public void removeAllFeedImages() {
        this.images.forEach(image -> image.setFeed(null));
        this.images.clear();
    }

    public void addTagUser(FeedTagUser tagUser) {
        if (tagUser == null || tagUser.getUser() == null) return;
        if (tagUser.getUser().equals(author)) return;
        this.tagUsers.add(tagUser);
        tagUser.setFeed(this);
    }

    public void removeAllTagUsers() {
        this.tagUsers.forEach(tagUser -> tagUser.setFeed(null));
        this.tagUsers.clear();
    }

    public boolean delete() {
        if (this.deletedYn) return false;
        return this.deletedYn = true;
    }

    public void addHeart(FeedHeart heart) {
        ++heartCount;
        this.feedHeartList.add(heart);
        heart.setFeed(this);
    }

    public void removeHeart(FeedHeart heart) {
        --heartCount;
        this.feedHeartList.remove(heart);
        heart.setFeed(null);
    }

    public void addReply(FeedReply reply) {
        ++replyCount;
        this.feedReplyList.add(reply);
        reply.setFeed(this);
    }

    public boolean removeReply(FeedReply reply) {
        if (reply.delete()) {
            --replyCount;
            return true;
        }
        return false;
    }
}
