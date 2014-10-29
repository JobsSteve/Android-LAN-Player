package com.cvte.lanplayer;

public class GlobalData {

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

	/*
	 * ����ɨ��������ֹͣ�Ľ�����
	 */
	public static final String CTRL_SCAN_ACTION = "android.intent.action.ctrl_scan";

	/*
	 * ����ɨ��������ֹͣ�Ľ�����
	 */
	public static final String CTRL_RECV_ACTION = "android.intent.action.ctrl_recv";

	/*
	 * ����ɨ��
	 */
	public static final int STARE_SCAN = 1;

	/*
	 * ֹͣɨ��
	 */
	public static final int STOP_SCAN = 2;

	/*
	 * ����LAN����
	 */
	public static final int STARE_LAN_RECV = 1;

	/*
	 * ֹͣLAN����
	 */
	public static final int STOP_LAN_RECV = 2;

	/*
	 * ����ɨ�����Ľ�����
	 */
	public static final String GET_SCAN_DATA_ACTION = "android.intent.action.get_scan_data";

	/*
	 * �����������豸ɨ�赽����ʱ��Ľ�����
	 */
	public static final String IS_SCANED_ACTION = "android.intent.action.is_scaned";

	/*
	 * ���������յ������豸��Ϣ�Ľ�����
	 */
	public static final String RECV_LAN_SOCKET_MSG_ACTION = "android.intent.action.recv_lan_socket_msg";

	/*
	 * ������������յ�service�����������͹㲥�Ľ�����
	 */
	public static final String RECV_SOCKET_FROM_SERVICE_ACTION = "android.intent.action.recv_lan_socket_from_service";

	/*
	 * �յ��򵥵��ı���Ϣ
	 */
	public static final String RECV_SCAN = "recv_scan";

	/*
	 * �յ��򵥵��ı���Ϣ
	 */
	public static final int RECV_MSG = 1;

	/*
	 * �����ȡ�����б�
	 */
	public static final int REQUSET_MUSIC_LIST = 2;

	/*
	 * �����ȡ��������ָ��
	 */
	public static final String GET_BUNDLE_COMMANT = "commant";

	/*
	 * �����ȡBundle���������
	 */
	public static final String GET_BUNDLE_DATA = "getdata";

	/*
	 * �����ı���Ϣ���
	 */
	public static final String COMMAND_SEND_MSG = "msg";

}
