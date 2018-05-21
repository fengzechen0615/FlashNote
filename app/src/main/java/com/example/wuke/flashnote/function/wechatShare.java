package com.example.wuke.flashnote.function;

import android.content.Context;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by recur on 2018/5/21.
 */

public class wechatShare {

    private final String APP_ID = "wx8e10c61a51b672b1";

    private IWXAPI iwxapi;

    private void regtoWX(Context context){
        iwxapi = WXAPIFactory.createWXAPI(context,APP_ID,true);
        iwxapi.registerApp(APP_ID);
    }

    private void SMTOWX(String word){
        WXTextObject wxTextObject = new WXTextObject();
        wxTextObject.text = "hello";

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = wxTextObject;
        mediaMessage.description = "hello";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        iwxapi.sendReq(req);

    }

    private String shapeTheword(String message){
        return message;
    }

    public void shareInwechat(Context context, String message){
        regtoWX(context);
        SMTOWX(message);
    }

}
