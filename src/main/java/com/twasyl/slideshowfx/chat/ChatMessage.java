package com.twasyl.slideshowfx.chat;

import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;

import java.net.InetSocketAddress;

public class ChatMessage {
    private static final String JSON_MESSAGE_OBJECT = "message";
    private static final String JSON_MESSAGE_ID_ATTR = "id";
    private static final String JSON_MESSAGE_AUTHOR_ATTR = "author";
    private static final String JSON_MESSAGE_CONTENT_ATTR = "content";
    private static final String JSON_MESSAGE_ACTION_ATTR = "action";
    private static final String JSON_MESSAGE_SOURCE_ATTR = "source";
    private static final String JSON_MESSAGE_STATUS_ATTR = "status";

    private String id;
    private String author;
    private String content;
    private ChatMessageSource source;
    private ChatMessageAction action;
    private ChatMessageStatus status;
    private InetSocketAddress ip;

    public ChatMessage() {}

    public String getId() { return id; }
    public void setId(String id) {
        if(id != null) {
            if(id.startsWith("msg-")) this.id = id;
            else this.id = "msg-".concat(id);
        }
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public ChatMessageSource getSource() { return source; }
    public void setSource(ChatMessageSource source) { this.source = source; }

    public ChatMessageAction getAction() { return action; }
    public void setAction(ChatMessageAction action) { this.action = action; }

    public ChatMessageStatus getStatus() { return status; }
    public void setStatus(ChatMessageStatus status) { this.status = status; }

    public InetSocketAddress getIp() { return ip; }
    public void setIp(InetSocketAddress ip) { this.ip = ip; }

    private String getFormattedContent() {
        return getContent().replaceAll("\\n", "&#10;")
                .replaceAll("\\\\", "&#92;")
                .replaceAll("\'", "&#39;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    /**
     * Build a ChatMessage according the JSON representation
     * @param json The JSON representation of the message
     * @return A ChatMessage according the JSON representation
     * @throws IllegalArgumentException If the JSON representation is null or empty
     */
    public static ChatMessage build(String json, InetSocketAddress ip) throws IllegalArgumentException {
        if(json == null) throw new IllegalArgumentException("The JSON can not be null");
        if(json.isEmpty()) throw new IllegalArgumentException("The JSON can not beb empty");

        ChatMessage message = new ChatMessage();
        message.setIp(ip);

        JsonObject jsonObject = new JsonObject(json).getObject(JSON_MESSAGE_OBJECT);

        message.setId(jsonObject.getString(JSON_MESSAGE_ID_ATTR));
        message.setAuthor(jsonObject.getString(JSON_MESSAGE_AUTHOR_ATTR));
        message.setSource(ChatMessageSource.fromString(jsonObject.getString(JSON_MESSAGE_SOURCE_ATTR)));
        message.setAction(ChatMessageAction.fromString(jsonObject.getString(JSON_MESSAGE_ACTION_ATTR)));
        message.setStatus(ChatMessageStatus.fromString(jsonObject.getString(JSON_MESSAGE_STATUS_ATTR)));
        message.setContent(jsonObject.getString(JSON_MESSAGE_CONTENT_ATTR));

        return message;
    }

    public String toJSON() { return getJSONObject().toString(); }

    public String toJSON(InetSocketAddress ip) {
        JsonObject jsonObject = getJSONObject();

        if(this.getIp() != null && this.getIp().equals(ip)) {
            jsonObject.getObject(JSON_MESSAGE_OBJECT).putString(JSON_MESSAGE_AUTHOR_ATTR, "I");
        }

        return jsonObject.toString();
    }

    private JsonObject getJSONObject() {
        JsonObject jsonMessage = new JsonObject();

        if(getId() != null) jsonMessage.putString(JSON_MESSAGE_ID_ATTR, getId());

        if(getAuthor() != null) jsonMessage.putString(JSON_MESSAGE_AUTHOR_ATTR, getAuthor());

        if(getContent() != null) jsonMessage.putString(JSON_MESSAGE_CONTENT_ATTR, getFormattedContent());

        if(getSource() != null) jsonMessage.putString(JSON_MESSAGE_SOURCE_ATTR, getSource().getAsString());

        if(getAction() != null) jsonMessage.putString(JSON_MESSAGE_ACTION_ATTR, getAction().getAsString());

        if(getStatus() != null) jsonMessage.putString(JSON_MESSAGE_STATUS_ATTR, getStatus().getAsString());

        JsonObject json = new JsonObject();
        json.putObject(JSON_MESSAGE_OBJECT, jsonMessage);

        return json;
    }
}