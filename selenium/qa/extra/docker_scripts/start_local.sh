#!/bin/bash
mvn clean verify -Dhuburl=http://"$HUB_HOST":"$HUB_PORT"/wd/hub -Dconfig=$CONFIG -Dbaseurl=$BASE_URL -Dbrowser=hub -Dbaseurl=$BASE_URL -Dserverauthname=$USERNAME -Dserverauthpass=$PASS
cp target/failsafe-reports/emailable-report.html /usr/src/report/