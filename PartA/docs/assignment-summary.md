# Assignment 2 - Part A Notes

The assignment requires a Java, Selenium WebDriver, and TestNG automation framework for TutorialsNinja.

Implemented structure:

- `src/main/java/com/automation/config`: external configuration reader.
- `src/main/java/com/automation/core`: WebDriver creation.
- `src/main/java/com/automation/pages`: Page Object Model classes.
- `src/main/java/com/automation/utils`: reusable Excel reader.
- `src/test/java/com/automation/base`: shared test setup and teardown.
- `src/test/java/com/automation/listeners`: Allure failure screenshot listener.
- `src/test/java/com/automation/tests`: TC01-TC11 automated test classes.
- `src/test/resources`: `config.properties`, `allure.properties`, and Excel test data.

Run from this folder with:

```powershell
mvn test
```

Generate the Allure report with:

```powershell
mvn allure:report
```
