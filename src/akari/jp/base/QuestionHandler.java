package akari.jp.base;

public class QuestionHandler {
	private int size;
	private Question[] questions;

	public QuestionHandler(int size) {
		this.size = size;
		this.questions = new Question[this.size];
	}

	public QuestionHandler(Question[] questions) {
		this.questions = questions;
	}

	public Question getQuestion(int index) {
		return this.questions[index];
	}

	public int getScore() {
		int score = 0;

		for (int index = 0; index < questions.length; index++)
			if (questions[index].getChoose() == questions[index].getResult())
				score++;
		return score;
	}

}
