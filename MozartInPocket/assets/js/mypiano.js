$(function(){
	var $write = $('#write');
	console.log("mypiano.js launched");
	var chordFlag = 0;
	var glissFlag = 0;
	var graceFlag = 0;
	var rawScore; // the key is parsed but the beats is not
	/* if( window.jsInterface.uploadFile() != null){
        var s = window.jsInterface.uploadFile();
        $write.html(s);
        eventFire(document.querySelector('#write'),'keyup');
    }*/
	// black key
	$('ul#piano li span').click(function(){
		var $this = $(this);
		character = $this.attr('id');
		console.log(character);
		$write.html($write.html()+character);
		//translateNoteToKey(character);
		mp3String = mp3NoteToKey(character);
		console.log(mp3String);
		window.jsInterface.getCharacter(mp3String);
		eventFire(document.querySelector('#write'), 'keyup');
	});
	// white key
	$('ul#piano li div').click(function(){
		var $this = $(this);
		//console.log($this);
		if($this.hasClass('anchor')){
			character = $this.attr('id');
			console.log(character);
		}

		$write.html($write.html()+character);
		//**IMPORTANT manually find the listener event.
		//abc_editor.editarea.changelistener.fireChanged();
		//
		//translateNoteToKey(character);
		//setCaretAtEnd($write);
		$write.focus();
		eventFire(document.querySelector('#write'), 'keyup');
		mp3String = mp3NoteToKey(character);
		console.log(mp3String);
		window.jsInterface.getCharacter(mp3String);
	});

	$('ul#symbol li').click(function(){
		var $this = $(this);
		if($this.hasClass('delete')){
			html = $write.html();
			for(var i=html.length;html[i-1]=='\''|| html[i-1] ==','|| (html[i-2] =='^')
			|| (html[i-2] =='_') || (Number(html[i-1])<9 && Number(html[i-1])>0) 
			|| html[i-2] == "\/" ;i--);
			// The beat can be deleted first
			$write.html(html.substr(0,i-1));
		}
		if($this.hasClass('space')){
			$write.html($write.html()+" ");
		}
		if($this.hasClass('empty')){
			$write.html('');
		}
		if($this.hasClass('endSection')){
			$write.html($write.html()+"\|");
		}
		if($this.hasClass('endLine')){
			html = $write.html();
			if(html[html.length -1]!='n'){
				$write.html(html+"\n");
			}
		}
		if($this.hasClass('endScore'))
			$write.html($write.html()+":\|");
		eventFire(document.querySelector('#write'), 'keyup');
	});

	$('ul#pro li').click(function(){
		var $this = $(this);
		if($this.hasClass('chord')){

			if(chordFlag){
				character = "]";
			}
			else{
				character="[";
				chordFlag = 1;
			}
		}
		if($this.hasClass('glissando')){
			if(glissFlag)
				character = "}";
			else{
				character = "{";
				glissFlag = 1;
			}
		}
		if($this.hasClass('grace')){
			if(graceFlag)
				character = ")";
			else{
				character = "(";
				graceFlag = 1;
			}
		}
		$write.html($write.html()+character);
		eventFire(document.querySelector('#write'), 'keyup');
	});

	$('ul#beat li').click(function(){
		var $this = $(this);
		character = $this.attr('class');
		console.log(character);
		$write.html($write.html()+character);
		eventFire(document.querySelector('#write'), 'keyup');
	});

	$("#save").click(function(){
		// var fileName = prompt("File Name: ", "musicFileTemp" );

		var text = $write.html();
		console.log(text);
		window.jsInterface.getContentText(text);
	});

	$('#upload').click(function(){
		var s = window.jsInterface.uploadFile();

		console.log(s);
		// $write.html(s);
		// eventFire(document.querySelector('#write'),'keyup');
	});
	$('#playMusicScore').click(function(){
		var input = $write.html();
		console.log("PlayMusicSCore"+input);
		window.jsInterface.playMusicScore(input);
	});
	if (window.jsInterface.uploadFile()!=null ){
		$write.html(window.jsInterface.uploadFile());
		eventFire(document.querySelector('#write'), 'keyup');
	}
	window.onunload(function(){
		window.jsInterface.saveTemp($write.html());
	});
	//function saveWebViewTemp(){
	//	window.jsInterface.saveTemp($write.html());
	//}
})

function eventFire(el, etype){
	if (el.fireEvent) {
		(el.fireEvent('on' + etype));
	} else {
		var evObj = document.createEvent('Events');
		evObj.initEvent(etype, true, false);
		el.dispatchEvent(evObj);
		console.log(el+etype);
	}
}

function setCaretAtEnd(elem) {
	var elemLen = elem.val().length;
	// For IE Only
	if (document.selection) {
		// Set focus
		elem.focus();
		// Use IE Ranges
		var oSel = document.selection.createRange();
		// Reset position to 0 & then set at end
		oSel.moveStart('character', -elemLen);
		oSel.moveStart('character', elemLen);
		oSel.moveEnd('character', 0);
		oSel.select();
	}
	else if (elem.selectionStart || elem.selectionStart == '0') {
		// Firefox/Chrome
		elem.selectionStart = elemLen;
		elem.selectionEnd = elemLen;
		elem.focus();
	} // if
} // SetCaretAtEnd()
/// for Showing on the webpage
function translateNoteToKey(s){
	var i = s.length;

	if(s[i-1] == ","){
		s = s.substr(0,i-1) + "3";
	}
	else{
		if(s[i-1] == "\'"){
			if(s.substr(i-2,i) == "\'\'"){
				s = "C7";
			}
			else
			{
				s = s.substr(0,i-2) + s[i-2].toUpperCase() + "6";
			}
			//console.log(s);
		}
		else{
			if(s.toUpperCase() != s){
				s = s.substr(0,i).toUpperCase() + "5";
			}
			else{
				s = s.substr(0,i) + "4";
			}
		}

	}
	if(s[0] == "_"){
		s = s[1]+"b"+s[2];
	}


	console.log("translated key "+s);
	var note = KeyNoteMap.keyToNote[s];
	console.log(note);
	return(note);
}


//for playing on the tablet;
function mp3NoteToKey(s){
	var i = s.length;

	if(s[i-1] == ","){
		s = s.substr(0,i-1).toUpperCase() + "3";
	}
	else{
		if(s[i-1] == "\'"){
			if(s.substr(i-2,i) == "\'\'"){
				s = "C7";
			}
			else
			{
				s = s.substr(0,i-2).toUpperCase() + s[i-2].toUpperCase() + "6";
			}
			//console.log(s);
		}
		else{
			if(s.toUpperCase() != s){
				s = s.substr(0,i).toUpperCase() + "5";
			}
			else{
				s = s.substr(0,i) + "4";
			}
		}

	}
	if(s[0] == "_"){
		s = s[1]+"b"+s[2];
	}

	return(s);
	//console.log(s);

}


function scoreParser(s){
	var i = 0;
	KeyNoteMap();
	//keyNoteMap();
	//_? = match "_" 0 or 1;
	// \w = [A-Za-Z]
	// \d = [0-9]
	// g = global, return all the match string
	// string.match(reg) return all the match string when "global" indicated, 
	// reg.exec(string) will only return the first one
	regChar = /_?\w[',\/\d]*/g;
	var elemArray = s.match(regChar);
	var pitchSequence = null;
	for( k =0; k<elemArray.length;k++){
		elem = elemArray[k];
		console.log(elem);
		console.log(translateNoteToKey(elem))
		pitchSequence = pitchSequence + translateNoteToKey(elem);
	}
	console.log(pitchSequence);
	// what you should do tonight
	// write a score parsor to translate all the notes into pitch, and ticks
	// 
}

function KeyNoteMap(){
	KeyNoteMap.keyToNote = {};
	KeyNoteMap.noteToKey = {};
	(function () {
		var A0 = 0x15; // first note
		var C8 = 0x6C; // last note
		var number2key = ["C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"];
		for (var n = A0; n <= C8; n++) {
			var octave = (n - 12) / 12 >> 0;
			var name = number2key[n % 12] + octave;
			KeyNoteMap.keyToNote[name] = n;
			KeyNoteMap.noteToKey[n] = name;
		}
	})();
}
