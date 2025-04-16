import korlibs.korge.gradle.*

plugins {
    alias(libs.plugins.korge)
}

korge {
    id = "de.sauronbach.blockSmash"

    name = "Block Smash"

    icon = file("src/commonMain/resources/korge.png")

// To enable all targets at once

    //targetAll()

// To enable targets based on properties/environment variables
    //targetDefault()

// To selectively enable targets

    targetJvm()
    targetJs()
    targetDesktop()
    targetIos()
    androidSdk(compileSdk = 35, minSdk = 29, targetSdk = 35)
    targetAndroid()

    serializationJson()
}


dependencies {
    add("commonMainApi", project(":deps"))
    //add("commonMainApi", project(":korge-dragonbones"))
}
