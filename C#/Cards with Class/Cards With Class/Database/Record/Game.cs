using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Database.Record
{
    public class Game
    {
        private string m_GameId;
        private string m_StartTime;
        private string m_EndTime;
        private int m_NumPlayer;

        /// <summary>
        /// 
        /// </summary>
        public string GameId
        {
            get
            {
                return m_GameId;
            }
            set
            {
                m_GameId = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public string StartTime
        {
            get
            {
                return m_StartTime;
            }
            set
            {
                m_StartTime = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public string EndTime
        {
            get
            {
                return m_EndTime;
            }
            set
            {
                m_EndTime = value;
            }

        }

        /// <summary>
        /// 
        /// </summary>
        public int NumPlayer
        {
            get
            {
                return m_NumPlayer;
            }
            set
            {
                m_NumPlayer = value;
            }

        }


    }
}
