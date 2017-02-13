package org.mariotaku.twidere.extension.model

import android.content.Context
import org.mariotaku.twidere.R
import org.mariotaku.twidere.model.AccountDetails
import org.mariotaku.twidere.model.ParcelableMessage
import org.mariotaku.twidere.model.ParcelableMessageConversation
import org.mariotaku.twidere.model.ParcelableMessageConversation.ConversationType
import org.mariotaku.twidere.model.ParcelableUser
import org.mariotaku.twidere.util.UserColorNameManager

fun ParcelableMessageConversation.applyFrom(message: ParcelableMessage, details: AccountDetails) {
    account_key = details.key
    account_color = details.color
    message_type = message.message_type
    message_timestamp = message.message_timestamp
    local_timestamp = message.local_timestamp
    sort_id = message.sort_id
    text_unescaped = message.text_unescaped
    media = message.media
    spans = message.spans
    message_extras = message.extras
    sender_key = message.sender_key
    recipient_key = message.recipient_key
    is_outgoing = message.is_outgoing
    request_cursor = message.request_cursor
}

val ParcelableMessageConversation.timestamp: Long
    get() = if (message_timestamp > 0) message_timestamp else local_timestamp

fun ParcelableMessageConversation.getConversationName(context: Context,
        manager: UserColorNameManager): Pair<String, String?> {
    if (conversation_type == ConversationType.ONE_TO_ONE) {
        val user = this.user ?: return Pair(context.getString(R.string.direct_messages), null)
        return Pair(user.name, user.screen_name)
    }
    if (conversation_name != null) {
        return Pair(conversation_name, null)
    }
    return Pair(participants.joinToString(separator = ", ") { manager.getDisplayName(it, false) }, null)
}

fun ParcelableMessageConversation.getSummaryText(context: Context, manager: UserColorNameManager,
        nameFirst: Boolean): CharSequence? {
    return getSummaryText(context, manager, nameFirst, message_type, message_extras, sender_key,
            text_unescaped, this)
}

val ParcelableMessageConversation.user: ParcelableUser?
    get() {
        val userKey = if (is_outgoing) recipient_key else sender_key
        return participants.firstOrNull { it.key == userKey }
    }
