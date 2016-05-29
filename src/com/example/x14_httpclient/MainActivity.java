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
    			
    			//ʹ��HttpClient�����GET��ʽ�ύ
    			//1.����HttpClient����
    			HttpClient hc = new DefaultHttpClient();
    			
    			//2.����httpget���󣬹��췽���Ĳ���������ַ
    			HttpGet hg = new HttpGet(path);
    			
    			//3.ʹ�ÿͻ��˶��󣬰�get�����ͳ�ȥ
    			try {
					HttpResponse hr = hc.execute(hg);
					
					//�õ���Ӧͷ�е�״̬��
					StatusLine sl = hr.getStatusLine();
					
					//״̬��Ϊ200��ɹ�
					if(sl.getStatusCode() == 200){
						//�õ���Ӧͷʵ��
						HttpEntity he = hr.getEntity();
						
						//�õ�ʵ���е����ݣ����Ƿ������з��ص�������
						InputStream is = he.getContent();
						
						String text = Utils.getTextFromStream(is);
						
						//������Ϣ�����߳�ˢ��UI
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
    			
    			//1.�����ͻ��˶���
    			HttpClient hc = new DefaultHttpClient();
    			
    			//2.����post�������
    			HttpPost hp = new HttpPost(path);
    			
    			//��װform���ύ������
    			BasicNameValuePair bnvp = new BasicNameValuePair("name", name);
    			BasicNameValuePair bnvp2 = new BasicNameValuePair("pass",pass);
    			
    			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
    			parameters.add(bnvp);
    			parameters.add(bnvp2);
    			
    			try {
    				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"utf-8"); 
    				//����post��������ʵ�壬��Ҫ�ύ�����ݷ�װ��post������������
    				hp.setEntity(entity);
    				//3.ʹ�ÿͻ��˷���post����
					HttpResponse hr = hc.execute(hp);
					
					if(hr.getStatusLine().getStatusCode() == 200){
						InputStream is = hr.getEntity().getContent();
						
						String text = Utils.getTextFromStream(is);
						
						//������Ϣ�����߳�ˢ��UI
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
