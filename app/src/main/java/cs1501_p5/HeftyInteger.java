/**
 * HeftyInteger for CS1501 Project 5
 * @author	Dr. Farnan
 */
package cs1501_p5;

import java.util.Random;

public class HeftyInteger {

	private final byte[] ONE = {(byte) 1};
	private final byte[] ZERO = {(byte) 0};

	private byte[] val;

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	public HeftyInteger(byte[] b) {
		val = b;
	}

	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new HeftyInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.add(other.negate());
	}

	private boolean isZero( ){

		byte[] arr = this.getVal(); 

		boolean valZero = false; 

		for ( int i=0; i< this.length(); i++ ){
			if ( ((int)arr[i]&0xFF) == 0 ){
				valZero = true; 
			} else {
				return false; 
			}
		}
		return valZero; 
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public HeftyInteger multiply(HeftyInteger other) {
		// YOUR CODE HERE (replace the return, too...)

		// if ( isZero(other) || isZero(this) ){
		// 	return new HeftyInteger(ZERO); 
		// }

		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// actually compute the multiply 

		// trying karatsuba

		HeftyInteger x = new HeftyInteger(a);
		HeftyInteger y = new HeftyInteger(b); 

		// return karatsuba(x,y);


		return gradeSchool(x,y); 

	}

	/**
	 * Left Shift method 
	 * @param old
	 * @return new HeftyInteger bit shifted to the left 
	 */
	private HeftyInteger leftShift( HeftyInteger old ){

		byte[] oldArr = old.getVal(); 

		byte[] shiftedArr = new byte[oldArr.length]; // create new byte array same length as the original 

		for( int i=old.length()-1; i>=0; i-- ){ // start at the least significant byte 

			byte curByte = oldArr[i];  

			// System.out.println("CurrByte of the unshifted array is " +  ((int)curByte&0xFF) ); 

			curByte <<= 1; // now zero is in position of least significant bit 
			// must remember MSbit of this byte and 'insert' into LSbit of byte to the left

			// System.out.println("CurrByte after shift is " +  ((int)curByte&0xFF) );

			if ( i == old.length()-1 ){
				// if on LSByte - keep the zero in least significant bit position 
				// do nothing else 

				// System.out.println("this is the least significant byte: " ); 

				shiftedArr[ i ] = curByte; 
				continue; // move onto the next iteration of the loop 
			}

			// if not on least significant byte 
			// if byte to the right had 1 in the most significant bit position 
			if ( ( (oldArr[i+1] >>> 7)&0x01 ) == 0x01 ){
				// if MSbit of the byte to the right is 1
				// System.out.println( "The byte to the right of the current index: " +  ((int)oldArr[i+1]&0xFF) );
				
				curByte |= 0x01; // curByte now contains a 1 in the least significant bit positions

				// System.out.println( "Cur byte gets a 1 in the least significant bit positions: " +  ((int)curByte&0xFF) );

				shiftedArr[i] = curByte; 
				continue; 

			}

			shiftedArr[i] = curByte; 

		}

		HeftyInteger shifted = new HeftyInteger( shiftedArr ); 

		if ( (((oldArr[0]>>>7)&0x01)==0x01) && (((oldArr[0]>>>6)&0x01)==0x00) ){
			// if MSbit is 10 )(reading from left to right), must extend byte array by 1 
			
			shifted.extend( (byte)1 ); // extend is a void method 

			// System.out.println("We extended the byte array by 1"); 

		} else if ( (((oldArr[0]>>>7)&0x01)==0x00) && (((oldArr[0]>>>6)&0x01)==0x01) ){
			// if MSbits are 01, must extend by 0 

			shifted.extend( (byte)0 );

			// System.out.println("We extended the byte array by 0"); 
		}

		// System.out.println("Shifted byte array is: " + shifted.toBigInt() );

		return shifted; 

	}


	/**
	 * Helper method for multiply method
	 * Gradeschool algorithm used
	 * 
	 * @param x multiplicand
	 * @param y multiplier 
	 * @return HeftyInteger product of x and y 
	 */
	private HeftyInteger gradeSchool( HeftyInteger x, HeftyInteger y ){

		boolean negateProduct = false;
		
		if ( x.isNegative() && ! y.isNegative() || !x.isNegative() && y.isNegative() ){

			// System.out.println("Product should be negative"); 
			// product should be negative 
			negateProduct = true; 
		}

		if ( x.isNegative() ){
			x = x.negate(); 

			// System.out.println("x is now: " + x.toBigInt()); 
		}

		if ( y.isNegative() ){
			y = y.negate(); 
			// System.out.println("y is now: " + y.toBigInt()); 
		}

		HeftyInteger result = new HeftyInteger( ZERO ); 

		byte[] multiplicand = x.getVal();

		HeftyInteger temp = new HeftyInteger( multiplicand ); // keep track of curr partial sol 

		byte[] multiplier = y.getVal(); 

		for( int i = multiplier.length-1; i>=0; i-- ){

			byte currByte = multiplier[i]; 

			// System.out.println("First byte of multiplier: " + ((int)currByte&0xFF) );

			for( int count=0; count<8; count++ ){

				if ( (currByte & 1) != 0 ){ // if cur bit is not zero

					// add multiplicand to result 

					// System.out.println( " Curr bit is 1, Temp: " + temp.toBigInt() ); 

					result = result.add(temp); 

					// System.out.println("Curr Result: " + result.toBigInt() ); 

				}

				temp = leftShift( temp ); // left shift method returns a HeftyInteger 

				// System.out.println("Shifted Temp: " + temp.toBigInt() ); 

				currByte>>>=1; 

				// System.out.println("Right Shifted currByte: " + ((int)currByte&0xFF) ); 

			}
		} // end outer for loop 


		if ( negateProduct && !result.isNegative() || !negateProduct && result.isNegative() ){
			// product should be negative, but is positive 
			// OR
			// product should be positive, but is negative

			result = result.negate(); 

		} 

		return result; 

	}


	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public HeftyInteger[] XGCD(HeftyInteger other) {

		HeftyInteger[] result = new HeftyInteger[3]; 

		HeftyInteger c = new HeftyInteger( this.val ); 
		HeftyInteger d = new HeftyInteger( other.getVal() ); 

		HeftyInteger heftyZero = new HeftyInteger(ZERO); 
		HeftyInteger heftyOne = new HeftyInteger(ONE); 
		HeftyInteger heftyNegOne = heftyOne.negate(); 

		if ( c.isZero() && ! d.isZero() ){

			// (0, -d)
			if ( d.isNegative() ){
				result[0] = d.negate(); 
				result[1]= heftyZero; 
				result[2] = heftyNegOne; 

				return result; 

			} else {
			// (0, d)
				result[0] = d; 
				result[1]= heftyZero; 
				result[2] = heftyOne; 

				return result;

			}

		} else if ( ! c.isZero() && d.isZero() ){

			// (-c, 0)
			if ( c.isNegative() ){
				result[0] = c.negate(); 
				result[1]= heftyNegOne; 
				result[2] = heftyZero;

				return result;
				
			} else {
			// (c, 0)
				result[0] = c; 
				result[1]= heftyOne; 
				result[2] = heftyZero;

				return result;
			}
		}

		// neither c nor d is zero 

		boolean cNegative = false;
		boolean dNegative = false;

		if ( c.isNegative() ){
			cNegative = true;
			c = c.negate(); 
		}

		if ( d.isNegative() ){
			dNegative = true; 
			d = d.negate(); 
		}

		if ( c.compareTo(d) > 0 ){

			// System.out.println( c.toBigInt() + " is greater than " + d.toBigInt() ); 

			result = xgcd( c, d );

			if (cNegative){
				result[1] = result[1].negate(); 
			}

			if (dNegative) {
				result[2] = result[2].negate(); 
			}

			// return result; 

		} else if ( c.compareTo(d) < 0){

			// System.out.println( c.toBigInt() + " is less than " + d.toBigInt() ); 

			result = xgcd( d,c ); 

			if (cNegative){
				result[1] = result[1].negate(); 
			}

			if (dNegative) {
				result[2] = result[2].negate(); 
			}

			// System.out.println("Must swap result indices 1 and 2"); 

			HeftyInteger temp = result[1];
			result[1] = result[2];
			result[2] = temp;

			// return result; 

		} else {
			// c.compareTo(d)==0

			if ( cNegative && dNegative || !cNegative && dNegative ){
				// ( c, -d ) OR ( -c, -d ) => (0,-1)

				result[0] = d;
				result[1] = heftyZero;
				result[2] = heftyNegOne; 
			} else if ( !cNegative && !dNegative || cNegative && !dNegative ){
				// ( c,d ) OR ( -c, d ) => (0,1)

				result[0] = d; 
				result[1] = heftyZero;
				result[2] = heftyOne; 
			}

		}

		return result; 

	 }

	 // private helper method for XGCD 
	 // arguments are positive HeftyIntegers
	 // a > b 
	 private HeftyInteger[] xgcd( HeftyInteger a, HeftyInteger b ){

		HeftyInteger[] result = new HeftyInteger[3]; 

		HeftyInteger q, r1, r2, r, s1, s2, s, t1, t2, t; 

		// initialize all HeftyIntegers

		r1 = a;
		// System.out.println("R1 is: " + a.toBigInt() ); 

		r2 = b; 
		// System.out.println("R2 is: " + b.toBigInt() ); 

		
		q = a.divide(b); 
		// System.out.println("q is: " + q.toBigInt() ); 


		r = a.mod(b); 
		// System.out.println("r is: " + r.toBigInt() ); 

		s1 = new HeftyInteger( ONE );
		s2 = new HeftyInteger( ZERO );

		s = s1.subtract( s2.multiply(q) );
		// System.out.println("s is: " + s.toBigInt() ); 

		t1 = new HeftyInteger( ZERO );
		t2 = new HeftyInteger( ONE ); 

		t = t1.subtract( t2.multiply(q) ); 
		// System.out.println("t is: " + t.toBigInt() ); 

		while ( ! r.isZero() ){ 

			// System.out.println("\nr2 is not yet zero\n" ); 

			r1 = r2; 
			// System.out.println("r1 is: " + r1.toBigInt() ); 

			r2 = r; 
			// System.out.println("r2 is: " + r2.toBigInt() ); 

			// System.out.println("Divide " + r1.toBigInt() + " by " + r2.toBigInt() );

			q = r1.divide(r2);

			// System.out.println("q is: " + q.toBigInt() ); 

			r = r1.mod(r2); 
			// System.out.println("r is: " + r.toBigInt() ); 

			s1 = s2;
			// System.out.println("s1 is: " + s1.toBigInt() ); 

			s2 = s; 
			// System.out.println("s2 is: " + s2.toBigInt() ); 

			s = s1.subtract( s2.multiply(q) );
			// System.out.println("s is: " + s.toBigInt() ); 

			t1 = t2;
			// System.out.println("t1 is: " + t1.toBigInt() ); 

			t2 = t; 
			// System.out.println("t2 is: " + t2.toBigInt() ); 


			t = t1.subtract( t2.multiply(q) ); 
			// System.out.println("t is: " + t.toBigInt() ); 

		}

		assert r.isZero(); 
		// System.out.println("r is: (should be 0) " + r.toBigInt() ); 

		result[0] = r2; 
		result[1] = s2;
		result[2] = t2; 

		// System.out.println("Check: " + s1.multiply(a).toBigInt() + " plus " + t1.multiply(b).toBigInt() + " should be " + r1.toBigInt() );

		// System.out.println();

		return result;

	}

	/**
	 * Divide method (floor division) for HeftyInteger 
	 * 
	 * @param other divisor  
	 * @return the quotient of floor( this/other )
	 */
	public HeftyInteger divide( HeftyInteger other ){

		// System.out.println("\nCOMPARE TO METHOD\nthis: " + this.toBigInt() + " other: " + other.toBigInt() );

		if ( this.compareTo(other) == 0 ){
			// System.out.println("this is equal to other");
			return new HeftyInteger(ONE); 
		} else if ( this.compareTo(other) < 0 ){
			// System.out.println("this is less than other");
			return new HeftyInteger(ZERO); 
		} else {
			// System.out.println("this is greater than other");
			return divide(this, other); 
		}

	}
	
	/**
	 * Helper method for divide 
	 * 
	 * @param a dividend
	 * @param b divisor 
	 * @return quotient of a/b (floor division)
	 */
	private HeftyInteger divide( HeftyInteger a, HeftyInteger b){

		// System.out.println("\n(Entered divide method (a,b))\n");

		HeftyInteger dividend = a; 
		// System.out.println("Dividend is: " + dividend.toBigInt() ); 

		HeftyInteger divisor = b; 
		// System.out.println("Divisor is: " + divisor.toBigInt() ); 

		int n = dividend.countBits( ); // num bits in dividend

		// System.out.println("NumBits in Dividend is: " + n ); 

		HeftyInteger q = new HeftyInteger(ZERO); //quotient
		// System.out.println("Q is: " + q.toBigInt() ); 

		HeftyInteger remainder = dividend; // dividend originally
		// System.out.println("Remainder is: " + remainder.toBigInt() ); 
		
		for( int i=0; i<n; i++ ){
			divisor = leftShift( divisor );
		}

		// System.out.println("Shifted divisor is initially : " + divisor.toBigInt() ); 

		for( int i=0; i<n; i++ ){
			divisor = divisor.rightShift(); // right shift the divisor by 1 
			// System.out.println("Shifted right by 1 divisor is: " + divisor.toBigInt() ); 

			if ( divisor.compareTo( remainder ) > 0 ){

				// System.out.println("Divisor is greater than remainder"); 

				q = leftShift(q); 

				// System.out.println("Quotient is: " + q.toBigInt() ); 

			} else {

				remainder = remainder.subtract( divisor );
				// System.out.println("Divisor is <= Remainder, new remainder is: " + remainder.toBigInt() ); 
				q = leftShift( q );
				HeftyInteger one = new HeftyInteger(ONE); 
				q = q.add(one); 
				// System.out.println("q is: " + q.toBigInt() ); 
			}
		} // end for loop
		return q; 
	}


	/**
	 * 
	 * Count number of bits in num starting with the most significant 1 
	 * 
	 * @param num a positive HeftyInteger
	 * @return number of significant bits in num 
	 */
	public int countBits( ){

		// System.out.println("entered count bits method: this is: " + this.toBigInt() ); 

		byte[] arr = this.getVal(); 

		int numBytes = arr.length; 
		// System.out.println("num bytes is: " + numBytes); 

		int indexMSB = 0; 

		byte MSB = arr[indexMSB]; 

		int numBits = 0; 

		int numShifts = 0; 

		while ( ((int)MSB ) == 0x00 ){
			// System.out.println("MSB is 0 and MSB index is: " + indexMSB);
			indexMSB++;
			MSB = arr[ indexMSB ]; 
			numBytes -= 1; 
		}

		while ( ( (int)MSB>>>7 & 0x01) != 0x01 ){

			// System.out.println("entered loop");

			// TODO: check this!
			MSB <<= 1; // left shift (?) 
			numShifts++; 
		}

		// System.out.println("Num significant bits in the first significant byte is: " + numShifts+1 );

		numBits = (8-numShifts) + 8*(numBytes-1); 

		// System.out.println("numbits is: " + numBits); 

		return numBits; 
	}

	/**
	 * Unsigned right shift this HeftyInteger by 1 bit 
	 * 
	 * @return 
	 */
	private HeftyInteger rightShift( ){

		// System.out.println("Unshifted is: " + this.toBigInt() ); 

		byte[] old = this.getVal();

		byte[] shifted = new byte[ this.length() ]; // same length as original

		// System.out.println("MSB is " + (int)(old[0]&0xFF) ); 

		shifted[0] = (byte) ((old[0]&0xFF)>>>1);

		// System.out.println("Shifted MSB is " + (int)(shifted[0]&0xFF) ); 

		for( int i=1; i<this.length(); i++ ){

			byte prev = old[i-1]; 

			// System.out.println("Previous byte is " + (int)(prev&0xFF)); 
			// System.out.println("Index " + i + " cur byte is " + (int)(old[i]&0xFF) ); 
			// System.out.println("Previous byte shifted " + (int)(prevShifted&0xFF)); 

			if ( (int)(prev&0x01)==(0x01) ){
				// least significant bit of prev is 1
				// thus, most significant bit of cur index byte should become 1 

				// System.out.println("LSbit of previous was 1"); 

				shifted[i] = (byte) ( ((old[i]&0xFF)>>>1)  | 0x80 );

			} else {

				// System.out.println("LSbit of previous was 0"); 

				shifted[i] = (byte) ((old[i]&0xFF)>>>1) ; 

				// shifted[i] = (byte) (  (byte)(old[i]>>>1) & (byte)~(0x80) ); 

			}

			// System.out.println("Shifted cur byte is: " + (int)(shifted[i]&0xFF));
			// System.out.println(); 
		}
		return new HeftyInteger( shifted ); 

	}

	/**
	 * a mod b 
	 * 
	 * @return HeftyInteger result of a%b 
	 */
	public HeftyInteger mod( HeftyInteger other ){

		if ( this.compareTo(other)==0 ){
			return new HeftyInteger(ZERO);
		} else if ( this.compareTo(other)<0){
			return this; 
		} else {
			return mod( this, other ); 
		}

	}

	/**
	 * Helper method for mod 
	 * a and b are positive HeftyIntegers
	 * 
	 * @param a 
	 * @param b
	 * @return result of a % b 
	 */
	private HeftyInteger mod( HeftyInteger a, HeftyInteger b){

		// System.out.println("\n" + a.toBigInt() + " MOD " + b.toBigInt() + "\n" ); 

		HeftyInteger q = divide(a,b); 

		// System.out.println("Quotient is: " + q.toBigInt() ); 

		HeftyInteger remainder = a.subtract( q.multiply(b) ); 

		// System.out.println("Remainder is: " + remainder.toBigInt() + "\n" ); 

		return remainder; 

	}

	/**
	 * Compare to method 
	 * this.compareTo(other)
	 * 
	 * @param other
	 * @return 0 if this==other, -1 if this<other, +1 if this>other
	 */
	private int compareTo( HeftyInteger other ){

		// if this > other return +1
		// if this < other return -1
		// if this==other return 0 

		HeftyInteger result = this.subtract(other);

		if ( result.isNegative() ){

			// this < other 
			return -1; 

		} else if ( result.isZero() ){
			// this == other
			return 0; 
		} else {
			assert ! result.isNegative(); 

			return 1; 
		}		
	} // end compareTo 


} //end HeftyInteger 
