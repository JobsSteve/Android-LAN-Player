package com.example.socketsendlistdemo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	private static final String TAG = "SocketRecvListDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread() {
			public void run() {

				try {
					// �����������˵�Socket���������˿�6688
					ServerSocket socketConnection = new ServerSocket(6688);
					System.out.println("�������Ѿ��������ȴ����ӡ�");
					// ���տͻ������ӣ�������һ��socket����
					Socket scoket;
					scoket = socketConnection.accept();
					// �������ݵ��������������Ҫ��ObjectInputStream��ObjectOutputStream����
					ObjectInputStream in = new ObjectInputStream(
							scoket.getInputStream());
					ObjectOutputStream out = new ObjectOutputStream(
							scoket.getOutputStream());
					// ��ȡ�ͻ��˵Ķ���������
					SongList songList = (SongList) in.readObject();
					List cityList = songList.GetSongList();
					for (int i = 0; i < cityList.size(); i++) {

						Log.d(TAG, "�������˵õ��������ݣ�" + cityList.get(i).toString());
					}
					// ���ظ��ͻ��˵Ķ���
					SongList cityBack = new SongList();
					List list = new ArrayList();
					list.add("����");
					list.add("����");
					cityBack.SetSongList(list);
					out.writeObject(cityBack);
					out.flush();
					in.close();
					out.close();

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
