# Muslim App

An Android prayer-times app that schedules precise, Doze-resistant alarms via AlarmManager with setAlarmClock(), with full-screen ringing, snooze/dismiss notification actions, and boot-persistent scheduling.

## 🛠 Technical Implementation

### Alarm & Scheduling
The app leverages the **`AlarmManager`** API for high-precision timing:
- **`setAlarmClock()`**: Used to provide the highest priority scheduling, ensuring alarms are never deferred by the system and are visible in the status bar.
- **`AlarmScheduler`**: A dedicated component that manages the lifecycle of alarms, handling scheduling, cancellation, and exact alarm permission checks.

### Background Services
- **Foreground Service (`AlarmService`)**: When an alarm triggers, the app promotes itself to a foreground service. This ensures that:
    - Audio playback (Azan) continues uninterrupted.
    - Device vibration patterns are managed correctly.
    - The system doesn't kill the process while the user is interacting with the alarm.

### Notifications
- **High Importance Channels**: Custom notification channels configured in `MuslimApp` with `IMPORTANCE_HIGH` to ensure visibility.
- **Full-Screen Intent**: Uses `setFullScreenIntent` to launch the `AlarmRingingActivity` even if the screen is locked or the user is in another app.
- **Interactive Actions**: Integrated `AlarmActionReceiver` to handle "Dismiss" and "Snooze" actions without requiring the user to open the full app.

### Core Stack
- **UI**: 100% Jetpack Compose for a modern, reactive interface.
- **Architecture**: Clean Architecture principles with Hilt for Dependency Injection.
- **Database**: Room for local storage of prayer times and user settings.
- **Networking**: Ktor for fetching prayer time data.
- **Lifecycle**: Handles boot completion via `BootReceiver` to reschedule alarms automatically after a device restart.

## 🚀 Key Features

- **Precise Prayer Scheduling**: Calculates and schedules alarms for prayer times throughout the day.
- **Reliable Alarms**: Uses advanced Android scheduling APIs to ensure alarms trigger even when the device is in Doze mode.
- **Immersive Ringing Experience**: A full-screen activity that provides immediate focus when an alarm triggers.
- **Notification Controls**: Quick actions to snooze or dismiss alarms directly from the notification shade.

## 📸 Screenshots
<img width="460" height="800" alt="image" src="https://github.com/user-attachments/assets/ecc885d5-d497-4fd5-900d-6950865f8761" />
<img width="460" height="800" alt="image" src="https://github.com/user-attachments/assets/f9290437-7fa1-43bf-a7af-eac7521c8444" />
<img width="460" height="800" alt="image" src="https://github.com/user-attachments/assets/e8b5d3db-4445-4d55-af22-5cefc6a7961b" />
<img width="460" height="800" alt="image" src="https://github.com/user-attachments/assets/d99efdb0-245b-4e06-8364-4b542474efd1" />






