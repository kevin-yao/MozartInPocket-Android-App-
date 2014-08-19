package com.androidApp.mozartinpocket.compose;







import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreParser {
	private ArrayList<String> scorePartition = new ArrayList<String>();
	private ArrayList<Integer> scorePitches = new ArrayList<Integer>();
	private ArrayList<Integer> scoreBeats = new ArrayList<Integer>();
	private static HashMap<String, Integer> keyToNoteMap = new HashMap<String,Integer>();
	private static HashMap<Integer, String> noteToKeyMap = new HashMap<Integer, String>();
	public void scoreParser(String input){
		keyNoteTransition();// initialize keynotemap
		segmentScore(input);
		Iterator<String> it = scorePartition.iterator();
		while(it.hasNext()){
			String thisNote = it.next(); // get a raw note
			System.out.println(thisNote);
			String note = findKey(thisNote);// find the pitch of this note
			int beat = findBeat(thisNote);
			String key = keyParse(note); // parse the key to pitch
			int pitch = keyToNoteMap.get(key);// find the corresponding pitch
			System.out.println(pitch);
			scorePitches.add(pitch);
			scoreBeats.add(beat);
		}
		
	}
	public ArrayList<Integer> getScorePitches(){
		return scorePitches;
	}
	public ArrayList<Integer> getScoreBeats(){
		return scoreBeats;
	}
	// segment the whole music score to different note
	public void segmentScore(String input){
		String regex = "_?\\w[',\\/\\d]*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find()){
			String m = matcher.group();
			//System.out.println(m);
			scorePartition.add(m);
		}
	}
	// find the key in a note
	public String findKey(String key){
		String keyRegex = "_?\\w[',]*";
		Pattern pattern = Pattern.compile(keyRegex);
		Matcher matcher = pattern.matcher(key);
		String temp = null;
		if(matcher.find()){
			temp = matcher.group();
		}
		System.out.println(temp);
		return(temp);
	}
	public int findBeat(String key){
		int normalizedBeat = 4;
		String beatRegex = "\\d\\/?\\d?";
		Pattern pattern  = Pattern.compile(beatRegex);
		Matcher matcher = pattern.matcher(key);
		String temp = "1/2";
		if(matcher.find()){
			temp = matcher.group();
		}
		System.out.println(temp);
		if(temp.equals("1/2")){
			normalizedBeat = 3;
		}
		if(temp.equals("1")){
			normalizedBeat = 4;
		}
		if(temp.equals("2")){
			normalizedBeat = 5;
		}
		if(temp.equals("4")){
			normalizedBeat = 6;
		}
		if(temp.equals("1/4")){
			normalizedBeat = 2;
		}
		if(temp.equals("1/8")){
			normalizedBeat = 1;
		}
		return(normalizedBeat);
	}
	// parse the key format
	public String keyParse(String key){

		int i = key.length();
		if(key.charAt(i-1) == ','){
			key = key.substring(0,i-1) + "3";
		}
		else{
			if(key.charAt(i-1) == '\''){
				if(key.charAt(i-2) == '\''){
					key = "C7";
				}
				else
				{
					key = key.substring(0,i-2) + Character.toString(key.charAt(i-2)).toUpperCase() + "6";
				}
			}
			else{
				if(key.toUpperCase() != key){
					key = key.substring(0,i).toUpperCase() + "5";
				}
				else{
					key = key.substring(0,i) + "4";
				}
			}

		}
		if(key.charAt(0) == '_'){
			key = key.substring(1,2)+"b"+key.substring(2,3);
		}


		System.out.println("translate key "+ key);
		return(key);
	}
	public void keyNoteTransition(){
		int A0 = 0x15; // first note
		int C8 = 0x6C; // last note
		String[] number2key = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
		for (int n = A0; n <= C8; n++) {
			int octave = (n - 12) / 12 >> 0;
		String name = number2key[n % 12] + octave;
		//System.out.println(name);
		keyToNoteMap.put(name, n);
		noteToKeyMap.put(n, name);
		}
	}
	public static void main(String[] args){
		String input = "c'd'e'f'c''c'' c'' c'' ";
		ScoreParser parser = new ScoreParser();
		parser.scoreParser(input);	
	}
}