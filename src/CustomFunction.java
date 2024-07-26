package us.q3q.fido2 ;



public class CustomFunction  
{
	// true: There is no case of identical increment.
	public static boolean IsCorrectDelta(byte[] ps1Input, short s2Offset, short s2Len)
	{
		final byte s1Delta;
		
		s1Delta = (byte)(ps1Input[s2Offset]-ps1Input[(short)(s2Offset+1)]);
		for ( short i=(short)(s2Offset+1); i<(short)(s2Offset+s2Len-1); i++ )
		{
			if ( (byte)(ps1Input[i]-ps1Input[(short)(i+1)]) != s1Delta )
			{
				return true;
			}
		}	
		return false;
	}
	
	// true: There is no mirrored situation.
	public static boolean IsNonMirrored(byte[] ps1Input, short s2Offset, short s2Len)
	{
		short s2Head, s2Tail;
		
		s2Head = s2Offset;
		s2Tail = (short)(s2Offset+s2Len-1);
		while ( s2Tail > s2Head )
		{
			if (ps1Input[s2Head] != ps1Input[s2Tail])
			{
				return true;
			}
			s2Head++;
			s2Tail--;
		}
		return false;
	}
	
	// true: There is no occurrence of consecutive characters appearing in sequence.
	public static boolean IsNonContinuousChar(byte[] ps1Input, short s2Offset, short s2Len, short s2MaxTimes)
	{
		byte s1CurChar;
		short s2CurTimes;
		
		s1CurChar = ps1Input[s2Offset];
		s2CurTimes = (short)1;
		for ( short i=(short)(s2Offset+1); i<(short)(s2Offset+s2Len); i++ )
		{
			if ( ps1Input[i] == s1CurChar )
			{
				s2CurTimes++;
				if ( s2CurTimes > s2MaxTimes )
				{
					return false;
				}
			}
			else
			{
				s1CurChar = ps1Input[i];
				s2CurTimes = (short)1;
			}
		}
		
		return true;
	}
	
	public static final byte UPPER_LETTER			=	(byte)0x01;
	public static final byte LOWER_LETTER			=	(byte)0x02;
	public static final byte DIGIT_CHAR				=	(byte)0x04;
	public static final byte OTHER_CHAR				=	(byte)0x08;
	public static final byte INVALID_CHAR			=	(byte)0x80;
	
	// Check if the data meets the specified character type s1Type 
	// and the number of character categories is greater than or equal to s2TypeNum
	public static boolean IsCorrectFormat(byte[] ps1Input, short s2Offset, short s2Len, byte s1Type, short s2TypeNum)
	{
		byte s1CurType;
		byte s1TempType;
		short s2CurTypeNum;
		
		s1CurType = (byte)0x00;
		s2CurTypeNum = (short)0;
		for ( short i=s2Offset; i<(short)(s2Offset+s2Len); i++ )
		{
			s1TempType = GetFormatType(ps1Input[i]);
			if ( INVALID_CHAR == s1TempType )
			{
				return false;
			}
			if ( (byte)0x00 == (byte)(s1Type&s1TempType) )
			{
				return false;
			}
			if ( (byte)0x00 == (byte)(s1CurType&s1TempType) )
			{
				s1CurType |= s1TempType;
				s2CurTypeNum++;
			}
		}
		if ( s2CurTypeNum >= s2TypeNum )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static byte GetFormatType(byte s1Char)
	{
		if ( s1Char >= (byte)0x41 && s1Char <= (byte)0x5A )
		{
			return UPPER_LETTER;
		}
		else if ( s1Char >= (byte)0x61 && s1Char <= (byte)0x7A )
		{
			return LOWER_LETTER;
		}
		else if ( s1Char >= (byte)0x30 && s1Char <= (byte)0x39 )
		{
			return DIGIT_CHAR;
		}
		else if ( s1Char >= (byte)0x20 && s1Char <= (byte)0x7E )
		{
			return OTHER_CHAR;
		}
		else
		{
			return INVALID_CHAR;
		}
	}
	
}
