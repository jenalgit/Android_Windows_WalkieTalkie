package brunoricardo.android_windows_walkietalkie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String Address,Body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prob_main);
        Button teste=(Button) findViewById(R.id.button);
        teste.setOnClickListener(this);
        Button t1=(Button)findViewById(R.id.button2);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = "address = ? AND body = ? AND read = ?";
                ContentValues values=new ContentValues();
                values.put("read",true);
                String[] selectionArgs = {"", "", "0"};
                Uri uri=Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(uri,null,"read = 0",null,null);

                if (cursor.moveToFirst()) { // must check the result to prevent exception
                    do {
                        String msgData = "";
                            Address=cursor.getString(cursor.getColumnIndex("address"));
                            Body=cursor.getString(cursor.getColumnIndex("body"));
                        Log.d("Msg","Numero foi:"+Address);
                        Log.d("Msg","Mensagem foi:"+Body);
                        SendMessage ola=new SendMessage(Address,Body);
                        Thread teste=new Thread(ola);
                        teste.start();
                        // use msgData
                    } while (cursor.moveToNext());
                } else {
                    // empty box, no SMS
                }
                cursor.close();


            }
        });
    }

    @Override
    public void onClick(View v) {
        Thread t=new Thread(){
            @Override
            public void run(){


                try {
                    System.out.println("Starting Connection");
                    Socket s = new Socket("192.168.1.11", 4321);
                    System.out.println("Connection DONE");
                    DataInputStream dis=new DataInputStream((s.getInputStream()));
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    EditText editText=(EditText)findViewById(R.id.editText);
                    String text=editText.getText()+"";
                    dos.writeUTF(text);
                    dos.flush();
                    dos.close();
                    s.close();
                    System.out.println("Closing socket");
                    Socket s1 = new Socket("192.168.1.11", 4321);
                    DataInputStream dis1=new DataInputStream((s1.getInputStream()));
                    String text1=dis1.readUTF();
                    TextView ola=(TextView)findViewById(R.id.textView);
                    Log.d("Teste",dis.readUTF()+" text");
                    dis.close();
                    ola.setText(text1);

                    System.out.println("Closing socket");
                } catch (UnknownHostException e){
                    Log.d("Teste","There was an Unknown Erorr:");
                    Log.d("Teste",e.getMessage());
                } catch (IOException e) {
                    Log.d("Teste","There was an IOException:");
                    Log.d("Teste",e.getMessage());
                }
        }

        };
        t.start();

    }
}
