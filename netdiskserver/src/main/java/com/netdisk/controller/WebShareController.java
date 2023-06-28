package com.netdisk.controller;

import com.netdisk.annotation.GlobalInterceptor;
import com.netdisk.annotation.VerifyParam;
import com.netdisk.controller.commonfilecontroller.CommonFileController;
import com.netdisk.entity.constants.Constants;
import com.netdisk.entity.dto.SessionShareDto;
import com.netdisk.entity.dto.SessionWebUserDto;
import com.netdisk.entity.enums.FileDelFlagEnums;
import com.netdisk.entity.enums.ResponseCodeEnum;
import com.netdisk.entity.po.FileInfo;
import com.netdisk.entity.po.FileShare;
import com.netdisk.entity.po.UserInfo;
import com.netdisk.entity.query.FileInfoQuery;
import com.netdisk.entity.vo.FileInfoVO;
import com.netdisk.entity.vo.PaginationResultVO;
import com.netdisk.entity.vo.ResponseVO;
import com.netdisk.entity.vo.ShareInfoVO;
import com.netdisk.exception.BusinessException;
import com.netdisk.service.FileShareService;
import com.netdisk.service.UserInfoService;
import com.netdisk.utils.CopyTools;
import com.netdisk.utils.StringTools;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/showShare")
public class WebShareController extends CommonFileController {

    @Resource
    private FileShareService fileShareService;

    @Resource
    private UserInfoService userInfoService;

    //
    @PostMapping("/getShareLoginInfo")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO getShareLoginInfo(HttpSession session,
                                        @VerifyParam(required = true) String shareId) {
        SessionShareDto shareSessionDto = getSessionShareFromSession(session, shareId);
        if (shareSessionDto == null) {
            return getSuccessResponseVO(null);
        }
        ShareInfoVO shareInfoVO = getShareInfoCommon(shareId);
        //判断是否是当前用户分享的文件
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        shareInfoVO.setCurrentUser(userDto != null &&
                userDto.getUserId().equals(shareSessionDto.getShareUserId()));

        return getSuccessResponseVO(shareInfoVO);
    }

    /**
     * 获取分享信息
     */
    @PostMapping("/getShareInfo")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO getShareInfo(@VerifyParam(required = true) String shareId) {
        return getSuccessResponseVO(getShareInfoCommon(shareId));
    }

    private ShareInfoVO getShareInfoCommon(String shareId) {
        // 根据shareId获得FileShare
        FileShare share = fileShareService.getFileShareByShareId(shareId);
        // 如果FileShare为空或者已经过期
        if (share == null || (share.getExpireTime() != null && new Date().after(share.getExpireTime()))) {
            throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
        }
        // FileShare映射为ShareInfoVO
        ShareInfoVO shareInfoVO = CopyTools.copy(share, ShareInfoVO.class);
        FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(share.getFileId(), share.getUserId());
        if (fileInfo == null || !FileDelFlagEnums.USING.getFlag().equals(fileInfo.getDelFlag())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
        }
        shareInfoVO.setFileName(fileInfo.getFileName());
        UserInfo userInfo = userInfoService.getUserInfoByUserId(share.getUserId());
        shareInfoVO.setNickName(userInfo.getNickName());
        shareInfoVO.setAvatar(userInfo.getQqAvatar());
        shareInfoVO.setUserId(userInfo.getUserId());
        return shareInfoVO;
    }

    /**
     * 校验分享码
     */
    @RequestMapping("/checkShareCode")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO checkShareCode(HttpSession session,
                                     @VerifyParam(required = true) String shareId,
                                     @VerifyParam(required = true) String code) {
        SessionShareDto shareSessionDto = fileShareService.checkShareCode(shareId, code);
        session.setAttribute(Constants.SESSION_SHARE_KEY + shareId, shareSessionDto);
        return getSuccessResponseVO(null);
    }

    // 只能分享一个文件或者文件夹(可包含多个子文件夹和子文件)
    @RequestMapping("/loadFileList")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO loadFileList(HttpSession session,
                                   @VerifyParam(required = true) String shareId, String filePid) {
        // 查询出对应链接下的SessionShareDto
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        FileInfoQuery query = new FileInfoQuery();

        // 如果父目录不是根目录
        if (!StringTools.isEmpty(filePid) && !Constants.ZERO_STR.equals(filePid)) {
//            fileInfoService.checkRootFilePid(shareSessionDto.getFileId(), shareSessionDto.getShareUserId(), filePid);
            query.setFilePid(filePid);
        } else {
            // 如果是根目录
            query.setFileId(shareSessionDto.getFileId());
        }
        query.setUserId(shareSessionDto.getShareUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PaginationResultVO resultVO = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(resultVO, FileInfoVO.class));
    }


    // 判断分享是否失效
    private SessionShareDto checkShare(HttpSession session, String shareId) {
        SessionShareDto shareSessionDto = getSessionShareFromSession(session, shareId);
        if (shareSessionDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_903);
        }
        if (shareSessionDto.getExpireTime() != null && new Date().after(shareSessionDto.getExpireTime())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902);
        }
        return shareSessionDto;
    }

    @PostMapping("/getFolderInfo")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO getFolderInfo(HttpSession session,
                                    @VerifyParam(required = true) String shareId,
                                    @VerifyParam(required = true) String path) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        return super.getFolderInfo(path, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/getFile/{shareId}/{fileId}")
    public void getFile(HttpServletResponse response, HttpSession session,
                        @PathVariable("shareId") @VerifyParam(required = true) String shareId,
                        @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        super.getFile(response, fileId, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/ts/getVideoInfo/{shareId}/{fileId}")
    public void getVideoInfo(HttpServletResponse response,
                             HttpSession session,
                             @PathVariable("shareId") @VerifyParam(required = true) String shareId,
                             @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        super.getFile(response, fileId, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/createDownloadUrl/{shareId}/{fileId}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO createDownloadUrl(HttpSession session,
                                        @PathVariable("shareId") @VerifyParam(required = true) String shareId,
                                        @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        return super.createDownloadUrl(fileId, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/download/{code}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("code") @VerifyParam(required = true) String code) throws Exception {
        super.download(request, response, code);
    }

    @RequestMapping("/saveShare")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO saveShare(HttpSession session,
                                @VerifyParam(required = true) String shareId,
                                @VerifyParam(required = true) String shareFileIds,
                                @VerifyParam(required = true) String myFolderId) {
        // 校验分享链接
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        // 校验登陆用户和分享用户
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        if (shareSessionDto.getShareUserId().equals(webUserDto.getUserId())) {
            throw new BusinessException("自己分享的文件无法保存到自己的网盘");
        }

        fileInfoService.saveShare(shareSessionDto.getFileId(), shareFileIds, myFolderId,
                shareSessionDto.getShareUserId(), webUserDto.getUserId());
        return getSuccessResponseVO(null);
    }

}
