using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Database
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            MySQLConnect myConnection = new MySQLConnect();
            dataGridView1.Visible = true;
            dataGridView1.AutoGenerateColumns = true;
            dataGridView1.DataSource = myConnection.getDataSet().Tables[0].DefaultView; // dataset
            //dataGridView1.DataMember = "Players History";
        }
    }
}
