#!/bin/bash

# Путь к JAR-файлу (относительно скрипта)
JAR_FILE="tavrida.jar"

# Проверка существования JAR
if [ ! -f "$JAR_FILE" ]; then
  echo "❌ Файл $JAR_FILE не найден!"
  exit 1
fi

# Параметры памяти (важно для 1.8 ГБ RAM!)
# -Xmx — максимум heap
# -Xms — начальный heap
# -XX:MaxMetaspaceSize — ограничение метаспейса
# Итого: ~400–450 МБ максимум
JAVA_OPTS="
  -Xmx1024m
  -Xms512m
  -XX:MaxMetaspaceSize=96m
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -server
"

# Дополнительно: отключаем dev-тулы и включаем прод
SPRING_OPTS="
  --spring.profiles.active=prod
"

# Запуск
echo "🚀 Запуск Tavrida с ограничением памяти..."
echo "JAR: $(pwd)/$JAR_FILE"
echo "Память: -Xmx1024m -XX:MaxMetaspaceSize=96m"
echo "----------------------------------------"

java $JAVA_OPTS -jar "$JAR_FILE" $SPRING_OPTS

# Если нужно в фоне — используйте nohup (см. ниже)