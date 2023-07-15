package com.javiersc.hubdle.project.extensions.shared.features

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions.apis.BaseHubdleDelegateExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.apis.enableAndExecute
import com.javiersc.hubdle.project.extensions.java.hubdleJava
import com.javiersc.hubdle.project.extensions.kotlin.jvm.hubdleKotlinJvm
import com.javiersc.hubdle.project.extensions.shared.features.gradle.HubdleGradlePluginFeatureExtension
import com.javiersc.hubdle.project.extensions.shared.features.gradle.HubdleGradleVersionCatalogFeatureExtension
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property

public open class HubdleGradleFeatureExtension
@Inject
constructor(
    project: Project,
) : HubdleEnableableExtension(project) {

    override val isEnabled: Property<Boolean> = property { false }

    override val oneOfExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleJava, hubdleKotlinJvm)

    public val plugin: HubdleGradlePluginFeatureExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun plugin(action: Action<HubdleGradlePluginFeatureExtension> = Action {}) {
        plugin.enableAndExecute(action)
    }

    public val versionCatalog: HubdleGradleVersionCatalogFeatureExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun versionCatalog(
        action: Action<HubdleGradleVersionCatalogFeatureExtension> = Action {}
    ) {
        versionCatalog.enableAndExecute(action)
    }
}

public interface HubdleGradleDelegateFeatureExtension : BaseHubdleDelegateExtension {

    public val gradle: HubdleGradleFeatureExtension
        get() = project.getHubdleExtension()

    @HubdleDslMarker
    public fun gradle(action: Action<HubdleGradleFeatureExtension> = Action {}) {
        gradle.enableAndExecute(action)
    }
}
