
# ⏱️ CoolTimer

## Contents
- [Introduction](#introduction)
- [Download](#download)
- [Technologies](technologies)
- [Demo](#demo)
- [License](#license)

## Introduction
CoolTimer is made with modern Android techniques in Jetpack Compose Multiplatform targeting Android and iOS.
It's main purpose is to provide a user friendly interface and ready to use app for setting intervals with work and rest periods.

## Download
Go to [Releases](https://github.com/IvanCaEz/MultiplatformTimer/releases) to download the latest APK.

## Technologies
- [Kotlin](https://kotlinlang.org) based, using [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations.
- Jetpack Libraries:
  - [Jetpack Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html): Android’s modern toolkit for declarative UI development.
  - Lifecycle: Observes Android lifecycles and manages UI states upon lifecycle changes.
  - ViewModel: Manages UI-related data and is lifecycle-aware, ensuring data survival through configuration changes.
  - Navigation: Facilitates screen navigation.
  - [Room](https://developer.android.com/training/data-storage/room): Saves data in a local database using SQLite  
- Architecture
  - MVVM Architecture (View - Model - ViewModel)
- [Material-Components](https://github.com/material-components/material-components-android?tab=readme-ov-file): Material design components for building UI components.

## Demo
https://github.com/IvanCaEz/MultiplatformTimer/assets/125135728/73f2c848-4247-4028-9eb5-4e25607c4179

## License
````
Designed and developed by 2024 IvanCaEz (Iván Martínez Cañero)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````

