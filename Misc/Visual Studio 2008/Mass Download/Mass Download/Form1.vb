Imports System.IO

Public Class frm1

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click

        Try
            Dim out As StreamWriter
            Dim filein As StreamReader
            Dim num As String = "", line As String = "", line2 As String = "//" & TextBox6.Text & "/"
            Dim temp As Integer = 0, go As Boolean = True, yes As Boolean = True
            Dim i As String = TextBox6.Text
            Dim lastchapter As Double = TextBox1.Text
            Dim title As String = TextBox2.Text
            Dim firstpage As String = TextBox3.Text
            Dim website As String = TextBox4.Text
            Dim skip As Integer = 0

            While go
                skip = 0

                If Not line2.Contains("/" & i & "/") Then
                    i = line2.Substring(line2.IndexOf("/", 1) + 1, line2.IndexOf("/", line2.IndexOf("/", 1) + 1) - line2.IndexOf("/", 1) - 1)
                    TextBox1.Text = i
                    If i > lastchapter Then Return
                End If

                If yes Then
                    line = ControlChars.Quote & "C:\Program Files (x86)\GnuWin32\bin\wget" & ControlChars.Quote & " --output-document=" & ControlChars.Quote & "index" & ".html" & ControlChars.Quote & " " & ControlChars.Quote & website & firstpage & ControlChars.Quote
                    yes = False
                Else
                    line = ControlChars.Quote & "C:\Program Files (x86)\GnuWin32\bin\wget" & ControlChars.Quote & " --output-document=" & ControlChars.Quote & "index" & ".html" & ControlChars.Quote & " " & ControlChars.Quote & website & line2 & ControlChars.Quote
                End If

                out = File.CreateText("Download.bat")
                out.WriteLine(line)
                out.Close()
                Shell("Download.bat", AppWinStyle.NormalFocus, True, -1)

                filein = File.OpenText("index.html")

                yes = True

                If chk1.Checked Then
                    While yes
                        num = filein.ReadLine()

                        If num.Contains(".jpg") And num.Contains("<a ") Then
                            line = num

                            If skip >= TextBox5.Text Then
                                line2 = line.Substring(line.IndexOf(ControlChars.Quote, line.IndexOf("<a ")) + 2, line.IndexOf(ControlChars.Quote, line.IndexOf("<a ") + 10) - line.IndexOf(ControlChars.Quote, line.IndexOf("<a ")) - 2)
                                line = line.Substring(line.IndexOf("http", line.IndexOf("src")), line.IndexOf("alt") - line.IndexOf("http") - 2)
                                yes = False
                            Else
                                skip += 1
                            End If
                        End If
                    End While

                    filein.Close()

                    If Not My.Computer.FileSystem.DirectoryExists("C:\Users\The Science Guy\Desktop\Books\Manga\" & title & "\" & title & " Chapter " & i) Then
                        MkDir("C:\Users\The Science Guy\Desktop\Books\Manga\" & title & "\" & title & " Chapter " & i)
                    End If

                    line = ControlChars.Quote & "C:\Program Files (x86)\GnuWin32\bin\wget" & ControlChars.Quote & " --output-document=" & ControlChars.Quote & "C:\Users\The Science Guy\Desktop\Books\Manga\" & title & "\" & title & " Chapter " & i & "\" & line.Substring(line.Substring(0, line.Length - 2).LastIndexOf("/") + 1) & ControlChars.Quote & " " & ControlChars.Quote & line & ControlChars.Quote
                    out = File.CreateText("Download.bat")
                    out.WriteLine(line)
                    out.Close()

                    Shell("Download.bat", AppWinStyle.NormalFocus, True, -1)
                Else
                    While yes
                        num = filein.ReadLine()
                        If num <> "" And Not num.Contains(".jpg") And num.Contains("a href=") Then line2 = num.Substring(num.IndexOf("/"), num.Length - num.IndexOf("/") - 2)
                        line = num
                        If line.Contains(".jpg") Then
                            If skip >= TextBox5.Text Then
                                line = line.Substring(line.IndexOf("http"), line.IndexOf("alt") - line.IndexOf("http") - 2)
                                If line2.Contains("%") Then
                                    line2 = line2.Substring(0, line2.IndexOf("%")) & "!" & line2.Substring(line2.IndexOf("%") + 3)
                                Else
                                    'Ignore
                                End If
                                yes = False
                            Else
                                skip += 1
                            End If
                        End If
                    End While

                    filein.Close()

                    If Not My.Computer.FileSystem.DirectoryExists("C:\Users\The Science Guy\Desktop\Books\Manga\" & title & "\" & title & " Chapter " & i) Then
                        MkDir("C:\Users\The Science Guy\Desktop\Books\Manga\" & title & "\" & title & " Chapter " & i)
                    End If

                    line = ControlChars.Quote & "C:\Program Files (x86)\GnuWin32\bin\wget" & ControlChars.Quote & " --output-document=" & ControlChars.Quote & "C:\Users\The Science Guy\Desktop\Books\Manga\" & title & "\" & title & " Chapter " & i & "\" & line.Substring(line.Substring(0, line.Length - 2).LastIndexOf("/") + 1) & ControlChars.Quote & " " & ControlChars.Quote & line & ControlChars.Quote
                    out = File.CreateText("Download.bat")
                    out.WriteLine(line)
                    out.Close()

                    Shell("Download.bat", AppWinStyle.NormalFocus, True, -1)
                End If
            End While
        Catch
        End Try

        Return

    End Sub

End Class
