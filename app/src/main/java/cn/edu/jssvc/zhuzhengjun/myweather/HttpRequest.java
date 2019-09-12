package cn.edu.jssvc.zhuzhengjun.myweather;

import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class HttpRequest {

    public static void get_4tian(String str, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "http://api.help.bj.cn/apis/weather6d/?id=" + str;
        JsonObjectRequest request = new JsonObjectRequest(0,url, listener, errorListener);
        MyApplication.queue.add(request);
    }

    public static void get_36h(String str, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "http://api.help.bj.cn/apis/weather36h/?id=" + str;
        JsonObjectRequest request = new JsonObjectRequest(0,url, listener, errorListener);
        MyApplication.queue.add(request);
    }

    public static void get_zuiquan(String str, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "https://www.tianqiapi.com/api/?appid=33411613&appsecret=lQKMV1KB&version=v1&cityid=" + str;
        JsonObjectRequest request = new JsonObjectRequest(0,url, listener, errorListener);
        MyApplication.queue.add(request);
    }

    public static void bitmap(String str, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        ImageRequest request = new ImageRequest(str,listener,0,0, Bitmap.Config.RGB_565, errorListener);
        MyApplication.queue.add(request);
    }

}
