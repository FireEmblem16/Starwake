using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Database.Record;
using MySql.Data.MySqlClient;

namespace Database
{
    public class DatabaseManipulation : SqlDml
    {
        private DatabaseAuthorization m_MySQLConnection = null;

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="mySQLConnection"></param>
        public DatabaseManipulation(DatabaseAuthorization mySQLConnection)
        {
            m_MySQLConnection = mySQLConnection;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public bool insertIntoPlayer()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public bool insertIntoGame()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public bool insertIntoPlayGame()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public bool deleteFromPlayer()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public bool deleteFromGame()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public bool deleteFromPlayGame()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public List<Player> selectFromPlayer()
        {
            string query = "SELECT p.PlayerId, p.Name, p.DOB, p.Address, p.Email FROM Player as p";

            //Create a list to store the result
            List<string>[] list = new List<string>[5];
            list[0] = new List<string>();
            list[1] = new List<string>();
            list[2] = new List<string>();
            list[3] = new List<string>();
            list[4] = new List<string>();

            //Open connection
            if (this.m_MySQLConnection.openConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, m_MySQLConnection.getConnection());
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();

                //number of instance 
                int numRecord = 0;
                //Read the data and store them in the list
                while (dataReader.Read())
                {
                    list[0].Add(dataReader["PlayerId"] + "");
                    list[1].Add(dataReader["Name"] + "");
                    list[2].Add(dataReader["DOB"] + "");
                    list[3].Add(dataReader["Address"] + "");
                    list[4].Add(dataReader["Email"] + "");
                    numRecord++;
                }

                //close Data Reader
                dataReader.Close();

                //close Connection
                m_MySQLConnection.closeConnection();

                //return list to be displayed
                List<Player> playerList = new List<Player>();
                for (int i = 0; i < numRecord; i++)
                {
                    playerList.Add(new Player(list[0][i], list[1][i], list[2][i], list[3][i], list[4][i]));
                }
                return playerList;
               
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public List<Game> selectFromGame()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public List<PlayGame> selectFromPlayGame()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public int Count()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        public void Backup()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 
        /// </summary>
        public void Restore()
        {
            throw new NotImplementedException();
        }
    }
}
