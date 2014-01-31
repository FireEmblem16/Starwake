using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;

namespace Database
{
    /// <summary>
    /// This class provides the implementation for creating database and tables
    /// </summary>
    public class DatabaseCreation : SqlDdl
    {
        private DatabaseAuthorization m_MyConnection;
        /// <summary>
        /// Constructor of the class
        /// </summary>
        /// <param name="mySQLConnection"> </param>
        public DatabaseCreation(DatabaseAuthorization mySQLConnection)
        {
            m_MyConnection = mySQLConnection;
        }


        public void setConnection(DatabaseAuthorization mySQLConnection)
        {
            m_MyConnection = mySQLConnection;
        }

        /// <summary>
        /// Impelemeting the ceate database method from the interface
        /// </summary>
        /// <param name="statement"> The statement for example: CREATE DATABASE MyDatabase</param>
        /// <returns></returns>
        public bool createDatabase(string statement)
        {  
            MySqlCommand cmdDatabase = new MySqlCommand(statement, m_MyConnection.getConnection());
            if (!m_MyConnection.openConnection())
                return false;
            cmdDatabase.ExecuteNonQuery();
            if (!m_MyConnection.closeConnection())
                return false;
            return true;
        }

        /// <summary>
        /// Implementing the create table method of the interface
        /// </summary>
        /// <param name="statement">The statement for example: CREATE TABLE MyTable</param>
        /// <returns></returns>
        public bool createTable(string statement)
        {
            MySqlCommand cmdDatabase = new MySqlCommand(statement, m_MyConnection.getConnection());
            if (!m_MyConnection.openConnection())
                return false;
            cmdDatabase.ExecuteNonQuery();
            if (!m_MyConnection.closeConnection())
                return false;
            return true;
        }

        /// <summary>
        /// Implement the delete database method of the interface
        /// </summary>
        /// <param name="statement">The statement example is DROP TABLE TableName</param>
        /// <returns></returns>
        public bool deleteDatabase(string statement)
        {
            MySqlCommand cmdDatabase = new MySqlCommand(statement, m_MyConnection.getConnection());
            if (!m_MyConnection.openConnection())
                return false;
            cmdDatabase.ExecuteNonQuery();
            if (!m_MyConnection.closeConnection())
                return false;
            return true;
        }

        /// <summary>
        /// Implement the delete table method of the database
        /// </summary>
        /// <param name="statement">The statement for example: DROP DATABASE MyDatabase</param>
        /// <returns></returns>
        public bool deleteTable(string statement)
        {
            MySqlCommand cmdDatabase = new MySqlCommand(statement, m_MyConnection.getConnection());
            if (!m_MyConnection.openConnection())
                return false;
            cmdDatabase.ExecuteNonQuery();
            if (!m_MyConnection.closeConnection())
                return false;
            return true;
        }

        /// <summary>
        /// Implement the delete table method of the database
        /// </summary>
        /// <param name="statement">The statement for example: DROP DATABASE MyDatabase</param>
        /// <returns></returns>
        public bool renameTable(string statement)
        {
            MySqlCommand cmdDatabase = new MySqlCommand(statement, m_MyConnection.getConnection());
            if (!m_MyConnection.openConnection())
                return false;
            cmdDatabase.ExecuteNonQuery();
            if (!m_MyConnection.closeConnection())
                return false;
            return true;
        }

        /// <summary>
        /// This is for creating game card database
        /// </summary>
        public bool createGameCardDatabase()
        {
            MySqlCommand command = null;
            string useDatabaseStr = "use db30927;";
            string dropTableStr = "DROP TABLE IF EXISTS PlayGame, Player, Game;";
            string playerTable = "CREATE TABLE Player(PlayerId VARCHAR(10) NOT NULL," +
                                 "Name  VARCHAR(45), DOB DATE, Address VARCHAR(100), " +
                                 "Email VARCHAR(45), PRIMARY KEY (PlayerId));";
            string gameTable = "CREATE TABLE Game(GameId VARCHAR(10) NOT NULL,"+
	                            "StartTime DATETIME,EndTime DATETIME," +
	                            "NoPlayer INT,PRIMARY KEY (GameId));";
            string playgameTable = "CREATE TABLE PlayGame(PlayerId VARCHAR(10) NOT NULL," +
	                                "GameId VARCHAR(10) NOT NULL,Score INT,GameType VARCHAR(45)," +
	                                 "PRIMARY KEY (PlayerId, GameId),FOREIGN KEY (PlayerId) "+
                                    "REFERENCES Player(PlayerId),FOREIGN KEY (GameId) REFERENCES Game(GameId));";

            if (!m_MyConnection.openConnection())
                return false;
            command = new MySqlCommand(useDatabaseStr, m_MyConnection.getConnection());
            command.ExecuteNonQuery();
            deleteTable(dropTableStr);
            createTable(playerTable);
            createTable(gameTable);
            createTable(playgameTable);

            if (!m_MyConnection.closeConnection())
                return false;
            return true;
        }
    }
}
