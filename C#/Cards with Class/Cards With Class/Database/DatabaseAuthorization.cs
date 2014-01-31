using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;


namespace Database
{
    public class DatabaseAuthorization : SqlDcl
    {
        private string m_ServerName;
        private string m_DatabaseName;
        private string m_UserName;
        private string m_Password;
        private MySqlConnection m_Connection;


        /// <summary>
        /// Constructor
        /// </summary>
        public DatabaseAuthorization()
        {
            //Initializing some member variables
            m_ServerName = "mysql.cs.iastate.edu";
            m_DatabaseName = "db30927";
            m_UserName = "u30927";
            m_Password = "x3em6sgVq";
            m_Connection = null;
        }


        /// <summary>
        /// Get and set Server property of class
        /// </summary>
        public string Server
        {
            get
            {
                return m_ServerName;
            }
            set
            {
                m_ServerName = value;
            }
        }

        /// <summary>
        /// Get and set Server property of class
        /// </summary>
        public string Database
        {
            get
            {
                return m_DatabaseName;
            }
            set
            {
                m_DatabaseName = value;
            }
        }

        /// <summary>
        /// Get and set Server property of class
        /// </summary>
        public string UserName
        {
            get
            {
                return m_UserName;
            }
            set
            {
                m_UserName = value;
            }
        }

        /// <summary>
        /// Get and set Server property of class
        /// </summary>
        public string Password
        {
            get
            {
                return m_Password;
            }
            set
            {
                m_Password = value;
            }
        }

        /// <summary>
        /// Implement opening connection method
        /// </summary>
        /// <returns></returns>
        public bool openConnection()
        {
            if (m_Connection == null)
            {
                string connectionString;
                connectionString = "SERVER=" + m_ServerName + ";" + "DATABASE=" +
                m_DatabaseName + ";" + "UID=" + m_UserName + ";" + "PASSWORD=" + m_Password + ";";
                //Creating a connection object
                m_Connection = new MySqlConnection(connectionString);
            }

            //Openning the connection to the database server.
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
                        Console.WriteLine("Cannot connect to server.");
                        break;

                    case 1045:
                        Console.WriteLine("Invalid username/password.");
                        break;
                }
                return false;
            }

        }

        /// <summary>
        /// Implemeting the close connection method.
        /// </summary>
        /// <returns></returns>
        public bool closeConnection()
        {
            try
            {
                m_Connection.Close();
                return true;
            }
            catch (MySqlException ex)
            {
                Console.WriteLine(ex.Message);
                return false;
            }
        }

        /// <summary>
        /// Return the connection of to the database 
        /// </summary>
        /// <returns></returns>
        public MySqlConnection getConnection()
        {
            return m_Connection;
        }
    }
}
