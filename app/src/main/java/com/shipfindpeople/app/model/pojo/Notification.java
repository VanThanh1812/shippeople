package com.shipfindpeople.app.model.pojo;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sonnd on 10/12/2016.
 */
public class Notification implements Serializable {
    private String Content;
    private String DateCreated;
    private String AvatarUrl;
    private String CreatorName;
    private String PostId;
    private String CreatorId;
    private String Phone;
    private boolean IsPinned;

    public Notification() {
    }

    public boolean isPinned() {
        return IsPinned;
    }

    public void setPinned(boolean pinned) {
        IsPinned = pinned;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public String getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(String creatorName) {
        CreatorName = creatorName;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getCreatorId() {
        return CreatorId;
    }

    public void setCreatorId(String creatorId) {
        CreatorId = creatorId;
    }
}
