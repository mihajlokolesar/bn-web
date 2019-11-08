#!/bin/bash
mvn clean verify -P hub -Dhuburl=http://"$HUB_HOST":"$HUB_PORT"/wd/hub -Dbaseurl=$BASE_URL -Dserverauthname=$USERNAME -Dserverauthpass=$PASS
cp target/failsafe-reports/emailable-report.html /usr/src/report/