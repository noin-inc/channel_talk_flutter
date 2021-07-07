import 'dart:async';

import 'package:flutter/services.dart';

class ChannelTalk {
  static const MethodChannel _channel = MethodChannel('channel_talk');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool?> boot({
    required String pluginKey,
    required String memberHash,
    String? memberId,
    String? email,
    String? name,
    String? mobileNumber,
    bool? trackDefaultEvent,
    bool? hidePopup,
    String? language,
  }) {
    Map<String, dynamic> config = {
      'pluginKey': pluginKey,
      'memberHash': memberHash,
    };

    if (memberId != null) {
      config['memberId'] = memberId;
    }
    if (email != null) {
      config['email'] = email;
    }
    if (name != null) {
      config['name'] = name;
    }
    if (mobileNumber != null) {
      config['mobileNumber'] = mobileNumber;
    }
    if (trackDefaultEvent != null) {
      config['trackDefaultEvent'] = trackDefaultEvent;
    }
    if (hidePopup != null) {
      config['hidePopup'] = hidePopup;
    }
    if (language != null) {
      config['language'] = language;
    }

    return _channel.invokeMethod<bool>('boot', config);
  }

  static Future<bool?> sleep() {
    return _channel.invokeMethod<bool>('sleep');
  }

  static Future<bool?> shutdown() {
    return _channel.invokeMethod<bool>('shutdown');
  }

  static Future<bool?> showChannelButton() {
    return _channel.invokeMethod<bool>('showChannelButton');
  }

  static Future<bool?> hideChannelButton() {
    return _channel.invokeMethod<bool>('hideChannelButton');
  }

  static Future<bool?> showMessenger() {
    return _channel.invokeMethod<bool>('showMessenger');
  }

  static Future<bool?> hideMessenger() {
    return _channel.invokeMethod<bool>('hideMessenger');
  }

  static Future<bool?> openChat({
    required String chatId,
    required String message,
  }) {
    return _channel.invokeMethod<bool>(
      'openChat',
      {
        'chatId': chatId,
        'message': message,
      },
    );
  }

  static Future<bool?> track({
    required String eventName,
    Map<String, dynamic>? properties,
  }) {
    Map<String, dynamic> data = {
      'eventName': eventName,
    };

    if (properties != null) {
      data['properties'] = properties;
    }

    return _channel.invokeMethod<bool>(
      'track',
      data,
    );
  }

  static Future<bool?> updateUser({
    String? name,
    String? mobileNumber,
    String? email,
    String? avatarUrl,
    Map<String, dynamic>? customAttributes,
    String? language,
    List<String>? tags,
  }) {
    return _channel.invokeMethod<bool>('updateUser', {
      'name': name,
      'mobileNumber': mobileNumber,
      'email': email,
      'avatarUrl': avatarUrl,
      'customAttributes': customAttributes,
      'language': language,
      'tags': tags,
    });
  }

  static Future<bool?> initPushToken({
    required String deviceToken,
  }) {
    return _channel.invokeMethod<bool>('initPushToken', {
      'deviceToken': deviceToken,
    });
  }

  static Future<bool?> isChannelPushNotification({
    required Map<String, dynamic> content,
  }) {
    return _channel.invokeMethod<bool>('isChannelPushNotification', {
      'content': content,
    });
  }

  static Future<bool?> receivePushNotification({
    required Map<String, dynamic> content,
  }) {
    return _channel.invokeMethod('receivePushNotification', {
      'content': content,
    });
  }

  static Future<bool?> storePushNotification({
    required Map<String, dynamic> content,
  }) {
    return _channel.invokeMethod<bool>('storePushNotification', {
      'content': content,
    });
  }

  static Future<bool?> hasStoredPushNotification() {
    return _channel.invokeMethod<bool>('hasStoredPushNotification');
  }

  static Future<bool?> openStoredPushNotification() {
    return _channel.invokeMethod<bool>('openStoredPushNotification');
  }

  static Future<bool?> isBooted() {
    return _channel.invokeMethod<bool>('isBooted');
  }

  static Future<bool?> setDebugMode({
    required bool flag,
  }) {
    return _channel.invokeMethod<bool>('setDebugMode', {
      'flag': flag,
    });
  }

  static Future<bool?> setPage({
    required page,
  }) {
    return _channel.invokeMethod<bool>('setPage', {
      'page': page,
    });
  }

  static Future<bool?> resetPage() {
    return _channel.invokeMethod<bool>('resetPage');
  }
}
