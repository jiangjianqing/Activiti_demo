#!/bin/bash

#获取当前执行脚本的绝对路径
root=$(cd `dirname $0`; pwd)

function install(){
	cd $1
	mvn clean install
	cd $root
}

install ./common.db.base
install ./common.db.base.test
install ./common.db.dal.identity
install ./common.db.dal.log
install ./common.service.utils
install ./common.security
install ./common.web.server
