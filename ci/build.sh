#!/usr/bin/env bash
set -euo pipefail
WITH_BIO="${1:-1}"
echo "=== Build voi van tay: ${WITH_BIO} ==="
npm install
npx cap add android
npx capacitor-assets generate --android
sed -i 's#<application#<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>\n    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>\n    <uses-permission android:name="android.permission.USE_BIOMETRIC"/>\n    <application#' android/app/src/main/AndroidManifest.xml
sed -i "s/versionCode 1/versionCode ${GITHUB_RUN_NUMBER:-1}/" android/app/build.gradle
if [ "$WITH_BIO" = "1" ]; then
  D=android/app/src/main/java/app/kaizen1
  mkdir -p "$D"
  cp ci/KaizenBioPlugin.java "$D/KaizenBioPlugin.java"
  cp ci/MainActivity.java "$D/MainActivity.java"
  sed -i '0,/^dependencies {/s//dependencies {\n    implementation "androidx.biometric:biometric:1.1.0"/' android/app/build.gradle
fi
npx cap sync android
cat android/app/src/main/assets/capacitor.plugins.json || true
mkdir -p ~/.android
cp keystore/debug.keystore ~/.android/debug.keystore
cd android
chmod +x gradlew
./gradlew assembleDebug
