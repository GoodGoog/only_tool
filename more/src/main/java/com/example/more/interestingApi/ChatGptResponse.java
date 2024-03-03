package com.example.more.interestingApi;

/**
 * Created by zhangqy
 * Data : 2024/3/3
 */
public class ChatGptResponse {


    /**
     * code : 200
     * model : wifi
     * ChatGPT : 3.5
     * message : 我是一个基于人工智能技术的语言模型助手，可以回答各种问题并提供信息和帮助。如果您有任何问题或需要帮助，请随时告诉我！我会尽力为您提供支持。
     */

    private String code;
    private String model;
    private String ChatGPT;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getChatGPT() {
        return ChatGPT;
    }

    public void setChatGPT(String ChatGPT) {
        this.ChatGPT = ChatGPT;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
