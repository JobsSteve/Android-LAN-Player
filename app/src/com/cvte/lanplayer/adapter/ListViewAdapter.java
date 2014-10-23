package com.cvte.lanplayer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cvte.lanplayer.R;
import com.cvte.lanplayer.constant.AppConstant;
import com.cvte.lanplayer.constant.AppFunction;
import com.cvte.lanplayer.data.MusicData;
import com.cvte.lanplayer.holder.ListViewHolder;

public class ListViewAdapter extends BaseAdapter {
	
	//���ֱ����б�
	private List<MusicData> mList;
	//�õ�һ��LayoutInfalter�����������벼��
	private LayoutInflater mInflater;
	//���ֲ���״̬
	private int mPlayState;
	//����λ��
	private int mPlayIndex;
	
	//���캯��
	public ListViewAdapter(Context context,List<MusicData> list)
	{
		this.mInflater = LayoutInflater.from(context);
		this.mList = list;
	}
	
	/*
	 * ���ò���״̬
	 * playindex����������
	 * playstate����״̬
	 */
	public void setPlayState(int playindex,int playstate)
	{
		this.mPlayIndex = playindex;
		this.mPlayState = playstate;
		notifyDataSetChanged();
	}
	
	//���¸����б�
	public void setListAdapter(List<MusicData> list)
	{
		this.mList = list;
		notifyDataSetChanged();
	}
	
	//��ȡ��ǰ����״̬
	public int getPlayState()
	{
		return this.mPlayState;
	}
	
	//��ȡ��ǰ��������
	public int getPlayIndex()
	{
		return this.mPlayIndex;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListViewHolder holder;
		if(convertView == null)
		{
			//��ȡlistview_item�����ļ���ͼ
			convertView = mInflater.inflate(R.layout.listview_item, null);
			holder = new ListViewHolder();
			//��ȡ�����ؼ�
			//����״̬ͼ��
			holder.imgState = (ImageButton)convertView.findViewById(R.id.musicplaystate);
			//�����ļ�����
			holder.sName = (TextView)convertView.findViewById(R.id.musicName);
			//�����ݳ���
			holder.sArtist = (TextView)convertView.findViewById(R.id.musicAritst);
			//���ֲ�����ʱ��
			holder.sTime = (TextView)convertView.findViewById(R.id.musicTime);
			//������
			holder.sPos = (TextView)convertView.findViewById(R.id.musiclistPos);
			convertView.setTag(holder);
		}
		else
		{
			//ȡ��ViewHolder����
			holder = (ListViewHolder)convertView.getTag();
		}
		//���ò���״̬ͼ��
		ShowPlayStateIcon(convertView,position);
		//����TextView��ʾ����
		holder.sName.setText((CharSequence) mList.get(position).getFileName());
		holder.sArtist.setText((CharSequence)mList.get(position).getMusicArtist());
		holder.sTime.setText((CharSequence)AppFunction.ShowTime(mList.get(position).getMusicDuration()));
		holder.sPos.setText((CharSequence)String.format("%d. ", mList.get(position).getMusicID()));
		
		return convertView;
	}
	
	/*
	 * �������ֲ���״̬ͼ��
	 */
	private void ShowPlayStateIcon(View view, int position)
	{
		//��ȡ����״̬ͼ��
		ImageButton imgbtn = (ImageButton)view.findViewById(R.id.musicplaystate);
		//�ж��Ƿ��ǵ�ǰ��ѡ���ֵ�������
		if(position != mPlayIndex)
		{
			//���ǵ�ǰ������������������״̬ͼ�����أ�gone��ռ�κοռ䣩
			imgbtn.setVisibility(View.GONE);
			return ;
		}
		//�ǵ�ǰ��ѡ�����֣�����״̬ͼ����ʾ
		imgbtn.setVisibility(View.VISIBLE);
		//�ж���ѡ��������״̬��MUSIC_PAUSE:��ͣ    MUSIC_PLAYING:������
		if(mPlayState == AppConstant.MusicPlayState.PLAY_STATE_PAUSE)
		{
			imgbtn.setBackgroundResource(R.drawable.list_play_icon);
		}
		else
		{
			imgbtn.setBackgroundResource(R.drawable.list_pause_icon);
		}
	}
}
