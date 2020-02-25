#!/bin/sh

mvn clean package -DskipTests -Pproduction
echo "OK Maven\n"
rm signage-cms-0.0.1-SNAPSHOT.jar
cp ../signage-cms/target/signage-cms-0.0.1-SNAPSHOT.jar .
echo "Copy signage-cms\n"
rm signage-tenant-0.0.1-SNAPSHOT.jar
cp ../signage-tenant/target/signage-tenant-0.0.1-SNAPSHOT.jar .
echo "Copy signage-tenant\n"
rm wowza_key.pem
cp ../signage-cms/wowza_key.pem .
echo "Copy wowza_key.pem\n"