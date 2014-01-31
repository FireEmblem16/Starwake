Imports System.IO

Public Class frm1

    Private Sub Form1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

        Return

    End Sub

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click

        Dim out As StreamWriter
        Dim filein As StreamReader
        Dim num As String = "", line As String = ""
        Dim temp As Integer = 0, go As Boolean = True

        For i As Integer = 111 To 616
            If i = 404 Then i = 405
            Me.Text = i.ToString()

            line = ControlChars.Quote & "C:\Program Files (x86)\GnuWin32\bin\wget" & ControlChars.Quote & " --output-document=" & ControlChars.Quote & "index" & ".html" & ControlChars.Quote & " http://www.xkcd.org/" & i & "/"

            out = File.CreateText("Download.bat")
            out.WriteLine(line)
            out.Close()

            Shell("Download.bat", AppWinStyle.NormalFocus, True, -1)

            filein = File.OpenText("index.html")

            While go
                line = filein.ReadLine()

                If line.Contains(".png") Then
                    If temp = 1 Then
                        go = False

                        line = line.Substring(line.IndexOf("http"), line.IndexOf(".png") - line.IndexOf("http") + 4)
                    Else
                        temp = 1
                    End If
                ElseIf filein.EndOfStream Then
                    go = False
                End If
            End While

            go = True
            temp = 0
            filein.Close()

            line = ControlChars.Quote & "C:\Program Files (x86)\GnuWin32\bin\wget" & ControlChars.Quote & " --output-document=" & ControlChars.Quote & "C:\Users\The Science Guy\Desktop\Books\Comics\xkcd\" & line.Substring(28) & ControlChars.Quote & " " & line

            out = File.CreateText("Download.bat")
            out.WriteLine(line)
            out.Close()

            Shell("Download.bat", AppWinStyle.NormalFocus, True, -1)
        Next

        Return

    End Sub

End Class
