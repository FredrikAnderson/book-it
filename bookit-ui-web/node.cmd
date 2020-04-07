@echo off
@IF EXIST "%~dp0\node\node.exe" (
  "%~dp0\node\node.exe" %*
) else (
	echo %~dp0\node\node.exe, not found.
)