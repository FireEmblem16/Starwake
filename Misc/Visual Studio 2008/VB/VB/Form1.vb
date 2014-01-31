'Don Nye
'
'This is a store program

Public Class frm1

    Public Sub New(Optional ByRef Selected As Integer = 0)

        InitializeComponent()

        If Selected = 0 Then
            rad1.Checked = False
            rad2.Checked = False
        ElseIf Selected = 1 Then
            rad1.Checked = True
            rad2.Checked = False
        ElseIf Selected = 2 Then
            rad1.Checked = False
            rad2.Checked = True
        End If

    End Sub

    Private Sub btn1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn1.Click

        If Not (rad1.Checked Or rad2.Checked) Then
            MessageBox.Show("You have not selected a type of computer", "Non-Fatal Error", MessageBoxButtons.OK)
        ElseIf rad1.Checked Then

        ElseIf rad2.Checked Then

        End If

    End Sub

    Private Sub btn2_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn2.Click

        End

    End Sub

End Class
