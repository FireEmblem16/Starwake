
public class WarriorHeart extends Thread
{
	public WarriorHeart(DataSegment[] memory, int warrior_number, int initial_address, boolean run_concurrently, int total_warriors)
	{
		super("Program #" + new Integer(warrior_number).toString());
		setDaemon(true);
		
		mem = memory;
		remaining_warriors = total_warriors;
		Thor_God_Of_Thunder = new Warrior(warrior_number,initial_address);
		name = "Program #" + new Integer(warrior_number).toString();
		
		if(run_concurrently)
			start();
		
		return;
	}
	
	public WarriorHeart(DataSegment[] memory, String warrior_name, int warrior_number, int initial_address, boolean run_concurrently, int total_warriors)
	{
		super(warrior_name);
		setDaemon(true);
		
		mem = memory;
		remaining_warriors = total_warriors;
		Thor_God_Of_Thunder = new Warrior(warrior_number,initial_address);
		name = warrior_name;
		
		if(run_concurrently)
			start();
		
		return;
	}
	
	public void run()
	{
		while(Thor_God_Of_Thunder.state == Warrior.State.ALIVE && remaining_warriors > 1)
			if(!step())
				Thor_God_Of_Thunder.state = Warrior.State.DEAD;
		
		return;
	}
	
	public void notify_of_death()
	{
		remaining_warriors--;
		return;
	}
	
	/**
	 * @return
	 * Returns false if an illegal instruction was encountered.
	 */
	public boolean step()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		DataSegment.Opcodes op = operation.opcode;
		
		switch(op)
		{
			case DAT:
				return execute_dat();
			case MOV:
				return execute_mov();
			case ADD:
				return execute_add();
			case JMP:
				return execute_jmp();
			case JMZ:
				return execute_jmz();
			case SLT:
				return execute_slt();
			case CMP:
				return execute_cmp();
			default:
				break;
		}
		
		return false;
	}
	
	/**
	 * This instruction has two purposes. First, it can be used as a generic placeholder for arbitrary data.
	 * Second, attempting to execute this instruction terminates the simulation and the program which tried
	 * to execute it loses the match. This is the only way that a program can terminate, therefore each
	 * warrior attempts to overwrite the other one's program with DAT instructions. Both A and B operands
	 * must be immediate.
	 */
	public boolean execute_dat()
	{
		Thor_God_Of_Thunder.increment_ip();
		return false;
	}
	
	/**
	 * If the A operand is immediate, the value of this operand is copied into the B field of the instruction
	 * specified by MOV's B operand. If neither operand is immediate, the entire instruction (including all
	 * field values and addressing modes) at location A is copied to location B. The B operand cannot be
	 * immediate.
	 */
	public boolean execute_mov()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		Thor_God_Of_Thunder.increment_ip();
		
		if(operation.B.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		if(operation.A.mode == DataSegment.AddressMode.IMMEDIATE)
		{
			int move_to = operation.B.get_relative_address(operation.adr,mem);
			mem[move_to].B.val = operation.A.val;
			
			return true;
		}
		
		int relative_address_a = operation.A.get_relative_address(operation.adr,mem);
		DataSegment relative_a_space = mem[relative_address_a];
		int relative_address_b = operation.B.get_relative_address(operation.adr,mem);
		DataSegment relative_b_space = mem[relative_address_b];
		
		relative_b_space.make_equal_to(relative_a_space);
		return true;
	}
	
	/**
	 * If the A operand is immediate, its value is added to the value of the B field of the instruction
	 * specified by ADD's B operand, and the final result is stored into the B field of that same instruction. If
	 * neither operand is immediate, then they both specify the locations of two instructions in memory. In
	 * this case, the A and B fields of one instruction are respectively added to the A and B fields of the
	 * second instruction, and both results are respectively written to the A and B fields of the instruction
	 * specified by the ADD's B operand. The B operand cannot be immediate.
	 */
	public boolean execute_add()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		Thor_God_Of_Thunder.increment_ip();
		
		if(operation.B.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		if(operation.A.mode == DataSegment.AddressMode.IMMEDIATE)
		{
			int add_this = operation.A.get_relative_value(operation.adr,mem);
			int relative_address_b = operation.B.get_relative_address(operation.adr,mem);
			DataSegment.Data relative_b_space = mem[relative_address_b].B;
			
			relative_b_space.val += add_this;
			relative_b_space.validate_value();
		}
		
		int relative_address_a = operation.A.get_relative_address(operation.adr,mem);
		DataSegment relative_a_space = mem[relative_address_a];
		int relative_address_b = operation.B.get_relative_address(operation.adr,mem);
		DataSegment relative_b_space = mem[relative_address_b];
		
		relative_b_space.A.val += relative_a_space.A.val;
		relative_b_space.A.validate_value();
		relative_b_space.B.val += relative_a_space.B.val;
		relative_b_space.B.validate_value();
		
		return true;
	}

	/**
	 * Jump to the address specified by the A operand. In other words, the instruction pointer is loaded
	 * with a new address (instead of being incremented), and the next instruction executed after the JMP
	 * will be from the memory location specified by A. The A operand cannot be immediate. The B
	 * operand must be immediate, but is not used by this instruction.
	 */
	public boolean execute_jmp()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		
		if(operation.A.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		if(operation.B.mode != DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		int relative_address_a = operation.A.get_relative_address(operation.adr,mem);
		Thor_God_Of_Thunder.goto_instruction(relative_address_a);
		
		return true;
	}

	/**
	 * If the B field of the instruction specified by JMZ's B operand is zero, then jump to the address
	 * specified by the A operand. Neither the A nor B operand can be immediate.
	 */
	public boolean execute_jmz()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		
		if(operation.A.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		if(operation.B.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		int relative_value_b = operation.B.get_relative_value(operation.adr,mem);
		if(relative_value_b != 0)
			return true;
		
		int relative_address_a = operation.A.get_relative_address(operation.adr,mem);
		Thor_God_Of_Thunder.goto_instruction(relative_address_a);
		
		return true;
	}

	/**
	 * If A is an immediate operand, its value is compared with the value in the B field of the instruction
	 * specified by SLT's B operand. If A is not immediate, the B fields of the two instructions specified by
	 * the operands are compared instead. If the first value (i.e the one specified by A) is less than the
	 * second value, then the next instruction is skipped. The B operand cannot be immediate.
	 */
	public boolean execute_slt()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		Thor_God_Of_Thunder.increment_ip();
		
		if(operation.B.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		int relative_value_a = operation.A.get_relative_value(operation.adr,mem);
		int relative_value_b = operation.B.get_relative_value(operation.adr,mem);
		
		if(relative_value_a < relative_value_b)
			Thor_God_Of_Thunder.increment_ip();
		
		return true;
	}

	/**
	 * The entire contents of memory locations specified by A and B are checked for equality. If the two
	 * locations are equal, then the next instruction is skipped. Memory locations are considered equal to
	 * another if they both have the same opcodes and they have the same values and addressing modes in
	 * their respective operand fields. The A or B operands cannot be immediate.
	 */
	public boolean execute_cmp()
	{
		DataSegment operation = mem[Thor_God_Of_Thunder.ip];
		Thor_God_Of_Thunder.increment_ip();
		
		if(operation.A.mode == DataSegment.AddressMode.IMMEDIATE || operation.B.mode == DataSegment.AddressMode.IMMEDIATE)
			return false;
		
		int relative_address_a = operation.A.get_relative_address(operation.adr,mem);
		DataSegment relative_a_space = mem[relative_address_a];
		int relative_address_b = operation.B.get_relative_address(operation.adr,mem);
		DataSegment relative_b_space = mem[relative_address_b];
		
		if(relative_a_space.equals(relative_b_space))
			Thor_God_Of_Thunder.increment_ip();
		
		return true;
	}
	
	public DataSegment[] mem;
	public Warrior Thor_God_Of_Thunder;
	public String name;
	public int remaining_warriors;
}
