package com.study.doc.api;

import com.study.doc.model.MessageCount;
import com.study.lib.api.base.APIBaseResponse;
import com.study.lib.api.base.BaseBody;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommentApiService {
    @POST("yxy-api-gateway/api/json/sysMsg/getNewMessageCount")
    Flowable<APIBaseResponse<MessageCount>>
    getNewMessageCount(@Body BaseBody body);
}
