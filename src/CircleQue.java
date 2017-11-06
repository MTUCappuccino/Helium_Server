
public class CircleQue {
	
	CircleQue(int numRetention) {
		hold = numRetention;
		Queue = new Message[hold];
	}

	private Message[] Queue;
	private int minDex = 0;
	public int count = 0;
	private int hold;
	
	public void add(Message mess) {
		if (count > hold) {
			minDex += 1;
		}
		int realDex = count % hold;
		count += 1;
		Queue[realDex] = mess;
	}
	
	public Message get(int num) {
		if (num < minDex) {
			return null;
		}
		if (num > count) {
			return null;
		}
		int realDex = count % hold;
		return Queue[realDex];
	}
	
	public void set(int id, Message mess) {
		if (id < minDex) {
			return;
		}
		if (id > count) {
			return;
		}
		int realDex = count % hold;
		Queue[realDex] = mess;
	}
	
	/**
	 * messDump
	 * checks if a ghost last seen message is valid
	 * @param lastKnown
	 * @return
	 */
	public String messDump(int lastKnown) {
		return (lastKnown < minDex ? minDex + "," : lastKnown + ",") + count;
		
	}
}
