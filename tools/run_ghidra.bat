@echo off
setlocal
set LK_GH=D:\tools\ghidra\support\analyzeHeadless.bat
set LK_PROJ=D:\RedmiTurbo5Max_hyperos4_lk_patcher\ghidra_proj
set LK_NAME=LKProj
set LK_SCRIPTS=D:\RedmiTurbo5Max_hyperos4_lk_patcher\tools\ghidra_scripts
set LK_EXP=D:\RedmiTurbo5Max_hyperos4_lk_patcher\ghidra_export
set LK_BIN=D:\RedmiTurbo5Max_hyperos4_lk_patcher\extracted

if not exist "%LK_PROJ%" mkdir "%LK_PROJ%"
if not exist "%LK_EXP%" mkdir "%LK_EXP%"

echo ==== PASS1 h3_orig
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -import "%LK_BIN%\h3_orig_lk.bin" -processor AARCH64:LE:64:v8A -loader BinaryLoader -loader-baseAddr 0xFFFF000090700000 -scriptPath "%LK_SCRIPTS%" -postScript MarkCode.java
echo ==== PASS1 h3_orig exit=%ERRORLEVEL%

echo ==== PASS1 h3_patched
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -import "%LK_BIN%\h3_patched_lk.bin" -processor AARCH64:LE:64:v8A -loader BinaryLoader -loader-baseAddr 0xFFFF000090700000 -scriptPath "%LK_SCRIPTS%" -postScript MarkCode.java
echo ==== PASS1 h3_patched exit=%ERRORLEVEL%

echo ==== PASS1 h4_orig
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -import "%LK_BIN%\h4_orig_lk.bin" -processor AARCH64:LE:64:v8A -loader BinaryLoader -loader-baseAddr 0xFFFF000090700000 -scriptPath "%LK_SCRIPTS%" -postScript MarkCode.java
echo ==== PASS1 h4_orig exit=%ERRORLEVEL%

echo ==== PASS2 export
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -process h3_orig_lk.bin -noanalysis -scriptPath "%LK_SCRIPTS%" -postScript ExportDecomp.java "%LK_EXP%\h3_orig"
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -process h3_patched_lk.bin -noanalysis -scriptPath "%LK_SCRIPTS%" -postScript ExportDecomp.java "%LK_EXP%\h3_patched"
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -process h4_orig_lk.bin -noanalysis -scriptPath "%LK_SCRIPTS%" -postScript ExportDecomp.java "%LK_EXP%\h4_orig"

echo ==== DONE
