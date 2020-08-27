import java.io.*;
import java.util.*;
public class ATMSimulator{					//Class declaration
	public static void main(String args[]){
		int ch;
		UI obj = new UI();			//Creating object of subclass
		ch = obj.firstscr();		//Calling function of subclass	
		if(ch == 0){				
			obj.secondscr();		//Calling function of subclass
		}
	}
}
class UI extends ATMSimulator{
	public int cc_no;				//Stores user feeded credit card number
	public int pin;					//Stores user feeded pin number
	public int ch,ch1;
	public int k, l=0, m=0;
	public int[] database;			//Stores the database of customers in an array
	public int ctr = 0;
	Scanner sc = new Scanner(System.in);		//Scanner object to read data from command prompt
	public int firstscr(){
		System.out.println("----------------------------------------------------------------");
		System.out.println("                    WELCOME TO INDIAN BANK                      ");
		try{
			ctr = 0;
			File f = new File("C:\\Users\\Harsh Salkar\\Desktop\\SIM\\Database.txt");			//File object to open the database file
			Scanner s = new Scanner (f);														//Scanner object that reads data from the file
			while(s.hasNext()){				//Checks if cursor has reached end of file
				ctr++;						//Counts the length of database array
				s.nextInt();				
			}
			int[] db = new int[ctr];
			Scanner s1 = new Scanner(f);
			for(int i = 0; i<ctr; i++){
				db[i] = s1.nextInt();		//Reads data from database and stores in a local array
			}
			database = db;					//Copies data from local array to global array
		}
		catch(Exception e){
			System.out.println(e);
		}
		System.out.print("Enter Credit Card Number: ");
		cc_no = sc.nextInt();
		for(int i =0; i<ctr-1; i=i+3){				//Iterates every 3rd element as credit card number is stored at every 3rd index
			if(database[i]==cc_no){					//Condition to check if user has entered valid credit card number
				k=i;								//Stores position of credit card number, to work with only the corressponding data
				System.out.println("Valid Credit Card Number!");
				l=1;								//Flag set to 1 for valid credit card number feeded
				break;
			}
		}
		if(l == 1){
			System.out.print("Enter Pin Number: ");
			pin = sc.nextInt();
			if(pin == database[k+1]){				//Condition to check if user has entered valid pin number
				System.out.println("Access Granted :)");
			}
			else{
				System.out.println("Invalid Pin Entered!\nEnter 0 to RETRY and 1 to EXIT.");
				ch = sc.nextInt();
				switch(ch){
					case 1:	System.out.println("TRANSACTION CANCELLED!");
							m=1;
							break;
					default: firstscr();
							break;
				}
			}
		}
		else{
			System.out.println("Invalid Credit Card Number!\nEnter 0 to RETRY and 1 to EXIT.");
			ch1 = sc.nextInt();
			switch(ch1){
				case 1: System.out.println("TRANSACTION CANCELLED!");
						m=1;
						break;
				default:firstscr();
						break;   				
			}
		}
		return m;
	}
	public void secondscr(){
		int ch2, amt, pin1, temp, temp1;
		String filename;
		System.out.println("----------------------------------------------------------------");
		System.out.println("                     VIRAR SAHAKARI BANK                        ");
		System.out.println("1.Balance Enquiry                          2.Cash Withdrawal    ");
		System.out.println("3.Cash Deposit                             4.Change Pin         \n");
		System.out.print("Enter your choice: ");
		ch2 = sc.nextInt();
		switch(ch2){
			case 1:	System.out.println("Your balance is "+database[k+2]);
        			break;
			case 2: System.out.print("Enter amount to be withdrawn: ");
					amt = sc.nextInt();
					temp = database[k+2];				//Temporary variable to store current balance of customer
					temp1 = database[ctr-1];			//Temporary variable to store current balance cash in ATM
					database[k+2] -= amt;				//Updates balance of customer after withdrawal
					database[ctr-1] -= amt;				//Updates balance of customer after withdrawal
					if(amt >15000){
						System.out.println("Transaction amount exceeds limit per transaction(150000).");
						database[k+2] = temp;			//Restores previous balance due to unsuccessful transaction
						database[ctr-1] = temp1;		//Restores previous balance due to unsuccessful transaction
					}
					else if(database[k+2]<2000){
						System.out.println("INSUFFICIENT Balance to proceed with Transaction!\nMinimum balance post transaction should be 2000.");
						database[k+2] = temp;			//Restores previous balance due to unsuccessful transaction
						database[ctr-1] = temp1;		//Restores previous balance due to unsuccessful transaction
					}
					else if(database[ctr-1] < 0){
						System.out.println("ATM is out of cash.");
						database[ctr-1] = temp1;		//Restores previous balance due to unsuccessful transaction
					}
					else{
						try{
							filename = Integer.toString(database[k]);
							FileWriter fw=new FileWriter(filename+".txt");	//Creates receipt file    
							fw.write("\n\t     VIRAR SAHAKARI BANK\n\n");
							fw.write("Credit Card Number:"+database[k]+" \n");
							fw.write("Cash Withdrawn:"+amt+" \n");
							fw.write("Updated Balance:"+database[k+2]+" \n\n");
							fw.write("TRANSACTION SUCCESSFUL! HOPE TO SEE YOU AGAIN :) ");
							fw.close();
						}
						catch(Exception e){
							System.out.println(e);
						}
					}
					break;
			case 3: System.out.print("Enter amount to be deposited: ");
        			amt = sc.nextInt();
        			database[k+2] += amt;		//Updates balance of customer after deposit
					database[ctr-1] += amt;		//Updates balance of atm after deposit
					try{
						filename = Integer.toString(database[k]);
						FileWriter fw=new FileWriter(filename+".txt");   //Creates receipt file   	
						fw.write("\n\t     VIRAR SAHAKARI BANK\n\n");
						fw.write("Credit Card Number:"+database[k]+" \n");
						fw.write("Cash Deposited:"+amt+" \n");
						fw.write("Updated Balance:"+database[k+2]+" \n\n");
						fw.write("TRANSACTION SUCCESSFUL! HOPE TO SEE YOU AGAIN :) ");
						fw.close();
					}
					catch(Exception e){
						System.out.println(e);
					}
        			break;
			case 4: System.out.print("Enter the new pin number: ");
        			pin1 = sc.nextInt();
        			database[k+1] = pin1;			//Updates the pin number of the customer
        			System.out.println("PIN Number has been successfully updated !");
        			break;
			default:System.out.println("Enter 0 to RETRY or 1 to EXIT.");
        			int ch3 = sc.nextInt();
        			if(ch3 == 0){
						secondscr();
					}
        			break;
		}
		try{
			FileWriter fw=new FileWriter("C:\\Users\\Harsh Salkar\\Desktop\\SIM\\Database.txt");     //Updates the database
			for(int i=0;i<database.length; i=i+3){
				if(i==database.length -1)
					fw.write(database[i]+" ");
				else
					fw.write(database[i]+" "+database[i+1]+" "+database[i+2]+"\n");
			}
			fw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}