package sample;

class ObserverTest extends Observer{
	public static void main(String[] args){
		SubjectTest s = new SubjectTest();
		ObserverTest o = new ObserverTest();	
		
		s.subscribe(o);
		o.subject = s;

		// cause change in subjectTest, causing notifyAllSubscribers()
		s.setTestString("I am not a cat");
	}
	
	public void update(){
		//print changes automatically
		System.out.println(subject.toString() + "							Test Passed");
	}
}