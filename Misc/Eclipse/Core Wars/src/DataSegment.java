
public class DataSegment
{
	public DataSegment(int address)
	{
		adr = address;
		opcode = Opcodes.DAT;
		A = new Data(0,"#",DataType.A);
		B = new Data(0,"#",DataType.B);
		
		validate_address();
		return;
	}
	
	public DataSegment(int address, String operation, int operand_a, String address_mode_a, int operand_b, String address_mode_b)
	{
		adr = address;
		A = new Data(operand_a,address_mode_a,DataType.A);
		B = new Data(operand_b,address_mode_b,DataType.B);
		
		opcode = string_to_opcode(operation);
		validate_address();
		return;
	}
	
	public Opcodes string_to_opcode(String op)
	{
		if(op == null)
			return null;
		
		if(op.equals("DAT"))
			return Opcodes.DAT;
		
		if(op.equals("MOV"))
			return Opcodes.MOV;
		
		if(op.equals("ADD"))
			return Opcodes.ADD;
		
		if(op.equals("JMP"))
			return Opcodes.JMP;
		
		if(op.equals("JMZ"))
			return Opcodes.JMZ;
		
		if(op.equals("SLT"))
			return Opcodes.SLT;
		
		if(op.equals("CMP"))
			return Opcodes.CMP;
		
		return null;
	}
	
	public void validate_address()
	{
		adr = Statics.mod_memory_size(adr);
		return;
	}
	
	/**
	 * Does not change the address.
	 */
	public void make_equal_to(DataSegment d)
	{
		opcode = d.opcode;
		A.make_equal_to(d.A);
		B.make_equal_to(d.B);
		
		return;
	}
	
	/**
	 * Does not compare the value of adr when deciding equality.
	 */
	public boolean equals(Object obj)
	{
		if(!(obj instanceof DataSegment))
			return false;
		
		DataSegment d = (DataSegment)obj;
		
		if(opcode != d.opcode)
			return false;
		
		if(!A.equals(d.A))
			return false;
		
		if(!B.equals(d.B))
			return false;
		
		return true;
	}
	
	public enum Opcodes
	{DAT,MOV,ADD,JMP,JMZ,SLT,CMP}
	
	public enum AddressMode
	{IMMEDIATE,DIRECT,INDIRECT}
	
	public enum DataType
	{A,B}
	
	public class Data
	{
		public Data(int value, String address_mode, DataType A_or_B)
		{
			val = value;
			type = A_or_B;
			mode = string_to_mode(address_mode);
			
			validate_value();
			return;
		}
		
		public AddressMode string_to_mode(String str)
		{
			if(str == null)
				return null;
			
			if(str.equals("#"))
				return AddressMode.IMMEDIATE;
			
			if(str.equals("$"))
				return AddressMode.DIRECT;
			
			if(str.equals("@"))
				return AddressMode.INDIRECT;
			
			return null;
		}
		
		public void validate_value()
		{
			val = Statics.mod_memory_size(val);
			return;
		}
		
		/**
		 * @return
		 * Returns -1 if a valid address mode was not found.
		 */
		public int get_relative_value(int ip, DataSegment[] memory)
		{
			if(mode == AddressMode.IMMEDIATE)
				return val;
			
			if(mode == AddressMode.DIRECT)
			{
				int relative_val = Statics.mod_memory_size(ip + val);
				Data direct_value = memory[relative_val].B;
				return direct_value.val;
			}
			
			if(mode == AddressMode.INDIRECT)
			{
				int relative_val = Statics.mod_memory_size(ip + val);
				Data direct_value = memory[relative_val].B;
				int indirect_val = Statics.mod_memory_size(relative_val + direct_value.val);
				Data indirect_data = memory[indirect_val].B;
				return indirect_data.val;
			}
			
			return -1;
		}
		
		/**
		 * @return
		 * Returns -1 if a valid address mode was not found.
		 */
		public int get_relative_address(int ip, DataSegment[] memory)
		{
			if(mode == AddressMode.IMMEDIATE)
				return val;
			
			if(mode == AddressMode.DIRECT)
				return Statics.mod_memory_size(ip + val);
			
			if(mode == AddressMode.INDIRECT)
			{
				int relative_val = Statics.mod_memory_size(ip + val);
				Data direct_value = memory[relative_val].B;
				return Statics.mod_memory_size(relative_val + direct_value.val);
			}
			
			return -1;
		}
		
		public void make_equal_to(Data d)
		{
			val = d.val;
			mode = d.mode;
			type = d.type;
			
			return;
		}
		
		public boolean equals(Object obj)
		{
			if(!(obj instanceof Data))
				return false;
			
			Data d = (Data)obj;
			
			if(val != d.val)
				return false;
			
			if(mode != d.mode)
				return false;
			
			if(type != d.type)
				return false;
			
			return true;
		}
		
		public int val;
		public AddressMode mode;
		public DataType type;
	}
	
	public int adr;
	public Opcodes opcode;
	public Data A;
	public Data B;
}
