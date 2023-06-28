package com.netdisk.service;




import com.netdisk.entity.dto.SessionWebUserDto;
import com.netdisk.entity.po.UserInfo;
import com.netdisk.entity.query.UserInfoQuery;
import com.netdisk.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * 用户信息 业务接口
 */
public interface UserInfoService {
    void register(String email, String nickName, String password, String emailCode);

    SessionWebUserDto login(String email, String password);


    void resetPwd(String email, String password, String emailCode);

    void updateUserInfoByUserId(UserInfo userInfo, String userId);

    UserInfo getUserInfoByUserId(String userId);

    PaginationResultVO findListByPage(UserInfoQuery userInfoQuery);

    Integer findCountByParam(UserInfoQuery param);

    List<UserInfo> findListByParam(UserInfoQuery param);

    void updateUserStatus(String userId, Integer status);

    void changeUserSpace(String userId, Integer changeSpace);



}