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
        Me.Label1 = New System.Windows.Forms.Label
        Me.grp1 = New System.Windows.Forms.GroupBox
        Me.rad2 = New System.Windows.Forms.RadioButton
        Me.rad1 = New System.Windows.Forms.RadioButton
        Me.btn1 = New System.Windows.Forms.Button
        Me.btn2 = New System.Windows.Forms.Button
        Me.grp1.SuspendLayout()
        Me.SuspendLayout()
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 27.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.Location = New System.Drawing.Point(32, 9)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(396, 42)
        Me.Label1.TabIndex = 0
        Me.Label1.Text = "Trojan Computer Store"
        '
        'grp1
        '
        Me.grp1.Controls.Add(Me.rad2)
        Me.grp1.Controls.Add(Me.rad1)
        Me.grp1.Location = New System.Drawing.Point(170, 65)
        Me.grp1.Name = "grp1"
        Me.grp1.Size = New System.Drawing.Size(115, 76)
        Me.grp1.TabIndex = 0
        Me.grp1.TabStop = False
        Me.grp1.Text = "Computer Choices"
        '
        'rad2
        '
        Me.rad2.AutoSize = True
        Me.rad2.Location = New System.Drawing.Point(26, 43)
        Me.rad2.Name = "rad2"
        Me.rad2.Size = New System.Drawing.Size(65, 17)
        Me.rad2.TabIndex = 1
        Me.rad2.TabStop = True
        Me.rad2.Text = "Desktop"
        Me.rad2.UseVisualStyleBackColor = True
        '
        'rad1
        '
        Me.rad1.AutoSize = True
        Me.rad1.Location = New System.Drawing.Point(26, 19)
        Me.rad1.Name = "rad1"
        Me.rad1.Size = New System.Drawing.Size(58, 17)
        Me.rad1.TabIndex = 1
        Me.rad1.TabStop = True
        Me.rad1.Text = "Labtop"
        Me.rad1.UseVisualStyleBackColor = True
        '
        'btn1
        '
        Me.btn1.Location = New System.Drawing.Point(188, 147)
        Me.btn1.Name = "btn1"
        Me.btn1.Size = New System.Drawing.Size(75, 23)
        Me.btn1.TabIndex = 0
        Me.btn1.Text = "Continue"
        Me.btn1.UseVisualStyleBackColor = True
        '
        'btn2
        '
        Me.btn2.Location = New System.Drawing.Point(188, 176)
        Me.btn2.Name = "btn2"
        Me.btn2.Size = New System.Drawing.Size(75, 23)
        Me.btn2.TabIndex = 3
        Me.btn2.Text = "Quit"
        Me.btn2.UseVisualStyleBackColor = True
        '
        'frm1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(469, 212)
        Me.Controls.Add(Me.btn2)
        Me.Controls.Add(Me.btn1)
        Me.Controls.Add(Me.grp1)
        Me.Controls.Add(Me.Label1)
        Me.Name = "frm1"
        Me.Text = "Buy Now!"
        Me.grp1.ResumeLayout(False)
        Me.grp1.PerformLayout()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents grp1 As System.Windows.Forms.GroupBox
    Friend WithEvents rad2 As System.Windows.Forms.RadioButton
    Friend WithEvents rad1 As System.Windows.Forms.RadioButton
    Friend WithEvents btn1 As System.Windows.Forms.Button
    Friend WithEvents btn2 As System.Windows.Forms.Button

End Class
