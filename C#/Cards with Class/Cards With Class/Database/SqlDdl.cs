using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Database
{
    /// <summary>
    /// This class is for SQL Data Definition Tasks. Some main tasks are create/change/delete database
    /// and tables. This class will be used for the task of the administration. Note that, we only
    /// support for creating database and tables that are compatable with MySQL.
    /// </summary>
    public interface SqlDdl
    {
        /// <summary>
        /// This method is used for creating a database according to the statement
        /// </summary>
        /// <param name="statement"> The Statement follows the systax of MySQL</param>
        /// <returns>True if create database successfully; otherwise return false/returns>
        bool createDatabase(String statement);

        /// <summary>
        /// This method is used for creating the table of the database according to the statement
        /// </summary>
        /// <param name="statement">The Statement follows the systax of MySQL</param>
        /// <returns>True if create database successfully; otherwise return false</returns>
        bool createTable(String statement);


        /// <summary>
        /// This method is used for deleting a database according to the statement
        /// </summary>
        /// <param name="statement"> The Statement follows the systax of MySQL</param>
        /// <returns>True if delete database successfully; otherwise return false/returns>
        bool deleteDatabase(String statement);


        /// <summary>
        /// This method is used for deleting the table of the database according to the statement
        /// </summary>
        /// <param name="statement">The Statement follows the systax of MySQL</param>
        /// <returns>True if delete database successfully; otherwise return false</returns>
        bool deleteTable(String statement);

        /// <summary>
        /// This method is used for altering the table of the database according to the statement
        /// </summary>
        /// <param name="statement">The Statement follows the systax of MySQL</param>
        /// <returns>True if altering table successfully; otherwise return false</returns>
        bool renameTable(String statement);


    }
}
