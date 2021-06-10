# TagEditText
[![JitPack](https://jitpack.io/v/hearsilent/TagEditText.svg)](https://jitpack.io/#hearsilent/TagEditText)
[![license](https://img.shields.io/github/license/hearsilent/TagEditText.svg)](https://github.com/hearsilent/TagEditText/blob/main/LICENSE)

A simple Android Tag EditText.

<img src="https://github.com/hearsilent/TagEditText/raw/main/screenshots/device-2021-06-11-003802.png" height="500">    <img src="https://github.com/hearsilent/TagEditText/raw/main/screenshots/device-2021-06-11-003906.png" height="500">

## Setup

The easiest way to add the **TagEditText** library to your project is by adding it as a dependency to your build.gradle

**Step 1.** Add the JitPack repository to your build file
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency
```gradle
dependencies {
    implementation 'com.github.hearsilent:TagEditText:1.0.0'
}
```

## Usage

Setup tag list by:
```java
TagEditText.setTags(mTagList);
```

Setup tag list's item layout by:
```java
TagEditText.setAdapter(new ArrayAdapter());
```

## Customize
You can change [`item_user_suggestion.xml`](https://github.com/hearsilent/TagEditText/blob/main/app/src/main/res/layout/item_user_suggestion.xml) as you want.

## TODO
- Support change tag list background color dynamic
- Support change tagged users color dynamic
- Auto showing tag list when re-focused

## Compatibility

Android LOLLIPOP 5.0+

## Credits

Users from [maximedegreve/TinyFaces](https://github.com/maximedegreve/TinyFaces).

## License

    MIT License

    Copyright (c) 2021 HearSilent

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

