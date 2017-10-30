
public class CircleQue {
	
	CircleQue(int numRetention) {
		hold = numRetention;
		Queue = new Message[hold];
	}

	private Message[] Queue;
	private int index = 0;
	private int count = 0;
	private int hold;
	
	public void add(Message mess) {
		int realDex = index % hold;
		count += 1;
		Queue[realDex] = mess;
	}
	
	public Message get(int num) {
		if (num < count - hold) {
			return null;
		}
		if (num > count) {
			return null;
		}
		int realDex = index % hold;
		return Queue[realDex];
	}
}
