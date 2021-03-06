/*
 * Copyright (c) 2018 Zac Sweers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("ClassName", "unused")

import org.gradle.api.Project
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String?.letIfEmpty(fallback: String): String {
  return if (this == null || isEmpty()) {
    fallback
  } else {
    this
  }
}

fun String?.execute(workingDir: File, fallback: String): String {
  Runtime.getRuntime().exec(this, null, workingDir).let {
    it.waitFor()
    return try {
      it.inputStream.reader().readText().trim().letIfEmpty(fallback)
    } catch (e: Exception) {
      fallback
    }
  }
}

object build {
  val standardFreeKotlinCompilerArgs = listOf(
      "-Xjsr305=strict",
      "-progressive",
      "-XXLanguage:+NewInference",
      "-XXLanguage:+SamConversionForKotlinFunctions",
      "-Xinline-classes",
      "-Xjvm-default=enable",
      "-Xuse-experimental=kotlin.Experimental",
      "-Xuse-experimental=kotlin.RequiresOptIn",
      "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
      "-Xuse-experimental=kotlin.experimental.ExperimentalTypeInference",
      "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
      "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
  )

  fun isCi(): Boolean = System.getenv("CI")?.toBoolean() == true
}

object deps {
  object versions {
    const val androidTestSupport = "1.1.0-rc01"
    const val apollo = "1.3.3"
    const val autodispose = "1.4.0"
    const val chuck = "1.1.0"
    const val crumb = "0.1.0"
    const val dagger = "2.25.2"
    const val espresso = "3.1.0-alpha1"
    const val hyperion = "0.9.24"
    const val inspector = "0.3.0"
    const val kotlin = "1.3.70"
    const val kotpref = "2.10.0"
    const val leakcanary = "2.2"
    const val legacySupport = "28.0.0"
    const val markwon = "4.2.2"
    const val moshi = "1.9.2"
    const val okhttp = "4.4.0"
    const val retrofit = "2.7.2"
    const val spotless = "3.27.2"
    const val tikxml = "0.8.13" // https://github.com/Tickaroo/tikxml/issues/114
  }

  object android {
    object androidx {
      const val annotations = "androidx.annotation:annotation:1.1.0"
      const val legacyAnnotations = "com.android.support:support-annotations:28.0.0"
      const val appCompat = "androidx.appcompat:appcompat:1.2.0-alpha03"

      private const val coreVersion = "1.3.0-alpha02"
      const val core = "androidx.core:core:$coreVersion"
      const val coreKtx = "androidx.core:core-ktx:$coreVersion"

      const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
      const val customTabs = "androidx.browser:browser:1.3.0-alpha01"
      const val design = "com.google.android.material:material:1.2.0-alpha05"
      const val drawerLayout = "androidx.drawerlayout:drawerlayout:1.1.0-alpha04"

      private const val emojiVersion = "1.1.0-alpha01"
      const val emoji = "androidx.emoji:emoji:$emojiVersion"
      const val emojiAppcompat = "androidx.emoji:emoji-appcompat:$emojiVersion"

      private const val fragmentVersion = "1.3.0-alpha01"
      const val fragment = "androidx.fragment:fragment:$fragmentVersion"
      const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragmentVersion"

      object viewModel {
        private const val version = "2.3.0-alpha01"
        const val core = "androidx.lifecycle:lifecycle-viewmodel:$version"
        const val ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
      }

      const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
      const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-beta01"
      const val palette = "androidx.palette:palette:1.0.0"
      const val paletteKtx = "androidx.palette:palette-ktx:1.0.0"

      private const val preferenceVersion = "1.1.0"
      const val preference = "androidx.preference:preference:$preferenceVersion"
      const val preferenceKtx = "androidx.preference:preference-ktx:$preferenceVersion"
      const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-beta03"

      object lifecycle {
        private const val version = "2.2.0"
        const val apt = "androidx.lifecycle:lifecycle-compiler:$version"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
        const val ktx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
      }

      object liveData {
        private const val version = "2.2.0-alpha01"
        const val ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
      }

      object room {
        private const val version = "2.2.4"
        const val apt = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val runtime = "androidx.room:room-runtime:$version"
        const val rxJava2 = "androidx.room:room-rxjava2:$version"
      }
    }

    object build {
      const val compileSdkVersion = 29
      const val minSdkVersion = 21
      const val targetSdkVersion = 29
    }

    object firebase {
      const val core = "com.google.firebase:firebase-core:17.2.3"
      const val config = "com.google.firebase:firebase-config:18.0.0"
      const val database = "com.google.firebase:firebase-database:19.2.1"
      const val gradlePlugin = "com.google.firebase:firebase-plugins:2.0.0"
      const val perf = "com.google.firebase:firebase-perf:18.0.0"
    }

    const val gradlePlugin = "com.android.tools.build:gradle:4.1.0-alpha02"
  }

  object apollo {
    const val androidSupport = "com.apollographql.apollo:apollo-android-support:${versions.apollo}"
    const val gradlePlugin = "com.apollographql.apollo:apollo-gradle-plugin:${versions.apollo}"
    const val httpcache = "com.apollographql.apollo:apollo-http-cache:${versions.apollo}"
    const val runtime = "com.apollographql.apollo:apollo-runtime:${versions.apollo}"
    const val rx2Support = "com.apollographql.apollo:apollo-rx2-support:${versions.apollo}"
  }

  object assistedInject {
    private const val version = "0.5.2"
    const val annotations = "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
    const val processor = "com.squareup.inject:assisted-inject-processor-dagger2:$version"
  }

  object auto {
    const val common = "com.google.auto:auto-common:0.10"
    const val service = "com.google.auto.service:auto-service:1.0-rc6"
  }

  object autoDispose {
    const val core = "com.uber.autodispose:autodispose:${versions.autodispose}"
    const val android = "com.uber.autodispose:autodispose-android:${versions.autodispose}"
    const val androidArch = "com.uber.autodispose:autodispose-android-archcomponents:${versions.autodispose}"
    const val lifecycle = "com.uber.autodispose:autodispose-lifecycle:${versions.autodispose}"
  }

  object build {
    val ci get() = System.getenv("GITHUB_WORKFLOW") != null
    const val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:1.0.5"

    fun gitSha(project: Project): String {
      // query git for the SHA, Tag and commit count. Use these to automate versioning.
      return "git rev-parse --short HEAD".execute(project.rootDir, "none")
    }

    fun gitTag(project: Project): String {
      return "git describe --tags".execute(project.rootDir, "dev")
    }

    fun gitCommitCount(project: Project): Int {
      return 100 + ("git rev-list --count HEAD".execute(project.rootDir, "0").toInt())
    }

    private val DATE_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.US)
        .withZone(ZoneId.systemDefault())

    fun gitTimestamp(project: Project): String {
      val timestamp = "git log -n 1 --format=%at".execute(project.rootDir, "0").trim().toLong()
      return DATE_DISPLAY_FORMAT.format(Instant.ofEpochSecond(timestamp))
    }

    object gradlePlugins {
      const val bugsnag = "com.bugsnag:bugsnag-android-gradle-plugin:4.7.3"
      const val playPublisher = "com.github.triplet.gradle:play-publisher:2.7.2"
      const val redacted = "dev.zacsweers.redacted:redacted-compiler-plugin-gradle:0.2.0"
      const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:${versions.spotless}"
    }

    object repositories {
      const val google = "https://maven.google.com"
      const val jitpack = "https://jitpack.io"
      const val kotlineap = "https://dl.bintray.com/kotlin/kotlin-eap"
      const val kotlinx = "https://kotlin.bintray.com/kotlinx"
      const val plugins = "https://plugins.gradle.org/m2/"
      const val snapshots = "https://oss.sonatype.org/content/repositories/snapshots/"
    }

    const val javapoet = "com.squareup:javapoet:1.12.1"
  }

  object chuck {
    const val debug = "com.readystatesoftware.chuck:library:${versions.chuck}"
    const val release = "com.readystatesoftware.chuck:library-no-op:${versions.chuck}"
  }

  object coil {
    private const val VERSION = "0.9.5"
    const val base = "io.coil-kt:coil-base:$VERSION"
    const val default = "io.coil-kt:coil:$VERSION"
    const val gif = "io.coil-kt:coil-gif:$VERSION"
  }

  object corbind {
    private const val VERSION = "1.3.1"
    const val core = "ru.ldralighieri.corbind:corbind:$VERSION"
    object androidx {
      const val core = "ru.ldralighieri.corbind:corbind-core:$VERSION"
    }
    const val material = "ru.ldralighieri.corbind:corbind-material:$VERSION"
  }

  object crumb {
    const val annotations = "com.uber.crumb:crumb-annotations:${versions.crumb}"
    const val compiler = "com.uber.crumb:crumb-compiler:${versions.crumb}"
    const val compilerApi = "com.uber.crumb:crumb-compiler-api:${versions.crumb}"
  }

  object dagger {
    object android {
      object apt {
        const val processor = "com.google.dagger:dagger-android-processor:${versions.dagger}"
      }

      const val runtime = "com.google.dagger:dagger-android:${versions.dagger}"
      const val support = "com.google.dagger:dagger-android-support:${versions.dagger}"
    }

    object apt {
      const val compiler = "com.google.dagger:dagger-compiler:${versions.dagger}"
    }

    const val runtime = "com.google.dagger:dagger:${versions.dagger}"
    const val spi = "com.google.dagger:dagger-spi:${versions.dagger}"
  }

  object hyperion {
    object core {
      const val debug = "com.willowtreeapps.hyperion:hyperion-core:${versions.hyperion}"
      const val release = "com.willowtreeapps.hyperion:hyperion-core-no-op:${versions.hyperion}"
    }

    object plugins {
      const val appInfo = "com.star_zero:hyperion-appinfo:1.0.0"
      const val attr = "com.willowtreeapps.hyperion:hyperion-attr:${versions.hyperion}"
      const val chuck = "com.github.Commit451:Hyperion-Chuck:1.0.0"
      const val crash = "com.willowtreeapps.hyperion:hyperion-crash:${versions.hyperion}"
      const val disk = "com.willowtreeapps.hyperion:hyperion-disk:${versions.hyperion}"
      const val geigerCounter = "com.willowtreeapps.hyperion:hyperion-geiger-counter:${versions.hyperion}"
      const val measurement = "com.willowtreeapps.hyperion:hyperion-measurement:${versions.hyperion}"
      const val phoenix = "com.willowtreeapps.hyperion:hyperion-phoenix:${versions.hyperion}"
      const val recorder = "com.willowtreeapps.hyperion:hyperion-recorder:${versions.hyperion}"
      const val sharedPreferences = "com.willowtreeapps.hyperion:hyperion-shared-preferences:${versions.hyperion}"
      const val timber = "com.willowtreeapps.hyperion:hyperion-timber:${versions.hyperion}"
    }
  }

  object inspector {
    object apt {
      const val compiler = "io.sweers.inspector:inspector-compiler:${versions.inspector}"

      object extensions {
        const val android = "io.sweers.inspector:inspector-android-compiler-extension:${versions.inspector}"
        const val autovalue = "io.sweers.inspector:inspector-autovalue-compiler-extension:${versions.inspector}"
        const val nullability = "io.sweers.inspector:inspector-nullability-compiler-extension:${versions.inspector}"
      }
    }

    const val core = "io.sweers.inspector:inspector:${versions.inspector}"

    object factoryCompiler {
      const val apt = "io.sweers.inspector:inspector-factory-compiler:${versions.inspector}"

      object compileOnly {
        const val annotations = "io.sweers.inspector:inspector-factory-compiler-annotations:${versions.inspector}"
      }
    }

  }

  object kotlin {
    private const val coroutinesVersion = "1.3.4"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    const val coroutinesRx = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutinesVersion"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
    const val metadata = "org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.1.0"
    const val noArgGradlePlugin = "org.jetbrains.kotlin:kotlin-noarg:${versions.kotlin}"
    const val poet = "com.squareup:kotlinpoet:1.5.0"

    object stdlib {
      const val core = "org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlin}"
      const val jdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions.kotlin}"
      const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"
    }
  }

  object markwon {
    const val core = "io.noties.markwon:core:${versions.markwon}"
    const val extStrikethrough = "io.noties.markwon:ext-strikethrough:${versions.markwon}"
    const val extTables = "io.noties.markwon:ext-tables:${versions.markwon}"
    const val extTasklist = "io.noties.markwon:ext-tasklist:${versions.markwon}"
    const val html = "io.noties.markwon:html:${versions.markwon}"
    const val image = "io.noties.markwon:image:${versions.markwon}"
    const val imageCoil = "io.noties.markwon:image-coil:${versions.markwon}"
    const val linkify = "io.noties.markwon:linkify:${versions.markwon}"
    const val syntaxHighlight = "io.noties.markwon:syntax-highlight:${versions.markwon}"
  }

  object misc {
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${versions.leakcanary}"
    const val leakCanaryObjectWatcherAndroid = "com.squareup.leakcanary:leakcanary-object-watcher-android:${versions.leakcanary}"
    const val bugsnag = "com.bugsnag:bugsnag-android:4.22.3"
    const val byteunits = "com.jakewharton.byteunits:byteunits:0.9.1"

    object debug {
      private const val FLIPPER_VERSION = "0.32.2"
      const val flipper = "com.facebook.flipper:flipper:$FLIPPER_VERSION"
      const val flipperNetwork = "com.facebook.flipper:flipper-network-plugin:$FLIPPER_VERSION"
      const val soLoader = "com.facebook.soloader:soloader:0.8.2"
      const val guava = "com.google.guava:guava:28.2-android"
      const val madge = "com.jakewharton.madge:madge:1.1.4"
      const val processPhoenix = "com.jakewharton:process-phoenix:2.0.0"
      const val scalpel = "com.jakewharton.scalpel:scalpel:1.1.2"
      const val telescope = "com.mattprecious.telescope:telescope:2.2.0"
    }

    const val flick = "me.saket:flick:1.7.0"
    const val gestureViews = "com.alexvasilkov:gesture-views:2.2.0"
    const val inboxRecyclerView = "me.saket:inboxrecyclerview:2.0.0-beta3"
    const val javaxInject = "org.glassfish:javax.annotation:10.0-b28"
    const val jsoup = "org.jsoup:jsoup:1.13.1"
    const val jsr250 = "javax.annotation:jsr250-api:1.0"
    const val jsr305 = "com.google.code.findbugs:jsr305:3.0.2"
    const val kotpref = "com.chibatching.kotpref:kotpref:${versions.kotpref}"
    const val kotprefEnum = "com.chibatching.kotpref:enum-support:${versions.kotpref}"
    const val lottie = "com.airbnb.android:lottie:3.4.0"
    const val moshiLazyAdapters = "com.serjltt.moshi:moshi-lazy-adapters:2.2"
    const val okio = "com.squareup.okio:okio:2.4.3"
    const val recyclerViewAnimators = "jp.wasabeef:recyclerview-animators:3.0.0"
    const val tapTargetView = "com.getkeepsafe.taptargetview:taptargetview:1.13.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val unbescape = "org.unbescape:unbescape:1.1.6.RELEASE"
  }

  object moshi {
    const val adapters = "com.squareup.moshi:moshi-adapters:${versions.moshi}"
    const val core = "com.squareup.moshi:moshi:${versions.moshi}"
    const val compiler = "com.squareup.moshi:moshi-kotlin-codegen:${versions.moshi}"
    object sealed {
      private const val VERSION = "0.1.0"
      const val annotations = "dev.zacsweers.moshisealed:moshi-sealed-annotations:${VERSION}"
      const val compiler = "dev.zacsweers.moshisealed:moshi-sealed-codegen:${VERSION}"
    }
  }

  object okhttp {
    const val core = "com.squareup.okhttp3:okhttp:${versions.okhttp}"

    object debug {
      const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${versions.okhttp}"
    }

    const val webSockets = "com.squareup.okhttp3:okhttp-ws:${versions.okhttp}"
  }

  object retrofit {
    const val core = "com.squareup.retrofit2:retrofit:${versions.retrofit}"

    object debug {
      const val mock = "com.squareup.retrofit2:retrofit-mock:${versions.retrofit}"
    }

    const val moshi = "com.squareup.retrofit2:converter-moshi:${versions.retrofit}"
    const val rxJava2 = "com.squareup.retrofit2:adapter-rxjava2:${versions.retrofit}"
  }

  object rx {
    const val android = "io.reactivex.rxjava2:rxandroid:2.1.1"

    const val dogTag = "com.uber.rxdogtag:rxdogtag:1.0.0"
    const val dogTagAutoDispose = "com.uber.rxdogtag:rxdogtag-autodispose:1.0.0"
    const val java = "io.reactivex.rxjava2:rxjava:2.2.18"
    const val relay = "com.jakewharton.rxrelay2:rxrelay:2.1.1"
  }

  object tikxml {
    const val annotation = "com.tickaroo.tikxml:annotation:${versions.tikxml}"
    const val apt = "com.tickaroo.tikxml:processor:${versions.tikxml}"
    const val core = "com.tickaroo.tikxml:core:${versions.tikxml}"
    const val htmlEscape = "com.tickaroo.tikxml:converter-htmlescape:${versions.tikxml}"
    const val retrofit = "com.tickaroo.tikxml:retrofit-converter:${versions.tikxml}"
  }

  object test {
    object android {
      object espresso {
        const val core = "androidx.test.espresso:espresso-core:${versions.espresso}"
        const val contrib = "androidx.test.espresso:espresso-contrib:${versions.espresso}"
        const val web = "androidx.test.espresso:espresso-web:${versions.espresso}"
      }

      const val runner = "androidx.test:runner:${versions.androidTestSupport}"
      const val rules = "androidx.test:rules:${versions.androidTestSupport}"
    }

    const val junit = "junit:junit:4.13"
    const val robolectric = "org.robolectric:robolectric:4.0-alpha-1"
    const val truth = "com.google.truth:truth:1.0.1"
  }
}
