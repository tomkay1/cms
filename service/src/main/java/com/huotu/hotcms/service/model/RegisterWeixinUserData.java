package com.huotu.hotcms.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.codehaus.plexus.util.StringUtils;

@Getter
public class RegisterWeixinUserData {
    private Long userId;
    private Integer bindUserCount;   //绑定用户数
    private String levelName;//会员等级
    private String nickName;    //昵称
    private String headImgUrl; //头像
    private Integer relatedType;//0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号

    public RegisterWeixinUserData() {

    }

    public RegisterWeixinUserData(JsonNode node) {
        this.userId = Long.parseLong(node.get("userid").asText());
        this.bindUserCount = StringUtils.isEmpty(node.get("bindUserCount").asText()) ? 0 : Integer.parseInt(node.get("bindUserCount").asText());
        this.levelName = node.get("levelName").asText();
        this.nickName = node.get("nickName").asText();
        this.headImgUrl = node.get("headImgUrl").asText();
        this.relatedType = StringUtils.isEmpty(node.get("relatedType").asText()) ? 0 : Integer.parseInt(node.get("relatedType").asText());
    }
}