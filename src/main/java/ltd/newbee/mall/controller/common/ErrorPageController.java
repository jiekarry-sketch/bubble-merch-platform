package ltd.newbee.mall.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller  //                继承了一个spring里面的自动配置类
public class ErrorPageController extends AbstractErrorController {

    private final static String ERROR_PATH = "/error"; //私有常量存储错误页面路径

    //构造函数，和类名称一致       获取错误属性                     获取错误视图解析器，将状态码映射为错误页面
    public ErrorPageController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
        // 调用父类构造函数
        super(errorAttributes, errorViewResolvers);
    }

    /**
     * 错误页面
     * 当客户端请求希望返回HTML格式的错误页面，则调用此方法
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        HttpStatus status = getStatus(request); //获取错误状态码
        if (HttpStatus.BAD_REQUEST == status) { //  当错误码是400
            return new ModelAndView("error/error_400"); //返回error_400.html页面
        } else if (HttpStatus.NOT_FOUND == status) {
            return new ModelAndView("error/error_404");
        } else {
            return new ModelAndView("error/error_5xx");
        }
    }

    /**
     * 返回json格式的错误信息
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody //返回的对象转化为json格式，返回值直接写入 HTTP 响应体，而不是解析为视图名称
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        log.info("进入错误页面，error~~");
        //获取包含错误信息的map
        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values()));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);//返回的对象
    }
}
