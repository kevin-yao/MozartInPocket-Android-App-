package com.androidApp.mozartinpocket.compose;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaPlayer;
import android.os.Environment;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

public class MusicScorePlayer {
	MidiTrack tempoTrack = new MidiTrack();
	MidiTrack noteTrack = new MidiTrack();
	private String musicScoreName;
	private String dir = "/DCIM/Audio";
	private String input = null;
	private File sdCard = Environment.getExternalStorageDirectory();
	private File directory = new File(sdCard.getAbsolutePath() + dir);
	private String absoluteFileName;

	public MusicScorePlayer(String musicScoreName, String input){
		this.musicScoreName = musicScoreName;
		if(input!=null){
			this.input = input;
		}
	}
	// 2. Add events to the tracks
	// 2a. Track 0 is typically the tempo map
	public void playMusicScore(){
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
		Tempo t = new Tempo();
		t.setBpm(228);
		tempoTrack.insertEvent(ts);
		tempoTrack.insertEvent(t);
		ScoreParser parser = new ScoreParser();
		//String input = "C1/2 D1/4EF_G,A2B cdefgab _a' _D,CDEFGABcdefgabCDEFGABBCDEAFG";
		//input = "CCGGAAG1 FFEEDDC1 GGFFEED1 GGFFEED1 CCGGAAG1 FFEEDDC1";
		//input = "GABc dedB|dedB dedB|c2ec B2dB|c2A2A2BA||GABc dedB|dedB dedB|c2ec B2dB|A2F2G4 g2gf g2Bd|g2fe dBGB c2ec B2dB c2A2A2df||g2gfg2Bd|g2fe dBGB|c2ec B2dB A2F2G4";
		//String input = "A1/4A1/4B1/4B1/4c1/4c1/4B1/4B1/4A1/4A1/4E1/4E1/4C1/4C1/4B,1/4B,1/4G1/4G1/4F1/4F1/4E1/4F1/4G1/4F2 "
				//+ "F1/4F1/4G1/4G1/4A1/4A1/4B1/4B1/4G1/4G1/4D1/4D1/4F1/4F1/4E1/4E1/4D1/4E1/4F1/4E2 E1/4B,1/4C1/4E1/4D1/4E1, G,1/4C1/4E1/4D1/4E1 G,1/4C1/4F1/4E1/4F1 G,CFEF1 FEF_GG1 GAGAE1";
		parser.scoreParser(input);
		ArrayList<Integer> noteList = parser.getScorePitches();
		ArrayList<Integer> beatList = parser.getScoreBeats();
		long thisInterval = 360;
		// 2b. Track 1 will have some notes in it
		for(int i = 0; i < noteList.size(); i++)
		{	
			int channel = 0, pitch = noteList.get(i), velocity = 100;
			int beat = beatList.get(i);
			//long beatUnit = 120;
			long lastTick = noteTrack.getLengthInTicks();
			long duration = 120;
			long nextInterval = 360;
			if(beat >= 3){
				duration = 120 << (beat-3);
			}
			else{
				duration = 120 >> (3-beat);
				nextInterval = nextInterval >> (3-beat);
			}
			
			noteTrack.insertNote(channel, pitch, velocity, lastTick+thisInterval, duration);
			thisInterval = nextInterval;
		}

		// It's best not to manually insert EndOfTrack events; MidiTrack will
		// call closeTrack() on itself before writing itself to a file

		// 3. Create a MidiFile with the tracks we created
		ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(noteTrack);

		MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

		// 4. Write the MIDI data to a file

		try {
			directory.mkdirs();
			absoluteFileName = directory.getAbsolutePath() +"/"+ musicScoreName +".mid";
			File output = new File(absoluteFileName);
			midi.writeToFile(output);
			
			//playMIDI(absoluteFileName);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void playMIDI(){
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(absoluteFileName);
			mp.prepare();
			mp.start();
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.stop();	
				}
			});

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	}
