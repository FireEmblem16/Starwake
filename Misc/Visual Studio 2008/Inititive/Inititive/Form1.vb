'////////////////////////////////////////////////////////////////////////////////////////////'
'////////////////////////////////////////////////////////////////////////////////////////////'
'/////////////////////////// Dungeons and Dragons Initive Recorder //////////////////////////'
'////////////////////////////////////////////////////////////////////////////////////////////'
'////////////////////////////////////////////////////////////////////////////////////////////'
'/////////////////////////////////Produced by Omacron Games//////////////////////////////////'
'/////////////////This product is not available for redistributing or copying////////////////'
'////This product is not open source so if you are reading this you better have permission///'
'////////////////////////////////////////////////////////////////////////////////////////////'
'////////////////////////////////////////////////////////////////////////////////////////////'

Imports System.IO

Public Class frm1

    Private Const NONEXIST As Integer = -1943823
    Private Const MAX As Integer = 30
    Private number As Integer, tempint As Integer
    Private initive(MAX) As Integer
    Private names(MAX) As String, notes(MAX) As String, temp As String, tempstr As String
    Private current As String = "New File"

    Private Sub frm1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

        HandleLoad()

    End Sub

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click 'Next

        HandleNext()

    End Sub

    Private Sub Button2_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button2.Click 'Last

        HandleLast()

    End Sub

    Private Sub Button3_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button3.Click 'Insert

        Insert()

    End Sub

    Private Sub Button4_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button4.Click 'Remove

        RemoveSoul(Val(InputBox("Which position will you remove?", "Position Removal Input")))

    End Sub

    Private Sub Button5_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button5.Click 'Move

        MovePlayersBase(True)

    End Sub

    Private Sub Button6_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button6.Click 'Check Initive

        InitiveCheck(True)

    End Sub

    Private Sub Button7_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button7.Click 'Save

        SaveFile()

    End Sub

    Private Sub Button8_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button8.Click 'Load

        LoadFile(True)

    End Sub

    Private Sub SaveFile()

        Dim tempstring As String = ""
        Dim y As Integer
        Dim z As Boolean = True

        tempstring = InputBox("File Name: ", "Save inititive file", current) & ".txt"

        If tempstring = ".txt" Then
            z = False
        End If

        If File.Exists(tempstring) And tempstring <> ".txt" Then
            y = MessageBox.Show("Overwrite File?", "Overwrite?", MessageBoxButtons.YesNo)
            If y = Windows.Forms.DialogResult.No Then
                z = False
            End If
        End If

        If z Then
            FileOpen(1, tempstring, OpenMode.Output)
            For i = 1 To MAX
                WriteLine(1, names(i))
                WriteLine(1, initive(i))
                WriteLine(1, notes(i))
            Next

            FileClose(1)
        End If

    End Sub

    Private Sub LoadFile(ByVal z As Boolean)

        Dim FilePrep As StreamReader
        Dim tempstring As String = ""
        Dim tempstr As String = ""
        Dim tempstrb As String = ""
        Dim y As Integer
        Dim k As Integer = 1

        If z Then
            tempstring = InputBox("File Name: ", "Save inititive file")
            If tempstring = "" Then
                z = False
            End If
            tempstr = tempstring
            tempstring += ".txt"
        Else
            z = True
        End If

        If Not File.Exists(tempstring) And tempstring <> ".txt" Then
            y = MessageBox.Show("File not found. Retry?", "Non-Fatal Error", MessageBoxButtons.RetryCancel)
            If y = Windows.Forms.DialogResult.Retry Then
                LoadFile(False)
            Else
                z = False
            End If
        End If

        If z Then
            FilePrep = File.OpenText(tempstring)
            For i = 1 To MAX
                tempstrb = FilePrep.ReadLine()
                names(i) = Replace(tempstrb, ControlChars.Quote, "")
                initive(i) = Val(FilePrep.ReadLine())
                tempstrb = FilePrep.ReadLine()
                notes(i) = Replace(tempstrb, ControlChars.Quote, "")
            Next

            current = tempstr
            FilePrep.Close()

            number = 0

            While initive(k) <> NONEXIST
                k += 1
                number += 1
                MakeVisibleYesNo(True)
            End While

            MoveLabels()

        End If

    End Sub

    Private Sub HandleLoad()

        number = 0
        tempint = 0
        temp = ""
        tempstr = ""
        For j = 1 To MAX - 1
            names(j) = ""
        Next
        For k = 1 To MAX - 1
            initive(k) = NONEXIST
        Next
        For i = 1 To MAX - 1
            notes(i) = names(i)
        Next

    End Sub

    Private Sub HandleNext()

        notes(1) = rtb1.Text
        MovePlayers()
        MoveLabels()
        DisplayInt()

    End Sub

    Private Sub HandleLast()

        notes(1) = rtb1.Text
        MovePlayersBack()
        MoveLabels()
        DisplayInt()

    End Sub

    Private Sub InitiveCheck(ByVal z As Boolean)

        Dim x As Integer = 0, y As Integer = 0
        If z Then
            x = Val(InputBox("Which position will you check?", "Position Input"))
        End If
        If x <= number And x >= 1 Then
            If names(x) <> "" Then
                txt4.Text = initive(x)
            End If
        Else
            y = MessageBox.Show("There is no established player here.", "Non-Fatal Error", MessageBoxButtons.RetryCancel)
            If y = Windows.Forms.DialogResult.Retry Then
                InitiveCheck(False)
            Else
                y = MessageBox.Show("Reinput Data?", "Non-Fatal Error", MessageBoxButtons.YesNo)
                If y = Windows.Forms.DialogResult.Yes Then
                    InitiveCheck(True)
                End If
            End If
        End If

    End Sub

    Private Function Redeclare() As Boolean

        Dim x As Integer

        x = MessageBox.Show("Initive value out of range. Reenter value?", "Non-Fatal Error", MessageBoxButtons.YesNo)
        If x = Windows.Forms.DialogResult.Yes Then
            txt2.Text = Val(InputBox("Input new initive value.", "Redefinition Input"))
            Return True
        End If

        Return False

    End Function

    Private Sub Insert()

        Dim i As Integer = 1

        If txt1.Text <> "" And txt2.Text <> "" Then
            If number < MAX And txt2.Text > NONEXIST Then
                temp = txt1.Text
                tempint = txt2.Text
                tempstr = rtb1.Text
                While initive(i) >= tempint And i <= MAX
                    i += 1
                End While
                number += 1
                MovePlayersOutOfTheWay(i)
                names(i) = temp
                initive(i) = tempint
                notes(i) = tempstr
                MoveLabels()
                MakeVisibleYesNo(True)
            ElseIf txt2.Text <= NONEXIST Then
                If Redeclare() Then
                    Insert()
                End If
                ClearAll()
            ElseIf number = MAX Then
                MessageBox.Show("Inititive stream is full. Remove a player first of forget this player.", "Non-Fatal Error", MessageBoxButtons.OK)
            End If

            If names(1) <> "" Then
                DisplayInt()
            End If
        End If

    End Sub

    Private Sub MakeVisibleYesNo(ByVal what As Boolean)

        If number = 1 Then Label1.Visible = what
        If number = 2 Then Label2.Visible = what
        If number = 3 Then Label3.Visible = what
        If number = 4 Then Label4.Visible = what
        If number = 5 Then Label5.Visible = what
        If number = 6 Then Label6.Visible = what
        If number = 7 Then Label7.Visible = what
        If number = 8 Then Label8.Visible = what
        If number = 9 Then Label9.Visible = what
        If number = 10 Then Label10.Visible = what
        If number = 11 Then Label11.Visible = what
        If number = 12 Then Label12.Visible = what
        If number = 13 Then Label13.Visible = what
        If number = 14 Then Label14.Visible = what
        If number = 15 Then Label15.Visible = what
        If number = 16 Then Label16.Visible = what
        If number = 17 Then Label17.Visible = what
        If number = 18 Then Label18.Visible = what
        If number = 19 Then Label19.Visible = what
        If number = 20 Then Label20.Visible = what
        If number = 21 Then Label21.Visible = what
        If number = 22 Then Label22.Visible = what
        If number = 23 Then Label23.Visible = what
        If number = 24 Then Label24.Visible = what
        If number = 25 Then Label25.Visible = what
        If number = 26 Then Label26.Visible = what
        If number = 27 Then Label27.Visible = what
        If number = 28 Then Label28.Visible = what
        If number = 29 Then Label29.Visible = what
        If number = 30 Then Label30.Visible = what
        ClearAll()

    End Sub

    Private Sub MovePlayersBase(ByVal z As Boolean)

        Dim x As Integer, y As Integer
        Dim x1 As String, x2 As String
        Dim y1 As Integer
        If z Then
            x = Val(InputBox("Which position will you move?", "Position Moving Input"))
        End If
        If x >= 1 And x <= number Then
            y = Val(InputBox("What is the new initive?", "Position Moving Input"))
            x1 = names(x)
            x2 = notes(x)
            RemoveSoul(x)
            txt1.Text = x1
            txt2.Text = y
            rtb1.Text = x2
            Insert()
        Else
            y1 = MessageBox.Show("There is no established player here.", "Non-Fatal Error", MessageBoxButtons.RetryCancel)
            If y1 = Windows.Forms.DialogResult.Retry Then
                MovePlayersBase(False)
            Else
                y1 = MessageBox.Show("Reinput Data?", "Non-Fatal Error", MessageBoxButtons.YesNo)
                If y1 = Windows.Forms.DialogResult.Yes Then
                    MovePlayersBase(True)
                End If
            End If
            ClearAll()
        End If

        If names(1) <> "" Then
            DisplayInt()
        End If

    End Sub

    Private Sub MovePlayersOutOfTheWay(ByVal i As Integer)

        For k = 29 To i Step -1
            names(k + 1) = names(k)
            initive(k + 1) = initive(k)
            notes(k + 1) = notes(k)
        Next

    End Sub

    Private Sub MovePlayers()

        temp = names(1)
        tempint = initive(1)
        tempstr = notes(1)
        For i = 1 To (number - 1)
            names(i) = names(i + 1)
            initive(i) = initive(i + 1)
            notes(i) = notes(i + 1)
        Next
        names(number) = temp
        initive(number) = tempint
        notes(number) = tempstr

    End Sub

    Private Sub RemoveSoul(ByVal pos)

        Dim y As Integer = 0

        If pos >= 1 And pos <= number Then
            If pos < MAX Then
                For i = pos To number - 1
                    names(i) = names(i + 1)
                    initive(i) = initive(i + 1)
                    notes(i) = notes(i + 1)
                Next
            End If
            names(number) = ""
            initive(number) = 0
            notes(number) = ""
            MakeVisibleYesNo(False)
            number -= 1
            ClearAll()
            MoveLabels()
            If names(1) <> "" Then
                DisplayInt()
            End If
        Else
            y = MessageBox.Show("There is no established player here.", "Non-Fatal Error", MessageBoxButtons.RetryCancel)
            If y = Windows.Forms.DialogResult.Retry Then
                RemoveSoul(pos)
            Else
                y = MessageBox.Show("Reinput Data?", "Non-Fatal Error", MessageBoxButtons.YesNo)
                If y = Windows.Forms.DialogResult.Yes Then
                    RemoveSoul(Val(InputBox("Which position will you remove?", "Position Removal Input")))
                End If
            End If
        End If

    End Sub

    Private Sub MovePlayersBack()

        Dim tempintb As Integer = 0

        For k = MAX To 1 Step -1
            If names(k) <> "" Then
                tempintb = k
                Exit For
            End If
        Next
        temp = names(tempintb)
        tempint = initive(tempintb)
        tempstr = notes(tempintb)
        For i = tempintb To 1 Step -1
            names(i) = names(i - 1)
            initive(i) = initive(i - 1)
            notes(i) = notes(i - 1)
        Next
        names(1) = temp
        initive(1) = tempint
        notes(1) = tempstr

    End Sub

    Private Sub MoveLabels()

        Label1.Text = names(1)
        Label2.Text = names(2)
        Label3.Text = names(3)
        Label4.Text = names(4)
        Label5.Text = names(5)
        Label6.Text = names(6)
        Label7.Text = names(7)
        Label8.Text = names(8)
        Label9.Text = names(9)
        Label10.Text = names(10)
        Label11.Text = names(11)
        Label12.Text = names(12)
        Label13.Text = names(13)
        Label14.Text = names(14)
        Label15.Text = names(15)
        Label16.Text = names(16)
        Label17.Text = names(17)
        Label18.Text = names(18)
        Label19.Text = names(19)
        Label20.Text = names(20)
        Label21.Text = names(21)
        Label22.Text = names(22)
        Label23.Text = names(23)
        Label24.Text = names(24)
        Label25.Text = names(25)
        Label26.Text = names(26)
        Label27.Text = names(27)
        Label28.Text = names(28)
        Label29.Text = names(29)
        Label30.Text = names(30)
        ClearAll()

    End Sub

    Private Sub DisplayInt()

        txt3.Text = initive(1)
        rtb1.Text = notes(1)

    End Sub

    Private Sub ClearAll()

        txt1.Clear()
        txt2.Clear()
        txt3.Clear()
        txt4.Clear()
        rtb1.Clear()

    End Sub

End Class
