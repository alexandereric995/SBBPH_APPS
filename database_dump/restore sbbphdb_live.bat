@Echo off  
echo [%date% %time%] Starting delete previous db....  
mysql -u root -proot -h localhost -e "drop database sbbphdb_live ; create database sbbphdb_live ;"
echo [%date% %time%] done drop db and create new 
echo [%date% %time%] Starting restore to local db....  
mysql -u root -proot sbbphdb_live < sbbphdb.sql  
echo [%date% %time%] done.  
pause  