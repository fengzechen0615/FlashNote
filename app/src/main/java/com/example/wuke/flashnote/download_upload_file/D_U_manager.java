package com.example.wuke.flashnote.download_upload_file;

/**
 * Created by kumbaya on 2018/5/25.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

public class D_U_manager {

    private Context context;
    /** 下载文件服务器地址 */
    private String downloadFileServerUrl;
    /** 下载文件服务器地址 */
    private String uploadFileServerUrl;

    private OnStateListener uploadFileStateListener;
    private OnStateListener downloadFileFileStateListener;


    public void Upload(String s) {
        new UpLoadrecordFile(s).execute();// 调用异步任务
    }

    /** 异步任务-录音上传 */
    public class UpLoadrecordFile extends AsyncTask<String, Integer, String> {


        String s;

        public UpLoadrecordFile(String s) {
            this.s = s;
        }

        File f = new File(Environment.getExternalStorageDirectory() + "/msc/" + s);
        @Override
        protected String doInBackground(String... parameters) {
            // TODO Auto-generated method stub11
            return UploadFile.uploadFile(f);

        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                if (uploadFileStateListener != null)
                    uploadFileStateListener.onState(-2, "上传文件失败");
                return;
            }

            if (result != null)
                if (uploadFileStateListener != null)
                    uploadFileStateListener.onState(0, "上传文件成功");
        }

    }

    /** 下载后播放 */
    public void downloadFileAndPlay(String uploadFilename) {
        new DownloadRecordFile().execute(uploadFilename);
    }

    /** 异步任务-下载后播放 */
    public class DownloadRecordFile extends AsyncTask<String, Integer, File> {

        @Override
        protected File doInBackground(String... parameters) {
            // TODO Auto-generated method stub11
            try {
                String filename = "iat.wav";
                return DownloadFile.DownloadFromUrlToData("http://10.0.1.47:8080/DownloadFile?filename="
                        + parameters[0], filename, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(File result) {
        }
    }

    public void uploadfile(String s){
        D_U_manager m = new D_U_manager();
        m.Upload(s);
    }

    public void downloadfile(String s){
        D_U_manager m =new D_U_manager();
        m.downloadFileAndPlay(s);
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDownloadFileServerUrl(String downloadFileServerUrl) {
        this.downloadFileServerUrl = downloadFileServerUrl;
    }

    public String getUploadFileServerUrl() {
        return uploadFileServerUrl;
    }

    public void setUploadFileServerUrl(String uploadFileServerUrl) {
        this.uploadFileServerUrl = uploadFileServerUrl;
    }

    public OnStateListener getUploadFileStateListener() {
        return uploadFileStateListener;
    }

    public void setUploadFileStateListener(
            OnStateListener uploadFileStateListener) {
        this.uploadFileStateListener = uploadFileStateListener;
    }

    public OnStateListener getDownloadFileFileStateListener() {
        return downloadFileFileStateListener;
    }

    public void setDownloadFileFileStateListener(
            OnStateListener downloadFileFileStateListener) {
        this.downloadFileFileStateListener = downloadFileFileStateListener;
    }

    public String getDownloadFileServerUrl() {
        return downloadFileServerUrl;
    }
}