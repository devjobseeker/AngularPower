package org.asu.chilll.power.enums;

public enum FileFolderName {
	Digit_Span("DigitSpan"),
	Digit_Span_Running("DigitSpanRunning"),
	Phonological_Binding("PhonologicalBinding"),
	Number_Update_Visual("NumberUpdateVisual"),
	Nonword_Repetition("NonWordRepetition"),
	
	Repetition_Detection_Auditory("RepetitionDetectionAuditory"),	//N-Back Auditory
	Repetition_Detection_Visual("RepetitionDetectionVisual"),	//N-Back Visual
	Number_Update_Auditory("NumberUpdateAuditory"),
	
	Audio_Recording_Test("zTestAudioRecording");
	
	private final String id;
	
	FileFolderName(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}