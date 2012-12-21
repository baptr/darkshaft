Operation: Darkshaft
====================
Cooperative multi-player, persistent progression, cross-platform Tower-D.

Building and Running
--------------------

### Desktop
Desktop builds should be easy.

    $ git clone https://github.com/baptr/darkshaft.git
    $ cd darkshaft/desktop
    $ ant run

### Android
Android build is uglier and more complicated.
If you don't already have the Android SDK, [download it](http://developer.android.com/sdk/index.html) and get it set up.

Once that's done, the build itself isn't too bad...

    $ git clone https://github.com/baptr/darkshaft.git
    $ cd darkshaft/android
    $ echo "sdk.dir=/LOCATION/OF/UNPACKED/ANDROID/SDK" >> local.properties
    $ ant debug

With any luck, you'll end with "Success" and be able to `ant installd`.

### Eclipse
To Be Determined...
