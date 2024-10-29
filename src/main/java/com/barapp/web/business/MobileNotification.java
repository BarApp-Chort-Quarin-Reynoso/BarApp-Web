package com.barapp.web.business;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MobileNotification {
    String id;
    String title;
    String body;
    String image;
    String click_action;
    String title_key;
    String body_key;

    @Builder.Default
    List<String> title_args = new ArrayList<>();
    @Builder.Default
    List<String> body_args = new ArrayList<>();
    @Builder.Default
    Map<String, String> data = new HashMap<>();
}
