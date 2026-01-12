@echo off
echo Redeploying StockWise Inventory Management System...
mvn clean package
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo Starting StockWise Inventory Management System...
java --module-path target\lib --add-modules javafx.controls,javafx.fxml -jar target\stockwise-1.0-SNAPSHOT-shaded.jar
pause
