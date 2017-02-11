package irdc.ex08_11;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* import相关class */

public class EX08_11 extends Activity {
  /*
   * 变量声明 filename：上传后在服务器上的文件名称 uploadFile：要上传的文件路径 actionUrl：服务器上对应的程序路径
   */


    //要上传文件的sdcard地址
    private String srcPath = Environment.getExternalStorageDirectory() + "/1.jpg";
    private String uploadFile = srcPath;
    String uploadUrl = "http://192.168.1.36:8080/FileUpload/FileUploadServlet";
    private TextView mText1;
    private TextView mText2;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mText1 = (TextView) findViewById(R.id.myText2);
        mText1.setText("文件路径：\n" + uploadFile);
        mText2 = (TextView) findViewById(R.id.myText3);
        mText2.setText("上传网址：\n" + uploadUrl);
    /* 设置mButton的onClick事件处理 */
        mButton = (Button) findViewById(R.id.myButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadFile();
            }
        });
    }

    /* 上传文件至Server的方法 */
    private void uploadFile() {


        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection
                    .getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos
                    .writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                            + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();

            Toast.makeText(this, "上传成功", Toast.LENGTH_LONG).show();
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            setTitle(e.getMessage());
        }

    }

}
