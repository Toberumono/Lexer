@echo OFF
SET startpath="%~dp0"

cd ..\
powershell -Command "(New-Object Net.WebClient).DownloadFile('https://github.com/Toberumono/Structures/archive/master.zip', 'structures.zip')"
"%JAVA_HOME%\bin\jar" xf structures.zip
del structures.zip
Rename Structures-master structures

cd structures
if exist setup_project.bat (call setup_project.bat) else (call ant)

cd ..\
powershell -Command "(New-Object Net.WebClient).DownloadFile('https://github.com/Toberumono/Utils/archive/master.zip', 'utils.zip')"
"%JAVA_HOME%\bin\jar" xf utils.zip
del utils.zip
Rename Utils-master utils

cd utils
if exist setup_project.bat (call setup_project.bat) else (call ant)

cd ..\
cd %startpath%
call ant
