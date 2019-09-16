package com.drcuiyutao.babyhealth.api;

import com.drcuiyutao.lib.api.base.APIBaseResponse;
import com.drcuiyutao.lib.api.base.BaseBody;
import com.drcuiyutao.babyhealth.test.StartUpData;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("yxy-api-gateway/api/json/sysConfig/startup")
    Flowable<APIBaseResponse<StartUpData>>
    startup(@Body BaseBody body);
}
