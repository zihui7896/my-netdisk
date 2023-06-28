package com.netdisk.controller;

import com.netdisk.annotation.GlobalInterceptor;
import com.netdisk.annotation.VerifyParam;
import com.netdisk.controller.basecontroller.BaseController;
import com.netdisk.entity.dto.SessionWebUserDto;
import com.netdisk.entity.enums.FileDelFlagEnums;
import com.netdisk.entity.query.FileInfoQuery;
import com.netdisk.entity.vo.FileInfoVO;
import com.netdisk.entity.vo.PaginationResultVO;
import com.netdisk.entity.vo.ResponseVO;
import com.netdisk.service.FileInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/recycle")
public class RecycleController extends BaseController {

    @Resource
    private FileInfoService fileInfoService;

    // 加载回收站列表
    @PostMapping("/loadRecycleList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadRecycleList(HttpSession session, Integer pageNo, Integer pageSize) {
        FileInfoQuery query = new FileInfoQuery();
        query.setPageSize(pageSize);
        query.setPageNo(pageNo);
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("recovery_time desc");
        query.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
    }

    // 回收站文件还原到根目录
    @RequestMapping("/recoverFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO recoverFile(HttpSession session,
                                  @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.recoverFileBatch(webUserDto.getUserId(), fileIds);
        return getSuccessResponseVO(null);
    }


    @PostMapping("/delFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO delFile(HttpSession session,
                              @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.delFileBatch(webUserDto.getUserId(), fileIds, false);
        return getSuccessResponseVO(null);
    }

}
