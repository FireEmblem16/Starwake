using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Database.Record;

namespace Database
{
    /// <summary>
    /// Database DLL Class. Tutorial 
    /// http://www.c-sharpcorner.com/UploadFile/mahesh/dll12222005064058AM/dll.aspx
    /// </summary>
    public class DatabaseAccess
    {
        private DatabaseAuthorization m_MyAuthorization;
        private DatabaseManipulation m_DataManipulation;

        /// <summary>
        /// Constructor
        /// </summary>
        public DatabaseAccess()
        {
            m_MyAuthorization = new DatabaseAuthorization();
            m_DataManipulation = new DatabaseManipulation(m_MyAuthorization);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="val1"></param>
        /// <param name="val2"></param>
        /// <returns></returns>
        public long add(long val1, long val2)
        {
            return val1 + val2;
        }

        public List<Player> selectFromPlayer()
        {
            return m_DataManipulation.selectFromPlayer();
        }
    }
}