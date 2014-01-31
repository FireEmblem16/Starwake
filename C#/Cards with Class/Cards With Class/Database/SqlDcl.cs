using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Database
{
    /// <summary>
    /// This interface is used for the tasks access SQL server. Data Control Language (DCL)
    /// 
    /// </summary>
    public interface SqlDcl
    {
        /// <summary>
        /// Setting and getting the server name that contain the database
        /// </summary>
        string Server
        { get; set; }

        /// <summary>
        /// Setting and getting the the name of the database
        /// </summary>
        string Database
        { get; set; }

        /// <summary>
        /// Setting and getting the user name that has the authorization of the database
        /// </summary>
        string UserName
        { get; set; }

        /// <summary>
        /// Setting and getting the password of a user that has the authorization of the database
        /// </summary>
        string Password
        { get; set; }

        /// <summary>
        /// Open connection to the server that contains database
        /// </summary>
        /// <returns>True if open connection to server OK. If not, return false</returns>
        bool openConnection();


        /// <summary>
        /// Close the connection to the server after finishing tasks
        /// </summary>
        /// <returns>True if close connection OK, otherwise, return false</returns>
        bool closeConnection();





        


    }
   
}
