@echo off
set FX_PATH="C:\ProgramasNaoNativos\Programas(x64)\Lingugem Programação\Java\javafx\javafx-sdk-21.0.3\lib"
javac --module-path %FX_PATH% --add-modules javafx.controls,javafx.fxml -d bin src/main/java/service/*.java src/main/java/dao/*.java src/main/java/yourpackage/*.java
java --module-path %FX_PATH% --add-modules javafx.controls,javafx.fxml -cp bin service.MainApplication
