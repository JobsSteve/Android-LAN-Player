package com.cvte.lanplayer.data;

import java.io.Serializable;

public class MusicData implements Serializable{
	
	
	//������
	private int _MusicID;
	//�ļ���
	private String _FileName;
	//������
	private String _MusicName;
	//����������ʱ��
	private int _MusicDuration;
	//�ݳ���
	private String _MusicArtist;
	//ר��
	private String _MusicAlbum;
	//���
	private String _MusicYear;
	//�ļ�����
	private String _FileType;
	//�ļ���С
	private String _FileSize;
	//�ļ�·��
	private String _FilePath;
	
	public int getMusicID()
	{
		return _MusicID;
	}
	
	public void setMusicID(int musicID)
	{
		this._MusicID = musicID;
	}

	//�ļ���
	public String getFileName()
	{
		return _FileName;
	}
	
	public void setFileName(String fileName)
	{
		this._FileName = fileName;
	}
	//��������
	public String getMusicName()
	{
		return _MusicName;
	}
	
	public void setMusicName(String musicName)
	{
		this._MusicName = musicName;
	}
	//����������ʱ��
	public int getMusicDuration()
	{
		return _MusicDuration;
	}
	
	public void setMusicDuration(int musicDuration)
	{
		this._MusicDuration = musicDuration;
	}
	//�ݳ���
	public String getMusicArtist()
	{
		return _MusicArtist;
	}
	
	public void setMusicArtist(String musicArtist)
	{
		this._MusicArtist = musicArtist;
	}
	//ר��
	public String getMusicAlbum()
	{
		return _MusicAlbum;
	}
	
	public void setMusicAlbum(String musicAlbum)
	{
		this._MusicAlbum = musicAlbum;
	}
	//���
	public String getMusicYear()
	{
		return _MusicYear;
	}
	
	public void setMusicYear(String musicYear)
	{
		this._MusicYear = musicYear;
	}
	//�ļ�����
	public String getFileType()
	{
		return _FileType;
	}
	
	public void setFileType(String fileType)
	{
		this._FileType = fileType;
	}
	//�ļ���С
	public String getFileSize()
	{
		return _FileSize;
	}
	
	public void setFileSize(String fileSize)
	{
		this._FileSize = fileSize;
	}
	//�ļ�·��
	public String getFilePath()
	{
		return _FilePath;
	}
	
	public void setFilePath(String filePath)
	{
		this._FilePath = filePath;
	}
}
