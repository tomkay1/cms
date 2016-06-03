#! /bin/sh
lessPath=$1
cssPath=$2
echo " lessPaths: ${lessPath} "
echo " cssPaths: ${cssPath}"
lessc ${lessPath} > ${cssPath}
exit 0
