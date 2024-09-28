package com.barapp.web.business;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MobileNotification {
    private String id;
    private String title;
    private String body;
    private String image;
    private String click_action;
    private String title_key;
    @Builder.Default
    private List<String> title_args = new ArrayList<>();
    private String body_key;
    private List<String> body_args = new ArrayList<>();

    @Builder.Default
    private Map<String, String> data = new HashMap<>();
}
