import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 0:40
 */
public class qiniuTest {

    //ak
    public  static String accessKey = "VhzIevXWz7r-JCnfo1ywWrT2BXX1-qVeHYAVwSh_";
    //sk
    public  static String secretKey = "FanierP4vcykXTRymQ53FcSpfCloEeXpqzMeBObE";
    //存储空间名字
    public  static String bucket = "hxy-file-space2";

    public static void main(String[] args) {

      upload2Qiniu("D:\\Java学习\\SpringCloud\\7、传智健康项目\\资料-传智健康项目\\day04\\资源\\图片资源\\3bd90d2c-4e82-42a1-a401-882c88b06a1a2.jpg","testImages");

    }
    @Test
    public void test()
    {
        upload2Qiniu("D:\\Java学习\\SpringCloud\\7、传智健康项目\\资料-传智健康项目\\day04\\资源\\图片资源\\3bd90d2c-4e82-42a1-a401-882c88b06a1a2.jpg","testImages");
//         deleteFileFromQiniu("testImages");
    }


    public static void upload2Qiniu(String filePath,String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());//华东区
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //上传文件
    public static void upload2Qiniu(byte[] bytes, String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //删除文件
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    @Test
    public void testFreemarker() throws IOException, TemplateException {
        freemarker.template.Configuration configuration=new freemarker.template.Configuration(freemarker.template.Configuration.getVersion());

        configuration.setDirectoryForTemplateLoading(new File("health_parent/health_common/resources/ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template=configuration.getTemplate("text.ftl");
        Map map=new HashMap<>();
        map.put("name","hou");
        map.put("say","你好");
        Writer writer=new FileWriter(".\\health_parent\\health_common\\resources\\ftl\\testresult.html");//输出地址
        template.process(map,writer);
        writer.close();
    }


}
