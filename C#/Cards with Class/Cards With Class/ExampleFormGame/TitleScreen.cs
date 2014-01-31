using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace ExampleFormGame
{
    public partial class TitleScreen : Form
    {
        public TitleScreen()
        {
            InitializeComponent();
            return;
        }

        private void StartButton_Click(object sender, EventArgs e)
        {
            Hide();
            new Table(this).Show();
			
            return;
        }
    }
}
