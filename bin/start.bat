@echo off

echo start of sync ...

rem Check if we have a usable JDK
if "%JAVA_HOME%" == "" goto noJavaHome
if not "%JAVA_HOME%" == "" goto startSync


rem JAVA_HOME is null
:noJavaHome
echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.

pause
goto exit


rem start of sync
:startSync
pushd "%cd%"
cd ..
set DBWord_HOME=%cd%
popd
set  CLASSPATH=%DBWord_HOME%\lib\commons-lang-2.5.jar;
set  CLASSPATH=%CLASSPATH%%DBWord_HOME%\lib\freemarker-2.3.23.jar;
set  CLASSPATH=%CLASSPATH%%DBWord_HOME%\lib\mysql-connector-java-5.1.35.jar;

echo %CLASSPATH%
java -classpath %DBWord_HOME%\DBWord.jar;%CLASSPATH% com.db.DBWord.App %DBWord_HOME%

echo over of sync ...
pause