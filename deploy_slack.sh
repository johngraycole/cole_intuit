#/bin/sh
set -x


# TOMCAT_PATH="/var/lib/tomcat6/webapps/"
TOMCAT_PATH="/usr/share/apache-tomcat-6.0.36/webapps/"
PROJECT="Cole_Intuit"

# sudo service tomcat6 stop
sudo /etc/rc.d/rc.tomcat stop
sudo rm -rf ${TOMCAT_PATH}${PROJECT}*
sudo cp ${PROJECT}.war ${TOMCAT_PATH}
# sudo service tomcat6 start
sudo /etc/rc.d/rc.tomcat start

