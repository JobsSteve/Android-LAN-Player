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
		 * Socketͨ�ŵĶ˿�
		 */
		public static final int SOCKET_TRANSMIT_PORT = 38281;

		/*
		 * Socket�ļ�����˿�
		 */
		public static final int SOCKET_FILE_TRANSMIT_PORT = 38381;

	}

	public class LANScanCtrl {

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
		 * �����ȡĳ�������ļ�
		 */
		public static final int COMMAND_REQUSET_MUSIC_FILE = 5;

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

	/**
	 * �������ò���
	 * 
	 * @author JHYin
	 * 
	 */
	public class Other {
		/*
		 * �������������������ļ��洢·��
		 * 
		 * ע�⣺���ϡ�sdcard�� ע�⣺windows��linux���»��߷�ʽ�ǲ�ͬ��
		 */
		public static final String SAVE_LAN_FILE_DIR = "LanDownloads";
	}

	/**
	 * Ӧ���ڲ���֪ͨ��Ϣ
	 * 
	 * 
	 * @author JHYin
	 *
	 */
	public class AppInform{
		
		
		/*
		 * �յ����������б������
		 */
		public static final int COMMAND_REFLASH_MUSIC_LIST = 1;
		
		/*
		 * �յ���ʾTOAST��Ϣ������
		 */
		public static final int COMMAND_SHOW_TOAST_MSG = 2;
		

		/*
		 * ������������յ�Ӧ���ڲ���֪ͨ��Ϣ�Ľ�����
		 */
		public static final String RECV_APP_INFORM_ACTION = "android.intent.action.recv_app_inform_action";

	}
	
	
}
