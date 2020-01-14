# swing-worker-troubleshooting

Here we have a simple Swing app that now has the need to add a time-consuming task, and we need to be updating a
progress bar while it runs.

The current solution runs entirely on the EDT (packaged 100% inside the `AnalysisService` class), and as such, blocks
the progress bar from being updated/repainted.

I have attempted to add `AnalysisWorker` (a Swing Worker) however it still appears to block the EDT somehow.

To change the code over to use the `AnalysisWorker`, see the code comments inside `SimpleApp`'s on-click handler for
the `analyzeButton`. You just need to comment-out a try-catch block and uncomment the worker code.

Additionally, the solution needs:

1. To handle both checked and unchecked exceptions being thrown from inside the worker (if it makes any difference which kind is thrown); and
2. Conditionally prompt the user with a `JOptionPane` with code that is running inside the worker
3. Return a result back to the EDT so we can update the UI based on the computed result from inside the worker

### Requirements
- Java 8+
- The build uses the Gradle Wrapper so you don't even need Gradle installed

### Build and run locally
```
./gradlew clean build shadowJar && java -jar build/libs/simple-app.jar
```
