plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-parcelize'
    id 'com.google.devtools.ksp'
}

android {
    namespace 'com.netopt'
    compileSdk 35

    defaultConfig {
        applicationId "com.netopt"
        minSdk 28           // Shizuku API 推荐 Android 9+
        targetSdk 35
        versionCode 1
        versionName "1.0.0"
    }

    buildFeatures {
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.10'
    }
    kotlinOptions {
        jvmTarget = '17'
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
}

dependencies {
    // ── Shizuku ──────────────────────────────────────────────────────
    // Shizuku API：核心 IPC 接口
    implementation 'dev.rikka.shizuku:api:13.1.5'
    // Shizuku Provider：自动根据设备选择 root 或 adb 模式
    implementation 'dev.rikka.shizuku:provider:13.1.5'

    // ── Jetpack Compose ──────────────────────────────────────────────
    def composeBom = platform('androidx.compose:compose-bom:2024.04.01')
    implementation composeBom
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.material:material-icons-extended'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    debugImplementation 'androidx.compose.ui:ui-tooling'

    // ── AndroidX Core ────────────────────────────────────────────────
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.2'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2'
    implementation 'androidx.activity:activity-compose:1.9.0'
    implementation 'androidx.navigation:navigation-compose:2.7.7'

    // ── Coroutines ───────────────────────────────────────────────────
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1'

    // ── DataStore（配置持久化） ───────────────────────────────────────
    implementation 'androidx.datastore:datastore-preferences:1.1.1'

    // ── 图表（网速历史图） ────────────────────────────────────────────
    implementation 'com.patrykandpatrick.vico:compose-m3:1.14.0'

    // ── 工具 ──────────────────────────────────────────────────────────
    implementation 'com.github.topjohnwu.libsu:core:5.2.2'  // root 回退支持（可选）
}
