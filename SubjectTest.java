package sample;

class SubjectTest extends Subject{

	String testString;
	
	public void setTestString(String s){
		testString = s;
		// notify observer(s) of change
		notifyAllSubscribers();
	}

	public String toString(){
		return testString;
	}
}