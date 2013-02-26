#/bin/sh
set -x

TOMCAT_PATH="/opt/tomcat/webapps/"
PROJECT="Cole_Intuit"

sudo /etc/init.d/tomcat stop
sudo rm -rf ${TOMCAT_PATH}${PROJECT}*
sudo cp ${PROJECT}.war ${TOMCAT_PATH}
sudo /etc/init.d/tomcat start

