@setlocal enableextensions
@cd /d "%~dp0"
SET JAVA=.\jre64\bin\java.exe
SET _JAVA_OPTIONS=
SET MAIN=javamods.Main
FOR /F "tokens=* USEBACKQ" %%a in (`%JAVA% -cp javamods.jar %MAIN% -bootstrap`) do (SET JAVAMODS_CLASSPATH=%%a)
SET CLASSPATH=.;.\*;%JAVAMODS_CLASSPATH%
%JAVA% -Djava.awt.headless=true -Dzomboid.steam=1 -Dzomboid.znetlog=1 -XX:-CreateCoredumpOnCrash -XX:-OmitStackTraceInFastThrow -XX:+UseZGC -Xms1800m -Xmx2048m -Djava.library.path=./win64/;./ -cp %CLASSPATH% %MAIN%
PAUSE
