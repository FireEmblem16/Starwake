namespace ExampleNetworkedGame
{
	partial class Form1
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if(disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
			this.button1 = new System.Windows.Forms.Button();
			this.button2 = new System.Windows.Forms.Button();
			this.textBox1 = new System.Windows.Forms.TextBox();
			this.domainUpDown1 = new System.Windows.Forms.DomainUpDown();
			this.SuspendLayout();
			// 
			// button1
			// 
			this.button1.Location = new System.Drawing.Point(21,38);
			this.button1.Name = "button1";
			this.button1.Size = new System.Drawing.Size(75,23);
			this.button1.TabIndex = 0;
			this.button1.Text = "Host";
			this.button1.UseVisualStyleBackColor = true;
			this.button1.Click += new System.EventHandler(this.button1_Click);
			// 
			// button2
			// 
			this.button2.Location = new System.Drawing.Point(103,38);
			this.button2.MaximumSize = new System.Drawing.Size(75,23);
			this.button2.MinimumSize = new System.Drawing.Size(75,23);
			this.button2.Name = "button2";
			this.button2.Size = new System.Drawing.Size(75,23);
			this.button2.TabIndex = 1;
			this.button2.Text = "Join";
			this.button2.UseVisualStyleBackColor = true;
			this.button2.Click += new System.EventHandler(this.button2_Click);
			// 
			// textBox1
			// 
			this.textBox1.Location = new System.Drawing.Point(103,12);
			this.textBox1.Name = "textBox1";
			this.textBox1.Size = new System.Drawing.Size(75,20);
			this.textBox1.TabIndex = 2;
			// 
			// domainUpDown1
			// 
			this.domainUpDown1.Items.Add("North");
			this.domainUpDown1.Items.Add("East");
			this.domainUpDown1.Items.Add("South");
			this.domainUpDown1.Items.Add("West");
			this.domainUpDown1.Location = new System.Drawing.Point(22,12);
			this.domainUpDown1.Name = "domainUpDown1";
			this.domainUpDown1.Size = new System.Drawing.Size(75,20);
			this.domainUpDown1.TabIndex = 3;
			this.domainUpDown1.Text = "North";
			// 
			// Form1
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F,13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(194,72);
			this.Controls.Add(this.domainUpDown1);
			this.Controls.Add(this.textBox1);
			this.Controls.Add(this.button2);
			this.Controls.Add(this.button1);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.MaximizeBox = false;
			this.MaximumSize = new System.Drawing.Size(200,100);
			this.MinimumSize = new System.Drawing.Size(200,100);
			this.Name = "Form1";
			this.Text = "Title Screen";
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Button button1;
		private System.Windows.Forms.Button button2;
		private System.Windows.Forms.TextBox textBox1;
		private System.Windows.Forms.DomainUpDown domainUpDown1;
	}
}

