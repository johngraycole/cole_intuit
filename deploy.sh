#/bin/sh
set -x

TOMCAT_PATH="/var/lib/tomcat6/webapps/"
PROJECT="Cole_Intuit"

sudo service tomcat6 stop
sudo rm -rf ${TOMCAT_PATH}${PROJECT}*
sudo cp ${PROJECT}.war ${TOMCAT_PATH}
sudo service tomcat6 start


