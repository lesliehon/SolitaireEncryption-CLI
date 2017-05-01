/* 	
 *	SolitaireEncryption
 *	Leslie Hon
 *	Date: 9 November 2011
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SolitaireEncryption {
	private static LinkedList deck;
	private static char [] message;
	private static LinkedList keystream;
	public static void main(String args[]){
		deck = new LinkedList(); //Create a new Linked List for the program
		//check the arg.
		if(args.length !=3){
			System.out.println("Error args.");
			System.exit(0);
		}
		else{
			try{
				BufferedReader bf = new BufferedReader( new FileReader(args[1]));
				String [] line = bf.readLine().split(" ");
				for (int i = 0 ; i < line.length ; i++){
					deck.addToTail( Integer.parseInt(line[i]));
				}
				bf.close();
			}catch(IOException iox){
				System.out.println(iox);
                System.exit(0);
			}
			
			message = args[2].toUpperCase().trim().replaceAll("[^A-Z]","").toCharArray();
			
			if(args[0].equals("keygen")){
				keygen(true);
			}else{
				keygen(false);
				if( args[0].equals("en")) encrypt();
				else if( args[0].equals("de"))	decrypt();
                else { System.out.println("Error args"); System.exit(0); }
			}
		}
	}
	
	public static void keygen(boolean printResult){
		keystream = new LinkedList();
			for(int i = 1 ; i <= message.length ; i++){
				boolean finish = false;
				while(!finish){
					finish = true;
					
					//S1
					deck.move( 27 ,1);
					if(printResult) System.out.println("S1: "+deck);
					
					//S2
					deck.move( 28 ,2);
					if(printResult) System.out.println("S2: "+deck);
					
					//S3 Do Triple cut for the Deck
					//Handle special case
					int rJoker = Math.max(deck.search(27), deck.search(28));
					int fJoker = Math.min(deck.search(27), deck.search(28));
					if( (deck.search(27)== 28 && deck.search(28)== 1) || (deck.search(28)== 28 && deck.search(27)== 1)){
						//No need to do anything when the 27 or 28 are in the head and tail!!!
					}else if(deck.search(27)== 28 || deck.search(28)== 28){
						/*if the position of 27 is in the last
						than cut the head before 28 ( pos 27 -1) and paste in the tail */
						LinkedList head = deck.removeListFromHead(fJoker);
						deck.addListToTail(head);
					}else if(deck.search(27)== 1 || deck.search(28)== 1){
						/*if the position of 28 is in the first
						than cut the after after 28 and paste in the head */
						LinkedList tail = deck.removeListFromTail(rJoker);
						deck.addListToHead(tail);
					}else{
						//Normal case
						LinkedList head , tail;
						tail = deck.removeListFromTail(rJoker);
						head = deck.removeListFromHead(fJoker);
						deck.addListToHead(tail);
						deck.addListToTail(head);
					}
					if(printResult) System.out.println("S3: "+deck); //print S3 result
					
					
					//S4 Do the countcut
					LinkedList headcount;
					int lastnumber;
					if( !(deck.search(27)==28 || deck.search(28)==28) ){
						headcount = deck.removeListFromHead( (Integer) deck.get(28) +1 );
						lastnumber = (Integer) deck.removeFromTail();
						deck.addListToTail(headcount);
						deck.addToTail(lastnumber);
					}
					if(printResult) System.out.println("S4: "+deck);
					
					//Get the key stream value
					int key;
					if( deck.search(27) == 1 || deck.search(28) == 1) 
						key = (Integer) deck.get(28);
					else 
						key = (Integer) deck.get( ( (Integer) deck.get(1) ) + 1 );
					
					//Check the keystream value
					if(key == 27 || key == 28){
						if(printResult) System.out.println("Joker: Key skipped");
						finish = false;
					}
					else {
						if(printResult) System.out.println("Key "+ i + ": "+ key);
						keystream.addToTail(key);
					}
					
				}
			}
			if(printResult) System.out.println("Keystream values: "+ keystream);
	}
	
	public static void encrypt(){
		String m = "Encrypted message: ";
		for(int i = 0 ; i < message.length ; i++){
			int eMessage = message[i] + (Integer) keystream.get(i+1);		
			if((char) eMessage > 'Z') eMessage -= 26;
			m+= (char) eMessage;
			System.out.println(message[i] +"\t"+ ((int)(message[i] -64)) +"\t"+ (keystream.get(i+1) +"\t"+ (eMessage-64) +"\t"+ ((char) eMessage ) ));
		}
		System.out.println(m);
	}
	
	public static void decrypt(){
		String m = "Decrypted message: ";
		for(int i = 0 ; i < message.length ; i++){
			int dMessage = message[i] - (Integer) keystream.get(i+1);
			if( (char) dMessage < 'A' ) dMessage +=26;
			m+= (char) dMessage;
			System.out.println(message[i] +"\t"+ ((int)(message[i] -64)) +"\t"+ (keystream.get(i+1) +"\t"+ (dMessage-64) +"\t"+ (char) dMessage ));
		}
		System.out.println(m);
	}	
}
