Android Record Voice Project
============================

使用Android来录音，坑很多。

初始化的坑
-----

不论是页面上的component，还是文件，还是Recorder/Player，都必须在`onCreate`方法调用以后才能初始化，否则异常。所以要把它们放到`lazy`里（还好kotlin突出提供了lazy）

权限的坑
-------

- `AndroidManifest.xml`里面要加权限：`<uses-permission android:name="android.permission.RECORD_AUDIO"/>`
- 由于录音属于“危险权限”，所以光加上面的还不够，必须在运行时先向用户请求权限：`ActivityCompat.requestPermissions`，用户同意以后，才可以调用录音相关的API
- 可使用`ActivityCompat.checkSelfPermission`来检查用户是否同意过某个权限
- 在`ActivityCompat.requestPermissions`时传入的第三个参数`requestCode`是由自己定义的，可用来在`onRequestPermissionsResult`检查结果

录音的坑
-------

- 不论是录音`MediaPlayer`还是播放`MediaPlayer`，这两个类都有自己内部维护的状态机。所以`reset/start/stop/release`这几个方法一定要在理解后小心调用。参看：<https://developer.android.com/reference/android/media/MediaRecorder>
- 如果开始录音但是没有录到声音，调用`recorder.stop()`方法居然会抛异常！所以要针对这个情况处理（就是catch一下这个exception）

Resource
--------

- States: <https://developer.android.com/reference/android/media/MediaRecorder>