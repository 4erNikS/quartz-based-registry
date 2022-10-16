#!/bin/sh
set -e
echo "---------------------------------OPTS------------------------------------"
echo "JAVA_OPTS="$JAVA_OPTS
echo "WAIT_FOR_IT="$WAIT_FOR_IT
echo "-------------------------------------------------------------------------"


sleep 30
java $JAVA_OPTS -jar ./registry.jar