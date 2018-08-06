/**
 * 
 */
package com.yyz.cyuanw.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ToastUtil {

	public static void show(Context context, String info) {
		if(context!=null){
			Toast.makeText(context, info, Toast.LENGTH_LONG).show();
		}
		
	}

	public static void show(Context context, int info) {
		if(context!=null){
			Toast.makeText(context, info, Toast.LENGTH_LONG).show();	
		}
	}
	public static void httpErrorsShow(Context context,String resultJson){
		try {
			if(context!=null){
				JSONObject json = new JSONObject(resultJson);
				JSONObject errors=json.optJSONObject("errors");
				String detail=errors.optString("detail");
				if(detail!=null){
					Toast.makeText(context, detail, Toast.LENGTH_SHORT).show();
				}
			}
		} catch (JSONException e) {
			Log.e("sy", "请求返回的httpErrors的json格式不正确",e);
		}
	}
}
