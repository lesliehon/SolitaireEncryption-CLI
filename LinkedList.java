/* 	
 *	SolitaireEncryption
 *	Leslie Hon
 *	Date: 9 November 2011
*/

class ListNode {
	private Object data;
	private ListNode next;
	ListNode(Object o) { data = o; next = null; }
	ListNode(Object o, ListNode nextNode) { data = o; next = nextNode; }
	Object getData() { return data; }
	ListNode getNext() { return next; }
	void setNext(Object o){data = o;}
	void setNext(ListNode t){next = t;}
} // class ListNode

class EmptyListException extends RuntimeException {
	public EmptyListException () { super ("List is empty"); }
} // class EmptyListException

class LinkedList {
	private ListNode head;
	private ListNode tail;
	private int count =0 ;
	public LinkedList() { head = tail = null; }
	public LinkedList(ListNode h, ListNode t){ head = h; tail = t; tail.setNext(null); recount();}
	public boolean isEmpty() { return head == null; }
	public void addToHead(Object item) {
		if (isEmpty())
		      head = tail = new ListNode(item);
		    else
		      head = new ListNode(item, head);
		count++;

	}
	public void addToTail(Object item) {
		if (isEmpty())
		      	head = tail = new ListNode(item);
		    else{
		      // tail = tail.next = new ListNode(item); //This is old code
		    	tail.setNext( new ListNode(item) ); // newCode
		    	tail = tail.getNext();
		    }
		count++;
		return;
	}
																																			
	public Object removeFromHead() throws EmptyListException {
		    Object item = null;
		    if (isEmpty())
		      throw new EmptyListException();
		    item = head.getData();
		    if (head == tail) // not head.equals(tail)
		      head = tail = null;
		    else
		      head = head.getNext();
		    count--;
		    return item;
	}
	
	public Object removeFromTail() throws EmptyListException {
		Object item = null;
	    if (isEmpty())
	      throw new EmptyListException();
	    item = tail.getData();
	    if (head == tail)
	      head = tail = null;
	    else
	    {
	      ListNode current = head;
	      while (current.getNext() != tail)
	        current = current.getNext();
	      tail = current;
	      current.setNext(null);
	    }
	    count--;
	    return item;

	}
		
	
	public String toString () {
		String s = "[ ";
		ListNode current = head;
		while (current != null) {
			s += current.getData() + " ";
			current = current.getNext();
		}
		return s + "]";
	}
	
	public int count(){		
		return count;
	}
	
	
	public Object remove(int n) {
		Object item = null;
		if (n <= count) { 
			if (n == 1) return removeFromHead();
			if (n == count) return removeFromTail();
			ListNode current = head;
			ListNode previous = null;
			for (int i = 1; i < n; i++) {
				previous = current;
				current = current.getNext();
			}
			item = current.getData();
			previous.setNext(current.getNext());
		}
		count--;
		return item;
	}
	
	public void add(int n, Object item) {
		if (n == 1) { addToHead(item); return;}
		if (n > count) {addToTail(item); return;}
		ListNode current = head;
		for (int i = 1; i < n-1; i++) current = current.getNext();
		current.setNext(new ListNode(item, current.getNext()));
		count++;
	}
	
	public Object get(int n){
		ListNode current = head;
		int c = 1;
		if(n < 1 || n > count) return null;
		while (c < n){
			c++;
			current = current.getNext();
		}
		return current.getData();
	}
	


	/*Assignment Part
	The method under this line is designed for the DSA Assignment SolitaireEncryption*/
	
	//The method to search the ListNode, input the Object and return the position of LinkedList
	//Return -1 when not found
	public int search(Object item){
		if(isEmpty()) return -1; //If the LinkedList is null return -1 (Notfound)
		ListNode current = head;
		for(int i = 1 ; i <= count ; i++ ){
			if( current.getData().equals(item) ) return i;
			else current = current.getNext();
		}
		return -1;
	}
	
	//Move the ListNode after n items , Input the Object p and n , the ListNode contain p will move after n position.
	//It will swap with head when the ListNode is tail
	public void move (Object p, int n){
		for(int c = 0 ; c < n ; c++){
			if(isEmpty()|| search(p) == -1 || n <= 0) return;
			ListNode previous = tail , current = head , next = head.getNext();
			//Locate to previous , current , and next position
			int pos = search(p);
			for(int i = 1 ; i < pos ; i++){
				previous = current;
				current = current.getNext();
				if (current == tail) next = head;
				else next = current.getNext();
			}
			//finish locate	
			if(current== head){
				current.setNext( next.getNext() );
				next.setNext(current);
				head = next;
			}else if(current == tail){
				ListNode ohead = next , otail = current;
				otail.setNext(ohead.getNext());
				ohead.setNext(null);
				head = otail;
				tail = ohead;
				previous.setNext(tail);
				
			}else{
				previous.setNext(current.getNext());
				current.setNext( next.getNext() );
				next.setNext(current);
				if(next == tail) tail=current;
			}
		
		}
		return;
	}
	
	//Remove a LinkedList from Head (head to n-1 items)
	//The method will return in LinkedList
	public LinkedList removeListFromHead(int n) throws EmptyListException {
		if(isEmpty()) throw new EmptyListException();
		if(n <= 1 || n> count) new LinkedList();
		
		ListNode  previous = null , current = head;
		for (int i = 1; i < n; i++) {
			previous = current;
			current = current.getNext();
		}
		LinkedList rmList = new LinkedList(head , previous);
		head = current;
		recount();
		return rmList;
	}
	
	//Remove a LinkedList from Tail (n+1 to tail)
	//The method will return in LinkedList
	public LinkedList removeListFromTail(int n) throws EmptyListException{
		if(isEmpty()) throw new EmptyListException();
		if(n <= 1 || n > count) new LinkedList();
		
		ListNode current = head;
		for (int i = 1; i < n; i++) {
			current = current.getNext();
		}
		LinkedList tmp = new LinkedList( current.getNext() , tail);
		this.tail = current;
		tail.setNext(null);
		recount();
		return tmp;
	}
	
	//Paste the LinkedList (list) in head of the LinkedList
	public void addListToHead(LinkedList list) {
		if(isEmpty()){head = list.head; tail= list.tail; return;}
		list.tail.setNext(this.head);
		head = list.head;
		recount();
		return;
	}
	
	//Paste the LinkedList (list) after the tail of the LinkedList
	public void addListToTail(LinkedList list){
		if(list.isEmpty()) return;
		if(isEmpty()){head = list.head; tail= list.tail; return;}
		tail.setNext(list.head);
		tail = list.tail;
		recount();
		return;
	}
	
	//Renew the count of the LinkedList
	public void recount(){
		//refresh the count of the LinkedList
		if(isEmpty()){ count = 0; return;}
		int nc = 1;
		ListNode current = head;
		while(current.getNext()!=null){
			current= current.getNext();
			nc++;
		}
		count = nc;
	}
} // class LinkedList