package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userID";
    public static final String BODY_KEY = "message";
    public static final String RECEIVER_ID_KEY = "receiverID";
    public static final String SENDER_ID_KEY = "senderID";
    public static final String TASK_ID_POINTER = "taskPointingTo";



    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public String getUserIdKey(){return getString(USER_ID_KEY);}

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public void setReceiverIdKey(User otherUserId){put(RECEIVER_ID_KEY,otherUserId);}
    public User getReceiverIdKey(){return (User) getParseUser(RECEIVER_ID_KEY);}

    public void setSenderIdKey(User senderUserId){put(SENDER_ID_KEY,senderUserId);}
    public User getSenderIdKey(){return (User) getParseUser(SENDER_ID_KEY);}

    public void setTaskIdPointer(Task task){put(TASK_ID_POINTER,task);}
    public Task getTaskIdPointer(){return (Task) getParseObject(TASK_ID_POINTER);}

}
