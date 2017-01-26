import java.util.Random;
import java.util.ArrayList;


/**
 * Class that does all the major work, in junction with the Person class
 * 
 * @author Joe Cardona, Samantha Brech, Spencer Escalante
 *
 */
public class RSA
{
	//Driver for testing the project
	public static void main(String[] args)
	{
		Person Mike = new Person();
	    Person Dave = new Person();

		Person Alice = new Person();
		Person Bob = new Person();

		String msg = new String ("Bob, let's have lunch."); 	// message to be sent to Bob
		long []  cipher;
		cipher =  Alice.encryptTo(msg, Bob);			// encrypted, with Bob's public key

		System.out.println ("Message is: " + msg);
		System.out.println ("Alice sends:");
		show (cipher);

		System.out.println ("\nBob decodes and reads: " + Bob.decrypt (cipher));	// decrypted,
									// with Bob's private key.
		System.out.println ();

		msg = new String ("No thanks, I'm busy");
		cipher = Bob.encryptTo (msg, Alice);
		
		System.out.println ("Message is: " + msg);
		System.out.println ("Bob sends:");
		show (cipher);

		System.out.println ("\nAlice decodes and reads: " + Alice.decrypt (cipher));
		System.out.println ("\n\n\n");
		
		
		
	    String message = "Hey Mike are you going out this weekend?";
	    long [] encipheredText =  Dave.encryptTo(message, Mike);
	
	    System.out.println("Dave sends: " + message);
	    System.out.println("What is actually being sent to Mike is:");
	    show(encipheredText);
	    System.out.println("\nMike reads: " + Mike.decrypt(encipheredText));
	    
	    String reply = "Yeah totally it's going to be a fun weekend!";
	    long [] encipheredReply = Mike.encryptTo(reply, Dave);
	    System.out.println("\nMike sends is: " + reply);
	    System.out.println("What Dave will recieve is:");
	    show(encipheredReply);
	    System.out.println("\nDave reads: " + Dave.decrypt(encipheredReply));
	}
	
	/**
	 * Find the multiplicative inverse of a long e (mod m)
	 * @author 		Sam Brech
	 * @param e		the encryption exponent 
	 * @param m		the mod
	 * @return		the multiplicative inverse
	 */
	public static long inverse(long e, long m)
	{		
		ArrayList<Long> r = new ArrayList<Long>();		// remainder column
		ArrayList<Long> q = new ArrayList<Long>();		// quotient column
		ArrayList<Long> u = new ArrayList<Long>();		// u column
		
		// initializing ArrayLists to represent table
		r.add(m);
		r.add(e);
		q.add(null);
		q.add(null);
		u.add((long) 0);
		u.add((long) 1);

		// keep track of current position in lists
		// since all lists are the same size, only one variable is needed
		int cur = r.size();
	

		// loop through while last value in r > 1
		while (r.get(cur-1) > 1)
		{
			// add next quotient to quotient column
			q.add(r.get(cur - 2) / r.get(cur - 1));

			// add next remainder to remainder column
			r.add(r.get(cur - 2) % r.get(cur - 1));

			// add next u-value to u column
			u.add(u.get(cur - 2) - (q.get(cur) * u.get(cur - 1)));

			// increment cur since list has had an addition
			cur++;
		}
		
		long result = u.get(cur - 1);
		
		// assure result is lowest positive number
		if (result < 0)
		{
			return m + u.get(cur - 1);
		}
		else
		{
			return u.get(cur - 1);
		}
	}
	

	/**
	 * Raise b to the power of p (mod m)
	 * @author		Sam Brech
	 * @param b		the base number
	 * @param p		the exponent to raise the base
	 * @param m		the mod
	 * @return		the result of the power (mod m)
	 */
	public static long modPower(long b, long p, long m)
	{
		long result = 1;
		
		// special case
		if (p % (m - 1) == 0)
		{
			return 1;
		}
		// find all of the powers of 2 that go into the power given
		while (p > 0)
		{
			if (p % 2 == 1)
			{
				result = (result * b) % m;
			}
			
			b = (b * b) % m;
			p /= 2;
		}
		// obtain result
		long quotient = result / m;
		result -= (m * quotient);
		
		return result;
		
	}
	
	/**
	 * takes the decrypted longs and converts them back into a string representing the initial plaintext
	 * 
	 * This method is different than the original api where it takes the entire array of longs and returns the
	 * plaintext string instead of the output of two characters in string form
	 * 
	 * @author Joe Cardona
	 * @param x array of longs that represent the decrypted characters
	 * still in long form 
	 * @return
	 */
	public static String longTo2Chars(long[] x)
	{
		String value = "";

		for(int i = 0; i < x.length; i++)
		{
			value += (char)Math.floorDiv(x[i], 256);
			value += (char)Math.floorMod(x[i], 256);
		}

		return value;
	}
	
	/**
	 * Takes in a String message and turns it into an array of longs blocking two characters at a time
	 * so the blocked letters would be at indexes 0,1 then 2,3 and so on to length-2, length-1
	 * 
	 * This method is different than the original api in that it takes in the entire message instead of just two characters
	 * in the form of a string
	 * 
	 * @author Joe Cardona
	 * @param msg
	 * @return array of blocked longs representing the message inputed
	 */
	public static long[] toLong(String msg)
	{
		if((msg.length() % 2) != 0)
		{
			msg += " ";
		}

		long[] val = new long[msg.length() / 2];

		for(int i = 0; i < val.length; i++)
		{
			val[i] = (long) msg.charAt(i + i) * 256;
			val[i] += (long) msg.charAt((i + i) + 1);
		}

		return val;
	}
	
	/**
	 * returns a random prime number between the boundaries of m and n
	 * @author Joe Cardona
	 * @param m
	 * @param n
	 * @param rand
	 * @return random prime number in the form of a long
	 */
	public static long randPrime(int m, int n, Random rand)
	{
		int rNum = (rand.nextInt(n - m) + m);

		while(!(Prime(rNum)))
		{
			rNum = (rand.nextInt(n - m) + m);
		}

		return (long) rNum;
		
	}
	//returns true or false if the integer given is prime or not
	//used in randPrime
	private static boolean Prime(int num)
	{
		for(int i = 2; i <= Math.sqrt(num); i++)
		{
			if((num % i) == 0)
			{
				return false;
			}
		}

		return true;
	}
	
	/**
	 * @param n 
	 * @param rand
	 * @param limit
	 * @return number relatively prime to n
	 */
	public static long relPrime(long n, Random rand, int limit)
	{
		long temp = rand.nextInt(limit);

		while(gcd(n, temp) != 1)
		{
			temp = rand.nextInt(limit);
		}

		return temp;
	}
	//for use in relPrime
	private static long gcd(long a, long b)
	{
		long t;

		while(b != 0)
		{
			t = a;
			a = b;
			b = t % b;
		}

		return a;
	}
	
	/**
	 * Display an array of longs on stdout
	 * @author Spencer Escalante
	 * @param cipher
	 */
	public static void show(long[] cipher)
	{
		String longs = "[";
		for(int i = 0; i<cipher.length;i++)
		{
			if(i == cipher.length-1)
			{
				longs = longs + cipher[i];		
			}
			else
			{
				longs = longs + cipher[i] + ", ";
			}
		}
		longs = longs + "]";
		System.out.print(longs);
		System.out.println();
	}
	
	

}
