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
        Me.components = New System.ComponentModel.Container
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(frm1))
        Me.tbtn1 = New System.Windows.Forms.ToolStripSplitButton
        Me.NewToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem
        Me.OpenToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem
        Me.SaveToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem
        Me.RemoveToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem
        Me.ExitToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem
        Me.tool1 = New System.Windows.Forms.ToolStrip
        Me.ToolStripSeparator1 = New System.Windows.Forms.ToolStripSeparator
        Me.ToolStripSplitButton1 = New System.Windows.Forms.ToolStripSplitButton
        Me.CompileToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem
        Me.cmbo2 = New System.Windows.Forms.ToolStripComboBox
        Me.ToolStripSeparator2 = New System.Windows.Forms.ToolStripSeparator
        Me.cmbo1 = New System.Windows.Forms.ToolStripComboBox
        Me.tim1 = New System.Windows.Forms.Timer(Me.components)
        Me.rich1 = New System.Windows.Forms.RichTextBox
        Me.tool1.SuspendLayout()
        Me.SuspendLayout()
        '
        'tbtn1
        '
        Me.tbtn1.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image
        Me.tbtn1.DropDownItems.AddRange(New System.Windows.Forms.ToolStripItem() {Me.NewToolStripMenuItem, Me.OpenToolStripMenuItem, Me.SaveToolStripMenuItem, Me.RemoveToolStripMenuItem, Me.ExitToolStripMenuItem})
        Me.tbtn1.Image = CType(resources.GetObject("tbtn1.Image"), System.Drawing.Image)
        Me.tbtn1.ImageTransparentColor = System.Drawing.Color.Magenta
        Me.tbtn1.Name = "tbtn1"
        Me.tbtn1.Size = New System.Drawing.Size(32, 22)
        Me.tbtn1.Text = "File"
        '
        'NewToolStripMenuItem
        '
        Me.NewToolStripMenuItem.Name = "NewToolStripMenuItem"
        Me.NewToolStripMenuItem.ShortcutKeys = CType((System.Windows.Forms.Keys.Control Or System.Windows.Forms.Keys.N), System.Windows.Forms.Keys)
        Me.NewToolStripMenuItem.Size = New System.Drawing.Size(159, 22)
        Me.NewToolStripMenuItem.Text = "New"
        '
        'OpenToolStripMenuItem
        '
        Me.OpenToolStripMenuItem.Name = "OpenToolStripMenuItem"
        Me.OpenToolStripMenuItem.ShortcutKeys = CType((System.Windows.Forms.Keys.Control Or System.Windows.Forms.Keys.O), System.Windows.Forms.Keys)
        Me.OpenToolStripMenuItem.Size = New System.Drawing.Size(159, 22)
        Me.OpenToolStripMenuItem.Text = "Open"
        '
        'SaveToolStripMenuItem
        '
        Me.SaveToolStripMenuItem.Name = "SaveToolStripMenuItem"
        Me.SaveToolStripMenuItem.ShortcutKeys = CType((System.Windows.Forms.Keys.Control Or System.Windows.Forms.Keys.S), System.Windows.Forms.Keys)
        Me.SaveToolStripMenuItem.Size = New System.Drawing.Size(159, 22)
        Me.SaveToolStripMenuItem.Text = "Save"
        '
        'RemoveToolStripMenuItem
        '
        Me.RemoveToolStripMenuItem.Name = "RemoveToolStripMenuItem"
        Me.RemoveToolStripMenuItem.ShortcutKeys = CType((System.Windows.Forms.Keys.Control Or System.Windows.Forms.Keys.D), System.Windows.Forms.Keys)
        Me.RemoveToolStripMenuItem.Size = New System.Drawing.Size(159, 22)
        Me.RemoveToolStripMenuItem.Text = "Remove"
        '
        'ExitToolStripMenuItem
        '
        Me.ExitToolStripMenuItem.Name = "ExitToolStripMenuItem"
        Me.ExitToolStripMenuItem.ShortcutKeys = CType((System.Windows.Forms.Keys.Control Or System.Windows.Forms.Keys.Q), System.Windows.Forms.Keys)
        Me.ExitToolStripMenuItem.Size = New System.Drawing.Size(159, 22)
        Me.ExitToolStripMenuItem.Text = "Exit"
        '
        'tool1
        '
        Me.tool1.AllowDrop = True
        Me.tool1.Items.AddRange(New System.Windows.Forms.ToolStripItem() {Me.tbtn1, Me.ToolStripSeparator1, Me.ToolStripSplitButton1, Me.ToolStripSeparator2, Me.cmbo1})
        Me.tool1.Location = New System.Drawing.Point(0, 0)
        Me.tool1.Name = "tool1"
        Me.tool1.Size = New System.Drawing.Size(1024, 25)
        Me.tool1.TabIndex = 0
        Me.tool1.Text = "Tool Bar"
        '
        'ToolStripSeparator1
        '
        Me.ToolStripSeparator1.Name = "ToolStripSeparator1"
        Me.ToolStripSeparator1.Size = New System.Drawing.Size(6, 25)
        '
        'ToolStripSplitButton1
        '
        Me.ToolStripSplitButton1.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image
        Me.ToolStripSplitButton1.DropDownItems.AddRange(New System.Windows.Forms.ToolStripItem() {Me.CompileToolStripMenuItem, Me.cmbo2})
        Me.ToolStripSplitButton1.Image = CType(resources.GetObject("ToolStripSplitButton1.Image"), System.Drawing.Image)
        Me.ToolStripSplitButton1.ImageTransparentColor = System.Drawing.Color.Magenta
        Me.ToolStripSplitButton1.Name = "ToolStripSplitButton1"
        Me.ToolStripSplitButton1.Size = New System.Drawing.Size(32, 22)
        Me.ToolStripSplitButton1.Text = "Compile"
        '
        'CompileToolStripMenuItem
        '
        Me.CompileToolStripMenuItem.Name = "CompileToolStripMenuItem"
        Me.CompileToolStripMenuItem.ShortcutKeys = System.Windows.Forms.Keys.F7
        Me.CompileToolStripMenuItem.Size = New System.Drawing.Size(181, 22)
        Me.CompileToolStripMenuItem.Text = "Compile"
        '
        'cmbo2
        '
        Me.cmbo2.Items.AddRange(New Object() {"Display Errors", "Don't Display Errors"})
        Me.cmbo2.Name = "cmbo2"
        Me.cmbo2.Size = New System.Drawing.Size(121, 23)
        '
        'ToolStripSeparator2
        '
        Me.ToolStripSeparator2.Name = "ToolStripSeparator2"
        Me.ToolStripSeparator2.Size = New System.Drawing.Size(6, 25)
        '
        'cmbo1
        '
        Me.cmbo1.Name = "cmbo1"
        Me.cmbo1.Size = New System.Drawing.Size(121, 25)
        '
        'tim1
        '
        Me.tim1.Enabled = True
        Me.tim1.Interval = 10
        '
        'rich1
        '
        Me.rich1.Location = New System.Drawing.Point(0, 24)
        Me.rich1.Name = "rich1"
        Me.rich1.Size = New System.Drawing.Size(1024, 740)
        Me.rich1.TabIndex = 1
        Me.rich1.Text = ""
        Me.rich1.WordWrap = False
        '
        'frm1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackgroundImage = Global.Omacron_Luryi_Asm_Env.My.Resources.Resources.shanaonfirehh8
        Me.ClientSize = New System.Drawing.Size(1024, 764)
        Me.Controls.Add(Me.rich1)
        Me.Controls.Add(Me.tool1)
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.MaximizeBox = False
        Me.MaximumSize = New System.Drawing.Size(1040, 800)
        Me.MinimumSize = New System.Drawing.Size(1040, 800)
        Me.Name = "frm1"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "Omacron-Luryi Asm Env"
        Me.tool1.ResumeLayout(False)
        Me.tool1.PerformLayout()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents tbtn1 As System.Windows.Forms.ToolStripSplitButton
    Friend WithEvents NewToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents OpenToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents ExitToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents tool1 As System.Windows.Forms.ToolStrip
    Friend WithEvents cmbo1 As System.Windows.Forms.ToolStripComboBox
    Friend WithEvents tim1 As System.Windows.Forms.Timer
    Friend WithEvents rich1 As System.Windows.Forms.RichTextBox
    Friend WithEvents SaveToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents RemoveToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents ToolStripSplitButton1 As System.Windows.Forms.ToolStripSplitButton
    Friend WithEvents ToolStripSeparator1 As System.Windows.Forms.ToolStripSeparator
    Friend WithEvents CompileToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents ToolStripSeparator2 As System.Windows.Forms.ToolStripSeparator
    Friend WithEvents cmbo2 As System.Windows.Forms.ToolStripComboBox

End Class
