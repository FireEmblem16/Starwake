using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace GameMenu
{
    public partial class SignupForm : Form
    {
        public SignupForm()
        {
            InitializeComponent();
        }

        public string getUserFullName()
        {
            return textBoxFullName.Text;
        }

        public string getUserName()
        {
            return textBoxUserName.Text;
        }

        public string getEmail()
        {
            return textBoxEmail.Text;
        }

        public string getPassword()
        {
            return textBoxPassword.Text;
        }

        public List<String> getSignUpInfo()
        {
            List<String> list = new List<string>();
            list.Add(textBoxFullName.Text);
            list.Add(textBoxUserName.Text);
            list.Add(textBoxEmail.Text);
            list.Add(textBoxPassword.Text);
            return list;
        }


    }
}
