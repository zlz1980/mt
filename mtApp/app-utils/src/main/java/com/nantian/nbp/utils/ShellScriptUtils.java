package com.nantian.nbp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ShellScriptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellScriptUtils.class);

    /**
     * 通过文件路径执行脚本
     */
    public static ScriptResult executeScriptByPath(String scriptPath) throws Exception {
        return executeScript(scriptPath, null);
    }

    /**
     * 通过脚本内容执行脚本
     */
    public static ScriptResult executeScriptByContent(String scriptContent) throws Exception {
        return executeScript(null, scriptContent);
    }

    private static ScriptResult executeScript(String scriptPath, String scriptContent) throws Exception {
        // 安全性检查
        validateScriptInput(scriptPath, scriptContent);
        // 创建临时文件（如果需要）
        File tempScriptFile = null;
        if (scriptContent != null) {
            tempScriptFile = createTempScriptFile(scriptContent);
        }
        try {
            // 构建脚本执行命令
            String[] command;
            if (scriptPath != null) {
                command = new String[]{"/bin/bash", scriptPath};
            } else {
                command = new String[]{"/bin/bash", tempScriptFile.getAbsolutePath()};
            }
            // 使用ProcessBuilder执行脚本
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            // 设置安全的环境变量
            Map<String, String> environment = processBuilder.environment();
            environment.put("PATH", "/usr/local/bin:/usr/bin:/bin");
            environment.put("HOME", System.getProperty("user.home"));

            // 执行脚本
            Process process = processBuilder.start();
            return waitForProcess(process);
        } finally {
            // 清理临时文件
            if (tempScriptFile != null && tempScriptFile.exists()) {
                boolean delFlag = tempScriptFile.delete();
                LOGGER.info("tempScriptFile.delete [{}]",delFlag);
            }
        }
    }

    private static void validateScriptInput(String scriptPath, String scriptContent) throws SecurityException {
        // 验证输入参数
        if (scriptPath == null && scriptContent == null) {
            throw new SecurityException("Script path or content must be provided");
        }
        // 检查脚本路径是否在允许的目录范围内
        if (scriptPath != null) {
            Path path = Paths.get(scriptPath);
            if (!path.toFile().exists()) {
                throw new SecurityException("Script file does not exist: " + scriptPath);
            }
            // 可以添加更多的路径安全检查
        }
        // 检查脚本内容长度
        if (scriptContent != null && scriptContent.length() > 100000) {
            throw new SecurityException("Script content too long");
        }
    }

    private static File createTempScriptFile(String scriptContent) throws IOException {
        File tempFile = File.createTempFile("temp_script_", ".sh");
        boolean executable = tempFile.setExecutable(true);
        LOGGER.info("tempScriptFile setExecutable[{}]",executable);
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile.toPath())) {
            writer.write(scriptContent);
        }
        return tempFile;
    }

    private static ScriptResult waitForProcess(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Script execution interrupted", e);
        }
        return new ScriptResult(exitCode, output.toString());
    }

    public static class ScriptResult {
        private final int exitCode;
        private final String output;

        public ScriptResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }
    }
}
