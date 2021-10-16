import com.javiersc.plugins.core.groupId

allprojects {
    group = groupId
}

plugins.apply(LifecycleBasePlugin::class)

tasks {
    withType<Test> {
        maxParallelForks = Runtime.getRuntime().availableProcessors()
        useJUnitPlatform()
        useTestNG()
    }
}
