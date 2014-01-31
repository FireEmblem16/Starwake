<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class frm1
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(frm1))
        Me.cmbo1 = New System.Windows.Forms.ComboBox
        Me.cmbo2 = New System.Windows.Forms.ComboBox
        Me.Label1 = New System.Windows.Forms.Label
        Me.btn1 = New System.Windows.Forms.Button
        Me.rtb1 = New System.Windows.Forms.RichTextBox
        Me.rtb2 = New System.Windows.Forms.RichTextBox
        Me.btn2 = New System.Windows.Forms.Button
        Me.btn3 = New System.Windows.Forms.Button
        Me.SuspendLayout()
        '
        'cmbo1
        '
        Me.cmbo1.FormattingEnabled = True
        Me.cmbo1.ImeMode = System.Windows.Forms.ImeMode.NoControl
        Me.cmbo1.Items.AddRange(New Object() {"Romanji", "Kana", "English"})
        Me.cmbo1.Location = New System.Drawing.Point(13, 13)
        Me.cmbo1.Name = "cmbo1"
        Me.cmbo1.Size = New System.Drawing.Size(121, 21)
        Me.cmbo1.TabIndex = 0
        '
        'cmbo2
        '
        Me.cmbo2.FormattingEnabled = True
        Me.cmbo2.Items.AddRange(New Object() {"Romanji", "Kana", "English"})
        Me.cmbo2.Location = New System.Drawing.Point(170, 13)
        Me.cmbo2.Name = "cmbo2"
        Me.cmbo2.Size = New System.Drawing.Size(121, 21)
        Me.cmbo2.TabIndex = 1
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.BackColor = System.Drawing.SystemColors.Control
        Me.Label1.Location = New System.Drawing.Point(141, 18)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(20, 13)
        Me.Label1.TabIndex = 2
        Me.Label1.Text = "To"
        '
        'btn1
        '
        Me.btn1.Location = New System.Drawing.Point(13, 41)
        Me.btn1.Name = "btn1"
        Me.btn1.Size = New System.Drawing.Size(60, 23)
        Me.btn1.TabIndex = 3
        Me.btn1.Text = "Translate"
        Me.btn1.UseVisualStyleBackColor = True
        '
        'rtb1
        '
        Me.rtb1.Location = New System.Drawing.Point(13, 71)
        Me.rtb1.Name = "rtb1"
        Me.rtb1.Size = New System.Drawing.Size(189, 152)
        Me.rtb1.TabIndex = 4
        Me.rtb1.Text = ""
        '
        'rtb2
        '
        Me.rtb2.Location = New System.Drawing.Point(13, 229)
        Me.rtb2.Name = "rtb2"
        Me.rtb2.Size = New System.Drawing.Size(189, 155)
        Me.rtb2.TabIndex = 6
        Me.rtb2.TabStop = False
        Me.rtb2.Text = ""
        '
        'btn2
        '
        Me.btn2.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.btn2.Location = New System.Drawing.Point(78, 41)
        Me.btn2.Name = "btn2"
        Me.btn2.Size = New System.Drawing.Size(58, 23)
        Me.btn2.TabIndex = 7
        Me.btn2.Text = "Hiragana"
        Me.btn2.UseVisualStyleBackColor = True
        '
        'btn3
        '
        Me.btn3.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.btn3.Location = New System.Drawing.Point(140, 41)
        Me.btn3.Name = "btn3"
        Me.btn3.Size = New System.Drawing.Size(62, 23)
        Me.btn3.TabIndex = 8
        Me.btn3.Text = "Katakana"
        Me.btn3.UseVisualStyleBackColor = True
        '
        'frm1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackgroundImage = CType(resources.GetObject("$this.BackgroundImage"), System.Drawing.Image)
        Me.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch
        Me.ClientSize = New System.Drawing.Size(458, 396)
        Me.Controls.Add(Me.btn3)
        Me.Controls.Add(Me.btn2)
        Me.Controls.Add(Me.rtb2)
        Me.Controls.Add(Me.rtb1)
        Me.Controls.Add(Me.btn1)
        Me.Controls.Add(Me.Label1)
        Me.Controls.Add(Me.cmbo2)
        Me.Controls.Add(Me.cmbo1)
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.Name = "frm1"
        Me.Text = "Translator"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents cmbo1 As System.Windows.Forms.ComboBox
    Friend WithEvents cmbo2 As System.Windows.Forms.ComboBox
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents btn1 As System.Windows.Forms.Button
    Friend WithEvents rtb1 As System.Windows.Forms.RichTextBox
    Friend WithEvents rtb2 As System.Windows.Forms.RichTextBox
    Friend WithEvents btn2 As System.Windows.Forms.Button
    Friend WithEvents btn3 As System.Windows.Forms.Button

End Class
