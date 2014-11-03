package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * 向其他设备发送获得音乐列表请求
 * 
 * @author JHYin
 * 
 */
public class RequestSocketMusicListUtil {

	private final String TAG = "RequestSocketMusicListUtil";

	private static RequestSocketMusicListUtil instance = null;

	private Thread mSendThread = null;
	private Socket mSocket = null;

	private String mTargetIP = null;

	/**
	 * 私有默认构造子
	 */
	private RequestSocketMusicListUtil() {

	}

	/**
	 * 静态工厂方法
	 */
	public static synchronized RequestSocketMusicListUtil getInstance() {

		if (instance == null) {
			instance = new RequestSocketMusicListUtil();
		}

		return instance;
	}

	/**
	 * 对其他设备请求获取其音乐列表
	 * 
	 * @param targetIP
	 */
	public void RequestSocketMusicList(String targetIP) {

		mTargetIP = targetIP;

		mSendThread = new Thread() {
			@Override
			public void run() {
				super.run();
				// 关闭输入流、socket
				try {

					// 实例化传输对象
					SocketTranEntity msg = new SocketTranEntity();
					msg.setmCommant(GlobalData.COMMAND_REQUSET_MUSIC_LIST);

					// 连接到服务器端
					mSocket = new Socket(mTargetIP,
							GlobalData.SOCKET_TRANSMIT_PORT);
					// 使用ObjectOutputStream和ObjectInputStream进行对象数据传输
					ObjectOutputStream out = new ObjectOutputStream(
							mSocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(
							mSocket.getInputStream());
					// 将客户端的对象数据流输出到服务器端去
					out.writeObject(msg);
					out.flush();

					out.close();
					in.close();
					mSocket.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};

		mSendThread.start();

	}

	public void StopSendMsg() throws IOException {
		if (mSendThread != null) {
			mSendThread.interrupt();
		}

		if (mSocket != null) {
			mSocket.close();
		}
	}
}
