package org.example.Travel.member;

import lombok.Data;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class MemberVO {
    private int num;
    private String id;
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pw;
    private String name;
    private String tel;
    private String email;

    private String regdate;
    private String user_role;

}
