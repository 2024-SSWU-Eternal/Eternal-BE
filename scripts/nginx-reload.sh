#!/bin/bash
echo "> Nginx 설정 변경 및 재시작" >> /home/ec2-user/deploy.log
sudo service nginx reload
