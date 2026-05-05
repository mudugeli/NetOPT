name: Build NetOpt APK

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]
  workflow_dispatch:   # 允许手动触发

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: 检出代码
        uses: actions/checkout@v4

      - name: 设置 JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: 设置 Android SDK
        uses: android-actions/setup-android@v3

      - name: 赋予 Gradle 执行权限
        run: chmod +x ./gradlew

      - name: 缓存 Gradle 依赖
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: gradle-

      - name: 编译 Debug APK
        run: ./gradlew assembleDebug --stacktrace

      - name: 上传 Debug APK
        uses: actions/upload-artifact@v4
        with:
          name: NetOpt-debug
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 30

      # ── Release 签名打包（可选，需配置 Secrets） ──────────────────
      - name: 编译 Release APK
        run: ./gradlew assembleRelease --stacktrace
        continue-on-error: true   # 没配置签名时跳过

      - name: 签名 Release APK
        if: ${{ env.KEYSTORE_BASE64 != '' }}
        env:
          KEYSTORE_BASE64: ${{ secrets.KEYSTORE_BASE64 }}
          KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
          KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
          STORE_PASSWORD: ${{ secrets.STORE_PASSWORD }}
        run: |
          echo "$KEYSTORE_BASE64" | base64 -d > keystore.jks
          $ANDROID_HOME/build-tools/34.0.0/apksigner sign \
            --ks keystore.jks \
            --ks-key-alias "$KEY_ALIAS" \
            --ks-pass "pass:$STORE_PASSWORD" \
            --key-pass "pass:$KEY_PASSWORD" \
            --out app/build/outputs/apk/release/app-release-signed.apk \
            app/build/outputs/apk/release/app-release-unsigned.apk

      - name: 上传 Release APK
        uses: actions/upload-artifact@v4
        with:
          name: NetOpt-release
          path: app/build/outputs/apk/release/app-release*.apk
          retention-days: 30
