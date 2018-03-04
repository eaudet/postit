# postit
Pay your bills, stamp a postit and archive to your local
harddrive/iCloud/GDrive/Dropbox.

## Debug
mvn clean install -Dmaven.surefire.debug="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent -Djava.compiler=NONE"
