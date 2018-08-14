# Material Dialogs

Looking for older versions, before version 2.0.0? [Click here](README_OLD.md).

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)
[ ![Files](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Afiles/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Afiles/_latestVersion)
[ ![Color](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acolor/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acolor/_latestVersion)

![Screenshots](https://raw.githubusercontent.com/afollestad/material-dialogs/master/art/readmeshowcase.png)

# Table of Contents - Core

1. [Gradle Dependency](#gradle-dependency)
2. [Changes in Version 2](#changes-in-version-2)
3. [Getting Started](#getting-started)
    1. [Basics](#basics)
    2. [Kotlin Magic](#kotlin-magic)
    3. [Adding an Icon](#adding-an-icon)
    4. [Action Buttons](#action-buttons)
    5. [Callbacks](#callbacks)
    6. [Dismissing](#dismissing)
4. [Lists](#lists)
    1. [Plain](#plain)
    2. [Single Choice](#single-choice)
    3. [Multiple Choice](#multiple-choice)
    4. [Custom Adapters](#custom-adapters)
5. [Checkbox Prompts](#checkbox-prompts)
6. [Custom Views](#custom-views)

# Table of Contents - Input

1. [Text Input](#text-input)
    1. [Max Length](#max-length)
    2. [Custom Validation](#custom-validation)

# Table of Contents - Files

1. [Gradle Dependency](#gradle-dependency-1)
2. [File Choosers](#file-choosers)
3. [Folder Choosers](#folder-choosers)

# Table of Contents - Color

1. [Gradle Dependency](#gradle-dependency-2)
2. [Color Choosers](#color-choosers)

---

# Core

## Gradle Dependency

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
	
    implementation 'com.afollestad.material-dialogs:core:2.0.0-alpha1'
}
```

## Changes in Version 2

The whole library has been rebuilt, layouts and everything. The library is 100% Kotlin. APIs have 
changed and a lot of things will be broken if you upgrade from the older version. Other things 
to note:

1. There is no longer a separate `Builder` class, it's all-in-one.
2. All classes exist in the `core` module, the other extension dependencies
take advantage of Kotlin extensions to add functionality to it.
3. The use of the neutral button is deprecated to discourage use, see the 
[newer Material guidelines](https://material.io/design/components/dialogs.html#actions).
4. There is no longer a progress dialog included in library, since they are discouraged by Google, 
and discouraged by me. You should prefer a non-blocking inline progress indicator.
5. No dynamic color support, your dialogs will match your app theme. *I will be making sure 
[Aesthetic](https://github.com/afollestad/aesthetic) works correctly with this library if you really 
need dynamic theming.*
6. Other things will probably be added here.

## Getting Started

### Basics

### Kotlin Magic

### Adding an Icon

### Action Buttons

### Callbacks

### Dismissing 

## Lists

### Plain

### Single Choice

### Multiple Choice

### Custom Adapters 

## Checkbox Prompts

## Custom Views

---

# Input

## Gradle Dependency

The `input` module contains extensions to the core module, such as a text input dialog.

```gradle
dependencies {
	
    implementation 'com.afollestad.material-dialogs:input:2.0.0-alpha1'
}
```

## Text Input

### Max Length

### Custom Validation

---

# Files

## Gradle Dependency

The `files` module contains extensions to the core module, such as a file and folder chooser.

```gradle
dependencies {
	
    implementation 'com.afollestad.material-dialogs:files:2.0.0-alpha1'
}
```

## File Choosers

## Folder Choosers

---

# Color

## Gradle Dependency

The `color` module contains extensions to the core module, such as a color chooser.

```gradle
dependencies {
	
    implementation 'com.afollestad.material-dialogs:color:2.0.0-alpha1'
}
```

## Color Choosers