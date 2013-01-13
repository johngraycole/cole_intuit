#/bin/sh
set -x

TOMCAT_PATH="/usr/share/apache-tomcat-6.0.36/webapps/"
PROJECT="Cole_Intuit"

sudo /etc/rc.d/rc.tomcat stop
sudo rm -rf ${TOMCAT_PATH}${PROJECT}*
cp ${PROJECT}.war ${TOMCAT_PATH}
sudo /etc/rc.d/rc.tomcat start

