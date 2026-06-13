package com.example.more.third.retrofit;

import java.util.List;

public class MovieMoney {

    /**
     * code : 200
     * day : 2026年06月13日
     * content : ["南宋开国皇帝赵构出生","英国物理学家数学家麦克斯韦出生","中国与俄国签订不平等条约中俄天津条约","爱尔兰诗人威廉巴特勒叶芝出生","中国近现代著名作曲家钢琴家冼星海出生","美国经济学家约翰纳什出生","联合国秘书长潘基文出生","日本作家太宰治逝世","英国通货膨胀率创欧洲最高","以色列从黎巴嫩撤军","中国小说家柳青去世","美国发现世界首例艾滋病病例","沙特阿拉伯国王阿齐兹逝世","美国先驱者10号空间探测器越过海王星轨道"]
     */

    private String code;
    private String day;
    private List<String> content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
