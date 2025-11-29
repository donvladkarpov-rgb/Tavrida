#!/bin/bash

nohup ./start-tavrida.sh > tavrida.log 2>&1 &
echo $! > tavrida.pid
echo "✅ Tavrida запущен в фоне. PID: $(cat tavrida.pid)"
echo "Логи: tail -f tavrida.log"