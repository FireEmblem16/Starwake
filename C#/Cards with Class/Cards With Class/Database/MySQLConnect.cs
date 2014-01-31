using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;
using System.Data;

namespace Database
{
    class MySQLConnect
    {
        private MySqlConnection m_Connection;
        private string m_Server;
        private string m_Database;
        private string m_UserName;
        private string m_Password;

        //Construction 
        public MySQLConnect()
        {
            //Initializing the members
            m_Server = "mysql.cs.iastate.edu";
            m_Database = "db30927";
            m_UserName = "u30927";
            m_Password = "x3em6sgVq";
            //Creating a connection string
            string connectionString;
            connectionString = "SERVER=" + m_Server + ";" + "DATABASE=" +
            m_Database + ";" + "UID=" + m_UserName + ";" + "PASSWORD=" + m_Password + ";";
            //Creating a connection object
            m_Connection = new MySqlConnection(connectionString);
        }

        /** This function is used for openning the connection to database **/
        private bool OpenConnection()
        {
            try
            {
                m_Connection.Open();
                return true;
            }
            catch (MySqlException ex)
            {
                //Handling error when connecting to server
                switch (ex.Number)
                {
                    case 0:
                        System.Windows.Forms.MessageBox.Show("Cannot connect to server.");
                        break;

                    case 1045:
                        System.Windows.Forms.MessageBox.Show("Invalid username/password.");
                        break;
                }
                return false;
            }
        }

        /** This method is used for closing connection to database **/
        private bool closeConnection()
        {
            try
            {
                m_Connection.Close();
                return true;
            }
            catch (MySqlException ex)
            {
                System.Windows.Forms.MessageBox.Show(ex.Message);
                return false;
            }
        }

        /////////////////////////////////////////////////////////////
        // The following methods are for DML (insert, update, ..) ///
        /////////////////////////////////////////////////////////////

        /** This method is used for getting the data set containing all information about player **/
        public DataSet getDataSet()
        {
            string query = "SELECT p.Name, pg.Score, pg.GameType, g.StartTime, g.EndTime FROM Player as p, PlayGame as pg, " +
                      "Game as g where p.PlayerId = pg.PlayerId and pg.GameId = g.GameId";

              //Open connection
              if (this.OpenConnection() == true)
              {
                  //Create Command
                  MySqlCommand cmd = new MySqlCommand(query, m_Connection);
                  MySqlDataAdapter adapter = new MySqlDataAdapter(cmd);
                  DataSet dataSet = new DataSet();
                  adapter.Fill(dataSet);
                  //close Connection
                  this.closeConnection();
                  return dataSet;
              }
              return null;
        }

        //Select statement
        public List<string>[] Select()
        {
            string query = "SELECT p.Name, pg.Score, pg.GameType, g.StartTime, g.EndTime FROM Player as p, PlayGame as pg, "+
            "Game as g where p.PlayerId = pg.PlayerId and pg.GameId = g.GameId";

            //Create a list to store the result
            List<string>[] list = new List<string>[5];
            list[0] = new List<string>();
            list[1] = new List<string>();
            list[2] = new List<string>();
            list[3] = new List<string>();
            list[4] = new List<string>();

            //Open connection
            if (this.OpenConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, m_Connection);
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();

                //Read the data and store them in the list
                while (dataReader.Read())
                {
                    list[0].Add(dataReader["Name"] + "");
                    list[1].Add(dataReader["Score"] + "");
                    list[2].Add(dataReader["GameType"] + "");
                    list[3].Add(dataReader["StartTime"] + "");
                    list[4].Add(dataReader["EndTime"] + "");
                }

                //close Data Reader
                dataReader.Close();

                //close Connection
                this.closeConnection();

                //return list to be displayed
                return list;
            }
            else
            {
                return list;
            }
        }

         //Insert statement -- will be updated
        public void Insert(string insertStatement)
        {
           // string query = "INSERT INTO ...";

            //open connection
            if (this.OpenConnection() == true)
            {
                //create command and assign the query and connection from the constructor
                MySqlCommand command = new MySqlCommand(insertStatement, m_Connection);

                //Execute command
                command.ExecuteNonQuery();

                //close connection
                this.closeConnection();
            }
        }

        //Update statement -- will be updated
        public void Update()
        {
        }

        //Delete statement -- will be updated
        public void Delete()
        {
        }
    }


}
