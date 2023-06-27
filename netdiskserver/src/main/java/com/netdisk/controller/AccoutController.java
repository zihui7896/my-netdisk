package com.netdisk.controller;


/*
 * 用户信息 controller
 */

import com.netdisk.entity.constants.Constants;
import com.netdisk.entity.dto.CreateImageCode;
import com.netdisk.entity.vo.ResponseVO;
import com.netdisk.exception.BusinessException;
import com.netdisk.service.EmailCodeService;
import com.netdisk.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController("userInfoController")
public class AccoutController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private EmailCodeService emailCodeService;

    @GetMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session
            , @RequestParam(value = "type", required = false) Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache"); //响应消息不能缓存
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        String code = vCode.getCode();
        if (type == null || type == 0) {
            session.setAttribute(Constants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }

    /**
     * @param email     需要接收验证码的邮箱
     * @param checkCode 发送给服务器的验证码
     * @param type      0:注册  1:找回密码
     */
    @PostMapping("/sendEmailCode")
    public ResponseVO sendEmailCode(HttpSession session, String email, String checkCode, Integer type) {
        try {
            // 如果验证码错误
            if (!checkCode.equals(session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
                throw new BusinessException("验证码错误");
            }
            // 如果验证码正确，发送邮箱验证码
            emailCodeService.sendEmailCode(email, type);
            return getSuccessResponseVO(null);
        } finally {
            // 删除session中保存的邮箱验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }
    }

}
