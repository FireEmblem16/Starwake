Imports System.IO

Public Class Form1

    Private Sub Timer1_Tick(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Timer1.Tick

        Dim rand As New Random

        If rand.Next(0, 10) > 7 Then
            TextBox1.Text = rand.Next(0, 100)
        ElseIf rand.Next(0, 10) > 7 Then
            TextBox1.Text = rand.Next(100, 999)
        ElseIf rand.Next(0, 10) > 7 Then
            TextBox1.Text = rand.Next(1000, 9999)
        ElseIf rand.Next(0, 10) > 7 Then
            TextBox1.Text = rand.Next(10000, 100000000)
        End If

        Return

    End Sub

End Class
