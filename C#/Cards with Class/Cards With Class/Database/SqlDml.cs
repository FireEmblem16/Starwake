using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Database.Record;

namespace Database
{
    /// <summary>
    /// This class is for SQL Data Manupulation Tasks. Some main tasks are add/update/get/remove data from 
    /// database. For example, adding new user (player) to the system; updating the playing history of a 
    /// user.
    /// </summary>
    public interface SqlDml
    {
        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        bool insertIntoPlayer();


        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        bool insertIntoGame();


        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        bool insertIntoPlayGame();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        bool deleteFromPlayer();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        bool deleteFromGame();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        bool deleteFromPlayGame();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        List<Player> selectFromPlayer();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        List<Game> selectFromGame();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        List<PlayGame> selectFromPlayGame();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        int Count();


        /// <summary>
        /// 
        /// </summary>
        void Backup();

        /// <summary>
        /// 
        /// </summary>
        void Restore();
    

        // There will be several utilities here ... to be continued

    }
}
