package org.asu.chilll.power.enums;

public enum GameIdType {
	Cross_Modal_Binding("CMB"),
	Digit_Span("DS"),
	Digit_Span_Running("DSR"),
	Location_Span("LS"),
	Location_Span_Running("LSR"),
	Number_Update_Visual("NUV"),
	Phonological_Binding("PBS"),
	Visual_Binding_Span("VBS"),
	Visual_Span("VS"),
	Visual_Span_Running("VSR"),
	
	Nonword_Repetition("NR"),
	Repetition_Detection_Auditory("RDA"),	//N-Back Auditory
	Repetition_Detection_Visual("RDV"),	//N-Back Visual
	
	Number_Update_Auditory("NUA"),
	
	Game_Progress("Game_Progress"),
	Student_Profile("Student_Profile"),
	AllGames("AllGames");
	
	private final String id;
	
	GameIdType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}