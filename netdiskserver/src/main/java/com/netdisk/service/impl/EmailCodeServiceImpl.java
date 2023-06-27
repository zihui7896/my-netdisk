package com.netdisk.service.impl;


import com.netdisk.config.AppConfig;
import com.netdisk.entity.dto.SysSettingsDto;
import com.netdisk.entity.po.EmailCode;
import com.netdisk.entity.po.UserInfo;
import com.netdisk.entity.query.EmailCodeQuery;
import com.netdisk.entity.query.UserInfoQuery;
import com.netdisk.mappers.EmailCodeMapper;
import com.netdisk.mappers.UserInfoMapper;
import com.netdisk.service.EmailCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;


/**
 * 邮箱验证码 业务接口实现
 */
@Service
public class EmailCodeServiceImpl implements EmailCodeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private AppConfig appConfig;


    /**
     * 发送邮箱验证码的前置和后置工作
     * @param toEmail 发送的邮箱地址
     * @param type  0:注册  1:找回密码
     */
    @Override
    @Transactional
    public void sendEmailCode(String toEmail, Integer type) {
        //如果是注册，校验邮箱是否已存在
        if (type == Constants.REGISTER_ZERO) {
            UserInfo userInfo = userInfoMapper.selectByEmail(toEmail);
            if (null != userInfo) {
                throw new BusinessException("邮箱已经存在");
            }
        }

        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        sendEmailCode(toEmail, code);

        // 数据库中的其他验证码置于不可用
        emailCodeMapper.disableEmailCode(toEmail);

        // 封装EmailCode对象
        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(toEmail);
        emailCode.setStatus(Constants.REGISTER_ZERO);
        emailCode.setCreateTime(new Date());

        // 插入数据库
        emailCodeMapper.insert(emailCode);
    }


    }

}