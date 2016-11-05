#!/usr/bin/env bash
#从数据库备份中重建数据库,名为hotcms

mysql <<EOF
drop database IF EXISTS hotcms;
create database hotcms;
use hotcms;
source $1.sql
EOF

