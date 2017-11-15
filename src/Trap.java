import java.util.ArrayList;

public class Trap {
	
	
	ArrayList<Person> trapped;
	
	private String currentAnswer;
	private String currentQuestion;
	
	protected void question() {
		int select = (int) (Math.random() * 10);
		System.out.println(select);
		
		switch(select) {
		case 0 :
			q0();
			break;
		case 1 :
			q1();
			break;
		case 2 :
			q2();
			break;
		case 3 :
			q3();
			break;
		case 4 :
			q4();
			break;
		case 5 :
			q5();
			break;
		case 6 :
			q6();
			break;
		case 7 :
			q7();
			break;
		case 8 :
			q8();
			break;
		case 9 :
			q9();
			break;
		}
	}
	
	private void q9() {
		// TODO Auto-generated method stub
		
	}

	private void q8() {
		// TODO Auto-generated method stub
		
	}

	private void q7() {
		// TODO Auto-generated method stub
		
	}

	private void q6() {
		// TODO Auto-generated method stub
		
	}

	private void q5() {
		// TODO Auto-generated method stub
		
	}

	private void q4() {
		// TODO Auto-generated method stub
		
	}

	private void q3() {
		// TODO Auto-generated method stub
		
	}

	private void q2() {
		// TODO Auto-generated method stub
		
	}
	
	private void q1() {
		String direction = direction();
		String question = "You start facing " + direction;
		
		for (int i = 0; i < 3; i++) {
			question.concat(" " + L_R());
		}
	}

	private void q0() {
		int answer = (int) (Math.random() * 100) + 1;
		currentAnswer = String.valueOf(answer);
		currentQuestion = "I am thinking of a number between 1 and 100, guess.";
	}
	
	private String direction() {
		int answer = (int) (Math.random() * 4) + 1;
		switch(answer) {
		case 1 :
			return "North";
		case 2 :
			return "East";
		case 3 : 
			return "South";
		case 4 :
			return "West";
		default :
			return "North";
		}
	}
	
	private String L_R() {
		double temp = (Math.random() * 2) + 1;
		int answer = (int) Math.round(temp);
		switch(answer) {
		case 1 :
			return "left";
		case 2 : 
			return "right";
		default :
			return "left";
		}
	}


}
