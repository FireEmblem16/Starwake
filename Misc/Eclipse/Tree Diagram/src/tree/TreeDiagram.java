///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// Omacron Storm //////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// tree  Package //////////////////////////////////////
/////////////////////////////////// TreeDiagram ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////// Contains functions for handling and managing TreeDiagrams. ////////////// 
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
package tree;

/**
 * Manages a TreeBranch and provides an interface to it.
 */
public class TreeDiagram<E>
{
	/**
	 * Creates a new TreeDiagram and begins a new TreeBranch.
	 * @return <b>TreeDiagram*</b>
	 * Returns a pointer to the new TreeDiagram.
	 */
	public TreeDiagram()
	{
		this.Trunk = new TreeBranch<E>(null,"Trunk");
		this.Branch = this.Trunk;
		this.Seed = this.Trunk;
		
		return;
	}
	
	/**
	 * Creates a new TreeDiagram and begins a new TreeBranch.
	 * @param TreeName
	 * The name to be given to the trunk of the tree. The default is Trunk.
	 * @return <b>TreeDiagram*</b>
	 * Returns a pointer to the new TreeDiagram.
	 */
	public TreeDiagram(String TreeName)
	{
		this.Trunk = new TreeBranch(null,TreeName);
		this.Branch = this.Trunk;
		this.Seed = this.Trunk;
		
		return;
	}
	
	/**
	 * Enters a branch of the current TreeBranch.
	 * Indexes are 1 based.
	 * @param branch
	 * The branch to enter.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be entered.
	 * Returns false if the branch does not exist or could not be entered.
	 */
	public boolean EnterBranch(int branch)
	{
		TreeBranch TTemp = this.Branch.GetBranch(branch);
		
		if(TTemp == null)
			return false;
		
		this.Seed = this.Branch;
		this.Branch = TTemp;
		
		return true;
	}
	
	/**
	 * Enters a branch of the Tree if it is at or below the current sub level.
	 * @param Branch
	 * The branch to enter.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be entered.
	 * Returns false if the branch does not exist or could not be entered.
	 */
	public boolean EnterBranch(String Branch)
	{
		TreeBranch TTemp = this.Trunk.FindBranch(Branch);
		
		if(TTemp == null)
			return false;
		
		this.Seed = this.Branch;
		this.Branch = TTemp;
		
		return true;
	}
	
	/**
	 * Enters a branch of the Tree if it is at the current sub level.
	 * @param Branch
	 * The branch to enter.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be entered.
	 * Returns false if the branch does not exist or could not be entered.
	 */
	public boolean EnterBranchShallow(String Branch)
	{
		TreeBranch TTemp2 = this.Branch;
		
		if(TTemp2 == null)
			return false;
		
		TreeBranch[] TTempAry = TTemp2.GetBranches();
		TreeBranch TTemp = null;
		
		for(int i = 0;i < TTempAry.length;i++)
			if(TTempAry[i].GetName().equals(Branch))
			{
				TTemp = TTempAry[i];
				break;
			}
		
		if(TTemp == null)
			return false;
		
		this.Seed = this.Branch;
		this.Branch = TTemp;
		
		return true;
	}
	
	/**
	 * Enters a branch of the Tree.
	 * @param Branch
	 * The branch to enter.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be entered.
	 * Returns false if the branch does not exist or could not be entered.
	 */
	public boolean EnterBranch(TreeBranch<E> Branch)
	{
		TreeBranch TTemp = this.Trunk.FindBranch(Branch);
		
		if(TTemp == null)
			return false;
		
		this.Seed = this.Branch;
		this.Branch = TTemp;
		
		return true;
	}
	
	/**
	 * Enters a branch of the Tree.
	 * @param Data
	 * The data inside the branch to enter.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be entered.
	 * Returns false if the branch does not exist or could not be entered.
	 */
	public boolean EnterBranch(E Data)
	{
		TreeBranch TTemp = this.Trunk.FindData(Data);
		
		if(TTemp == null)
			return false;
		
		this.Seed = this.Branch;
		this.Branch = TTemp;
		
		return true;
	}
	
	/**
	 * Returns the current branch to the trunk.
	 * @return <b>void</b>
	 */
	public void ReturnToTrunk()
	{
		this.Seed = this.Branch;
		this.Branch = this.Trunk;
		
		return;
	}
	
	/**
	 * Returns to the stem before this branch.
	 * @return <b>boolean</b>
	 * Returns false if the current branch was the trunk.
	 * Otherwise returns true.
	 */
	public boolean ExitBranch()
	{
		TreeBranch TTemp = this.Branch.GetStem();
		
		if(TTemp == null)
			return false;
		
		this.Seed = this.Branch;
		this.Branch = TTemp;
		
		return true;
	}
	
	/**
	 * Returns to the previous branch visited.
	 * @return <b>boolean</b>
	 * Returns false if the seed is null.
	 */
	public boolean ReturnToSeed()
	{
		if(this.Seed == null)
			return false;
		
		TreeBranch TTemp = this.Branch;
		this.Branch = this.Seed;
		this.Seed = TTemp;
		
		return true;
	}
	
	/**
	 * Gets the trunk of this TreeDiagram.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to the trunk of this TreeDiagram.
	 */
	public TreeBranch<E> GetTrunk()
	{
		return this.Trunk;
	}
	
	/**
	 * Gets the current branch of this TreeDiagram.
	 * @return <b>TreeBranch*</b>
	 * Returns a pointer to the current branch of this TreeDiagram.
	 */
	public TreeBranch<E> GetCurrentBranch()
	{
		return this.Branch;
	}
	
	/**
	 * Returns a pointer to the branch with name [name].
	 * @param name
	 * The name of the branch to return.
	 * @return <b>TreeBranch**</b>
	 * Returns the TreeBranch with name [name].
	 * If not found returns null instead.
	 */
	public TreeBranch<E> GetBranch(String name)
	{
		return this.Trunk.FindBranch(name);
	}
	
	/**
	 * Returns a pointer to the branch with data [Data].
	 * @param Data
	 * The data in the branch to return.
	 * @return <b>TreeBranch**</b>
	 * Returns the TreeBranch with data [Data].
	 * If not found returns null instead.
	 */
	public TreeBranch<E> GetBranchWithData(E Data)
	{
		return this.Trunk.FindData(Data);
	}
	
	/**
	 * Searches if the TreeDiagram has Data in it.
	 * @param Data
	 * The data to search for.
	 * @return <b>boolean</b>
	 * Returns true iff Data is found. Searches with compareTo if
	 * Data is a comparable object. Otherwise searches for identical objects.
	 */
	public boolean Contains(E Data)
	{
		if(this.Trunk.FindData(Data) != null)
			return true;
		
		return false;
	}
	
	/**
	 * Sets the data associated with the current branch.
	 * @param data
	 * The data to be assigned to the current branch.
	 * @return <b>void</b>
	 */
	public void SetData(E data)
	{
		this.Branch.SetData(data);
		
		return;
	}
	
	/**
	 * Gets the data associated with the current branch.
	 * @return <b>Object*</b>
	 * Returns the object pointer to the data of the current branch.
	 */
	public E GetData()
	{	
		return this.Branch.GetData();
	}
	
	/**
	 * Sets the name associated with the current branch.
	 * @param data
	 * The name to be assigned to the current branch.
	 * @return <b>boolean</b>
	 * Returns true if the name change was successful.
	 * Returns false if the name is already taken by a different TreeBranch.
	 */
	public boolean SetName(String data)
	{
		TreeBranch TTemp = this.Trunk.FindBranch(data);
		
		if(TTemp == null)
		{
			this.Branch.SetName(data);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the name associated with the current branch.
	 * @return <b>Object*</b>
	 * Returns the name of the current branch.
	 */
	public String GetName()
	{	
		return this.Branch.GetName();
	}
	
	/**
	 * Finds the first available generic name.
	 * @return <b>String</b>
	 * Returns a name in the form Sprout [i] where i is the next available
	 * generic sprout name.
	 */
	public String GetNextGenericName()
	{
		String STemp = null;
		int ITemp = this.Trunk.GetTotalNumberOfSprouts() + 1;
		
		for(int i = 1;i < ITemp;i++)
		{
			TreeBranch TTemp = this.Trunk.FindBranch("Sprout " + i);
			
			if(TTemp == null)
			{
				STemp = "Sprout " + i;
				i = ITemp - 1;
			}
		}
		
		if(STemp == null)
			STemp = "Sprout " + Integer.toString(ITemp);
		
		return STemp;
	}
	
	/**
	 * Adds a new branch to the current branch with a default name.
	 * @return <b>void</b>
	 */
	public void AddBranch()
	{
		AddBranch(this.GetNextGenericName());
		
		return;
	}
	
	/**
	 * Adds a new branch to the current branch.
	 * @param Name
	 * The name that the branch should have.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be made with the given name.
	 * Returns false otherwise. If false the name was given a default otherwise.
	 */
	public boolean AddBranch(String Name)
	{
		TreeBranch TTemp = this.Trunk.FindBranch(Name);
		
		if(TTemp == null)
		{
			this.Branch.AddBranch(new TreeBranch(this.Branch,Name));
			return true;
		}
		
		this.AddBranch();
		
		return false;
	}
	
	/**
	 * Adds a new branch to the current branch without a name.
	 * @return <b>void</b>
	 */
	public void AddBranchFast()
	{
		this.Branch.AddBranch(new TreeBranch(this.Branch));
		
		return;
	}
	
	/**
	 * Adds a new branch to the current branch with a name without checking for duplicates.
	 * @param name
	 * The name for the branch.
	 * @return <b>void</b>
	 */
	public void AddBranchFast(String name)
	{
		this.Branch.AddBranch(new TreeBranch(this.Branch,name));
		
		return;
	}
	
	/**
	 * Adds a new branch to the current branch with a default name.
	 * @return <b>void</b>
	 */
	public void AddBranchAndEnter()
	{
		AddBranchAndEnter(this.GetNextGenericName());
		
		return;
	}
	
	/**
	 * Adds a new branch to the current branch.
	 * @param Name
	 * The name that the branch should have.
	 * @return <b>boolean</b>
	 * Returns true if the branch could be made with the given name.
	 * Returns false otherwise. If false the name was given a default otherwise.
	 */
	public boolean AddBranchAndEnter(String Name)
	{
		TreeBranch TTemp = this.Trunk.FindBranch(Name);
		
		if(TTemp == null)
		{
			this.Branch.AddBranch(new TreeBranch(this.Branch,Name));
			EnterBranch(Name);
			
			return true;
		}
		
		this.AddBranch();
		
		return false;
	}
	
	/**
	 * Adds a new branch to the current branch without a name.
	 * @return <b>void</b>
	 */
	public void AddBranchFastAndEnter()
	{
		TreeBranch<E> TTemp = new TreeBranch(this.Branch);
		this.Branch.AddBranch(TTemp);
		EnterBranch(TTemp);
		
		return;
	}
	
	/**
	 * Adds a new branch to the current branch with a name without checking for duplicates.
	 * @param name
	 * The name for the branch.
	 * @return <b>void</b>
	 */
	public void AddBranchFastAndEnter(String name)
	{
		TreeBranch<E> TTemp = new TreeBranch(this.Branch,name);
		this.Branch.AddBranch(TTemp);
		EnterBranch(TTemp);
		
		return;
	}
	
	/**
	 * Removes a branch (and all sub-branches) from the current branch.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to remove.
	 * @return <b>boolean</b>
	 * Returns true if the branch was successfully removed and otherwise returns false.
	 */
	public boolean RemoveBranch(int Branch)
	{
		return this.Branch.RemoveBranch(Branch);
	}
	
	/**
	 * Removes a branch (and all sub-branches) from the Tree.
	 * Exits the current branch if the branch to be removed is the current branch.
	 * If the trunk is removed it is replaced with an unnamed TreeBranch.
	 * @param Branch
	 * The branch to remove.
	 * @return <b>boolean</b>
	 * Returns true if the branch was successfully removed and otherwise returns false.
	 */
	public boolean RemoveBranch(String Branch)
	{
		TreeBranch<E> TTemp = this.Trunk.FindBranch(Branch);
		
		if(TTemp == null)
			return false;
		
		if(TTemp == this.Branch)
			ExitBranch();
		
		if(TTemp == this.Trunk)
		{
			this.Trunk = new TreeBranch<E>();
			this.Branch = null;
			this.Seed = null;
		}
		else
			TTemp.GetStem().RemoveBranch(TTemp);

		return true;
	}
	
	/**
	 * Removes the current branch and every node below it from the tree.
	 * If the trunk is removed a new one will be created and no name will
	 * be issued to the new trunk nor will any data be retained.
	 * @return <b>boolean</b>
	 * Returns false if the current branch could not be found or removed.
	 */
	public boolean RemoveCurrent()
	{
		TreeBranch<E> TTemp = Branch;
		
		if(ExitBranch())
		{
			TreeBranch<E>[] TATemp = GetBranches();
			
			for(int i = 0;i < TATemp.length;i++)
				if(TATemp[i] == TTemp)
				{
					RemoveBranch(i+1);
					Seed = null;
					
					return true;
				}
		}
		else if(TTemp == Trunk)
		{
			Trunk = new TreeBranch<E>();
			Seed = Trunk;
			Branch = Seed;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Inserts a new Branch at the current level in the hierarchy and moves the previously
	 * current branch down one node. All data in the moved branches is preserved.
	 * @return <b>void</b>
	 */
	public void InsertBranch()
	{
		TreeBranch TTemp = this.GetCurrentBranch();
		this.Branch = new TreeBranch<E>(TTemp.GetStem(),this.GetNextGenericName());
		this.Branch.AddBranch(TTemp);
		
		if(TTemp.GetStem() == null)
			this.Trunk = this.Branch;
		else
			this.Branch.GetStem().EditBranch(this.Branch.GetStem().GetBranchIndex(TTemp.Name),this.Branch);
		
		TTemp.SetStem(this.Branch);
		
		return;
	}
	
	/**
	 * Inserts a new Branch at the current level in the hierarchy and moves the previously
	 * current branch down one node. All data in the moved branches is preserved. The branch
	 * will have no default name giving to it.
	 * @return <b>void</b>
	 */
	public void InsertBranchFast()
	{
		TreeBranch TTemp = this.GetCurrentBranch();
		this.Branch = new TreeBranch<E>(TTemp.GetStem());
		this.Branch.AddBranch(TTemp);
		
		if(TTemp.GetStem() == null)
			this.Trunk = this.Branch;
		else
			this.Branch.GetStem().EditBranch(this.Branch.GetStem().GetBranchIndex(TTemp.Name),this.Branch);
		
		TTemp.SetStem(this.Branch);
		
		return;
	}
	
	/**
	 * Inserts a new Branch at the current level in the hierarchy and moves the previously
	 * current branch down one node. All data in the moved branches is preserved.
	 * @param <b>Branch</b>
	 * The TreeBranch to add. If you are using unique naming then do not create a name for
	 * Branch in it's constructor and instead use another version of InsertBranch.
	 * @return <b>void</b>
	 */
	public void InsertBranch(TreeBranch<E> Branch)
	{
		TreeBranch<E> TTemp = this.GetCurrentBranch();
		this.Branch = Branch;
		this.Branch.SetName(this.GetNextGenericName());
		this.Branch.SetStem(TTemp.GetStem());
		this.Branch.AddBranch(TTemp);
		
		if(TTemp.GetStem() == null)
			this.Trunk = this.Branch;
		else
			this.Branch.GetStem().EditBranch(this.Branch.GetStem().GetBranchIndex(TTemp.Name),this.Branch);
		
		TTemp.SetStem(this.Branch);
		
		return;
	}
	
	/**
	 * Inserts a new Branch at the current level in the hierarchy and moves the previously
	 * current branch down one node. All data in the moved branches is preserved. The new
	 * Branch will have no default name given to it.
	 * @param <b>Branch</b>
	 * The TreeBranch to add. If you are using unique naming then do not create a name for
	 * Branch in it's constructor and instead use another version of InsertBranch.
	 * @return <b>void</b>
	 */
	public void InsertBranchFast(TreeBranch<E> Branch)
	{
		TreeBranch<E> TTemp = this.GetCurrentBranch();
		this.Branch = Branch;
		this.Branch.SetStem(TTemp.GetStem());
		this.Branch.AddBranch(TTemp);
		
		if(TTemp.GetStem() == null)
			this.Trunk = this.Branch;
		else
			this.Branch.GetStem().EditBranch(this.Branch.GetStem().GetBranchIndex(TTemp.Name),this.Branch);
		
		TTemp.SetStem(this.Branch);
		
		return;
	}
	
	/**
	 * Inserts a new Branch at the current level in the hierarchy and moves the previously
	 * current branch down one node. All data in the moved branches is preserved.
	 * @param <b>Branch</b>
	 * The TreeBranch to add.
	 * @param <b>Name</b>
	 * The name to give the TreeBranch. Will check for name uniqueness.
	 * @return <b>void</b>
	 */
	public void InsertBranch(String Name)
	{
		TreeBranch<E> TTemp = this.GetCurrentBranch();
		this.Branch = new TreeBranch<E>(TTemp.GetStem());
		this.Branch.AddBranch(TTemp);
		
		if(this.Trunk.FindBranch(Name) == null)
			this.Branch.SetName(Name);
		
		if(TTemp.GetStem() == null)
			this.Trunk = this.Branch;
		else
			this.Branch.GetStem().EditBranch(this.Branch.GetStem().GetBranchIndex(TTemp.Name),this.Branch);
		
		TTemp.SetStem(this.Branch);
		
		return;
	}
	
	/**
	 * Inserts a new Branch at the current level in the hierarchy and moves the previously
	 * current branch down one node. All data in the moved branches is preserved.
	 * @param <b>Branch</b>
	 * The TreeBranch to add.
	 * @param <b>Name</b>
	 * The name to give the TreeBranch. Will check for name uniqueness.
	 * @return <b>void</b>
	 */
	public void InsertBranch(TreeBranch<E> Branch, String Name)
	{
		TreeBranch<E> TTemp = this.GetCurrentBranch();
		this.Branch = Branch;
		this.Branch.SetStem(TTemp.GetStem());
		this.Branch.AddBranch(TTemp);
		
		if(this.Trunk.FindBranch(Name) == null)
			this.Branch.SetName(Name);
		
		if(TTemp.GetStem() == null)
			this.Trunk = this.Branch;
		else
			this.Branch.GetStem().EditBranch(this.Branch.GetStem().GetBranchIndex(TTemp.Name),this.Branch);
		
		TTemp.SetStem(this.Branch);
		
		return;
	}
	
	/**
	 * Changes a branch of the current branch.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to edit.
	 * @param Sprout
	 * The new TreeBranch<E> that should be inserted.
	 * @return <b>boolean</b>
	 * Returns true if the branch was successfully edited and otherwise returns false.
	 */
	public boolean EditBranch(int Branch, TreeBranch<E> Sprout)
	{
		return this.Branch.EditBranch(Branch,Sprout);
	}
	
	/**
	 * Returns a branch of the current branch.
	 * Indexes are 1 based.
	 * @param Branch
	 * The branch to return.
	 * @return <b>TreeBranch<E>*</b>
	 * Returns a pointer to a branch of the current branch.
	 * Returns null if branch [Branch] does not exist.
	 */
	public TreeBranch<E> GetBranch(int Branch)
	{
		return this.Branch.GetBranch(Branch);
	}
	
	/**
	 * Obtains the array of branches for the current branch.
	 * @return <b>TreeBranch<E>**</b>
	 * Returns a pointer to an array of pointers to branches of the current branch.
	 */
	public TreeBranch<E>[] GetBranches()
	{
		return this.Branch.GetBranches();
	}
	
	/**
	 * Returns the stem of the current branch.
	 * @return <b>TreeBranch<E>*</b>
	 * Returns a pointer to a stem of the current branch.
	 * Returns null if this Tree Diagram is the trunk.
	 */
	public TreeBranch<E> GetStem()
	{
		return this.Branch.GetStem();
	}
	
	/**
	 * Sets the current branch to the branch specified by [branch]
	 * @param branch
	 * The name of the branch to goto.
	 * @return <b>boolean</b>
	 * Returns true if the branch was entered.
	 * Returns false if the branch could not be entered or could not be found.
	 */
	public boolean GoToBranch(String branch)
	{
		TreeBranch<E> TTemp = this.Trunk.FindBranch(branch);
		
		if(TTemp != null)
		{
			this.Seed = this.Branch;
			this.Branch = TTemp;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the number of branches in the the current branch.
	 * @return <b>int</b>
	 * Returns the number of branches in the current branch as an integer.
	 */
	public int GetNumberOfSprouts()
	{
		return this.Branch.GetNumberOfSprouts();
	}
	
	/**
	 * Returns the number of branches in all TreeDiagrams of the current branch level or lower.
	 * @return <b>int</b>
	 * Returns the number of branches in all TreeDiagrams of the current branch level or lower as an integer.
	 */
	public int GetTotalNumberOfSprouts()
	{
		return this.Branch.GetTotalNumberOfSprouts();
	}
	
	/**
	 * Determines the maximum depth of the current branch.
	 * @return <b>int</b>
	 * Returns the maximum depth of all branches of the current branch.
	 * A Depth of 0 indicates that the current branch contains no branches.
	 */
	public int GetMaxDepth()
	{
		return this.Branch.GetMaxDepth();
	}
	
	/**
	 * Determines the maximum depth of branch [Branch] of the current branch.
	 * Indexs are 1 based.
	 * @param Branch
	 * The branch to scan for maximum depth.
	 * @return <b>int</b>
	 * Returns the maximum depth of the current branch.
	 * A Depth of -1 indicates that this Tree Diagram branch does not exist.
	 */
	public int GetMaxDepthOfBranch(int Branch)
	{
		return this.Branch.GetMaxDepthOfBranch(Branch);
	}
	
	/**
	 * Handles implicit and explicit calls to the toString function.
	 * @return <b>String</b>
	 * Returns a string containing the name of the current branch.
	 */
	public String toString()
	{
		return this.Trunk.GetName();
	}
	
	/**
	 * Determines the current depth of the TreeDiagram.
	 * @return <b>int</b>
	 * Returns the current depth of the TreeDiagram.
	 * A depth of 1 indicates that the current branch is the trunk.
	 */
	public int Depth()
	{
		int ITemp = 1;
		
		TreeBranch<E>[] Temp = new TreeBranch[2];
		Temp[0] = this.Branch;
		Temp[1] = this.Seed;
		
		for(;this.ExitBranch();ITemp++);
		
		this.Branch = Temp[0];
		this.Seed = Temp[1];
		
		return ITemp;
	}
	
	/**
	 * Contains a pointer to the topmost TreeBranch<E>.
	 */
	protected TreeBranch<E> Trunk;
	
	/**
	 * Conatains a pointer to the branch that TreeDiagram is currently on.
	 */
	protected TreeBranch<E> Branch;
	
	/**
	 * Contains a pointer to the last branch entered.
	 */
	protected TreeBranch<E> Seed;
}