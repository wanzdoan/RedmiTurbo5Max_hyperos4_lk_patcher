@echo off
setlocal
set LK_GH=D:\tools\ghidra\support\analyzeHeadless.bat
set LK_PROJ=D:\RedmiTurbo5Max_hyperos4_lk_patcher\ghidra_proj
set LK_NAME=LKProj
set LK_SCRIPTS=D:\RedmiTurbo5Max_hyperos4_lk_patcher\tools\ghidra_scripts
set LK_EXP=D:\RedmiTurbo5Max_hyperos4_lk_patcher\ghidra_export

call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -process h3_orig_lk.bin -noanalysis -scriptPath "%LK_SCRIPTS%" -postScript ExportDecomp.java "%LK_EXP%\h3_orig"
echo ==== h3_orig export exit=%ERRORLEVEL%
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -process h3_patched_lk.bin -noanalysis -scriptPath "%LK_SCRIPTS%" -postScript ExportDecomp.java "%LK_EXP%\h3_patched"
echo ==== h3_patched export exit=%ERRORLEVEL%
call "%LK_GH%" "%LK_PROJ%" %LK_NAME% -process h4_orig_lk.bin -noanalysis -scriptPath "%LK_SCRIPTS%" -postScript ExportDecomp.java "%LK_EXP%\h4_orig"
echo ==== h4_orig export exit=%ERRORLEVEL%
echo ==== DONE
