#!/bin/bash
JDK11=/usr/lib/jvm/java-11-openjdk-amd64/
JDK17=/usr/lib/jvm/java-17-openjdk-amd64/

ENV_FORK=FORK
ENV_VFORK=VFORK
ENV_posix_spawn=posix_spawn

echo $ENV_FORK
sh ./build_np.sh $JDK11 $ENV_FORK
sh ./build_np.sh $JDK17 $ENV_FORK
echo
echo $ENV_posix_spawn
sh ./build_np.sh $JDK11 $ENV_posix_spawn
sh ./build_np.sh $JDK17 $ENV_posix_spawn


echo $ENV_FORK
sh ./build.sh $JDK11 $ENV_FORK
sh ./build.sh $JDK17 $ENV_FORK
echo
echo $ENV_posix_spawn
sh ./build.sh $JDK11 $ENV_posix_spawn
sh ./build.sh $JDK17 $ENV_posix_spawn