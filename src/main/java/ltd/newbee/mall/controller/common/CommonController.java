package ltd.newbee.mall.controller.common;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;//验证码生成库
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ltd.newbee.mall.common.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.io.IOException;

/**
 *图形验证码easy-captcha
 */
@Controller
@RequestMapping("/common")
public class CommonController {
    /**
     * 后台登录验证码
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) throws IOException, FontFormatException {
        // 强制浏览器每次都重新请求新的验证码图片，而不是使用本地缓存的旧图片，确保用户每次看到的都不同。
        httpServletResponse.setHeader("Cache-Control","no-store");
        httpServletResponse.setHeader("Pragma","no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/png");//告诉浏览器返回的内容是一张PNG图片

        //使用 SpecCaptcha 组件生成验证码
        //SpecCaptcha 三个参数分别为宽、高、位数,150 代表图片宽度为 150 像素，高度为 40 像素，生成 4 个字符。
        SpecCaptcha specCaptcha = new SpecCaptcha(150,40,4);
        // 设置类型 数字和字母混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        //设置字体
        specCaptcha.setFont(Captcha.FONT_9);

        //验证码存入session 存入当前用户的 Session 中      key
        httpServletRequest.getSession().setAttribute("verifyCode",
                //获取刚才生成的随机字符串,转为小写
                specCaptcha.text().toLowerCase());
        // 输出图片流
        specCaptcha.out(httpServletResponse.getOutputStream());

    }

    /**
     * 前端登录验证码
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @GetMapping("/mall/kaptcha")
    public void mallKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/png");

        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(110, 40, 4);

        // 设置类型 数字和字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);

        //设置字体
        captcha.setCharType(Captcha.FONT_9);

        // 验证码存入session
        httpServletRequest.getSession().setAttribute(Constants.MALL_VERIFY_CODE_KEY, captcha.text().toLowerCase());

        // 输出图片流
        captcha.out(httpServletResponse.getOutputStream());
    }
}
