Pod::Spec.new do |spec|
    spec.name                     = 'knot_core'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://github.com/1gravity/Knot'
    spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Reactive state container library for KMM'

    spec.vendored_frameworks      = "build/cocoapods/framework/knot-core.framework"
    spec.libraries                = "c++"
    spec.module_name              = "#{spec.name}_umbrella"

    spec.ios.deployment_target = '14.1'

                

    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':knot-core',
        'PRODUCT_MODULE_NAME' => 'knot_core',
    }

    spec.script_phases = [
        {
            :name => 'Build knot_core',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION
            SCRIPT
        }
    ]
end