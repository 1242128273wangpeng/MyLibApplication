package com.lib.yimeng.com.mylibapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yimeng.common.BaseActivity;
import com.yimeng.common.utils.NetWorkUtils;
import com.yimeng.common.utils.SystemBarHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.text)
    TextView text;

    @Override
    protected void initData() {
        Toast.makeText(this, "initData", Toast.LENGTH_SHORT).show();
        text.setText("sss");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.text})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.text:
                Toast.makeText(this, "网络", Toast.LENGTH_SHORT).show();
                getContent("http://58.215.75.134/jeeba/validateLogin.do");
                break;
        }
    }

    public void getContent(String surl) {
        AsyncTask asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                StringBuilder stringBuilder = null;
                try {
                    String u = strings[0];
                    URL url = new URL(u);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8\n");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0)");
                    connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                    connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                    // 其中的memberName和password可通过fiddler来抓取
                    out.write("username=admin&password=123456");
                    out.flush();
                    out.close();
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    StringBuilder retStr = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String temp = br.readLine();
                    while (temp != null) {
                        retStr.append(temp);
                        temp = br.readLine();
                    }
                    br.close();
                    in.close();
                    System.out.println("1:" + retStr);
                    stringBuilder = new StringBuilder();
                    for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                        stringBuilder.append(header.getKey() + " " + header.getValue() + "\n");
                    }
                    System.out.println("2:" + stringBuilder.toString());
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(MainActivity.this, "结果是:" + s, Toast.LENGTH_SHORT).show();
            }
        };

        Object[] arg = new String[]{surl};
        asyncTask.execute(arg);
    }

}
