package com.netdisk.service.impl;

import com.netdisk.service.PostsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service

public class PostsServiceImpl implements PostsService {
    @Override
    public Map<String, String> postsAdd(Map<String, String> data) {
        Map<String, String> res = new HashMap<>();
        res.put("aa", "1");
        res.put("abb", "2");

        return res;
    }
}
