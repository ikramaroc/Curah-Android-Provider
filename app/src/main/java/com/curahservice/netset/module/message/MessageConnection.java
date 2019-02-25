package com.curahservice.netset.module.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageConnection {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("connections")
    @Expose
    private List<Connection> connections = null;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public class Connection {

        @SerializedName("connection_id")
        @Expose
        private Integer connectionId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_image")
        @Expose
        private String userImage;
        @SerializedName("lastmessage")
        @Expose
        private String lastmessage;
        @SerializedName("time")
        @Expose
        private String time;

        public Integer getConnectionId() {
            return connectionId;
        }

        public void setConnectionId(Integer connectionId) {
            this.connectionId = connectionId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getLastmessage() {
            return lastmessage;
        }

        public void setLastmessage(String lastmessage) {
            this.lastmessage = lastmessage;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }

}
