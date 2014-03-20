package akari.jp.base;

public class Question {
	private int id;
	private int form;
	private int kind;
	private String content;
	private int result;
	private int choose;
	private String answer1;
	private String answer2;
	private String answer3;
	private String note;
	private int count;
	private String resource;
	public Question() {

	}

	public Question(int id, int form, int kind, String content, int result,
			String answer1, String answer2, String answer3, String note,
			int count, String resource) {
		this.id = id;
		this.form = form;
		this.kind = kind;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.content = content;
		this.result = result;
		this.note = note;
		this.count = count;
		this.resource = resource;
	}
	
	public Question(int form, int kind, String content, int result,
			String answer1, String answer2, String answer3, String note,
			int count, String resource) {
		this.form = form;
		this.kind = kind;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.content = content;
		this.result = result;
		this.note = note;
		this.count = count;
		this.resource = resource;
	}

	// get, set data at here
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getForm() {
		return this.form;
	}

	public int getKind() {
		return this.kind;
	}

	public int getChoose() {
		return this.choose;
	}

	public int getResult() {
		return this.result;
	}

	public String getAnswer1() {
		return this.answer1;
	}

	public String getAnswer2() {
		return this.answer2;
	}

	public String getAnswer3() {
		return this.answer3;
	}

	public String getContent() {
		return this.content;
	}

	public String getNote() {
		return this.note;
	}

	public void setForm(int form) {
		this.form = form;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public void setChoose(int choose) {
		this.choose = choose;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}
		
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getContentofAnswer(int id) {
		switch (id) {
		case 0:
			return this.getAnswer1();
		case 1:
			return this.getAnswer2();
		case 2:
			return this.getAnswer3();
		default:
			return null;
		}
	}
}
