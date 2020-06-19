package org.lucas.build.compile;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.compile.JavaCompile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerConventionsPlugin implements Plugin<Project> {

    public static final String JAVA_SOURCE_VERSION_PROPERTY = "javaSourceVersion";

    public static final JavaVersion DEFAULT_COMPILER_VERSION = JavaVersion.VERSION_1_8;

    /**
     * 编译参数
     */
    private static final List<String> COMPILER_ARGS;

    /**
     * 测试编译参数
     */
    private static final List<String> TEST_COMPILER_ARGS;

    static {
        List<String> commonCompilerArgs = Arrays.asList(
                "-Xlint:serial", "-Xlint:cast", "-Xlint:classfile", "-Xlint:dep-ann",
                "-Xlint:divzero", "-Xlint:empty", "-Xlint:finally", "-Xlint:overrides",
                "-Xlint:path", "-Xlint:processing", "-Xlint:static", "-Xlint:try", "-Xlint:-options"
        );
        COMPILER_ARGS = new ArrayList<>();
        COMPILER_ARGS.addAll(commonCompilerArgs);
        COMPILER_ARGS.addAll(Arrays.asList(
                "-Xlint:varargs", "-Xlint:fallthrough", "-Xlint:rawtypes", "-Xlint:deprecation",
                "-Xlint:unchecked", "-Werror"
        ));
        TEST_COMPILER_ARGS = new ArrayList<>();
        TEST_COMPILER_ARGS.addAll(commonCompilerArgs);
        TEST_COMPILER_ARGS.addAll(Arrays.asList("-Xlint:-varargs", "-Xlint:-fallthrough", "-Xlint:-rawtypes",
                "-Xlint:-deprecation", "-Xlint:-unchecked", "-parameters"));
    }

    @Override
    public void apply(Project project) {
        // JAVA 插件约定
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> applyJavaCompileConventions(project));
    }

    private void applyJavaCompileConventions(Project project) {
        JavaPluginConvention java = project.getConvention().getPlugin(JavaPluginConvention.class);
        if (project.hasProperty(JAVA_SOURCE_VERSION_PROPERTY)) {
            JavaVersion javaSourceVersion = JavaVersion.toVersion(project.property(JAVA_SOURCE_VERSION_PROPERTY));
            java.setSourceCompatibility(javaSourceVersion);
        } else {
            java.setSourceCompatibility(DEFAULT_COMPILER_VERSION);
        }
        java.setTargetCompatibility(DEFAULT_COMPILER_VERSION);

        project.getTasks().withType(JavaCompile.class)
                .matching(compileTask -> compileTask.getName().equals(JavaPlugin.COMPILE_JAVA_TASK_NAME))
                .forEach(compileTask -> {
                    compileTask.getOptions().setCompilerArgs(COMPILER_ARGS);
                    compileTask.getOptions().setEncoding("UTF-8");
                });
        project.getTasks().withType(JavaCompile.class)
                .matching(compileTask -> compileTask.getName().equals(JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME)
                        || compileTask.getName().equals("compileTestFixturesJava"))
                .forEach(compileTask -> {
                    compileTask.getOptions().setCompilerArgs(TEST_COMPILER_ARGS);
                    compileTask.getOptions().setEncoding("UTF-8");
                });
    }

}
