@IF EXIST "%~dp0\node\node.exe" (
  "%~dp0\node\node.exe"  "%~dp0\node_modules\@angular\cli\bin\ng" %*
) ELSE (
	echo %~dp0\node\node.exe, not found.
)