part of channel_talk_flutter;

class ChannelTalkPopupData {
  String? chatId;
  String? avatarUrl;
  String? name;
  String? message;

  static ChannelTalkPopupData fromNative(Map<Object?, Object?>? popupEntry) {
    var popupData = ChannelTalkPopupData();
    if (popupEntry != null) {
      popupData.chatId = popupEntry['chatId'] as String?;
      popupData.avatarUrl = popupEntry['avatarUrl'] as String?;
      popupData.name = popupEntry['name'] as String?;
      popupData.message = popupEntry['message'] as String?;
    }
    return popupData;
  }
}
