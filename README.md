# AlarmManager

Alarm Manager:
--------------

AlarmManager is a bridge between application and Android system alarm service.
It can send a broadcast to your app (which can be completely terminated by user) at a scheduled time and your app can then perform any task accordingly.

With the help from PendingIntent and Intent, a bundle of information can also be sent together with the system broadcast to your application when alarm goes off.

However, since Android KitKat (API 19) and Android MarshMallow (API 23), Google has added different restrictions on AlarmManager to reduce the battery usage.
Alarm no longer goes off exactly at the assigned time by default.
System has the ability to defer any alarm in order to optimise the device performance.

Set up BroadcastReceiver:
-------------------------

There are two main parts for setting up an alarm. They are
1) Create a BroadcastReceiver to receive system broadcast and register it at the AndroidManifest.xml
2) Register an alarm with an alarm type, PendingIntent and designated time to the system ALARM_SERVICE

BroadcastReceiver is a class for receiving and handling the broadcast sent to your application.
In system alarm case, it receives broadcast from the system alarm service when alarm goes off.
It has to be either registered dynamically in activity or statically declared at AndroidManifest.xml.
The above codes show an example to register at AndroidManifest statically.

It is strongly recommended to check the action field in Intent to ensure the broadcast is coming from your assigned PendingIntent.

Setup in Activity:
------------------

Get AlarmManager instance:
--------------------------

val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

Prepare for an Intent:
----------------------

Android allows a bundle of information to be sent to a target receiver when alarm goes off.

val intent = Intent(this, AlarmReceiver::class.java)
intent.action = "FOO_ACTION"
intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo")

Prepare for a PendingIntent:
----------------------------

PendingIntent is a reference pointing to a token maintained by the Android system.
Thus, it will still be valid if the application is killed by user and can be broadcasted at some moment in the future.

val pendingIntentRequestCode = 0
val flag = 0
val pendingIntent = PendingIntent.getBroadcast(this, pendingIntentRequestCode, intent, flag)

There are totally 4 functions for initialising a PendingIntent but only 1 of them is applicable:
1) PendingIntent.getBroadcast() — Applicable to AlarmManager
2) PendingIntent.getActivity()
3) PendingIntent.getActivities()
4) PendingIntent.getService()

The request code can be treated as an identifier for different PendingIntent tokens with the same Intent.
In other words, it is only useful when you want multiple PendingIntent to have the same Intent.

Flags indicates how system should handle the new and existing PendingIntent s that have the same Intent.
0 indicates that system will use its default way to handle the creation of PendingIntent.
The following are some examples:
1) FLAG_UPDATE_CURRENT
2) FLAG_CANCEL_CURRENT
3) FLAG_IMMUTABLE

Set the alarm time and send to system:
--------------------------------------

val ALARM_DELAY_IN_SECOND = 10
val alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L
alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent)

Handling of alarm event:
-----------------------

AlarmManager provides two ways to listen to an alarm broadcast. They are
1) Local listener (AlarmManager.OnAlarmListener)
2) BroadcastReceiver specified at the Intent wrapped inside a PendingIntent.

Local Listener:
---------------

There is a limitation on AlarmManager.OnAlarmListener over PendingIntent.
It cannot work when the corresponding Activity or Fragment is destroyed since the callback object is released at the same time.
However, because PendingIntent is sent to the system alarm service, it can be fired even when the application is killed by user.

val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
val alarmTime = System.currentTimeMillis() + 4000
val tagStr = "TAG"
val handler = null // `null` means the callback will be run at the Main thread
alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, tagStr, object : AlarmManager.OnAlarmListener {
  override fun onAlarm() {
    Toast.makeText(this, "AlarmManager.OnAlarmListener", Toast.LENGTH_LONG).show()
  }
}, null)

Way to set alarm:
-----------------

To set a window of alarm time:
------------------------------

set() - It will let Android interrupt the scheduled time.
// Fully let Android defer alarm to optimise battery usage
alarmManager.set(AlarmManager.RTC_WAKEUP, fooAlarmTimeAtUTC, pendingIntent)

setExact() - It will ask Android to fire exactly at what the requested time is.
// Don't let Android defer alarm by any second
alarmManager.setExact(AlarmManager.RTC_WAKEUP, fooAlarmTimeAtUTC, pendingIntent)

setWindow() - which sits between set() and setExact(). It let Android defer alarm to optimise battery usage but guarantees the alarm will be fired within the given window of time.
// Let Android defer alarm but must fire within the given period of time
alarmManager.setWindow(AlarmManager.RTC_WAKEUP, fooAlarmTimeAtUTC, 10_000L, pendingIntent)

Doze Mode:
----------

When user turns screen off and does not plug in any power supply, device goes to Doze mode.
In Doze mode, Android restricts app from accessing to the network and CPU-intensive services.
Thus, the scheduled alarm will be suspended and deferred.

App StandBy:
------------

App Standby mode is similar to the Doze mode except the screen is not required to be off.
Android would consider an app to be idle when the app is sent to the background for a certain period of time with no foreground service and does not generate any notification.

setAndAllowWhileIdle() & setExactAndAllowWhileIdle() - It will take care of Doze mode and App Standby handling on the alarm services

Alarm reference time:
---------------------

AlarmManager accepts two types of time to fire an alarm:
1) Real Time Clock (RTC) - Real Time Clock is the absolute time since January 1, 1970 UTC
2) Elapsed Real Time - Elapsed Real Time is the relative time since the device is booted.

By default, AlarmManager fires an alarm only when device is not sleeping.
The types are RTC and ELAPSED_REALTIME.
In order to have an alarm goes off during device is sleeping, the type of alarm must be set to either RTC_WAKE_UP or ELAPSED_REALTIME_WAKEUP.

// Real Time Clock (Real time since 1st January, 1970)
val alarmTimeAtUTC = System.currentTimeMillis() + 1 * 1_000L
alarmManager.setExact(AlarmManager.RTC, alarmTimeAtUTC, pendingIntent)
alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent)

// Elapsed Real Time (since device is booted)
val alarmTimeAfterDeviceIsBooted = 1 * 1_000L
alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, alarmTimeAfterDeviceIsBooted, pendingIntent)
alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTimeAfterDeviceIsBooted, pendingIntent)

To cancel an alarm:
-------------------

1) To cancel a listener,
simply save a global instance of listener and cancel it with the below function:

val onAlarmListener = object : AlarmManager.OnAlarmListener {
  override fun onAlarm() {
     toast("Alarm goes off.")
  }
}
alarmManager.cancel(onAlarmListener)

2) To cancel a pending intent,
it is tricky to cancel a PendingIntent since it depends on both the Intent and requestCode used.

val requestCode = 0

fun getAlarmIntent(): Intent {
  val intent = Intent(this, AlarmReceiver::class.java)
  intent.action = ALARM_RECEIVER_ACTION
  return intent
}

fun cancelAlarm() {
  val sender = PendingIntent.getBroadcast(fooActivityContext, requestCode, getAlarmIntent(), 0)
  val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
  alarmManager.cancel(sender)
}

To set a repeating alarm:
-------------------------

val timeInterval = 5 * 1_000L
val alarmTime = System.currentTimeMillis()
alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, timeInterval , fooPendingIntent)

To preserve battery life, Android has changed the policy on handling repeating alarm since Android 5.1 (API 22).
It delays the alarm at least 5 seconds into the future and limit the repeat interval to at least 60 seconds.

However, unlike PendingIntent alarm, it can no longer function when application is killed by user.

Reset alarm:
------------

Android does not save any alarms when the device is turned off.
Therefore, all alarms will be missed when device is booted.
In order to maintain all the previously set alarms, we have to listen to the system boot event and manually set back all the alarms when device is turned on again.
Be aware that listening to android.intent.action.BOOT_COMPLETED is considered as a dangerous operation and the <uses-permission> of android.permission.RECEIVE_BOOT_COMPLETED must also be added in AndroidManifest.xml as required by Android.
