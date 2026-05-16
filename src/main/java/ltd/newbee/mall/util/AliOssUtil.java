package ltd.newbee.mall.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data // 生成getter和setter和tostring方法
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     */
    public String upload(byte[] bytes,String objectName){//返回值是一个 url
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);

        try {
            //执行上传                                   将字节文件包装成输入流，作为文件内容
            ossClient.putObject(bucketName,objectName,new ByteArrayInputStream(bytes));
        } catch (OSSException e) {
            log.error("OSSException: {},阿里云服务器错误", e.getErrorMessage());
        } catch (ClientException e) {
            log.error("ClientException: {}，客户端异常", e.getMessage());
        } finally {
            if(ossClient != null){
                ossClient.shutdown(); // 关闭OSSClient,释放资源
            }
        }

        // 构造文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("http://");
        stringBuilder.append(bucketName).append(".").append(endpoint).append("/").append(objectName);
        log.info("文件上传到:{}", stringBuilder);// 打印文件上传的url
        return  stringBuilder.toString();
    }
}

/**
 * 为什么用 byte[] 而不是直接传文件？
 * 这样工具类更通用。以后如果想把一段纯文本存成文件，或者把动态生成的二维码存上去，都可以转成字节数组丢给这个方法。
 */