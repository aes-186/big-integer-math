/**
 * A driver for CS1501 Project 5
 * @author	Dr. Farnan
 */
package cs1501_p5;

import java.math.BigInteger;


public class App {
	public static void main(String[] args) {
		byte[] TEN = {(byte) 10};
		HeftyInteger t = new HeftyInteger(TEN);

		BigInteger a = new BigInteger("82903580239809532840923804932");
		BigInteger b = new BigInteger("72389479823798532904075823792938888880000039");

		HeftyInteger a1 = new HeftyInteger( a.toByteArray());
		HeftyInteger b1 = new HeftyInteger(b.toByteArray());

		HeftyInteger[] result = a1.XGCD(b1); 

		System.out.println("GCD of: " + App.toBigInt(a1) + " and " + App.toBigInt(b1) );
		System.out.println("\nGCD: " + App.toBigInt( result[0] ) ); 
		System.out.println("x: " + App.toBigInt( result[1] ) ); 
		System.out.println("y: " + App.toBigInt( result[2] ) ); 

		HeftyInteger product1 = a1.multiply(result[1]);
		HeftyInteger product2 = b1.multiply(result[2]);

		HeftyInteger sum = product1.add(product2); 

		System.out.println("\nCHECK: \nSum of ax+by = " + App.toBigInt(sum));

	}

	public static BigInteger toBigInt(HeftyInteger x){

		return new BigInteger( x.getVal() ); 

	}
}
