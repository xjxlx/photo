package com.android.selector.photo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.luck.picture.lib.utils.PictureFileUtils

class UriUtil {
    fun isUri(uriString: String): Boolean =
        try {
            Uri.parse(uriString)
            true
        } catch (e: Exception) {
            false
        }

    fun uriToPath(
        context: Context,
        uriString: String,
    ): String {
        var result = ""
        runCatching {
            val uri = Uri.parse(uriString)
            uri?.let {
                val scheme = it.scheme
                if (TextUtils.isEmpty(scheme)) {
                    val path = it.path
                    if (path != null) {
                        result = path
                    }
                } else if (TextUtils.equals(ContentResolver.SCHEME_FILE, scheme)) {
                    // file:// 开头的
                    val path = it.path
                    if (path != null) {
                        result = path
                    }
                } else if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
                    // 使用选择器的三方类库去获取
                    result = PictureFileUtils.getPath(context, uri)
                }
            }
        }
        return result
    }
}
