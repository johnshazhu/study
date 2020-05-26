package com.study.doc.api;

import com.study.lib.api.base.APIBaseResponse;
import com.study.lib.api.base.BaseBody;
import com.study.doc.test.StartUpData;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("yxy-api-gateway/api/json/sysConfig/startup")
    Flowable<APIBaseResponse<StartUpData>>
    startup(@Body BaseBody body);
}
