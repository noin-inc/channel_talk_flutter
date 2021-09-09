package ai.deepnatural.channel_talk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zoyi.channel.plugin.android.ChannelIO;
import com.zoyi.channel.plugin.android.open.callback.BootCallback;
import com.zoyi.channel.plugin.android.open.config.BootConfig;
import com.zoyi.channel.plugin.android.open.enumerate.BootStatus;
import com.zoyi.channel.plugin.android.open.enumerate.ChannelButtonPosition;
import com.zoyi.channel.plugin.android.open.listener.ChannelPluginListener;
import com.zoyi.channel.plugin.android.open.model.PopupData;
import com.zoyi.channel.plugin.android.open.model.Profile;
import com.zoyi.channel.plugin.android.open.model.User;
import com.zoyi.channel.plugin.android.open.model.UserData;
import com.zoyi.channel.plugin.android.open.option.ChannelButtonOption;
import com.zoyi.channel.plugin.android.open.option.Language;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import java.util.*;


/**
 * ChannelTalkFlutterPlugin
 */
public class ChannelTalkFlutterPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, ChannelPluginListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private static Context context;
    private Activity activity;

    public static void registerWith(Application application) {
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "channel_talk");
        channel.setMethodCallHandler(this);

        context = flutterPluginBinding.getApplicationContext();

        try {
            ChannelIO.initialize((Application) context);
        } catch (Exception e) {
        }

        // set listener
        ChannelIO.setListener(this);

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
        switch (call.method) {
            case "boot":
                boot(call, result);
                break;
            case "sleep":
                sleep(call, result);
                break;
            case "shutdown":
                shutdown(call, result);
                break;
            case "showChannelButton":
                showChannelButton(call, result);
                break;
            case "hideChannelButton":
                hideChannelButton(call, result);
                break;
            case "showMessenger":
                showMessenger(call, result);
                break;
            case "hideMessenger":
                hideMessenger(call, result);
                break;
            case "openChat":
                openChat(call, result);
                break;
            case "track":
                track(call, result);
                break;
            case "updateUser":
                updateUser(call, result);
                break;
            case "initPushToken":
                initPushToken(call, result);
                break;
            case "isChannelPushNotification":
                isChannelPushNotification(call, result);
                break;
            case "receivePushNotification":
                receivePushNotification(call, result);
                break;
            case "storePushNotification":
                result.error("UNAVAILABLE", "There is no API in Android", null);
                break;
            case "hasStoredPushNotification":
                hasStoredPushNotification(call, result);
                break;
            case "openStoredPushNotification":
                openStoredPushNotification(call, result);
                break;
            case "isBooted":
                isBooted(call, result);
                break;
            case "setDebugMode":
                setDebugMode(call, result);
                break;
            case "setPage":
                setPage(call, result);
                break;
            case "resetPage":
                resetPage(call, result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onShowMessenger() {
    }

    @Override
    public void onHideMessenger() {
    }

    @Override
    public void onChatCreated(String s) {
    }

    @Override
    public void onBadgeChanged(int i) {
        channel.invokeMethod("onBadgeChanged", i);
    }

    @Override
    public void onProfileChanged(String s, @Nullable Object o) {
    }

    @Override
    public boolean onUrlClicked(String url) {
        channel.invokeMethod("onUrlClicked", url );
        return false;
    }

    @Override
    public boolean onPushNotificationClicked(String s) {
        return false;
    }

    @Override
    public void onPopupDataReceived(PopupData popupData) {
        Map<String, Object> popupDataEntry = new LinkedHashMap<>();
        popupDataEntry.put("chatId", popupData.getChatId());
        popupDataEntry.put("avatarUrl", popupData.getAvatarUrl());
        popupDataEntry.put("name", popupData.getName());
        popupDataEntry.put("message", popupData.getMessage());

        channel.invokeMethod("onPopupDataReceived", popupDataEntry);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
        activity = activityPluginBinding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // destroyed to change configuration.
        // This call will be followed by onReattachedToActivityForConfigChanges().
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
        // after a configuration change.
    }

    @Override
    public void onDetachedFromActivity() {
        // Clean up references.
    }

    public void boot(@NonNull MethodCall call, @NonNull final Result result) {
        String pluginKey = call.argument("pluginKey");
        if (pluginKey == null || pluginKey.isEmpty()) {
            result.error("UNAVAILABLE", "Missing argument(pluginKey)", null);
            return;
        }
        String memberHash = call.argument("memberHash");
        if (memberHash == null || memberHash.isEmpty()) {
            result.error("UNAVAILABLE", "Missing argument(memberHash)", null);
            return;
        }

        Profile profile = Profile.create();
        if (call.argument("email") != null) {
            profile.setEmail(call.argument("email"));
        }
        if (call.argument("name") != null) {
            profile.setName(call.argument("name"));
        }
        if (call.argument("mobileNumber") != null) {
            profile.setMobileNumber(call.argument("mobileNumber"));
        }

        ChannelButtonOption buttonOption = new ChannelButtonOption(
                ChannelButtonPosition.LEFT,
                16,
                23
        );

        BootConfig bootConfig = BootConfig.create(pluginKey)
                .setProfile(profile)
                .setMemberHash(memberHash);
        if (call.argument("memberId") != null) {
            bootConfig.setMemberId(call.argument("memberId"));
        }
        if (call.argument("trackDefaultEvent") != null) {
            bootConfig.setTrackDefaultEvent(call.argument("trackDefaultEvent"));
        }
        if (call.argument("hidePopup") != null) {
            bootConfig.setHidePopup(call.argument("hidePopup"));
        }
        if (call.argument("language") != null) {
            bootConfig.setLanguage(getLanguage(call.argument("language")));
        }

        ChannelIO.boot(bootConfig, new BootCallback() {
            @Override
            public void onComplete(BootStatus bootStatus, @Nullable User user) {
                if (bootStatus == BootStatus.SUCCESS && user != null) {
                    // convert to Map
                    Map<String, Object> userEntry = new LinkedHashMap<>();
                    userEntry.put("id", user.getId());
                    userEntry.put("memberId", user.getMemberId());
                    userEntry.put("name", user.getName());
                    userEntry.put("avatarUrl", user.getAvatarUrl());
                    userEntry.put("alert", user.getAlert());
                    userEntry.put("profile", user.getProfile());
                    userEntry.put("tags", user.getTags());
                    userEntry.put("language", user.getLanguage());

                    result.success(userEntry);
                } else {
                    result.error("ERROR", "Execution failed(boot)", null);
                }
            }
        });
    }

    public void sleep(@NonNull MethodCall call, @NonNull final Result result) {
        ChannelIO.sleep();
        result.success(true);
    }

    public void shutdown(@NonNull MethodCall call, @NonNull final Result result) {
        ChannelIO.shutdown();
        result.success(true);
    }

    public void showChannelButton(@NonNull MethodCall call, @NonNull final Result result) {
        if (!ChannelIO.isBooted()) {
            result.error("UNAVAILABLE", "Channel Talk is not booted", null);
        }

        ChannelIO.showChannelButton();
        result.success(true);
    }

    public void hideChannelButton(@NonNull MethodCall call, @NonNull final Result result) {
        if (!ChannelIO.isBooted()) {
            result.error("UNAVAILABLE", "Channel Talk is not booted", null);
        }

        ChannelIO.hideChannelButton();
        result.success(true);
    }

    public void showMessenger(@NonNull MethodCall call, @NonNull final Result result) {
        if (!ChannelIO.isBooted()) {
            result.error("UNAVAILABLE", "Channel Talk is not booted", null);
        }

        ChannelIO.showMessenger(this.activity);
        result.success(true);
    }

    public void hideMessenger(@NonNull MethodCall call, @NonNull final Result result) {
        if (!ChannelIO.isBooted()) {
            result.error("UNAVAILABLE", "Channel Talk is not booted", null);
        }

        ChannelIO.hideMessenger();
        result.success(true);
    }

    public void openChat(@NonNull MethodCall call, @NonNull final Result result) {
        if (!ChannelIO.isBooted()) {
            result.error("UNAVAILABLE", "Channel Talk is not booted", null);
        }

        String chatId = call.argument("chatId");
        String message = call.argument("message");

        ChannelIO.openChat(this.activity, chatId, message);
        result.success(true);
    }

    public void track(@NonNull MethodCall call, @NonNull final Result result) {
        String eventName = call.argument("eventName");
        if (eventName == null || eventName.isEmpty()) {
            result.error("UNAVAILABLE", "Missing argument(EventName)", null);
            return;
        }
        Map<String, Object> properties = call.argument("properties");

        ChannelIO.track(eventName, properties);
        result.success(true);
    }

    public void updateUser(@NonNull MethodCall call, @NonNull final Result result) {
        if (!ChannelIO.isBooted()) {
            result.error("UNAVAILABLE", "Channel Talk is not booted", null);
        }

        Map<String, Object> profileMap = new HashMap<>();
        if (call.argument("name") != null) {
            profileMap.put("name", call.argument("name"));
        }
        if (call.argument("mobileNumber") != null) {
            profileMap.put("mobileNumber", call.argument("mobileNumber"));
        }
        if (call.argument("email") != null) {
            profileMap.put("email", call.argument("email"));
        }
        if (call.argument("avatarUrl") != null) {
            profileMap.put("avatarUrl", call.argument("avatarUrl"));
        }
        if (call.argument("customAttributes") != null) {
            Map<String, Object> customAttributes = call.argument("customAttributes");
            for (Map.Entry<String, Object> entry : customAttributes.entrySet()) {
                profileMap.put(entry.getKey(), entry.getValue());
            }
        }

        Language enumLanguage = Language.JAPANESE;
        if (call.argument("language") != null) {
            enumLanguage = getLanguage(call.argument("language"));
        }

        List<String> tags = new ArrayList<String>();
        if (call.argument("tags") != null) {
            tags = call.argument("tags");
        }

        UserData userData = new UserData.Builder()
                .setLanguage(enumLanguage)
                .setProfileMap(profileMap)
                .setTags(tags)
                .build();

        ChannelIO.updateUser(userData, (e, user) -> {
            if (e == null && user != null) {
                result.success(true);
            } else if (e != null) {
                result.error("ERROR", "Execution failed(updateUser)", null);
            }
        });
    }

    public void initPushToken(@NonNull MethodCall call, @NonNull final Result result) {
        String deviceToken = call.argument("deviceToken");
        if (deviceToken == null || deviceToken.isEmpty()) {
            result.error("UNAVAILABLE", "Missing argument(deviceToken)", null);
            return;
        }

        try {
            ChannelIO.initPushToken(deviceToken);
        } catch (Exception e) {
        }
        result.success(true);
    }

    public void isChannelPushNotification(@NonNull MethodCall call, @NonNull final Result result) {
        Map<String, String> content = call.argument("content");
        if (content == null || content.isEmpty()) {
            result.error("UNAVAILABLE", "Missing argument(content)", null);
            return;
        }
        Boolean res = ChannelIO.isChannelPushNotification(content);
        result.success(res);
    }

    public void receivePushNotification(@NonNull MethodCall call, @NonNull final Result result) {
        Map<String, String> content = call.argument("content");
        if (content == null || content.isEmpty()) {
            result.error("UNAVAILABLE", "Missing argument(content)", null);
            return;
        }
        ChannelIO.receivePushNotification(context, content);
        result.success(true);
    }

    public void hasStoredPushNotification(@NonNull MethodCall call, @NonNull final Result result) {
        Boolean res = ChannelIO.hasStoredPushNotification(this.activity);
        result.success(res);
    }

    public void openStoredPushNotification(@NonNull MethodCall call, @NonNull final Result result) {
        ChannelIO.openStoredPushNotification(this.activity);
        result.success(true);
    }

    public void isBooted(@NonNull MethodCall call, @NonNull final Result result) {
        result.success(ChannelIO.isBooted());
    }

    public void setDebugMode(@NonNull MethodCall call, @NonNull final Result result) {
        Boolean flag = call.argument("flag");
        if (flag == null) {
            result.error("UNAVAILABLE", "Missing argument(flag)", null);
            return;
        }
        ChannelIO.setDebugMode(flag);
        result.success(true);
    }

    public void setPage(@NonNull MethodCall call, @NonNull final Result result) {
        String page = call.argument("page");
        if (page == null) {
            result.error("UNAVAILABLE", "Missing argument(page)", null);
            return;
        }
        ChannelIO.setPage(page);
        result.success(true);
    }

    public void resetPage(@NonNull MethodCall call, @NonNull final Result result) {
        ChannelIO.resetPage();
        result.success(true);
    }

    private Language getLanguage(String lang) {
        switch (lang) {
            case "english":
                return Language.ENGLISH;
            case "korean":
                return Language.KOREAN;
            case "japanese":
                return Language.JAPANESE;
            default:
                return Language.KOREAN;
        }
    }

}
