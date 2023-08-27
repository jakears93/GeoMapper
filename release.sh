#!/bin/bash
mac=false
windows=false
linux=false
version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
githubRelease=false

while getopts hmwlcg flag
do
  case "${flag}" in
    h) echo "-m for mac, -w for windows, -l for linux, -c clean releases, -g release all to github"; exit;;
    m) mac=true;;
    w) windows=true;;
    l) linux=true;;
    c) rm -r ./releases; exit;;
    g) githubRelease=true; mac=true; linux=true; windows=true;;
    *) echo "Invalid Flag"; exit;;
  esac
done

if [ "$mac" = false ] && [ "$windows" = false ] && [ "$linux" = false ] ; then
  echo "No Release Candidates Selected"
  exit;
fi

echo "Release $version: Windows-$windows MacOs-$mac Linux-$linux";

#Arg 1 = architecture, Arg2 = version
function generateRunscript() {
  runCommand=""
  scriptFile=""

  if [ "$1" = mac_x64 ] ; then
    scriptFile=GeoMapper
    runCommand="#!/bin/bash\nbin/jdk-18.0.1.1.jdk/Contents/Home/bin/java -jar GeoMapper-$2.jar"
  elif [ "$1" = win_x64 ] ; then
    scriptFile=GeoMapper.bat
    runCommand="bin\jdk-18.0.1.1\\\bin\java.exe -jar GeoMapper-$2.jar"
  elif [ "$1" = linux_x64 ] ; then
    scriptFile=GeoMapper
    runCommand="#!/bin/bash\nbin/jdk-18.0.1.1/bin/java -jar GeoMapper-$2.jar"
  fi

  touch ./releases/"$1"/"$scriptFile"
  chmod u+x ./releases/"$1"/"$scriptFile"
  echo -e "$runCommand" >> ./releases/"$1"/"$scriptFile"
  echo -e "\tCreated Run Script ./releases/$1/$scriptFile"
}

#Argument 1 = platform name, Argument 2 = architecture
function release() {
  platform=$1
  arch=$2
  echo "Building $platform Release $version"
  jarfile=GeoMapper-"$version".jar
  zipfile=./releases/GeoMapper-"$version"-"$arch".zip

  echo -e "\tBuilding $platform Jar $version"
  mvn clean package -Djavafx.platform="$platform" > /dev/null
  if [ "$?" -ne 0 ] ; then
    echo "ERROR: Unable to package Project for platform $platform"
    exit;
  fi
  rm -r ./releases/"$arch"
  mkdir -p ./releases/"$arch"
  cp ./target/"$jarfile" ./releases/"$arch"/
  if [ "$?" -ne 0 ] ; then
    echo "ERROR: Unable to find Jar File for Version $version"
    exit;
  fi

  echo -e "\tCreating Runscript"
  generateRunscript "$arch" "$version"

  echo -e "\tZipping $platform Release $version"
  rm ./releases/GeoMapper-*-"$arch".zip
  echo "$zipfile"
  zip -r "$zipfile" ./releases/"$arch"

  echo "Completed $platform Release $version"
}

if [ "$mac" = true ] ; then
  release mac mac_x64
fi

if [ "$windows" = true ] ; then
  release win win_x64
fi

if [ "$linux" = true ] ; then
  release linux linux_x64
fi

if [ "$githubRelease" = true ] ; then
  zipfileLinux=./releases/GeoMapper-"$version"-linux_x64.zip
  zipfileMac=./releases/GeoMapper-"$version"-mac_x64.zip
  zipfileWindows=./releases/GeoMapper-"$version"-win_x64.zip
  lastVersion=$(tail -1 releaseInfo)
  git tag v"$version"
  git push -u origin develop
  releaseNotes=$(git log --pretty=format:"- %s" v"$version"...v"$lastVersion")
  gh release create "$version" "[$zipfileLinux,$zipfileMac,$zipfileWindows]" -t "Release :$version" -n "$releaseNotes"
  if [ "$?" -ne 0 ] ; then
    echo "Unable to release $version on Github"
  fi
  echo "$version" >> releaseInfo
fi