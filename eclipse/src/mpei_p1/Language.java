package mpei_p1;

public class Language {
	private String name;
	private Boolean text;
	private Boolean audio;
	
	public Language(String name, Boolean text, Boolean audio) {
		this.name = name;
		this.text = text;
		this.audio = audio;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getText() {
		return text;
	}

	public void setText(Boolean text) {
		this.text = text;
	}

	public Boolean getAudio() {
		return audio;
	}

	public void setAudio(Boolean audio) {
		this.audio = audio;
	}

	@Override
	public String toString() {
		return "Language [name=" + name + ", text=" + text + ", audio=" + audio + "]";
	}
	
	
	
	
	

}
