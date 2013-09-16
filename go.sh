#!/bin/bash

set -e
set -o pipefail  # trace ERR through pipes
set -o errtrace  # trace ERR through 'time command' and other functions

CWD=$(dirname $0)

echo $CWD

SESSION='riemann-splash'

function error() {
    JOB="$0"              # job name
    LASTLINE="$1"         # line of error occurrence
    LASTERR="$2"          # error code
    echo "ERROR in ${JOB} : line ${LASTLINE} with exit code ${LASTERR}"
    tmux kill-session -t $SESSION
    exit 1
}
trap 'error ${LINENO} ${?}' ERR

tmux -2 new-session -d -s $SESSION

# riemann
tmux new-window -t $SESSION -n 'riemann'
tmux split-window -v
tmux select-pane -t 0
tmux send-key 'cd ../riemann && lein run' C-m
tmux select-pane -t 1
tmux send-key 'riemann-health -i 2' C-m

# riemann splash
tmux new-window -t $SESSION -n 'riemann-splash'
tmux split-window -v
tmux select-pane -t 0
tmux send-key 'lein cljsbuild clean && lein cljsbuild auto' C-m
tmux select-pane -t 1
tmux send-key 'cd resources/public && python -m SimpleHTTPServer' C-m

tmux -2 attach-session -t $SESSION

popd
