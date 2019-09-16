package com.drcuiyutao.babyhealth.api;

import com.drcuiyutao.babyhealth.model.MessageCount;
import com.drcuiyutao.lib.api.base.APIBaseResponse;
import com.drcuiyutao.lib.api.base.BaseBody;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommentApiService {
    @POST("yxy-api-gateway/api/json/sysMsg/getNewMessageCount")
    Flowable<APIBaseResponse<MessageCount>>
    getNewMessageCount(@Body BaseBody body);
}
