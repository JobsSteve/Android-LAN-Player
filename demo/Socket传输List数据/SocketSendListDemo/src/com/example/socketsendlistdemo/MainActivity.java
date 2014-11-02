package com.example.socketsendlistdemo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	private static final String TAG = "SocketSendListDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread() {
			public void run() {

				try {
					// ��װһ������ʵ��
					SongList city = new SongList();
					List list = new ArrayList();
					list.add("����");
					list.add("�Ϻ�");
					list.add("���");
					list.add("����");
					city.SetSongList(list);
					// ���ӵ���������
					Socket socketConnection = new Socket("192.168.159.4", 6688);
					// ʹ��ObjectOutputStream��ObjectInputStream���ж������ݴ���
					ObjectOutputStream out = new ObjectOutputStream(
							socketConnection.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(
							socketConnection.getInputStream());
					// ���ͻ��˵Ķ����������������������ȥ
					out.writeObject(city);
					out.flush();
					// ��ȡ�������˷��صĶ���������
					SongList cityBack = (SongList) in.readObject();
					List backList = cityBack.GetSongList();
					for (int i = 0; i < backList.size(); i++) {

						Log.d(TAG, "�ͻ��˵õ����س������ݣ�" + backList.get(i).toString());

					}
					out.close();
					in.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();

	}

}
