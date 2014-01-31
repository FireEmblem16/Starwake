using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Engine.Game.Screens;
using GameMenu.Connection;
using System.Net;


namespace GameMenu
{
   
    public partial class Form1 : Form
    {
        public static String ipName = "192.168.1.3";
        public GetScoreForm scoreForm = null;
        public SignupForm suForm = null;
        public Form1()
        {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Sign in
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click(object sender, EventArgs e)
        {
            NetworkExchange netOut = new NetworkExchange(true);
            List<IPAddress> connection = new List<IPAddress>();
            connection.Add(IPAddress.Parse(ipName));
            netOut.Connect(connection.ToArray());


            //network.getsth

            netOut.QueuePacket(@"<root>
                                        <command>login</command> 
                                        <userID>"+textBoxUserName.Text+@"</userID>
                                        <password>"+textBoxPassword.Text+@"</password>
                                        </root>");

            string returnStr = "";
            while ( !(returnStr.Equals("Login with Valid player") || returnStr.Equals("CAN NOT LOGIN INTO GAME: Invalid player")))
            {
                if (netOut.PeekString() != null)
                {
                    returnStr = netOut.GetString();
                    MessageBox.Show(returnStr);
                }
            }
        }

        private void signUpToolStripMenuItem_Click(object sender, EventArgs e)
        {
            suForm = new SignupForm();
            suForm.Show();
            
        }

        private void getScoreToolStripMenuItem_Click(object sender, EventArgs e)
        {
            scoreForm = new GetScoreForm();
            scoreForm.Show();
        }

        public List<String> getSignUpInfo()
        {
            return suForm.getSignUpInfo();

        }

        public string getUserNameForScore()
        {
            return scoreForm.getUserName();
        }

        public List<String> getLoginInfo()
        {
            List<String> list = new List<String>();
            list.Add(textBoxUserName.Text);
            list.Add(textBoxPassword.Text);
            return list;
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void playToolStripMenuItem_Click(object sender, EventArgs e)
        {
            new TitleScreen().Show();
        }

        private void aboutToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            new AboutScreen().ShowDialog();
        }

    }
}
