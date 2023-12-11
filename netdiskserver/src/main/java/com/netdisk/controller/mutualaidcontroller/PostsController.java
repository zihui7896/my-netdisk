package com.netdisk.controller.mutualaidcontroller;

import com.netdisk.annotation.GlobalInterceptor;
import com.netdisk.controller.basecontroller.BaseController;
import com.netdisk.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("huzhu")
public class PostsController extends BaseController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/posts/add")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return postsService.postsAdd(data);
    }
}
