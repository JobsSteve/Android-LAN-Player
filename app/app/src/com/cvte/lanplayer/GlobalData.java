package com.cvte.lanplayer;

public class GlobalData {

	/*
	 * ���ڽ��պͷ���LAN UDP socket�� �̵߳�ͬ����
	 */
	public static byte[] UDP_SOCKET_LOCK = new byte[0];

	/*
	 * ���ڽ��պͷ���LAN TCP socket�� �̵߳�ͬ����
	 */
	public static byte[] TCP_SOCKET_LOCK = new byte[0];

	public class TranPort {
		/*
		 * UDP�㲥�Ķ˿�
		 */
		public static final int UDP_PORT = 43708;

		/*
		 * Socketɨ���õĵĶ˿�
		 */
		public static final int SOCKET_PORT = 8080;

		/*
		 * Socketͨ�ŵĶ˿�
		 */
		public static final int SOCKET_TRANSMIT_PORT = 38281;
	}

	public class LANScanCtrl {
		/**
		 * ��ʼLANɨ��
		 */

		/*
		 * ����ɨ��������ֹͣ�Ľ�����
		 */
		public static final String CTRL_LAN_SCAN_ACTION = "android.intent.action.ctrl_scan";

		/*
		 * ����ɨ��
		 */
		public static final int STARE_SCAN = 1;

		/*
		 * ֹͣɨ��
		 */
		public static final int STOP_SCAN = 2;

		/**
		 * ����LANɨ��
		 */
		/*
		 * ����ɨ��������ֹͣ�Ľ�����
		 */
		public static final String CTRL_LAN_RECV_ACTION = "android.intent.action.ctrl_recv";

		/*
		 * ����LAN����
		 */
		public static final int STARE_LAN_RECV = 1;

		/*
		 * ֹͣLAN����
		 */
		public static final int STOP_LAN_RECV = 2;

	}

	/**
	 * SOCKETͨ��ָ��
	 */
	public class SocketTranCommand {

		/*
		 * �յ��򵥵��ı���Ϣ
		 */
		public static final int COMMAND_RECV_MSG = 1;

		/*
		 * �����ȡ�����б�
		 */
		public static final int COMMAND_REQUSET_MUSIC_LIST = 2;

		/*
		 * ���������б�
		 */
		public static final int COMMAND_SEND_MUSIC_LIST = 3;

		/*
		 * �յ�LANɨ���ȷ�ϰ�
		 */
		public static final int COMMAND_LAN_ASK = 4;

		/*
		 * �����ȡ��������Bundle��ָ��
		 */
		public static final String GET_BUNDLE_COMMANT = "getcommant";

		/*
		 * �����ȡͨ�õ�Bundle���������
		 */
		public static final String GET_BUNDLE_COMMON_DATA = "getdata";

		/*
		 * ������������յ�service�����������͹㲥�Ľ�����
		 */
		public static final String RECV_SOCKET_FROM_SERVICE_ACTION = "android.intent.action.recv_lan_socket_from_service";

	}

}
