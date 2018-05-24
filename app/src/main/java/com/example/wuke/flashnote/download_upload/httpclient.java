package com.example.wuke.flashnote.download_upload;

/**
 * Created by kumbaya on 2018/5/23.
 */

import java.io.File ;

import org.apache.commons.httpclient.HttpClient ;
import org.apache.commons.httpclient.HttpStatus ;
import org.apache.commons.httpclient.methods.PostMethod ;
import org.apache.commons.httpclient.methods.multipart.FilePart ;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity ;
import org.apache.commons.httpclient.methods.multipart.Part ;

public class httpclient

{
	/*
	 * private Context mContext ;
	 *
	 * public Hclient ( Context c ) { this.mContext = c ; }
	 */

    public void UpLoadFile (String str)
    {

        String targetURL = null ;// TODO 指定URL
        File targetFile = null ;// TODO 指定上传文件
        targetFile = new File ( str) ;
        targetURL = "http://39.106.205.176:8080/artifacts/file" ; // servleturl
        PostMethod filePost = new PostMethod ( targetURL ) ;
        try
        {
            // 通过以下方法可以模拟页面参数提交
            // filePost.setParameter("name", "中文");
            // filePost.setParameter("pass", "1234");
            Part [ ] parts =
                    { new FilePart ( targetFile.getName ( ) , targetFile ) } ;
            filePost.setRequestEntity ( new MultipartRequestEntity (
                    parts , filePost.getParams ( ) ) ) ;
            HttpClient client = new HttpClient ( ) ;
            client.getHttpConnectionManager ( ).getParams ( )
                    .setConnectionTimeout ( 5000 ) ;
            int status = client.executeMethod ( filePost ) ;
            if ( status == HttpStatus.SC_OK )
            {
                System.out.println ( "上传成功" ) ;
                // 上传成功
            }
            else
            {
                System.out.println ( "上传失败" ) ;
                // 上传失败
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace ( ) ;
        }
        finally
        {
            filePost.releaseConnection ( ) ;

        }
    }
}
