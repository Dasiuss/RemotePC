if $CmdLine[1]="volumeMute" then
	Send("{VOLUME_MUTE}")
ElseIf $CmdLine[1]="volumeDown" Then
	Send("{VOLUME_DOWN}")
ElseIf $CmdLine[1]="volumeUp" Then
	Send("{VOLUME_UP}")
ElseIf $CmdLine[1]="mediaPlay" Then
	Send("{media_play_pause}")
ElseIf $CmdLine[1]="mediaStop" Then
	Send("{media_stop}")
ElseIf $CmdLine[1]="mediaPrev" Then
	Send("{media_prev}")
ElseIf $CmdLine[1]="mediaNext" Then
	Send("{media_prev}")
ElseIf $CmdLine[1]="shutdown" Then
	Shutdown(9)
ElseIf $CmdLine[1]="sendText" Then
	Send($CmdLine[2])
EndIf
