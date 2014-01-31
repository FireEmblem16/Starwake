using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Database.Record
{
    public class Player
    {
        private string m_PlayerId;
        private string m_Name;
        private string m_DOB;
        private string m_Address;
        private string m_Email;

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="playerId"></param>
        /// <param name="name"></param>
        /// <param name="dob"></param>
        /// <param name="address"></param>
        /// <param name="email"></param>
        public Player(string playerId, string name, string dob, string address, string email)
        {
            m_PlayerId = playerId;
            m_Name = name;
            m_DOB = dob;
            m_Address = address;
            m_Email = email;
        }

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
        public string Name
        {
            get
            {
                return m_Name;
            }
            set
            {
                m_Name = value;
            }

        }

        /// <summary>
        /// 
        /// </summary>
        public string DOB
        {
            get
            {
                return m_DOB;
            }
            set
            {
                m_DOB = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public string Address
        {
            get
            {
                return m_Address;
            }
            set
            {
                m_Address = value;
            }

        }

        /// <summary>
        /// 
        /// </summary>
        public string Email
        {
            get
            {
                return m_Email;
            }
            set
            {
                m_Email = value;
            }
        }

        /// <summary>
        /// Convert information into string format
        /// </summary>
        /// <returns></returns>
        public string inToString()
        {
            return (m_PlayerId+"\t"+m_Name+"\t"+m_DOB+"\t"+m_Address+"\t"+m_Email);
        }
    }
}
