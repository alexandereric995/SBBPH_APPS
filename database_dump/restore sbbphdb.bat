@Echo off  
echo [%date% %time%] Starting delete previous db....  
mysql -u root -proot -h localhost -e "drop database sbbphdb ; create database sbbphdb ;"
echo [%date% %time%] done drop db and create new 
echo [%date% %time%] Starting restore to local db....  
mysql -u root -proot sbbphdb < sbbphdb.sql  
echo [%date% %time%] done.  
pause  