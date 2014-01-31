Public Class frm1

    Private Sub frm1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

        cmbo1.Text = "Kana"
        cmbo2.Text = "Romanji"
        Me.MaximizeBox = False

    End Sub

    Private Sub btn1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn1.Click

        translate()

    End Sub

    Private Sub btn2_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn2.Click

        If frm2.Visible = True Then
            frm2.Hide()
        Else
            frm2.Show()
        End If

    End Sub

    Private Sub btn3_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btn3.Click

        If frm3.Visible = True Then
            frm3.Hide()
        Else
            frm3.Show()
        End If

    End Sub

    Private Sub translate()

        rtb2.Clear()

        If cmbo1.Text = cmbo2.Text Then
            rtb2.Text = rtb1.Text
        Else
            If cmbo1.Text = "Romanji" Then
                If cmbo2.Text = "Kana" Then

                ElseIf cmbo2.Text = "English" Then

                End If
            ElseIf cmbo1.Text = "Kana" Then
                If cmbo2.Text = "Romanji" Then
                    KtoR()
                ElseIf cmbo2.Text = "English" Then

                End If
            ElseIf cmbo1.Text = "Enlgish" Then
                If cmbo2.Text = "Kana" Then

                ElseIf cmbo2.Text = "Romanji" Then

                End If
            End If
        End If

    End Sub

    Private Sub KtoR()

        Dim line As String = ""
        Dim place As Integer = 0

        line = rtb1.Text

        While place < line.Length
            Try
                tKtoR(True, line, place)
            Catch ex As Exception
                tKtoR(False, line, place)
            End Try
        End While

    End Sub

    Private Sub tKtoR(ByVal check As Boolean, ByRef line As String, ByRef place As Integer)

        If line.Substring(place, 1) = "あ" Or line.Substring(place, 1) = "ア" Then
            rtb2.Text += "a"
        ElseIf line.Substring(place, 1) = "い" Or line.Substring(place, 1) = "イ" Then
            rtb2.Text += "i"
        ElseIf line.Substring(place, 1) = "う" Or line.Substring(place, 1) = "ウ" Then
            rtb2.Text += "u"
        ElseIf line.Substring(place, 1) = "え" Or line.Substring(place, 1) = "エ" Then
            rtb2.Text += "e"
        ElseIf line.Substring(place, 1) = "お" Or line.Substring(place, 1) = "オ" Then
            rtb2.Text += "o"
        ElseIf line.Substring(place, 1) = "か" Or line.Substring(place, 1) = "カ" Then
            rtb2.Text += "ka"
        ElseIf line.Substring(place, 1) = "く" Or line.Substring(place, 1) = "ク" Then
            rtb2.Text += "ku"
        ElseIf line.Substring(place, 1) = "け" Or line.Substring(place, 1) = "ケ" Then
            rtb2.Text += "ke"
        ElseIf line.Substring(place, 1) = "こ" Or line.Substring(place, 1) = "コ" Then
            rtb2.Text += "ko"
        ElseIf line.Substring(place, 1) = "が" Or line.Substring(place, 1) = "ガ" Then
            rtb2.Text += "ga"
        ElseIf line.Substring(place, 1) = "ぐ" Or line.Substring(place, 1) = "グ" Then
            rtb2.Text += "gu"
        ElseIf line.Substring(place, 1) = "げ" Or line.Substring(place, 1) = "ゲ" Then
            rtb2.Text += "ge"
        ElseIf line.Substring(place, 1) = "ご" Or line.Substring(place, 1) = "ゴ" Then
            rtb2.Text += "go"
        ElseIf line.Substring(place, 1) = "さ" Or line.Substring(place, 1) = "サ" Then
            rtb2.Text += "sa"
        ElseIf line.Substring(place, 1) = "す" Or line.Substring(place, 1) = "ス" Then
            rtb2.Text += "su"
        ElseIf line.Substring(place, 1) = "せ" Or line.Substring(place, 1) = "セ" Then
            rtb2.Text += "se"
        ElseIf line.Substring(place, 1) = "そ" Or line.Substring(place, 1) = "ソ" Then
            rtb2.Text += "so"
        ElseIf line.Substring(place, 1) = "ざ" Or line.Substring(place, 1) = "ザ" Then
            rtb2.Text += "za"
        ElseIf line.Substring(place, 1) = "ず" Or line.Substring(place, 1) = "ズ" Then
            rtb2.Text += "zu"
        ElseIf line.Substring(place, 1) = "ぜ" Or line.Substring(place, 1) = "ゼ" Then
            rtb2.Text += "ze"
        ElseIf line.Substring(place, 1) = "ぞ" Or line.Substring(place, 1) = "ゾ" Then
            rtb2.Text += "zo"
        ElseIf line.Substring(place, 1) = "た" Or line.Substring(place, 1) = "タ" Then
            rtb2.Text += "ta"
        ElseIf line.Substring(place, 1) = "つ" Or line.Substring(place, 1) = "ツ" Then
            rtb2.Text += "tsu"
        ElseIf line.Substring(place, 1) = "て" Or line.Substring(place, 1) = "テ" Then
            rtb2.Text += "te"
        ElseIf line.Substring(place, 1) = "と" Or line.Substring(place, 1) = "ト" Then
            rtb2.Text += "to"
        ElseIf line.Substring(place, 1) = "だ" Or line.Substring(place, 1) = "ダ" Then
            rtb2.Text += "da"
        ElseIf line.Substring(place, 1) = "ぢ" Or line.Substring(place, 1) = "ヂ" Then
            rtb2.Text += "ji"
        ElseIf line.Substring(place, 1) = "づ" Or line.Substring(place, 1) = "ヅ" Then
            rtb2.Text += "zu"
        ElseIf line.Substring(place, 1) = "で" Or line.Substring(place, 1) = "デ" Then
            rtb2.Text += "de"
        ElseIf line.Substring(place, 1) = "ど" Or line.Substring(place, 1) = "ド" Then
            rtb2.Text += "do"
        ElseIf line.Substring(place, 1) = "な" Or line.Substring(place, 1) = "ナ" Then
            rtb2.Text += "na"
        ElseIf line.Substring(place, 1) = "ぬ" Or line.Substring(place, 1) = "ヌ" Then
            rtb2.Text += "nu"
        ElseIf line.Substring(place, 1) = "ね" Or line.Substring(place, 1) = "ネ" Then
            rtb2.Text += "ne"
        ElseIf line.Substring(place, 1) = "の" Or line.Substring(place, 1) = "ノ" Then
            rtb2.Text += "no"
        ElseIf line.Substring(place, 1) = "は" Or line.Substring(place, 1) = "ハ" Then
            rtb2.Text += "ha"
        ElseIf line.Substring(place, 1) = "ふ" Or line.Substring(place, 1) = "フ" Then
            rtb2.Text += "fu"
        ElseIf line.Substring(place, 1) = "へ" Or line.Substring(place, 1) = "ヘ" Then
            rtb2.Text += "he"
        ElseIf line.Substring(place, 1) = "ほ" Or line.Substring(place, 1) = "ホ" Then
            rtb2.Text += "ho"
        ElseIf line.Substring(place, 1) = "ば" Or line.Substring(place, 1) = "バ" Then
            rtb2.Text += "ba"
        ElseIf line.Substring(place, 1) = "ぶ" Or line.Substring(place, 1) = "ブ" Then
            rtb2.Text += "bu"
        ElseIf line.Substring(place, 1) = "べ" Or line.Substring(place, 1) = "ベ" Then
            rtb2.Text += "be"
        ElseIf line.Substring(place, 1) = "ぼ" Or line.Substring(place, 1) = "ボ" Then
            rtb2.Text += "bo"
        ElseIf line.Substring(place, 1) = "ぱ" Or line.Substring(place, 1) = "パ" Then
            rtb2.Text += "pa"
        ElseIf line.Substring(place, 1) = "ぷ" Or line.Substring(place, 1) = "プ" Then
            rtb2.Text += "pu"
        ElseIf line.Substring(place, 1) = "ぺ" Or line.Substring(place, 1) = "ペ" Then
            rtb2.Text += "pe"
        ElseIf line.Substring(place, 1) = "ぽ" Or line.Substring(place, 1) = "ポ" Then
            rtb2.Text += "po"
        ElseIf line.Substring(place, 1) = "ま" Or line.Substring(place, 1) = "マ" Then
            rtb2.Text += "ma"
        ElseIf line.Substring(place, 1) = "む" Or line.Substring(place, 1) = "ム" Then
            rtb2.Text += "mu"
        ElseIf line.Substring(place, 1) = "め" Or line.Substring(place, 1) = "メ" Then
            rtb2.Text += "me"
        ElseIf line.Substring(place, 1) = "も" Or line.Substring(place, 1) = "モ" Then
            rtb2.Text += "mo"
        ElseIf line.Substring(place, 1) = "や" Or line.Substring(place, 1) = "ヤ" Then
            rtb2.Text += "ya"
        ElseIf line.Substring(place, 1) = "ゆ" Or line.Substring(place, 1) = "ユ" Then
            rtb2.Text += "yu"
        ElseIf line.Substring(place, 1) = "よ" Or line.Substring(place, 1) = "ヨ" Then
            rtb2.Text += "yo"
        ElseIf line.Substring(place, 1) = "ら" Or line.Substring(place, 1) = "ラ" Then
            rtb2.Text += "ra"
        ElseIf line.Substring(place, 1) = "る" Or line.Substring(place, 1) = "ル" Then
            rtb2.Text += "ru"
        ElseIf line.Substring(place, 1) = "れ" Or line.Substring(place, 1) = "レ" Then
            rtb2.Text += "re"
        ElseIf line.Substring(place, 1) = "ろ" Or line.Substring(place, 1) = "ロ" Then
            rtb2.Text += "ro"
        ElseIf line.Substring(place, 1) = "わ" Or line.Substring(place, 1) = "ワ" Then
            rtb2.Text += "wa"
        ElseIf line.Substring(place, 1) = "を" Or line.Substring(place, 1) = "ヲ" Then
            rtb2.Text += "wo"
        ElseIf line.Substring(place, 1) = "ん" Or line.Substring(place, 1) = "ン" Then
            rtb2.Text += "n"
        ElseIf line.Substring(place, 1) = "っ" Then
            rtb2.Text += "t"
        ElseIf line.Substring(place, 1) = " " Then
            rtb2.Text += " "
        ElseIf line.Substring(place, 1) = "!" Then
            rtb2.Text += "!"
        ElseIf line.Substring(place, 1) = "-" Then
            rtb2.Text += "-"
        ElseIf line.Substring(place, 1) = "?" Then
            rtb2.Text += "?"
        End If

        If line.Substring(place, 1) = "き" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "kya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "kyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "kyo"
                    place += 1
                Else
                    rtb2.Text += "ki"
                End If
            Else
                rtb2.Text += "ki"
            End If
        ElseIf line.Substring(place, 1) = "ぎ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "gya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "gyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "gyo"
                    place += 1
                Else
                    rtb2.Text += "gi"
                End If
            Else
                rtb2.Text += "gi"
            End If
        ElseIf line.Substring(place, 1) = "し" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "sha"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "shu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "sho"
                    place += 1
                Else
                    rtb2.Text += "shi"
                End If
            Else
                rtb2.Text += "shi"
            End If
        ElseIf line.Substring(place, 1) = "じ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "ja"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "ju"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "jo"
                    place += 1
                Else
                    rtb2.Text += "ji"
                End If
            Else
                rtb2.Text += "ji"
            End If
        ElseIf line.Substring(place, 1) = "ち" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "cha"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "chu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "cho"
                    place += 1
                Else
                    rtb2.Text += "chi"
                End If
            Else
                rtb2.Text += "chi"
            End If
        ElseIf line.Substring(place, 1) = "に" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "nya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "nyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "nyo"
                    place += 1
                Else
                    rtb2.Text += "ni"
                End If
            Else
                rtb2.Text += "ni"
            End If
        ElseIf line.Substring(place, 1) = "ひ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "hya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "hyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "hyo"
                    place += 1
                Else
                    rtb2.Text += "hi"
                End If
            Else
                rtb2.Text += "hi"
            End If
        ElseIf line.Substring(place, 1) = "び" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "bya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "byu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "byo"
                    place += 1
                Else
                    rtb2.Text += "bi"
                End If
            Else
                rtb2.Text += "bi"
            End If
        ElseIf line.Substring(place, 1) = "ぴ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "pya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "pyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "pyo"
                    place += 1
                Else
                    rtb2.Text += "pi"
                End If
            Else
                rtb2.Text += "pi"
            End If
        ElseIf line.Substring(place, 1) = "み" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "mya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "myu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "myo"
                    place += 1
                Else
                    rtb2.Text += "mi"
                End If
            Else
                rtb2.Text += "mi"
            End If
        ElseIf line.Substring(place, 1) = "り" Then
            If check Then
                If line.Substring(place + 1, 1) = "ゃ" Then
                    rtb2.Text += "rya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ゅ" Then
                    rtb2.Text += "ryu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ょ" Then
                    rtb2.Text += "ryo"
                    place += 1
                Else
                    rtb2.Text += "ri"
                End If
            Else
                rtb2.Text += "ri"
            End If
        ElseIf line.Substring(place, 1) = "キ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "kya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "kyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "kyo"
                    place += 1
                Else
                    rtb2.Text += "ki"
                End If
            Else
                rtb2.Text += "ki"
            End If
        ElseIf line.Substring(place, 1) = "ギ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "gya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "gyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "gyo"
                    place += 1
                Else
                    rtb2.Text += "gi"
                End If
            Else
                rtb2.Text += "gi"
            End If
        ElseIf line.Substring(place, 1) = "シ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "sha"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "shu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "sho"
                    place += 1
                Else
                    rtb2.Text += "shi"
                End If
            Else
                rtb2.Text += "shi"
            End If
        ElseIf line.Substring(place, 1) = "ジ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "ja"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "ju"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "jo"
                    place += 1
                Else
                    rtb2.Text += "ji"
                End If
            Else
                rtb2.Text += "ji"
            End If
        ElseIf line.Substring(place, 1) = "チ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "cha"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "chu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "cho"
                    place += 1
                Else
                    rtb2.Text += "chi"
                End If
            Else
                rtb2.Text += "chi"
            End If
        ElseIf line.Substring(place, 1) = "ニ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "nya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "nyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "nyo"
                    place += 1
                Else
                    rtb2.Text += "ni"
                End If
            Else
                rtb2.Text += "ni"
            End If
        ElseIf line.Substring(place, 1) = "ヒ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "hya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "hyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "hyo"
                    place += 1
                Else
                    rtb2.Text += "hi"
                End If
            Else
                rtb2.Text += "hi"
            End If
        ElseIf line.Substring(place, 1) = "ビ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "bya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "byu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "byo"
                    place += 1
                Else
                    rtb2.Text += "bi"
                End If
            Else
                rtb2.Text += "bi"
            End If
        ElseIf line.Substring(place, 1) = "ピ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "pya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "pyu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "pyo"
                    place += 1
                Else
                    rtb2.Text += "pi"
                End If
            Else
                rtb2.Text += "pi"
            End If
        ElseIf line.Substring(place, 1) = "ミ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "mya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "myu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "myo"
                    place += 1
                Else
                    rtb2.Text += "mi"
                End If
            Else
                rtb2.Text += "mi"
            End If
        ElseIf line.Substring(place, 1) = "リ" Then
            If check Then
                If line.Substring(place + 1, 1) = "ャ" Then
                    rtb2.Text += "rya"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ュ" Then
                    rtb2.Text += "ryu"
                    place += 1
                ElseIf line.Substring(place + 1, 1) = "ョ" Then
                    rtb2.Text += "ryo"
                    place += 1
                Else
                    rtb2.Text += "ri"
                End If
            Else
                rtb2.Text += "ri"
            End If
        End If

        place += 1

    End Sub

End Class
