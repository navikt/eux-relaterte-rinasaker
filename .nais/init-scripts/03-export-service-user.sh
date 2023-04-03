#!/usr/bin/env bash

if test -f /var/run/secrets/nais.io/srveux-app/username;
then
    export  SRVEUX_APP_USERNAME=$(cat /var/run/secrets/nais.io/srveux-app/username)
fi
if test -f /var/run/secrets/nais.io/srveux-app/password;
then
    export  SRVEUX_APP_PASSWORD=$(cat /var/run/secrets/nais.io/srveux-app/password)
fi
