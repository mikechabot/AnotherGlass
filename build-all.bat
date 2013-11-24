net stop ApacheTomcat7
RD /S /Q "D:/Development/apache-tomcat-7.0.34/webapps/
RD /S /Q "D:/Development/apache-tomcat-7.0.34/work/
MKDIR "D:/Development/apache-tomcat-7.0.34/webapps/
call build-war.bat
copy "D:\Development\workspaces\AnotherGlass\AnotherGlass\build\ROOT.war" "D:\Development\apache-tomcat-7.0.34\webapps"
net start ApacheTomcat7