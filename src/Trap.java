import java.util.ArrayList;

public class Trap {
	
	
	ArrayList<Person> trapped;
	
	protected String currentAnswer;
	public String currentQuestion;
	
	protected void question() {
		int select = (int) (Math.random() * 10);
//		System.out.println(select);
		
		switch(select) {
		case 0 :
			currentAnswer = q0();
			break;
		case 1 :
			currentAnswer = q1();
			break;
		case 2 :
			currentAnswer = q2();
			break;
		case 3 :
			currentAnswer = q3();
			break;
		case 4 :
			currentAnswer = q4();
			break;
		case 5 :
			currentAnswer = q5();
			break;
		case 6 :
			currentAnswer = q6();
			break;
		case 7 :
			currentAnswer = q7();
			break;
		case 8 :
			currentAnswer = q8();
			break;
		case 9 :
			currentAnswer = q9();
			break;
		}
	}
	
	private String q9() {
		double select = Math.random();
		String question = "What shape am I?: ";
		String answer = "";
		
		if(select < .2) {
			question += "4 equal length sides";
			answer = "square";
		}
		if(select >= .2 && select < .4) {
			question += "4 sides";
			answer = "rectangle";
		}
		if(select >= .4 && select < .6) {
			question += "3 sides";
			answer = "triangle";
		}
		if(select >= .6 && select < .8) {
			question += "8 sides";
			answer = "octagon";
		}
		if (select >= .8) {
			question += "6 sides";
			answer = "hexagon";
		}
		
		currentQuestion = question;
		return answer;
	}

	private String q8() {
		double select = Math.random();
		String answer = "";
		String question = "Pick your poison: 1 or 2";
		if (select < .5) {
			answer = "1";
		} else {answer = "2";}
		
		currentQuestion = question;
		return answer;
		
	}

	private String q7() {
		double select = Math.random();
		String question = "What am I feeling: ";
		String answer = "";
		
		if(select < .33) {
			question += ":)";
			answer = "happy";
		}
		if(select >= .33 && select < .66) {
			question += ":(";
			answer = "sad";
		}
		if(select >= .66) {
			question += ":o";
			answer = "surprised";
		}
		
		
		currentQuestion = (question);
		return answer;
	}

	private String q6() {
		
		String question = "Echo me: ";
		
		char[] array = new char[10];
		for (int i = 0; i < 10; i++) {
			double s = Math.random() * 2 + 1;
			if (s < 2) {
				int b = (int) (Math.random() * 10) + 48;
				char c = (char) b;
				array[i] = c;
			} else {
				int b = (int) (Math.random() * 26) + 65;
				char c = (char) b;
				array[i] = c;
			}
		}
		String answer = new String(array);
		question += answer;
		
		currentQuestion = (question);
		return answer;	
	}

	private String q5() {
		double select = Math.random();
		String question = "Say the same: ";
		String answer = "";
		
		if(select < .25) {
			question += "UP";
			answer = "up";
		}
		if(select >= .25 && select < .5) {
			question += "DOWN";
			answer = "down";
		}
		if(select >= .5 && select < .75) {
			question += "LEFT";
			answer = "left";
		}
		if(select >= .75) {
			question += "RIGHT";
			answer = "right";
		}
		
		currentQuestion = (question);
		return answer;
		
		
	}

	private String q4() {
		double select = Math.random();
		String question = "Say the opposite: ";
		String answer = "";
		
		if(select < .25) {
			question += "UP";
			answer = "down";
		}
		if(select >= .25 && select < .5) {
			question += "DOWN";
			answer = "up";
		}
		if(select >= .5 && select < .75) {
			question += "LEFT";
			answer = "right";
		}
		if(select >= .75) {
			question += "RIGHT";
			answer = "left";
		}
		
		currentQuestion = (question);
		return answer;
		
	}

	private String q3() {
		double select = Math.random();
		String question = "What animal am I?: ";
		String answer = "";
		
		if(select < .2) {
			question += "MOO";
			answer = "cow";
		}
		if(select >= .2 && select < .4) {
			question += "ONIK";
			answer = "pig";
		}
		if(select >= .4 && select < .6) {
			question += "BAAA";
			answer = "sheep";
		}
		if(select >= .6 && select < .8) {
			question += "BARK";
			answer = "dog";
		}
		if (select >= .8) {
			question += "MEOW";
			answer = "cat";
		}
		
		currentQuestion = (question);
		return answer;
	}

	private String q2() {
		int answer = 0;
		String question = "Do the math: ";
		for (int i = 0; i < 10; i++) {
			int temp = (int) (Math.random() * 10);
			question += temp + "+";
			answer += temp;
		}
		currentQuestion = (question + "0");

		return answer + "";
		
	}
	
	private String q1() {
		String direction = direction();
		String question = "You start facing " + direction + ", you then turn:";
		
		for (int i = 0; i < 5; i++) {
			String temp = L_R();
			direction = turn(temp, direction);
			question = question + " " +temp + ",";
		}
		currentQuestion = (question + " which direction do you face?");
		return direction;
	}



	private String q0() {
		int answer = (int) (Math.random() * 100) + 1;
		currentAnswer = String.valueOf(answer);
		currentQuestion = "I am thinking of a number between 1 and 100, guess.";
		return answer + "";
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
		double temp = Math.random();
		if ( temp < .5) {
			return "left";
		}
		else {return "right";}
		
	}

	private String turn(String temp, String direction) {
		switch (direction.toLowerCase()) {
		case "north" :
			if (temp.equals("left")) {
				return "west";
			} else {return "east";}
		case "east" :
			if (temp.equals("left")) {
				return "north";
			} else {return "south";}
		case "south" :
			if (temp.equals("left")) {
				return "east";
			} else {return "west";}
		case "west" :
			if (temp.equals("left")) {
				return "south";
			} else {return "north";}
		}
		
		
		return null;
	}

}
