@echo off
echo Installing Git pre-commit hook...

REM 创建 .git/hooks 目录（如果不存在）
if not exist ".git\hooks" mkdir ".git\hooks"

REM 创建 pre-commit hook
echo @echo off > ".git\hooks\pre-commit.bat"
echo echo Running Spotless formatting... >> ".git\hooks\pre-commit.bat"
echo call mvn spotless:apply -q >> ".git\hooks\pre-commit.bat"
echo if %%errorlevel%% neq 0 ( >> ".git\hooks\pre-commit.bat"
echo     echo Code formatting failed! >> ".git\hooks\pre-commit.bat"
echo     exit /b 1 >> ".git\hooks\pre-commit.bat"
echo ^) >> ".git\hooks\pre-commit.bat"
echo git add . >> ".git\hooks\pre-commit.bat"
echo echo Code formatting completed! >> ".git\hooks\pre-commit.bat"
echo exit /b 0 >> ".git\hooks\pre-commit.bat"

REM 创建 Unix 风格的 pre-commit hook（兼容性）
echo #!/bin/sh > ".git\hooks\pre-commit"
echo echo "Running Spotless formatting..." >> ".git\hooks\pre-commit"
echo mvn spotless:apply -q >> ".git\hooks\pre-commit"
echo if [ $? -ne 0 ]; then >> ".git\hooks\pre-commit"
echo   echo "Code formatting failed!" >> ".git\hooks\pre-commit"
echo   exit 1 >> ".git\hooks\pre-commit"
echo fi >> ".git\hooks\pre-commit"
echo git add . >> ".git\hooks\pre-commit"
echo echo "Code formatting completed!" >> ".git\hooks\pre-commit"
echo exit 0 >> ".git\hooks\pre-commit"

echo Git hook installed successfully!
echo Now every git commit will automatically format your code.
pause