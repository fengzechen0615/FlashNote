package com.example.wuke.flashnote;

import java.util.List;

/**
 * Created by kumbaya on 2018/4/28.
 */

public interface AsyncResponse {
    void onDataReceivedSuccess(List<String> listData);
    void onDataReceivedFailed();
}
