Imports System.IO

Public Class Form1

    Private Sub Form1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

        Dim text As StreamReader, txt As StreamWriter, temp As String = "", i As Integer = 0, h As Integer = 0, tempb As String = 0

        text = File.OpenText("text.txt")
        txt = File.CreateText("txt.txt")

        While Not text.EndOfStream
            tempb = "glVertex3f("
            temp = text.ReadLine()
            For j As Integer = 1 To 3
                i = temp.IndexOf(";", h)
                tempb += temp.Substring(h, i - h) & "f"
                If j = 1 Or j = 2 Then
                    tempb += ","
                End If
                h = i + 2
            Next
            tempb += ");"
            h = 0
            txt.WriteLine(tempb)
        End While

        text.Close()
        txt.Close()
        End

    End Sub

End Class
