# C482 Mock Inventory System
Inventory application with JavaFX where the data will not be retained after the application is closed.

Tools used
- IDE - IntelliJ Community Edition, version 2021.1.3
  - There is a bug with version 2021.3, where scene builder will free up in IntelliJ.   
- JavaFX version - 17.0.1
- Java Version - Oracle JDK 11.0.13
- JavaDoc
- Windows 10

Setting for IntelliJ
- File > Settings > Path Variables: add a path with the name PATH_TO_FX and make the value the *lib* folder of JavaFX
- File > Project Structure > Libraries: add the same *lib* folder in the JavaFX SDK
- File > Project Structure > Project: the project SDK should be Java verions 11.0.13 and the project language level set to *11*
- File > Project Structure > SDK: This should have JDK 11.0.13 in the class path

IntelliJ Run/Build Configuration
To make sure that the program runs correctly, make the following run configuration:
- Run on: Local Machine
- Java 11 version "11.0.13" should be the drop down list for the SDK to run this on.
- Click Modify Options > Add VM Options and add this to the add VM option line
  -`--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics`
-Main Method: main.Main
