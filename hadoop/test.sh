# Start all hadoop daemons.  Run this on master node.

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

if [ -e "$bin/../libexec/hadoop-config.sh" ]; then
  . "$bin"/../libexec/hadoop-config.sh
else
  . "$bin/hadoop-config.sh"
fi

# start dfs daemons
"$bin"/start-dfs.sh --config $HADOOP_CONF_DIR

# start mapred daemons
"$bin"/start-mapred.sh --config $HADOOP_CONF_DIR
