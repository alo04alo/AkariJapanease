package akari.jp.n5;

import akari.jp.base.Question;
import akari.jp.base.QuestionHandler;
import android.app.Application;

public class N5Support extends Application {
	int kind;
	int form;
	int score;
	Question[] questions;
	QuestionHandler questionHandler;

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getForm() {
		return form;
	}

	public void setForm(int form) {
		this.form = form;
	}

	public Question[] getQuestions() {
		return questions;
	}

	public void setQuestions(Question[] questions) {
		this.questions = questions;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public QuestionHandler getQuestionHandler() {
		return questionHandler;
	}

	public void setQuestionHandler(QuestionHandler questionHandler) {
		this.questionHandler = questionHandler;
	}

}
