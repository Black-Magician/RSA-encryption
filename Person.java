
/**
This is the Person class used for RSA encryption
it encrypts using another person's public exponent and mod
and it will also decrypt with this persons private key 
@author Joe Cardona

*/
public class Person {
	
	private long pubExp;
	private long pubMod;
	private long privateKeyD;
	private long n;
	
	
	/**
	 * Person constructor that initializes p, q, m, e, and n for encryption and decryption
	 * @author Joe Cardona
	 */
	public Person()
	{
		java.util.Random Rand = new java.util.Random();
		long p = RSA.randPrime(1, 10000, Rand);
		long q = RSA.randPrime(1, 10000, Rand);
		
		pubMod = p*q;
		n = (p-1)*(q-1);
		pubExp = RSA.relPrime(n, Rand, 10000);
		privateKeyD = RSA.inverse(pubExp, n); 
	}
	
	/**
	 * @author Joe Cardona
	 * @param cipher
	 * @return
	 */
	public String decrypt(long[] cipher)
	{
			
		long[] val = new long[cipher.length];
		
		for(int i = 0;i<cipher.length;i++)
		{
			val[i] = RSA.modPower(cipher[i], this.privateKeyD, this.pubMod);
		}
		
		return RSA.longTo2Chars(val);
	}
		
	/**
	 * Method encrypts a String message
	 * @author Joe Cardona
	 * @param msg
	 * @param she
	 * @return encrypted msg in long array format
	 */
	public long[] encryptTo(String msg, Person she)
	{
		long e = she.getE();
		long m = she.getM();
		long[] retu = RSA.toLong(msg);
		long[] encPlain = new long[retu.length];
		
		for(int i = 0; i < retu.length;i++)
		{
			encPlain[i] = RSA.modPower(retu[i], e, m);
		}
			
		return encPlain;
	}
	
	public long getE()
	{
		return pubExp;
	}
	
	public long getM()
	{
		return pubMod;
	}
}
