package com.su.util;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class NetManager {
	  private static NetManager _netManager = null;
	  private List<NameValuePair> mParams;
	  private String  httpbegin = "http://120.24.228.100:8088/";
      public static  NetManager getInstance()
      {
    	  if (_netManager == null)
    	  {
    		  _netManager = new NetManager();
    	  }
    	  return _netManager;
      }
      public JSONObject sendHttpRequest(String url,List<NameValuePair> params,int requestType)
      {  
    	  url = httpbegin + url;
//    	  Log.v("suzhaohui","sendHttpRequest");
    	  String requestUrl;
    	  if(requestType == 0)
    	  {
    		  Log.v("suzhaohui","revertUrl begin");
    		  requestUrl = revertUrl(url, params, requestType);
    		  url = requestUrl;
    		  Log.v("suzhaohui","revertUrl end"+requestUrl);
    	  }
    	  else if(requestType == 1)
    	  {
    		  
    	  }
    	  String jsonData = HttpUtils.getHttpEntity(url, mParams, requestType);
    	 
    	  JSONObject result = null;
//    	  Log.v("suzhaohui-log",jsonData);
    	  try {
    		  if(jsonData != null)
    		  {
    			  
    			  result = new JSONObject( jsonData);
    		  }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  return result;
      }
      /**
       * ƴ��get��ʽurl����
       * 
       * @param url
       * @param params
       * @param method
       * @return
       */
      private String revertUrl(String url, List<NameValuePair> params, int method)
      {
          if ("".equals(url) || method != HttpUtils.HTTP_GET||params.size()<=0)
          {
              return url;
          }
          
          StringBuilder sb = new StringBuilder(url);
          
          sb.append(url.contains("?") ? "&" : "?");
          
          boolean needRemove = false;
          for (NameValuePair pair : params)
          {
              sb.append(pair.getName()).append("=").append(pair.getValue())
                      .append("&");
              needRemove = true;
          }
          
          url = needRemove ? sb.substring(0, sb.length() - 1) : sb.toString();
          return url;
      }
}


//HttpPost request = new HttpPost(url); 
////�ȷ�װһ�� JSON ���� 
//JSONObject param = new JSONObject(); 
//param.put("name", "rarnu"); 
//param.put("password", "123456"); 
////�󶨵����� Entry 
//StringEntity se = new StringEntity(param.toString());  
//request.setEntity(se); 
////�������� 
//HttpResponse httpResponse = new DefaultHttpClient().execute(request); 
////�õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ��������� 
//String retSrc = EntityUtils.toString(httpResponse.getEntity()); 
////���� JSON ���� 
//JSONObject result = new JSONObject( retSrc); 
//String token = result.get("token");
