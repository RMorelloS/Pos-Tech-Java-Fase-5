package com.postech.fase5.Fase_5_Ecommerce_Login.model;


import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean

public class User {
    @Getter
    @Setter
    private String userkey;
    @Getter
    @Setter
    private String userlogin;
    @Getter
    @Setter
    private String authorities;



    public static User fromMap(Map<String, AttributeValue> attributeMap) {
        User user = new User();

        if (attributeMap.containsKey("userLogin")) {
            user.setUserlogin(attributeMap.get("userLogin").s());
        }
        if (attributeMap.containsKey("userKey")) {
            user.setUserlogin(attributeMap.get("userKey").s());
        }

        if (attributeMap.containsKey("role")) {
            user.setUserlogin(attributeMap.get("role").s());
        }

        return user;
    }

}
