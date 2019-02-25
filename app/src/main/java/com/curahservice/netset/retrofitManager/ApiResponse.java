package com.curahservice.netset.retrofitManager;

import retrofit2.Call;

/**
 * Created by Neeraj Narwal on 31/5/17.
 */
public interface ApiResponse {
    void onSuccess(Call call, Object object);

    void onError(Call call, String errorMessage, ApiResponse apiResponse);

/** Uncomment below method and its occurenece in {@link ApiHitAndHandle}
 *  if you want a method which is sure to fire even request completes or fails
 */

//    void onFinish();

}
