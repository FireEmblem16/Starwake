#Region "Include Statements"
Imports System.IO
#End Region

Public Class frm1

    Private Sub frm1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

        projname = "No Project"
        folderpath = ""
        main = ""
        projfiles(0) = ""
        lastfile = ""
        Me.AllowDrop = True
        Me.rich1.AllowDrop = True
        Me.tool1.AllowDrop = True
        lastsave = True
        cmbo2.SelectedItem = "Don't Display Errors"

    End Sub

    Private Sub NewToolStripMenuItem_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles NewToolStripMenuItem.Click

        newfile()

    End Sub

    Private Sub OpenToolStripMenuItem_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles OpenToolStripMenuItem.Click

        openfile()

    End Sub

    Private Sub CompileToolStripMenuItem_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles CompileToolStripMenuItem.Click

        savefile()
        saveproj(projname)

        projectout = File.CreateText("C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\compile.bat")
        projectout.WriteLine("cd " & ControlChars.Quote & "C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\" & ControlChars.Quote)
        'projectout.WriteLine("cmd /a")
        projectout.WriteLine("nasm -o " & ControlChars.Quote & projname & ControlChars.Quote & " -f bin " & ControlChars.Quote & "Projects\" & folderpath & main & ".asm" & ControlChars.Quote) ' & " > c:\compile file.txt")
        projectout.WriteLine("exit")
        'projectout.WriteLine("exit")
        projectout.Close()

        Try
            If cmbo2.SelectedItem = "Don't Display Errors" Then
                Shell("C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\compile.bat", AppWinStyle.Hide, True, -1)
                '
            Else
                Shell("C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\compile.bat", AppWinStyle.Hide, True, -1)
                '
            End If
            If Not File.Exists("C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\" & projname) Then
                MessageBox.Show("Unable to compile source code", "Non-Fatal Error", MessageBoxButtons.OK)
            Else
                File.Delete("c:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\Projects\" & projname & "\Compiles\" & projname)
                File.Move("c:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\" & projname, "c:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\Projects\" & projname & "\Compiles\" & projname)
                MessageBox.Show("Project Compiled!?", "Success", MessageBoxButtons.OK)
            End If
        Catch ex As Exception
            MessageBox.Show(ex.ToString() & ControlChars.NewLine & " - Unable to compile source", "Non-Fatal Error", MessageBoxButtons.OK)
        End Try
    End Sub

    Private Sub ExitToolStripMenuItem_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles ExitToolStripMenuItem.Click

        If Not lastsave Then
            If MessageBox.Show("Save Project?", "Quit", MessageBoxButtons.YesNo) = Windows.Forms.DialogResult.Yes Then
                savefile()
                saveproj(projname)
            End If
        End If

        End

    End Sub

    Private Sub newfile()

        Dim type As Integer = 0
        Dim title As String = ""

        type = MessageBox.Show("New Project?", "New", MessageBoxButtons.YesNoCancel)

        If type = DialogResult.Yes Then
            newproj(InputBox("Project Name:", "New Project"))
        ElseIf type = DialogResult.No Then
            newasm(InputBox("File Name:", "New File"))
        End If

    End Sub

    Private Sub openfile(Optional ByRef filestuff As String = "")

        Try
            Dim title As String = "", temp As String = ""

            If filestuff = "" Then
                title = InputBox("File Name", "Open File")
            Else
                title = filestuff
            End If

            If title.IndexOf(".") = -1 Then
                title &= ".proj"
            End If

            If title <> "" Then
                If title.Substring(title.Length - 1, 1) = "m" Then
                    projectin = File.OpenText(path & folderpath & title)
                    rich1.Text = ""
                    While Not projectin.EndOfStream
                        rich1.Text += projectin.ReadLine() & ControlChars.NewLine
                    End While
                    lastsave = True
                    projectin.Close()

                    lastfile = title
                    cmbo1.SelectedItem = title
                    If cmbo1.SelectedItem <> title Then
                        cmbo1.Text = title.Substring(0, title.Length - 4)
                    End If
                ElseIf title.Substring(title.Length - 1, 1) = "j" Then
                    ReDim projfiles(0)

                    projectin = File.OpenText(path & title.Substring(0, title.Length - 5) & "\" & title)

                    rich1.Text = ""

                    projname = projectin.ReadLine()
                    folderpath = projname & "\"

                    Me.Text = "Omacron-Luryi Asm Env - " & projname
                    lastsave = True

                    projfiles(0) = projectin.ReadLine()
                    cmbo1.Items.Clear()

                    While projectin.Peek() <> 35
                        ReDim Preserve projfiles(projfiles.Length)
                        projfiles(projfiles.Length - 1) = projectin.ReadLine()
                        temp = projfiles(projfiles.Length - 1)
                        cmbo1.Items.Add(temp)
                    End While

                    projectin.ReadLine()

                    main = projectin.ReadLine()

                    cmbo1.SelectedIndex = cmbo1.Items.IndexOf(main)
                    lastfile = main

                    projectin.Close()

                    openfile(main & ".asm")
                End If
            End If

            open = True
        Catch ex As Exception
            MessageBox.Show("File could not be opened or not found", "Non-Fatal Error", MessageBoxButtons.OK)
        End Try

    End Sub

    Private Sub newproj(ByRef title As String)

        If title <> "" Then
            If File.Exists(path & title & ".proj") Then
                If MessageBox.Show("Overwrite Project File?", "New Project", MessageBoxButtons.OKCancel) = Windows.Forms.DialogResult.OK Then
                    createproj(title)
                End If
            Else
                createproj(title)
            End If
        End If

    End Sub

    Private Sub createproj(ByRef title As String)

        main = InputBox("Main code file", "New Project")

        lastfile = main

        folderpath = title & "\"

        If createanddeploy(path & title) Then

            If newasm(main) Then

                saveproj(title)

                projname = title

                Me.Text = "Omacron-Luryi Asm Env - " & title

            Else
                MessageBox.Show("Failed to create project", "Non-Fatal Error", MessageBoxButtons.OK)
            End If

        End If

    End Sub

    Private Function createanddeploy(ByRef folder As String) As Boolean

        If folder <> "" Then
            projectout = File.CreateText("C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\createfolder.bat")
            projectout.WriteLine("cd " & ControlChars.Quote & "C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\Projects" & ControlChars.Quote)
            projectout.WriteLine("mkdir " & ControlChars.Quote & folder & ControlChars.Quote)
            projectout.WriteLine("mkdir " & ControlChars.Quote & folder & "\Compiles" & ControlChars.Quote)
            projectout.WriteLine("exit")
            projectout.Close()

            Try
                If Shell("C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\createfolder.bat", AppWinStyle.Hide, True, -1) Then
                    MessageBox.Show("Unable to find createfolder.bat", "Non-Fatal Error", MessageBoxButtons.OK)
                Else
                    Return True
                End If
            Catch ex As Exception
                MessageBox.Show(ex.ToString() & ControlChars.NewLine & " - Unable to compile source", "Non-Fatal Error", MessageBoxButtons.OK)
            End Try
        Else
            MessageBox.Show("Can't create folder", "Non-Fatal Error", MessageBoxButtons.OK)
            Return False
        End If

        Return False

    End Function

    Private Sub saveproj(ByRef title As String)

        If title <> "No Project" Then
            projectout = File.CreateText(path & title & "\" & title & ".proj")
            projectout.WriteLine(title)

            Try
                For i As Integer = 0 To 1000000
                    projectout.WriteLine(projfiles(i))
                Next i
            Catch ex As Exception
                projectout.WriteLine("#")
                projectout.WriteLine(main)
            End Try

            projectout.Close()
        End If

    End Sub

    Private Function newasm(ByRef title As String) As Boolean

        If title <> "" Then
            If File.Exists(path & folderpath & title & ".asm") Then
                If MessageBox.Show("File already exists. Overwrite?", "New File", MessageBoxButtons.OKCancel) = Windows.Forms.DialogResult.OK Then
                    create(title)
                    Return True
                End If
            Else
                create(title)
                Return True
            End If
        End If

        Return False

    End Function

    Public Sub create(ByRef title As String)

        projectout = File.CreateText(path & folderpath & title & ".asm")
        projectout.Close()

        ReDim Preserve projfiles(projfiles.Length)
        projfiles(projfiles.Length - 1) = title

        cmbo1.Items.Add(title)
        cmbo1.SelectedIndex = cmbo1.Items.IndexOf(title)

        rich1.Text = ""

        lastfile = title

        savefile()
        saveproj(projname)

    End Sub

    Private Sub SaveToolStripMenuItem_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles SaveToolStripMenuItem.Click

        savefile()
        If projname <> "No Project" Then
            saveproj(projname)
            MessageBox.Show("Project Saved", "Project Saved", MessageBoxButtons.OK)
        End If

    End Sub

    Private Sub savefile()

        If lastfile = "" Then
            Dim newname As String = InputBox("Save As:", "Save File")
            If newname <> "" Then
                projectout = File.CreateText(path & folderpath & newname & ".asm")
                projectout.Write(rich1.Text)
                projectout.Close()
                MessageBox.Show("File Saved as: " & newname, "File Saved", MessageBoxButtons.OK)
                lastsave = True
            End If
        Else
            projectout = File.CreateText(path & folderpath & lastfile & ".asm")
            projectout.Write(rich1.Text)
            projectout.Close()
            lastsave = True
        End If

    End Sub

    Private Sub RemoveToolStripMenuItem_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles RemoveToolStripMenuItem.Click

        If lastfile <> main Then
            Dim l As Integer = projfiles.Length - 1
            For i As Integer = 0 To l
                If projfiles(i) = lastfile Then
                    For j As Integer = i To l
                        If j = l Then
                            projfiles(j) = ""
                            ReDim Preserve projfiles(l - 1)
                            File.Delete(path & folderpath & lastfile & ".asm")
                            cmbo1.Items.RemoveAt(cmbo1.Items.IndexOf(lastfile))
                            cmbo1.SelectedItem = main
                            saveproj(projname)
                            Exit For
                        ElseIf j < l Then
                            projfiles(j) = projfiles(j + 1)
                        End If
                    Next
                    Exit For
                End If

                If i = l Then
                    MessageBox.Show("Can not find file.", "Non-Fatal Error", MessageBoxButtons.OK)
                End If
            Next
        Else
            MessageBox.Show("Can not remove main file.", "Non-Fatal Error", MessageBoxButtons.OK)
        End If

    End Sub

    Private Sub Timer1_Tick(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles tim1.Tick

        If lastfile <> cmbo1.SelectedItem And cmbo1.SelectedItem <> "" And lastfile <> "" Then
            Try
                Dim i As Integer

                For i = 0 To projfiles.Length - 1
                    If projfiles(i) = lastfile Then
                        Exit For
                    End If
                Next i

                If i = projfiles.Length Then
                    cmbo1.Items.RemoveAt(cmbo1.Items.IndexOf(lastfile))
                    lastfile = ""
                End If

                If lastfile = "" Then
                    savefile()
                End If
                openfile(cmbo1.SelectedItem & ".asm")
            Catch ex As Exception
            End Try
        End If

        lastfile = cmbo1.SelectedItem

        If lastfile = "" And Not open Then
            rich1.Visible = False
        Else
            rich1.Visible = True
        End If

        If lastsave = False Then
            Me.Text = "Omacron-Luryi Asm Env - " & projname & "*"
        Else
            Me.Text = "Omacron-Luryi Asm Env - " & projname
        End If

    End Sub

    Private Sub rich1_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles rich1.TextChanged

        lastsave = False

    End Sub

    Private Sub frm1_DragDrop(ByVal sender As Object, ByVal e As DragEventArgs) Handles Me.DragDrop

        If e.Data.GetDataPresent(DataFormats.FileDrop) Then
            Dim filePaths As String() = CType(e.Data.GetData(DataFormats.FileDrop), String())

            For Each fileLoc As String In filePaths
                If File.Exists(fileLoc) Then
                    Using tr As TextReader = New StreamReader(fileLoc)
                        rich1.Text = tr.ReadToEnd()
                    End Using
                End If

                fileLoc = fileLoc.Substring(fileLoc.LastIndexOf("\") + 1)

                openfile(fileLoc)
            Next fileLoc

        End If

    End Sub

    Private Sub frm1_DragEnter(ByVal sender As Object, ByVal e As DragEventArgs) Handles Me.DragEnter

        If e.Data.GetDataPresent(DataFormats.FileDrop) Then
            e.Effect = DragDropEffects.Copy
        Else
            e.Effect = DragDropEffects.None
        End If

    End Sub

    Private Sub rich1_DragDrop(ByVal sender As Object, ByVal e As DragEventArgs) Handles rich1.DragDrop

        If e.Data.GetDataPresent(DataFormats.FileDrop) Then
            Dim filePaths As String() = CType(e.Data.GetData(DataFormats.FileDrop), String())

            For Each fileLoc As String In filePaths
                If File.Exists(fileLoc) Then
                    Using tr As TextReader = New StreamReader(fileLoc)
                        rich1.Text = tr.ReadToEnd()
                    End Using
                End If

                fileLoc = fileLoc.Substring(fileLoc.LastIndexOf("\") + 1)

                openfile(fileLoc)
            Next fileLoc

        End If

    End Sub

    Private Sub rich1_DragEnter(ByVal sender As Object, ByVal e As DragEventArgs) Handles rich1.DragEnter

        If e.Data.GetDataPresent(DataFormats.FileDrop) Then
            e.Effect = DragDropEffects.Copy
        Else
            e.Effect = DragDropEffects.None
        End If

    End Sub

    Private Sub tool1_DragDrop(ByVal sender As Object, ByVal e As DragEventArgs) Handles tool1.DragDrop

        If e.Data.GetDataPresent(DataFormats.FileDrop) Then
            Dim filePaths As String() = CType(e.Data.GetData(DataFormats.FileDrop), String())

            For Each fileLoc As String In filePaths
                If File.Exists(fileLoc) Then
                    Using tr As TextReader = New StreamReader(fileLoc)
                        rich1.Text = tr.ReadToEnd()
                    End Using
                End If

                fileLoc = fileLoc.Substring(fileLoc.LastIndexOf("\") + 1)

                openfile(fileLoc)
            Next fileLoc

        End If

    End Sub

    Private Sub tool1_DragEnter(ByVal sender As Object, ByVal e As DragEventArgs) Handles tool1.DragEnter

        If e.Data.GetDataPresent(DataFormats.FileDrop) Then
            e.Effect = DragDropEffects.Copy
        Else
            e.Effect = DragDropEffects.None
        End If

    End Sub

#Region "Member Variables"
    Private projectin As StreamReader
    Private projectout As StreamWriter
    Private projname As String
    Private folderpath As String
    Private path As String = "C:\Users\Nivmizzet\Documents\Visual Studio 2008\Projects\Omacron-Luryi Asm Env\Omacron-Luryi Asm Env\Projects\"
    Private main As String
    Private projfiles(0) As String
    Private lastfile As String
    Private open As Boolean = False
    Private lastsave As Boolean
#End Region

End Class