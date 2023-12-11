package com.netdisk.entity.mutualaid;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private String id;


    /**
     * 帖子标题
     */

    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * user_id
     */
    private String userId;
    /**
     * 帖子浏览数
     */
    private Long view = 0L;

    /**
     * 帖子回答数
     */

    private Long comments = 0L;


    /**
     * 帖子是否有正确回答，0: 没有， 1: 有
     */

    private Integer status = 0;
    /**
     * 帖子创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;
}
