@setlocal enableextensions
@cd /d "%~dp0"
SET JAVA=.\jre64\bin\java.exe
SET _JAVA_OPTIONS=
SET MAIN=zombie.javamods.Main
FOR /F "tokens=* USEBACKQ" %%a in (`%JAVA% -cp javamods.jar %MAIN% -bootstrap`) do (SET JAVAMODS_CLASSPATH=%%a)
SET CLASSPATH=.;.\*;%JAVAMODS_CLASSPATH%
%JAVA% -XX:+UseConcMarkSweepGC -XX:-CreateMinidumpOnCrash -XX:-OmitStackTraceInFastThrow -Xms2048m -Xmx2048m -Djava.library.path=./ -cp %CLASSPATH% %MAIN% -server
PAUSE
