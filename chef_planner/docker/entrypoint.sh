set -eu

if [ "${SPRING_DATASOURCE_PASSWORD:-}" = "" ] && [ -n "${SPRING_DATASOURCE_PASSWORD_FILE:-}" ]; then
  if [ -f "${SPRING_DATASOURCE_PASSWORD_FILE}" ]; then
    export SPRING_DATASOURCE_PASSWORD="$(cat "${SPRING_DATASOURCE_PASSWORD_FILE}")"
  else
    echo "ERROR: SPRING_DATASOURCE_PASSWORD_FILE apunta a '${SPRING_DATASOURCE_PASSWORD_FILE}', pero el fichero no existe."
    exit 1
  fi
fi

exec java ${JAVA_OPTS:-} -jar /app/app.jar