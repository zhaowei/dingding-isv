## install jenkins

wget -q -O - https://jenkins-ci.org/debian/jenkins-ci.org.key | sudo apt-key add -
sudo sh -c 'echo deb http://pkg.jenkins-ci.org/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt-get update
sudo apt-get install jenkins

/etc/default/jenkins 进入修改端口 HTTP_PORT=9000
/var/lib/jenkins 安装目录

sudo su jenkins -s /bin/bash 进入 jenkins 用户目录
ssh-keygen -t rsa  生成私钥和公钥供 github 访问使用

## upgrade java8

sudo apt-get install software-properties-common
sudo apt-get update
sudo apt-get install oracle-java8-installer
java -version

## maven

/usr/share/maven 主目录




