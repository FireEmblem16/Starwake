using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Database;
using Database.Record;


namespace DatabaseTest
{
    class Program
    {
        static void Main(string[] args)
        {
            DatabaseAccess dataAccessPoint = new DatabaseAccess();
            List<Player> playerList = dataAccessPoint.selectFromPlayer();
            for (int id = 0; id < playerList.Count; id++)
            {
                Console.WriteLine(playerList[id].inToString());
            }

        }
    }
}
