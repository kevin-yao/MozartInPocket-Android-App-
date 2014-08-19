package com.androidApp.mozartinpocket;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.androidApp.mozartinpocket.compose.ComposeActivity;
import com.mozartinpocket.entities.Music;

import android.os.Environment;


public class MusicItemList {
	
	public static List<String> musicItemList = new ArrayList<String>();
	public static HashMap<String, Music> musicMap = new HashMap<String,Music>();
	static String[] musicDB = {"C","C1 D1/2","C1/2 DE","CD EF","CD E1/2 FG","CDEF G1 A2","CD EF G1A1B1","C\nC","CD\nD","CDE\nC"};
	public static void populateMusicDB(){
	
		for(int i=1;i<11;i++){
			Music temp = new Music("Music"+i,"Author"+i,"2013/11/"+i,"This Music is Romantic");
			addItem(temp);
			String fileName = temp.getName();
			writeToSDCard(musicDB[i-1],fileName);
		}
		
	}
	public static void print(){
		Iterator<String> it = musicItemList.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	public static void addItem(Music musicItem){
		musicItemList.add(musicItem.getName());
		musicMap.put(musicItem.getName(),musicItem);
	}
	private static void writeToSDCard(String input, String fileName){
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File(sdCard.getAbsolutePath() + ComposeActivity.DIR);
		directory.mkdirs();
		File musicFile = new File(directory, fileName);
		try{
			PrintWriter writer = new PrintWriter(musicFile);
			writer.write(input);
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	}
