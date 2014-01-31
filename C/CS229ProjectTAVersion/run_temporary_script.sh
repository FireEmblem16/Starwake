#!/bin/bash
chmod u+x ./temporary_script.sh
./temporary_script.sh &
echo $! > pid