part of channel_talk_flutter;

class ChannelTalkUser {
  String? id;
  String? memberId;
  String? name;
  String? avatarUrl;
  int? alert;
  Map<Object?, Object?>? profile;
  bool? unsubscribeEmail;
  bool? unsubscribeTexting;
  List<Object?>? tags;
  String? language;

  static ChannelTalkUser fromNative(Map<Object?, Object?>? userEntry) {
    var user = ChannelTalkUser();
    if (userEntry != null) {
      user.id = userEntry['id'] as String?;
      user.memberId = userEntry['memberId'] as String?;
      user.name = userEntry['name'] as String?;
      user.avatarUrl = userEntry['avatarUrl'] as String?;
      user.alert = userEntry['alert'] as int?;
      user.profile = userEntry['profile'] as Map<Object?, Object?>?;
      user.unsubscribeEmail = userEntry['unsubscribeEmail'] as bool?;
      user.unsubscribeTexting = userEntry['unsubscribeTexting'] as bool?;
      user.tags = userEntry['tags'] as List<Object?>?;
      user.language = userEntry['language'] as String?;
    }
    return user;
  }
}
