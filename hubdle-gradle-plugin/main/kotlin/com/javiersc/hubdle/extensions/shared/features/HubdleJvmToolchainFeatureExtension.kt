package com.javiersc.hubdle.extensions.shared.features

import com.javiersc.hubdle.extensions.HubdleDslMarker
import com.javiersc.hubdle.extensions._internal.Configurable.Priority
import com.javiersc.hubdle.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.extensions.apis.BaseHubdleDelegateExtension
import com.javiersc.hubdle.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.extensions.apis.enableAndExecute
import com.javiersc.hubdle.extensions.kotlin.hubdleKotlinAny
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.JvmImplementation
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

public open class HubdleJvmToolchainFeatureExtension
@Inject
constructor(
    project: Project,
) : HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { false }

    override val priority: Priority = Priority.P3

    override val oneOfExtensions: Set<HubdleEnableableExtension>
        get() = hubdleKotlinAny

    public val javaVersion: Property<JavaVersion> = property { JavaVersion.VERSION_1_8 }

    private val javaLanguageVersion: Provider<JavaLanguageVersion> =
        javaVersion.map { JavaLanguageVersion.of(it.majorVersion) }

    @HubdleDslMarker
    public fun javaVersion(version: JavaVersion) {
        this.javaVersion.set(version)
    }

    public val vendor: Property<JvmVendorSpec?> = property { null }

    @HubdleDslMarker
    public fun vendor(vendor: JvmVendorSpec) {
        this.vendor.set(vendor)
    }

    public val implementation: Property<JvmImplementation?> = property { null }

    @HubdleDslMarker
    public fun implementation(implementation: JvmImplementation) {
        this.implementation.set(implementation)
    }

    @HubdleDslMarker
    public fun jvmToolchain(action: Action<JavaToolchainSpec> = Action {}) {
        userConfigurable { the<KotlinProjectExtension>().jvmToolchain(action::execute) }
    }

    override fun Project.defaultConfiguration() {
        configurable {
            configure<KotlinProjectExtension> {
                jvmToolchain { chain ->
                    chain.languageVersion.set(javaLanguageVersion)
                    chain.vendor.set(vendor)
                    chain.implementation.set(implementation)
                }
            }
        }
    }
}

public interface HubdleJvmToolchainDelegateFeatureExtension : BaseHubdleDelegateExtension {

    @HubdleDslMarker
    public fun javaVersion(version: JavaVersion) {
        jvmToolchain.javaVersion.set(version)
    }

    public val jvmToolchain: HubdleJvmToolchainFeatureExtension
        get() = project.getHubdleExtension()

    @HubdleDslMarker
    public fun jvmToolchain(action: Action<HubdleJvmToolchainFeatureExtension> = Action {}) {
        jvmToolchain.enableAndExecute(action)
    }
}

internal val HubdleEnableableExtension.hubdleJvmToolchainFeature: HubdleJvmToolchainFeatureExtension
    get() = getHubdleExtension()
