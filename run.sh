#!/bin/zsh

# StockWise - macOS/Linux Run Script
# Ensure Maven dependencies are copied
echo "Preparing dependencies..."
mvn dependency:copy-dependencies -q

# Run using JavaFX Maven plugin (handles compile, modules, classpath)
echo "Starting StockWise..."
mvn javafx:run
