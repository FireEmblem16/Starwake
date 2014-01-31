using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Database.Record
{
    public class PlayGame
    {
        private string m_PlayerId;
        private string m_GameId;
        private int m_Score;
        private string m_GameType;

        /// <summary>
        /// 
        /// </summary>
        public string PlayerId
        {
            get
            {
                return m_PlayerId;
            }
            set
            {
                m_PlayerId = value;
            }
        }

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
        public int Score
        {
            get
            {
                return m_Score;
            }
            set
            {
                m_Score = value;
            }


        }

        /// <summary>
        /// 
        /// </summary>
        public string GameType
        {
            get
            {
                return m_GameType;
            }
            set
            {
                m_GameType = value;
            }

        }
    }
}
