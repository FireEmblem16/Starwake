package oracle;

import java.util.ArrayList;
import java.util.HashMap;

public class LZEncryption
{
	private LZEncryption()
	{return;}
	
	public static String encode(String uncompressed)
	{
		Trie t = new Trie();
		ArrayList<Encoding> output = new ArrayList<Encoding>();
		
		int index = 0;
		
		while(index < uncompressed.length())
		{
			int len = 0;
			int prev = 0;
			int prefix_index = -1;
			
			while(index + len <= uncompressed.length() && (prefix_index = t.IndexOf(uncompressed.substring(index,index + len))) != -1)
			{
				len++;
				prev = prefix_index;
			}
			
			if(index + len > uncompressed.length())
			{ // We just need to output the prefix index
				output.add(new Encoding(prefix_index,""));
				index += len;
			}
			else
			{ // We need to output the prefix index and the suffix
				t.Add(uncompressed.substring(index,index + len));
				output.add(new Encoding(prev,uncompressed.substring(index + len - 1,index + len)));
				
				index += len;
			}
		}
		
		// Pack the bits
		ByteWrapper bw = new ByteWrapper();
				
		// Add the four codeword size bytes to the beginning of the bytewrapper
		int codeword_len = (int)Math.ceil(Math.log(t.size()) / Math.log(2));
		bw.PackBits(new BigByte(new byte[] {(byte)((codeword_len >> 24) & 0xFF),(byte)((codeword_len >> 16) & 0xFF),(byte)((codeword_len >> 8) & 0xFF),(byte)(codeword_len & 0xFF)}));
		
		// Pack all of the data but the last one which will likely require a special case
		for(int i = 0;i < output.size() - 1;i++)
		{
			Pair<BigByte,BigByte> e = output.get(i).ToBinary(codeword_len);
			
			bw.PackBits(e.s);
			bw.PackBits(e.t);
		}
		
		// Pack the last piece of data
		if(output.size() > 0)
		{
			Encoding e = output.get(output.size() - 1);
			Pair<BigByte,BigByte> p = e.ToBinary(codeword_len);
			
			bw.PackBits(p.s);
			
			if(!e.suffix.equals(""))
				bw.PackBits(p.t);
		}
		
		return bw.CastAsString();
	}
	
	public static String decode(String compressed)
	{
		ByteUnwrapper bu = new ByteUnwrapper(BytesFromString(compressed));
		
		HashMap<Integer,String> map = new HashMap<Integer,String>();
		map.put(0,"");
		
		int index = 1;
		String ret = "";
		
		int codeword_len = (((int)bu.Get(8).GetBits(0)) << 24) + (((int)bu.Get(8).GetBits(0)) << 16) + (((int)bu.Get(8).GetBits(0)) << 8) + (int)bu.Get(8).GetBits(0);
		
		while(bu.HasMore())
		{
			Encoding e = new Encoding(bu.Get(codeword_len),bu.Get(16));
			String str = map.get(e.prefix) + e.suffix;
			
			map.put(index++,str);
			ret += str;
		}
		
		return ret;
	}
	
	public static String StringFromBytes(byte[] b)
	{
		final char[] masks = {0x8000,0x4000,0x2000,0x1000,0x800,0x400,0x200,0x100,0x80,0x40,0x20,0x10,0x8,0x4,0x2,0x1};
		String ret = "";
		
		for(int i = 0;i < b.length;i += 2)
		{
			char c = 0x0;
			
			for(int j = 0;j < 16;j++)
				c |= ((b[i + j / 8] << (j < 8 ? 8 : 0)) & masks[j]);
			
			ret += c;
		}
		
		return ret;
	}
	
	public static byte[] BytesFromString(String str)
	{
		if(str == null || str.equals(""))
			return new byte[0];
		
		byte[] ret = new byte[str.length() << 1];
		
		for(int i = 0;i < str.length();i++)
		{
			ret[i << 1] = (byte)((str.charAt(i) >> 8) & 0xFF);
			ret[(i << 1) + 1] = (byte)(str.charAt(i) & 0xFF);
		}
		
		return ret;
	}
	
	protected static class Pair<S,T>
	{
		public Pair(S s, T t)
		{
			this.s = s;
			this.t = t;
			
			return;
		}
		
		public final S s;
		public final T t;
	}
	
	protected static class Encoding
	{
		public Encoding(int Prefix, String Suffix)
		{
			prefix = Prefix;
			suffix = Suffix;
			
			return;
		}
		
		public Encoding(BigByte Prefix, BigByte Suffix)
		{
			// Super lazy method activate
			int prefix_0 = 0;
			
			for(int i = 0;i < Prefix.length;i++)
				if(Prefix.msb())
					prefix_0 = (prefix_0 << 1) + (int)((Prefix.GetBits(i,1) >> 7) & 0x1);
				else
					prefix_0 = (prefix_0 << 1) + (int)Prefix.GetBits(i,1);
			
			prefix = prefix_0;
			
			if(Suffix == null || Suffix.length != 16)
			{
				suffix = "";
				return;
			}
			
			byte[] str = {Suffix.GetBits(0),Suffix.GetBits(8)};
			String stemp = StringFromBytes(str);
			
			suffix = stemp;
			return;
		}
		
		// We return the prefix bigbyte first
		public Pair<BigByte,BigByte> ToBinary(int code_width)
		{
			byte[] b = BytesFromString(suffix);
			
			int packed_prefix = prefix << (32 - code_width);
			byte[] packed_prefix_bytes = {(byte)((packed_prefix >> 24) & 0xFF),(byte)((packed_prefix >> 16) & 0xFF),(byte)((packed_prefix >> 8) & 0xFF),(byte)(packed_prefix & 0xFF)};
			
			return new Pair<BigByte,BigByte>(new BigByte(packed_prefix_bytes,code_width),new BigByte(b));
		}
		
		public String toString()
		{return "<" + prefix + "," + suffix + ">";}
		
		public final int prefix;
		public final String suffix;
	}
	
	protected static class Trie
	{
		public Trie()
		{
			root = new Node("",0);
			size = 1;
			
			return;
		}
		
		public boolean Add(String str)
		{
			boolean ret = root.Add(str,size);
			
			if(ret)
				size++;
			
			return ret;
		}
		
		public int IndexOf(String str)
		{
			if(str.equals(""))
				return 0;
			
			return root.IndexOf(str);
		}
		
		public boolean Contains(String str)
		{return root.Contains(str);}
		
		public int size()
		{return size;}
		
		protected Node root;
		protected int size;
		
		protected class Node
		{
			public Node(String n, int index)
			{
				val = n;
				Index = index;
				
				children = new ArrayList<Node>();
				return;
			}
			
			public boolean Add(String str, int index)
			{
				if(str == null || str.length() == 0)
					return false;
				
				char cstr = str.charAt(0);
				
				if(str.length() == 1)
				{
					// Super lazy insertion sort and linear search
					for(int i = 0;i < children.size();i++)
						if(cstr == children.get(i).val.charAt(0))
							return false;
						else if(cstr < children.get(i).val.charAt(0))
						{
							children.add(i,new Node(str,index));
							return true;
						}
					
					children.add(new Node(str,index));
					return true;
				}

				for(int i = 0;i < children.size();i++)
					if(cstr == children.get(i).val.charAt(0))
						return children.get(i).Add(str.substring(1),index);
				
				return false;
			}
			
			public Node Get(String str)
			{
				if(str == null || str.length() == 0)
					return null;
				
				char cstr = str.charAt(0);
				
				if(str.length() == 1)
				{
					// Super lazy insertion sort and linear search
					for(int i = 0;i < children.size();i++)
						if(cstr == children.get(i).val.charAt(0))
							return children.get(i);
					
					return null;
				}

				for(int i = 0;i < children.size();i++)
					if(cstr == children.get(i).val.charAt(0))
						return children.get(i).Get(str.substring(1));
				
				return null;
			}
			
			public int IndexOf(String str)
			{
				if(str == null || str.length() == 0)
					return -1;
				
				char cstr = str.charAt(0);
				
				if(str.length() == 1)
				{
					// Super lazy insertion sort and linear search
					for(int i = 0;i < children.size();i++)
						if(cstr == children.get(i).val.charAt(0))
							return children.get(i).Index;
					
					return -1;
				}

				for(int i = 0;i < children.size();i++)
					if(cstr == children.get(i).val.charAt(0))
						return children.get(i).IndexOf(str.substring(1));
				
				return -1;
			}
			
			public boolean Contains(String str)
			{
				if(str == null || str.length() == 0)
					return false;
				
				char cstr = str.charAt(0);
				
				if(str.length() == 1)
				{
					// Super lazy insertion sort and linear search
					for(int i = 0;i < children.size();i++)
						if(cstr == children.get(i).val.charAt(0))
							return true;
					
					return false;
				}

				for(int i = 0;i < children.size();i++)
					if(cstr == children.get(i).val.charAt(0))
						return children.get(i).Contains(str.substring(1));
				
				return false;
			}
			
			public String toString()
			{return val;}
			
			public final String val;
			public final int Index;
			protected final ArrayList<Node> children;
		}
	}
	
	protected static class BigByte
	{
		public BigByte(byte[] b)
		{
			this(b,b.length * 8,true);
			return;
		}
		
		public BigByte(byte[] b, int len)
		{
			this(b,len,true);
			return;
		}
		
		public BigByte(byte[] b, int len, boolean MSB)
		{
			bits = b;
			length = len;
			
			msb = MSB;
			return;
		}
		
		public byte GetBits(int i)
		{
			int index = i / 8;
			int sub_index = i - 8 * index;
			
			if(index < 0 || index >= bits.length)
				return 0x0; // Yeah, whatever
			
			if(sub_index == 0)
				return bits[index];
			
			byte b1 = bits[index];
			byte b2 = index < bits.length - 1 ? bits[index + 1] : 0x0;
			
			if(msb)
				return (byte)((b1 << sub_index) | ((b2 >> (8 - sub_index)) & GetInvertedMask(sub_index)));
			
			return (byte)(((b1 >> sub_index) & GetInvertedMask(sub_index)) | (b2 << (8 - sub_index)));
		}
		
		public byte GetBits(int i, int len)
		{
			if(len < 1 || len > 8)
				return 0x0;
			
			return (byte)(GetBits(i) & GetMask(len));
		}
		
		protected byte GetMask(int a)
		{
			if(msb)
				return GetMSBMask(a);
			
			return GetLSBMask(a);
		}
		
		protected byte GetInvertedMask(int a)
		{
			if(!msb)
				return GetMSBMask(a);
			
			return GetLSBMask(a);
		}
		
		protected byte GetMSBMask(int a)
		{
			final byte[] masks = {(byte)0x80,(byte)0xC0,(byte)0xE0,(byte)0xF0,(byte)0xF8,(byte)0xFC,(byte)0xFE,(byte)0xFF};
			
			if(a < 1 || a > 8)
				return 0x0;
			
			return masks[a - 1];
		}
		
		protected byte GetLSBMask(int a)
		{
			final byte[] masks = {0x1,0x3,0x7,0xF,0x1F,0x3F,0x7F,(byte)0xFF};
			
			if(a < 1 || a > 8)
				return 0x0;
			
			return masks[a - 1];
		}
		
		public boolean msb()
		{return msb;}
		
		public final byte[] bits;
		public final int length;
		
		protected boolean msb;
	}
	
	protected static class ByteUnwrapper
	{
		public ByteUnwrapper(byte[] b)
		{
			this(b,8 * b.length,true);
			return;
		}
		
		public ByteUnwrapper(byte[] b, int len)
		{
			this(b,len,true);
			return;
		}
		
		public ByteUnwrapper(byte[] b, int len, boolean MSB)
		{
			bytes = b;
			unwrapper = new BigByte(b,len,MSB);
			index = 0;
			
			msb = MSB;
			return;
		}
		
		public boolean HasMore()
		{return index < unwrapper.length;}
		
		public int RemainingBits()
		{return unwrapper.length - index;}
		
		public void Backtrack(int bits)
		{
			index -= bits;
			
			if(index < 0)
				Reset();
			
			return;
		}
		
		public void Reset()
		{
			index = 0;
			return;
		}
		
		public BigByte Get(int bits)
		{
			if(index == unwrapper.length)
				return null;
			else if(index + bits > unwrapper.length)
				return Get(unwrapper.length - index);
			
			BigByte ret = Peek(bits);
			index += bits;
			
			return ret;
		}
		
		public BigByte Peek(int bits)
		{
			byte[] b = new byte[bits / 8 + 1];
			
			for(int i = 0;i < bits;i += 8)
				b[i >> 3] = unwrapper.GetBits(index + i,Math.min(8,bits - i));
			
			return new BigByte(b,bits,msb);
		}
		
		protected byte GetMask(int a)
		{
			if(msb)
				return GetMSBMask(a);
			
			return GetLSBMask(a);
		}
		
		protected byte GetInvertedMask(int a)
		{
			if(!msb)
				return GetMSBMask(a);
			
			return GetLSBMask(a);
		}
		
		protected byte GetMSBMask(int a)
		{
			final byte[] masks = {(byte)0x80,(byte)0xC0,(byte)0xE0,(byte)0xF0,(byte)0xF8,(byte)0xFC,(byte)0xFE,(byte)0xFF};
			
			if(a < 1 || a > 8)
				return 0x0;
			
			return masks[a - 1];
		}
		
		protected byte GetLSBMask(int a)
		{
			final byte[] masks = {0x1,0x3,0x7,0xF,0x1F,0x3F,0x7F,(byte)0xFF};
			
			if(a < 1 || a > 8)
				return 0x0;
			
			return masks[a - 1];
		}
		
		public String toString()
		{
			String ret = "";
			
			for(int i = index;i < unwrapper.length;i++)
				if(unwrapper.GetBits(i,1) != 0)
					ret += "1";
				else
					ret += "0";
			
			return ret;
		}
		
		protected BigByte unwrapper;
		protected byte[] bytes;
		protected int index;
		
		protected boolean msb;
	}
	
	protected static class ByteWrapper
	{
		public ByteWrapper()
		{
			this(true);
			return;
		}
		
		public ByteWrapper(boolean MSB)
		{
			bytes = new ArrayList<Byte>();
			msb = MSB;
			
			return;
		}
		
		public void PackBits(BigByte b)
		{
			int remaining_bits = b.length;
			
			// First pack everything into the last byte that we can
			if(sub_index != 0)
			{
				byte remaining = b.GetBits(0,(8 - sub_index));
				
				if(msb) // If we are storing the most significant bit first (as we should be) then we need to move to the right
				{
					remaining >>= sub_index;
					remaining &= GetInvertedMask(8 - sub_index);
				}
				
				bytes.set(bytes.size() - 1,(byte)(bytes.get(bytes.size() - 1) | remaining));
				remaining_bits -= 8 - sub_index;
			}
			else
				sub_index = 8;
			
			// Now pack in more bytes until we run out of bits
			for(int i = 0;remaining_bits > 0;i++,remaining_bits -= 8)
				bytes.add(b.GetBits(i * 8 + 8 - sub_index));
			
			sub_index = (sub_index + b.length) % 8;
			return;
		}
		
		protected byte GetMask(int a)
		{
			if(msb)
				return GetMSBMask(a);
			
			return GetLSBMask(a);
		}
		
		protected byte GetInvertedMask(int a)
		{
			if(!msb)
				return GetMSBMask(a);
			
			return GetLSBMask(a);
		}
		
		protected byte GetMSBMask(int a)
		{
			final byte[] masks = {(byte)0x80,(byte)0xC0,(byte)0xE0,(byte)0xF0,(byte)0xF8,(byte)0xFC,(byte)0xFE,(byte)0xFF};
			
			if(a < 1 || a > 8)
				return 0x0;
			
			return masks[a - 1];
		}
		
		protected byte GetLSBMask(int a)
		{
			final byte[] masks = {0x1,0x3,0x7,0xF,0x1F,0x3F,0x7F,(byte)0xFF};
			
			if(a < 1 || a > 8)
				return 0x0;
			
			return masks[a - 1];
		}
		
		public String toString()
		{
			byte[] masks = {(byte)0x80,0x40,0x20,0x10,0x8,0x4,0x2,0x1};
			String ret = "";
			
			for(int i = 0;i < bytes.size();i++)
			{
				byte b = bytes.get(i).byteValue();
				
				for(int j = 0;j < 8;j++)
					if((b & masks[j]) != 0)
						ret += "1";
					else
						ret += "0";
			}
			
			return ret;
		}
		
		public String CastAsString()
		{
			final char[] masks = {0x8000,0x4000,0x2000,0x1000,0x800,0x400,0x200,0x100,0x80,0x40,0x20,0x10,0x8,0x4,0x2,0x1};
			boolean odd = (bytes.size() & 0x1) == 0x1;
			
			Byte[] b = new Byte[bytes.size()];
			bytes.toArray(b);
			
			byte[] ret = new byte[b.length + (odd ? 1 : 0)];
			
			for(int i = 0;i < b.length;i++)
				ret[i] = b[i].byteValue();
			
			// If we had an odd number of bytes we need to pack on some extra zeroes
			if(odd)
				ret[ret.length - 1] = 0x00;
			
			return StringFromBytes(ret);
		}
		
		protected ArrayList<Byte> bytes;
		protected int sub_index;
		
		protected boolean msb;
	}
}
