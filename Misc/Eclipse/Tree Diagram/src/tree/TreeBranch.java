///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// Omacron Storm //////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// tree  Package //////////////////////////////////////
/////////////////////////////////// TreeBranch ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
/////////////////// Contains the basic structure for a tree diagram. ////////////////// 
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
package tree;

/**
 * Contains the structure for a Tree Diagram.
 */
public class TreeBranch<E>
{
	/**
	 * Creates a new TreeBranch member that forms the trunk of the tree.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a new TreeBranch.
	 */
	public TreeBranch()
	{
		this.branches = new TreeBranch[0];
		this.Stem = null;
		this.Data = null;
		this.Name = null;
		
		return;
	}
	
	/**
	 * Creates a new TreeBranch member.
	 * @param Stem
	 * The parent TreeBranch.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a new TreeBranch.
	 */
	public TreeBranch(TreeBranch<E> Stem)
	{
		this.branches = new TreeBranch[0];
		this.Stem = Stem;
		this.Name = null;
		this.Data = null;
		
		return;
	}
	
	/**
	 * Creates a new TreeBranch member.
	 * @param Name
	 * The name of the TreeBranch.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a new TreeBranch.
	 */
	public TreeBranch(String Name)
	{
		this.branches = new TreeBranch[0];
		this.Stem = null;
		this.Name = Name;
		this.Data = null;
		
		return;
	}
	
	/**
	 * Creates a new TreeBranch member.
	 * @param Data
	 * The data of the TreeBranch.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a new TreeBranch.
	 */
	public TreeBranch(E Data)
	{
		this.branches = new TreeBranch[0];
		this.Stem = null;
		this.Name = null;
		this.Data = Data;
		
		return;
	}
	
	/**
	 * Creates a new TreeBranch member.
	 * @param Stem
	 * The parent TreeBranch.
	 * @param Name
	 * The name of the TreeBranch.
	 * @param Data
	 * The data of the TreeBranch.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a new TreeBranch.
	 */
	public TreeBranch(TreeBranch<E> Stem, String Name, E Data)
	{
		this.branches = new TreeBranch[0];
		this.Stem = Stem;
		this.Name = Name;
		this.Data = Data;
		
		return;
	}
	
	/**
	 * Creates a new TreeBranch member.
	 * @param Stem
	 * The parent TreeBranch.
	 * @param Name
	 * The name of the TreeBranch.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a new TreeBranch.
	 */
	public TreeBranch(TreeBranch<E> Stem, String Name)
	{
		this.branches = new TreeBranch[0];
		this.Stem = Stem;
		this.Name = Name;
		this.Data = null;
		
		return;
	}
	
	/**
	 * Sets the data associated with this TreeBranch.
	 * @param data
	 * The data to be assigned to this TreeBranch.
	 * @return <b>void</b>
	 */
	public void SetData(E data)
	{
		this.Data = data;
		
		return;
	}
	
	/**
	 * Gets the data associated with this TreeBranch.
	 * @return <b>E*</b>
	 * Returns the object pointer to the data of this TreeBranch.
	 */
	public E GetData()
	{	
		return this.Data;
	}
	
	/**
	 * Sets the name associated with this TreeBranch.
	 * @param data
	 * The name to be assigned to this TreeBranch.
	 * @return <b>void</b>
	 */
	public void SetName(String data)
	{
		this.Name = data;
		
		return;
	}
	
	/**
	 * Gets the name associated with this TreeBranch.
	 * @return <b>Object*</b>
	 * Returns the name of this TreeBranch.
	 */
	public String GetName()
	{	
		return this.Name;
	}
	
	/**
	 * Adds a new branch to the TreeBranch.
	 * @param Branch
	 * The branch to add.
	 * @return <b>void</b>
	 */
	public void AddBranch(TreeBranch<E> Branch)
	{
		TreeBranch<E>[] TTemp = new TreeBranch[this.branches.length+1];
		System.arraycopy(this.branches,0,TTemp,0,this.branches.length);
		TTemp[TTemp.length-1] = Branch;
		this.branches = TTemp;
		
		return;
	}
	
	/**
	 * Removes a branch from the TreeBranch.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to remove.
	 * @return <b>boolean</b>
	 * Returns true if the branch was successfully removed and otherwise returns false.
	 */
	public boolean RemoveBranch(int Branch)
	{
		if(Branch > this.branches.length || Branch < 1)
			return false;
		
		for(int i = Branch-1;i < this.branches.length-1;i++)
			this.branches[i] = this.branches[i+1];
		
		TreeBranch<E>[] TTemp = new TreeBranch[this.branches.length-1];
		System.arraycopy(this.branches,0,TTemp,0,this.branches.length-1);
		this.branches = TTemp;
			
		return true;
	}
	
	/**
	 * Removes a branch from the TreeBranch.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to remove.
	 * @return <b>boolean</b>
	 * Returns true if the branch was successfully removed and otherwise returns false.
	 */
	public boolean RemoveBranch(TreeBranch<E> Branch)
	{
		int ITemp = -1;
		
		for(int i = 0;i < this.branches.length;i++)
			if(Branch == this.branches[i])
			{
				ITemp = i;
				i = this.branches.length - 1;
			}
		
		if(ITemp == -1)
			return false;
		
		for(int i = ITemp;i < this.branches.length-1;i++)
			this.branches[i] = this.branches[i+1];
		
		TreeBranch<E>[] TTemp = new TreeBranch[this.branches.length-1];
		System.arraycopy(this.branches,0,TTemp,0,this.branches.length-1);
		this.branches = TTemp;
			
		return true;
	}
	
	/**
	 * Changes a branch of the TreeBranch.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to edit.
	 * @param Sprout
	 * The new TreeBranch that should be inserted.
	 * @return <b>boolean</b>
	 * Returns true if the branch was successfully edited and otherwise returns false.
	 */
	public boolean EditBranch(int Branch, TreeBranch<E> Sprout)
	{
		if(Branch > this.branches.length || Branch < 1)
			return false;
		
		this.branches[Branch-1] = Sprout;	
		return true;
	}
	
	/**
	 * Returns a branch of this Tree Diagram.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to return.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a branch of this Tree Diagram.
	 * Returns null if branch [Branch] does not exist.
	 */
	public TreeBranch<E> GetBranch(int Branch)
	{
		if(Branch > this.branches.length || Branch < 1)
			return null;
		
		return this.branches[Branch-1];
	}
	
	/**
	 * Returns a branch of this Tree Diagram named Branch.
	 * @param Branch
	 * The branch to return.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a branch of this Tree Diagram.
	 * Returns null if branch [Branch] does not exist.
	 * If more than one branch with the name Branch exists the last is returned.
	 */
	public TreeBranch<E> GetBranch(String Branch)
	{
		TreeBranch<E> ret = null;
		
		for(int i = 0;i < this.branches.length;i++)
			if(this.branches[i].GetName().equals(Branch))
				ret = this.branches[i];
		
		return ret;
	}
	
	/**
	 * Returns a branch index of this Tree Diagram named Branch.
	 * @param Branch
	 * The branch index to return.
	 * @return <b>int</b>
	 * Returns the index of a branch named Branch.
	 * Returns 0 if no such index exists.
	 */
	public int GetBranchIndex(String Branch)
	{
		for(int i = 0;i < this.branches.length;i++)
			if(this.branches[i].GetName() == null && Branch == null)
				return i+1;
			else if(Branch == null)
				return 0;
			else if(this.branches[i].GetName() == null)
				continue;
			else if(this.branches[i].GetName().equals(Branch))
				return i+1;
		
		return 0;
	}
	
	/**
	 * Obtains the array of branches for this TreeBranch.
	 * @return <b>TreeBranch**</b>
	 * Returns a pointer to an array of pointers to branches of this Tree Diagram.
	 */
	public TreeBranch<E>[] GetBranches()
	{
		return this.branches;
	}
	
	/**
	 * Returns the stem of this TreeBranch.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a stem of this Tree Diagram.
	 * Returns null if this Tree Diagram is the trunk.
	 */
	public TreeBranch<E> GetStem()
	{
		return this.Stem;
	}
	
	/**
	 * Sets the stem of this TreeBranch.
	 * @return <b>void</b>
	 */
	public void SetStem(TreeBranch<E> stem)
	{
		this.Stem = stem;
		
		return;
	}
	
	/**
	 * Finds and returns a pointer to a TreeBranch under or at this level with name [branch].
	 * @param branch
	 * The name of the TreeBranch to locate.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a the Branch specified.
	 * Returns null if the Branch could not be found.
	 */
	public TreeBranch<E> FindBranch(String branch)
	{
		if(this.Name != null)
			if(this.Name.equals(branch))
				return this;
		
		TreeBranch<E> TTemp = new TreeBranch<E>();
		
		for(int i = 0;i < this.branches.length;i++)
		{
			TTemp = this.branches[i].FindBranch(branch);
			
			if(TTemp != null)
				return TTemp;
		}
		
		return null;
	}
	
	/**
	 * Finds and returns a pointer to a TreeBranch under or at this level.
	 * @param branch
	 * The TreeBranch to locate.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a the Branch specified.
	 * Returns null if the Branch could not be found.
	 */
	public TreeBranch<E> FindBranch(TreeBranch<E> branch)
	{
		if(this == branch)
			return this;
		
		TreeBranch<E> TTemp = new TreeBranch<E>();
		
		for(int i = 0;i < this.branches.length;i++)
		{
			TTemp = this.branches[i].FindBranch(branch);
			
			if(TTemp != null)
				return TTemp;
		}
		
		return null;
	}
	
	/**
	 * Finds and returns a pointer to a TreeBranch under or at this level with the data [Data].
	 * If E is a comparable object it will look for equal objects. Otherwise it will look for
	 * objects to be the same object.
	 * @param Data
	 * The data to locate.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to a the Branch specified.
	 * Returns null if the Branch could not be found.
	 */
	public TreeBranch<E> FindData(E Data)
	{
		if(this.Data instanceof Comparable)
		{
			if(this.Data != null)
				if(this.Data.equals(Data))
					return this;
		
			TreeBranch<E> TTemp = new TreeBranch<E>();
		
			for(int i = 0;i < this.branches.length;i++)
			{
				TTemp = this.branches[i].FindData(Data);
				
				if(TTemp != null)
					return TTemp;
			}
		}
		else
		{
			if(this.Data != null)
				if(this.Data == Data)
					return this;
		
			TreeBranch<E> TTemp = new TreeBranch<E>();
		
			for(int i = 0;i < this.branches.length;i++)
			{
				TTemp = this.branches[i].FindData(Data);
				
				if(TTemp != null)
					return TTemp;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the number of branches in the this TreeBranch.
	 * @return <b>int</b>
	 * Returns the number of branches in this TreeBranch as an integer.
	 */
	public int GetNumberOfSprouts()
	{
		return this.branches.length;
	}
	
	/**
	 * Returns the number of branches in all TreeDiagrams of this level or lower.
	 * @return <b>int</b>
	 * Returns the number of branches in all TreeDiagrams of this level or lower as an integer.
	 */
	public int GetTotalNumberOfSprouts()
	{
		int ITemp = this.branches.length;
		
		for(int i = 0;i < this.branches.length;i++)
			ITemp += this.branches[i].GetTotalNumberOfSprouts();
		
		return ITemp;
	}
	
	/**
	 * Determines the maximum depth of this Tree Diagram.
	 * @return <b>int</b>
	 * Returns the maximum depth of all branches of this Tree Diagram.
	 * A Depth of 0 indicates that this Tree Diagram contains no branches.
	 */
	public int GetMaxDepth()
	{
		int ITemp = 0;
		
		if(this.branches.length == 0)
			return 0;
		
		for(int i = 0;i < this.branches.length;i++)
			if(this.branches[i].GetMaxDepth() > ITemp)
				ITemp = this.branches[i].GetMaxDepth();
		
		return ITemp + 1;
	}
	
	/**
	 * Determines the maximum depth of branch [Branch] of this Tree Diagram.
	 * Indexs are 1 based.
	 * @param Branch
	 * The branch to scan for maximum depth.
	 * @return <b>int</b>
	 * Returns the maximum depth of this Tree Diagram.
	 * A Depth of -1 indicates that this Tree Diagram branch does not exist.
	 */
	public int GetMaxDepthOfBranch(int Branch)
	{
		if(Branch > this.branches.length || Branch < 1)
			return -1;
		
		return this.branches[Branch-1].GetMaxDepth();
	}
	
	/**
	 * Handles the toString implicit calls.
	 * @return <b>String</b>
	 * Returns the name of the TreeBranch.
	 */
	public String toString()
	{
		if(this.Name == null)
			if(this.Data == null)
				return "null";
			else
				return this.Data.toString();
		
		return this.Name; 
	}
	
	/**
	 * Contains pointers to additional TreeDiagrams.
	 */
	protected TreeBranch<E>[] branches;
	
	/**
	 * Contains a pointer to the parent branch.
	 */
	protected TreeBranch<E> Stem;
	
	/**
	 * Conatains a pointer to data associated with this Branch. 
	 */
	protected E Data;
	
	/**
	 * Contains a name associated with this TreeBranch.
	 */
	protected String Name;
}
