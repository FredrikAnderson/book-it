@echo off
@IF EXIST "%~dp0\node\npm.cmd" (
  "%~dp0\node\npm.cmd" %*
) else (
	echo %~dp0\node\npm.cmd, not found.
)