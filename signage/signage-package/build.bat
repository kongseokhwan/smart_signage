call mvn clean package -DskipTests -Pproduction
echo OK Maven
del /Q signage-cms-0.0.1-SNAPSHOT.jar
copy ..\signage-cms\target\signage-cms-0.0.1-SNAPSHOT.jar .
echo Copy signage-cms
del /Q signage-tenant-0.0.1-SNAPSHOT.jar
copy ..\signage-tenant\target\signage-tenant-0.0.1-SNAPSHOT.jar .
echo Copy signage-tenant
del /Q wowza_key.pem
copy ..\signage-cms\wowza_key.pem .
echo Copy wowza_key.pem
