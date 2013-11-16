net stop ApacheTomcat7
RD /S /Q "D:/Development/apache-tomcat-7.0.34/webapps/
call build-war.bat
net start ApacheTomcat7