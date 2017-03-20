/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.fwms.myapplication.base.http;


import com.example.commonlibary.pickerview.util.LogUtil;

import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        printRequest(request);


        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();
        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
        try {
            String logResult = new String(responseBodyString.toCharArray());
            String url = request.url().toString();
            int indexOf = url.lastIndexOf("/");
            LogUtil.getInstance().e("请求接口= " + url.substring(indexOf+1,url.length()) + "  返回数据 = " + logResult);

        }catch (Exception e){

        }
        return newResponse;

    }

    /**
     * 打印请求体
     * @param request
     */
    private void printRequest(Request request) {
        try {
            String result = new String(bodyToString(request).toCharArray());

            LogUtil.getInstance().e("发送请求: url =" + request.url() + "?" + URLDecoder.decode(result,"UTF-8"));

        } catch (Exception e) {
            LogUtil.getInstance().e(e);
        }
    }

    private void printResponse(Response response) {
        try {
            Response copy = response.newBuilder().build();
            ResponseBody value = copy.body();
            Reader reader = value.charStream();
            String result = new String(new String(value.bytes()).toCharArray());
            String url = copy.request().url().toString();
            int indexOf = url.lastIndexOf("/");
            LogUtil.getInstance().e("请求接口 = "+url.substring(indexOf,url.length()-1) + "  返回结果 = " + result);
        } catch (Exception e) {
            LogUtil.getInstance().e(e);
        }
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if(copy!=null){
                copy.body().writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
