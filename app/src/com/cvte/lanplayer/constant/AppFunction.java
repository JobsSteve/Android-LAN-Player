package com.cvte.lanplayer.constant;

import java.util.Random;

import android.content.Context;
import android.widget.Toast;

public class AppFunction {
	
	/*
	 * ��msת��Ϊ����ʱ����ʾ����
	 */
	public static String ShowTime(int time)
	{
		//��msת��Ϊs
		time /=1000;
		//���
		int minute = time / 60;
		//����
		int second = time % 60;
		return String.format("%02d:%02d", minute,second);
	}
	
	/*
	 * ���������      
	 * num���Χ   
	 * index�̶�ֵ����������������ܵ���index��
	 */
	public static int GenerateRandom(int num, int index)
	{
		Random ran = new Random();
		int ranNum = ran.nextInt(num);
		if(num > 0)
		{
			if(ranNum == index)
			{
				ranNum = GenerateRandom(num,index);
			}
		}
		return ranNum;
	}
	
	/*
	 * Toast��ʾ
	 * 
	 */
	public static void DisplayToast(Context context, String str)
	{
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
}
