namespace Engine.Game.Screens
{
	partial class GameSelectScreen
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
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(GameSelectScreen));
			this.games_comboBox = new System.Windows.Forms.ComboBox();
			this.label1 = new System.Windows.Forms.Label();
			this.description_richTextBox = new System.Windows.Forms.RichTextBox();
			this.host_button = new System.Windows.Forms.Button();
			this.ip_textBox = new System.Windows.Forms.TextBox();
			this.direct_button = new System.Windows.Forms.Button();
			this.label2 = new System.Windows.Forms.Label();
			this.server_button = new System.Windows.Forms.Button();
			this.label3 = new System.Windows.Forms.Label();
			this.min_players = new System.Windows.Forms.Label();
			this.label5 = new System.Windows.Forms.Label();
			this.label6 = new System.Windows.Forms.Label();
			this.max_players = new System.Windows.Forms.Label();
			this.expected_time = new System.Windows.Forms.Label();
			this.graphic = new System.Windows.Forms.PictureBox();
			this.refresh_button = new System.Windows.Forms.Button();
			this.seat_comboBox = new System.Windows.Forms.ComboBox();
			this.label4 = new System.Windows.Forms.Label();
			this.label7 = new System.Windows.Forms.Label();
			this.name_textBox = new System.Windows.Forms.TextBox();
			((System.ComponentModel.ISupportInitialize)(this.graphic)).BeginInit();
			this.SuspendLayout();
			// 
			// games_comboBox
			// 
			this.games_comboBox.FormattingEnabled = true;
			this.games_comboBox.Location = new System.Drawing.Point(86,6);
			this.games_comboBox.Name = "games_comboBox";
			this.games_comboBox.Size = new System.Drawing.Size(156,21);
			this.games_comboBox.Sorted = true;
			this.games_comboBox.TabIndex = 0;
			this.games_comboBox.SelectedIndexChanged += new System.EventHandler(this.games_comboBox_SelectedIndexChanged);
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(12,9);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(68,13);
			this.label1.TabIndex = 1;
			this.label1.Text = "Select Game";
			// 
			// description_richTextBox
			// 
			this.description_richTextBox.Location = new System.Drawing.Point(248,6);
			this.description_richTextBox.Name = "description_richTextBox";
			this.description_richTextBox.ReadOnly = true;
			this.description_richTextBox.Size = new System.Drawing.Size(321,77);
			this.description_richTextBox.TabIndex = 2;
			this.description_richTextBox.Text = "";
			// 
			// host_button
			// 
			this.host_button.Location = new System.Drawing.Point(248,296);
			this.host_button.Name = "host_button";
			this.host_button.Size = new System.Drawing.Size(72,23);
			this.host_button.TabIndex = 3;
			this.host_button.Text = "Host Game";
			this.host_button.UseVisualStyleBackColor = true;
			this.host_button.Click += new System.EventHandler(this.host_button_Click);
			// 
			// ip_textBox
			// 
			this.ip_textBox.Location = new System.Drawing.Point(420,269);
			this.ip_textBox.Name = "ip_textBox";
			this.ip_textBox.Size = new System.Drawing.Size(149,20);
			this.ip_textBox.TabIndex = 4;
			this.ip_textBox.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
			// 
			// direct_button
			// 
			this.direct_button.Location = new System.Drawing.Point(483,296);
			this.direct_button.Name = "direct_button";
			this.direct_button.Size = new System.Drawing.Size(86,23);
			this.direct_button.TabIndex = 5;
			this.direct_button.Text = "Direct Connect";
			this.direct_button.UseVisualStyleBackColor = true;
			this.direct_button.Click += new System.EventHandler(this.direct_button_Click);
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(369,272);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(45,13);
			this.label2.TabIndex = 6;
			this.label2.Text = "Address";
			// 
			// server_button
			// 
			this.server_button.Location = new System.Drawing.Point(326,296);
			this.server_button.Name = "server_button";
			this.server_button.Size = new System.Drawing.Size(88,23);
			this.server_button.TabIndex = 7;
			this.server_button.Text = "Server Browser";
			this.server_button.UseVisualStyleBackColor = true;
			this.server_button.Click += new System.EventHandler(this.server_button_Click);
			// 
			// label3
			// 
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(12,41);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(88,13);
			this.label3.TabIndex = 8;
			this.label3.Text = "Minimum Players:";
			// 
			// min_players
			// 
			this.min_players.AutoSize = true;
			this.min_players.Location = new System.Drawing.Point(106,41);
			this.min_players.Name = "min_players";
			this.min_players.Size = new System.Drawing.Size(10,13);
			this.min_players.TabIndex = 9;
			this.min_players.Text = "-";
			// 
			// label5
			// 
			this.label5.AutoSize = true;
			this.label5.Location = new System.Drawing.Point(122,41);
			this.label5.Name = "label5";
			this.label5.Size = new System.Drawing.Size(91,13);
			this.label5.TabIndex = 10;
			this.label5.Text = "Maximum Players:";
			// 
			// label6
			// 
			this.label6.AutoSize = true;
			this.label6.Location = new System.Drawing.Point(12,70);
			this.label6.Name = "label6";
			this.label6.Size = new System.Drawing.Size(91,13);
			this.label6.TabIndex = 11;
			this.label6.Text = "Expected Length:";
			// 
			// max_players
			// 
			this.max_players.AutoSize = true;
			this.max_players.Location = new System.Drawing.Point(219,41);
			this.max_players.Name = "max_players";
			this.max_players.Size = new System.Drawing.Size(10,13);
			this.max_players.TabIndex = 12;
			this.max_players.Text = "-";
			// 
			// expected_time
			// 
			this.expected_time.AutoSize = true;
			this.expected_time.Location = new System.Drawing.Point(109,70);
			this.expected_time.Name = "expected_time";
			this.expected_time.Size = new System.Drawing.Size(10,13);
			this.expected_time.TabIndex = 13;
			this.expected_time.Text = "-";
			// 
			// graphic
			// 
			this.graphic.Location = new System.Drawing.Point(12,89);
			this.graphic.Name = "graphic";
			this.graphic.Size = new System.Drawing.Size(230,230);
			this.graphic.TabIndex = 14;
			this.graphic.TabStop = false;
			// 
			// refresh_button
			// 
			this.refresh_button.Location = new System.Drawing.Point(494,89);
			this.refresh_button.Name = "refresh_button";
			this.refresh_button.Size = new System.Drawing.Size(75,23);
			this.refresh_button.TabIndex = 15;
			this.refresh_button.Text = "Refresh";
			this.refresh_button.UseVisualStyleBackColor = true;
			this.refresh_button.Click += new System.EventHandler(this.refresh_button_Click);
			// 
			// seat_comboBox
			// 
			this.seat_comboBox.FormattingEnabled = true;
			this.seat_comboBox.Location = new System.Drawing.Point(362,243);
			this.seat_comboBox.Name = "seat_comboBox";
			this.seat_comboBox.Size = new System.Drawing.Size(59,21);
			this.seat_comboBox.TabIndex = 16;
			// 
			// label4
			// 
			this.label4.AutoSize = true;
			this.label4.Location = new System.Drawing.Point(327,246);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(29,13);
			this.label4.TabIndex = 17;
			this.label4.Text = "Seat";
			// 
			// label7
			// 
			this.label7.AutoSize = true;
			this.label7.Location = new System.Drawing.Point(428,246);
			this.label7.Name = "label7";
			this.label7.Size = new System.Drawing.Size(35,13);
			this.label7.TabIndex = 18;
			this.label7.Text = "Name";
			// 
			// name_textBox
			// 
			this.name_textBox.Location = new System.Drawing.Point(469,243);
			this.name_textBox.Name = "name_textBox";
			this.name_textBox.Size = new System.Drawing.Size(100,20);
			this.name_textBox.TabIndex = 19;
			// 
			// GameSelectScreen
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F,13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(579,332);
			this.Controls.Add(this.name_textBox);
			this.Controls.Add(this.label7);
			this.Controls.Add(this.label4);
			this.Controls.Add(this.seat_comboBox);
			this.Controls.Add(this.refresh_button);
			this.Controls.Add(this.graphic);
			this.Controls.Add(this.expected_time);
			this.Controls.Add(this.max_players);
			this.Controls.Add(this.label6);
			this.Controls.Add(this.label5);
			this.Controls.Add(this.min_players);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.server_button);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.direct_button);
			this.Controls.Add(this.ip_textBox);
			this.Controls.Add(this.host_button);
			this.Controls.Add(this.description_richTextBox);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.games_comboBox);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.MaximizeBox = false;
			this.MaximumSize = new System.Drawing.Size(585,360);
			this.MinimumSize = new System.Drawing.Size(585,360);
			this.Name = "GameSelectScreen";
			this.Text = "Game Select";
			((System.ComponentModel.ISupportInitialize)(this.graphic)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.ComboBox games_comboBox;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.RichTextBox description_richTextBox;
		private System.Windows.Forms.Button host_button;
		private System.Windows.Forms.TextBox ip_textBox;
		private System.Windows.Forms.Button direct_button;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Button server_button;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label min_players;
		private System.Windows.Forms.Label label5;
		private System.Windows.Forms.Label label6;
		private System.Windows.Forms.Label max_players;
		private System.Windows.Forms.Label expected_time;
		private System.Windows.Forms.PictureBox graphic;
		private System.Windows.Forms.Button refresh_button;
		private System.Windows.Forms.ComboBox seat_comboBox;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.Label label7;
		private System.Windows.Forms.TextBox name_textBox;


	}
}