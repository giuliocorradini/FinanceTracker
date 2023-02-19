# FinanceTracker

By Giulio Corradini.

<div style="text-align:center">
    <img src="app/src/main/resources/icons/Logo.svg" />
</div>

Project for the Object Oriented Programming course - Unimore a.y. 2022-2023.

## Structure

This project uses `gradle` as a build system. The directory structure hence
follow the one suggested and generated by this tool.

The source code can be found in:

```shell
cd app/src/main/java/financetracker
```

This project was tested against Java 17. It uses JavaFX for the GUI and modern language
features such as stream and lambda expressions.

## Build and run

To automatically download, install and configure the dependencies for this project, then
build the entire project use

```shell
./gradlew build
```

and to run it

```shell
./gradlew run
```

## Documentation

To generate documentation use:

```shell
./gradlew javadoc
```

then navigate to `app/build/docs/javadoc` or launch an http server

```shell
cd app/build/docs/javadoc
python3 -m http.server
```

then open `index.html`.

## Distribution

This project is configured to generate an uber-jar file that can be distributed.
An appropriate gradle task was set up, call it with:

```shell
./gradlew fatJar
```

A fat jar, or uber jar, is a Java archive that contains all the needed dependencies to run
without requiring any external bytecode.

Once generated you can find it in:

```shell
cd app/build/libs
```

and to run it

```shell
java -jar FinanceTracker.jar
```