#!/bin/bash
echo "Installing Git pre-commit hook..."

# 检查toolchains.xml是否存在
if [ ! -f "toolchains.xml" ]; then
    echo "Error: toolchains.xml not found!"
    echo "Please make sure toolchains.xml exists in the project root."
    exit 1
fi

# 从 toolchains.xml 中提取 Java 路径
JAVA_HOME_PATH=$(grep -o '<jdkHome>.*</jdkHome>' toolchains.xml | sed 's/<jdkHome>//g' | sed 's/<\/jdkHome>//g' | tr -d ' ')

if [ -z "$JAVA_HOME_PATH" ]; then
    echo "Error: Could not extract jdkHome from toolchains.xml"
    exit 1
fi

echo "Using Java from: $JAVA_HOME_PATH"

# 创建 .git/hooks 目录（如果不存在）
mkdir -p .git/hooks

# 创建 pre-commit hook，使用从 toolchains.xml 读取的路径
cat > .git/hooks/pre-commit << EOF
#!/bin/sh
echo "Running Spotless formatting..."
# 使用从 toolchains.xml 读取的 Java 路径
export JAVA_HOME="$JAVA_HOME_PATH"
export PATH="\$JAVA_HOME/bin:\$PATH"
# 使用toolchains配置
mvn spotless:apply -q --toolchains toolchains.xml
if [ \$? -ne 0 ]; then
  echo "Code formatting failed!"
  exit 1
fi
git add .
echo "Code formatting completed!"
exit 0
EOF

# 设置执行权限
chmod +x .git/hooks/pre-commit

echo "Git hook installed successfully!"
echo "Now every git commit will automatically format your code."