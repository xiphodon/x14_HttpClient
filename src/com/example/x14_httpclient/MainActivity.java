package com.example.x14_httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;

import com.example.x14_httpclient.utils.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(MainActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
		};
	};
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    
    
    public void get(View v){
    	EditText et_name = (EditText) findViewById(R.id.et_name);
    	EditText et_pass = (EditText) findViewById(R.id.et_password);
    	
    	final String name = et_name.getText().toString();
    	final String pass = et_pass.getText().toString();
    	
    	
    	Thread t = new Thread(){
    		@Override
    		public void run() {
    			
    			String path = "http://192.168.1.101:8080/user/loginservlet?name=" + URLEncoder.encode(name) + "&pass=" + pass;
    			
    			//使用HttpClient框架做GET方式提交
    			//1.创建HttpClient对象
    			HttpClient hc = new DefaultHttpClient();
    			
    			//2.创建httpget对象，构造方法的参数就是网址
    			HttpGet hg = new HttpGet(path);
    			
    			//3.使用客户端对象，把get请求发送出去
    			try {
					HttpResponse hr = hc.execute(hg);
					
					//拿到响应头中的状态行
					StatusLine sl = hr.getStatusLine();
					
					//状态码为200则成功
					if(sl.getStatusCode() == 200){
						//拿到响应头实体
						HttpEntity he = hr.getEntity();
						
						//拿到实体中的内容，就是服务器中返回的输入流
						InputStream is = he.getContent();
						
						String text = Utils.getTextFromStream(is);
						
						//发送消息让主线程刷新UI
						Message msg = handler.obtainMessage();
						msg.obj = text;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		}
    	};
    	
    	t.start();
    	
    }
    
    
    public void post(View v){
    	
    	EditText et_name = (EditText) findViewById(R.id.et_name);
    	EditText et_pass = (EditText) findViewById(R.id.et_password);
    	
    	final String name = et_name.getText().toString();
    	final String pass = et_pass.getText().toString();
    	
    	
    	Thread t = new Thread(){
    		@Override
    		public void run() {
    			String path = "http://192.168.1.101:8080/user/loginservlet";
    			
    			//1.创建客户端对象
    			HttpClient hc = new DefaultHttpClient();
    			
    			//2.创建post请求对象
    			HttpPost hp = new HttpPost(path);
    			
    			//封装form表单提交的数据
    			BasicNameValuePair bnvp = new BasicNameValuePair("name", name);
    			BasicNameValuePair bnvp2 = new BasicNameValuePair("pass",pass);
    			
    			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
    			parameters.add(bnvp);
    			parameters.add(bnvp2);
    			
    			try {
    				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"utf-8"); 
    				//设置post请求对象的实体，把要提交的数据封装在post请求的输出流中
    				hp.setEntity(entity);
    				//3.使用客户端发送post请求
					HttpResponse hr = hc.execute(hp);
					
					if(hr.getStatusLine().getStatusCode() == 200){
						InputStream is = hr.getEntity().getContent();
						
						String text = Utils.getTextFromStream(is);
						
						//发送消息让主线程刷新UI
						Message msg = handler.obtainMessage();
						msg.obj = text;
						handler.sendMessage(msg);
					}
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
    		}
    	};
    	
    	t.start();
    	
    }
    
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
