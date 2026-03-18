package com.android.selector.photo

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.android.selector.interfaces.PhotoCallBackListener
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.PictureFileUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

class PhotoUtil {
    private var mContext: Context? = null
    private val mListPhoto = mutableListOf<String>()

    fun openPhotoCamera(
        activity: Activity,
        listener: PhotoCallBackListener,
    ) {
        mContext = activity
        openPhotoCamera(PictureSelector.create(activity), activity, listener)
    }

    fun openPhotoCamera(
        fragment: Fragment,
        listener: PhotoCallBackListener,
    ) {
        val ctx = fragment.context
        if (ctx != null) {
            mContext = ctx
        }
        openPhotoCamera(PictureSelector.create(fragment), ctx, listener)
    }

    private fun openPhotoCamera(
        pictureSelector: PictureSelector,
        context: Context?,
        listener: PhotoCallBackListener,
    ) {
        pictureSelector
            .openCamera(SelectMimeType.ofImage())
            .setCompressEngine(
                CompressFileEngine { engineContext, source, call ->
                    Luban
                        .with(engineContext)
                        .load(source)
                        .ignoreBy(100)
                        .setCompressListener(
                            object : OnNewCompressListener {
                                override fun onStart() {}

                                override fun onSuccess(
                                    source: String?,
                                    compressFile: File?,
                                ) {
                                    call.onCallback(source, compressFile?.absolutePath)
                                }

                                override fun onError(
                                    source: String?,
                                    e: Throwable?,
                                ) {
                                    call.onCallback(source, null)
                                }
                            },
                        ).launch()
                },
            )
            .forResult(
                object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        val list = mutableListOf<String>()
                        result?.firstOrNull()?.let { media ->
                            list.add(getPath(context, media))
                        }
                        mListPhoto.clear()
                        mListPhoto.addAll(list)
                        listener.onCallBack(mListPhoto)
                    }

                    override fun onCancel() {
                        mListPhoto.clear()
                        listener.onCallBack(mListPhoto)
                    }
                },
            )
    }

    @JvmOverloads
    fun openPhotoGallery(
        activity: Activity,
        listener: PhotoCallBackListener,
        maxSize: Int = 9,
        spanCount: Int = 4,
    ) {
        mContext = activity
        openPhotoGallery(PictureSelector.create(activity), activity, listener, maxSize, spanCount)
    }

    @JvmOverloads
    fun openPhotoGallery(
        fragment: Fragment,
        listener: PhotoCallBackListener,
        maxSize: Int = 9,
        spanCount: Int = 4,
    ) {
        val ctx = fragment.context
        if (ctx != null) {
            mContext = ctx
        }
        openPhotoGallery(PictureSelector.create(fragment), ctx, listener, maxSize, spanCount)
    }

    private fun openPhotoGallery(
        pictureSelector: PictureSelector,
        context: Context?,
        listener: PhotoCallBackListener,
        maxSize: Int,
        spanCount: Int,
    ) {
        pictureSelector
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setCompressEngine(
                CompressFileEngine { engineContext, source, call ->
                    Luban
                        .with(engineContext)
                        .load(source)
                        .ignoreBy(100)
                        .setCompressListener(
                            object : OnNewCompressListener {
                                override fun onStart() {}

                                override fun onSuccess(
                                    source: String?,
                                    compressFile: File?,
                                ) {
                                    call.onCallback(source, compressFile?.absolutePath)
                                }

                                override fun onError(
                                    source: String?,
                                    e: Throwable?,
                                ) {
                                    call.onCallback(source, null)
                                }
                            },
                        ).launch()
                },
            )
            .setSelectionMode(if (maxSize > 1) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
            .setMaxSelectNum(maxSize)
            .setImageSpanCount(spanCount)
            .isPreviewImage(true)
            .isDisplayCamera(false)
            .forResult(
                object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        mListPhoto.clear()
                        result?.forEach { media ->
                            media?.let {
                                mListPhoto.add(getPath(context, it))
                            }
                        }
                        listener.onCallBack(mListPhoto)
                    }

                    override fun onCancel() {
                        mListPhoto.clear()
                        listener.onCallBack(mListPhoto)
                    }
                },
            )
    }

    /**
     * @param maxDuration 视频的录制时长
     * @param videoQuality 视频的录制质量，1：高质量 2：底质量
     */
    @JvmOverloads
    fun openVideoCamera(
        activity: Activity,
        listener: PhotoCallBackListener,
        maxDuration: Int = 60,
        videoQuality: Int = 1,
    ) {
        mContext = activity
        openVideoCamera(PictureSelector.create(activity), activity, listener, maxDuration, videoQuality)
    }

    @JvmOverloads
    fun openVideoCamera(
        fragment: Fragment,
        listener: PhotoCallBackListener,
        maxDuration: Int = 60,
        videoQuality: Int = 1,
    ) {
        val ctx = fragment.context
        if (ctx != null) {
            mContext = ctx
        }
        openVideoCamera(PictureSelector.create(fragment), ctx, listener, maxDuration, videoQuality)
    }

    private fun openVideoCamera(
        pictureSelector: PictureSelector,
        context: Context?,
        listener: PhotoCallBackListener,
        maxDuration: Int,
        videoQuality: Int,
    ) {
        pictureSelector
            .openCamera(SelectMimeType.ofVideo())
            .setRecordVideoMaxSecond(maxDuration)
            .setRecordVideoMinSecond(1)
            .setVideoQuality(videoQuality)
            .forResult(
                object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        mListPhoto.clear()
                        result?.firstOrNull()?.let { media ->
                            mListPhoto.add(getPath(context, media))
                        }
                        listener.onCallBack(mListPhoto)
                    }

                    override fun onCancel() {
                        mListPhoto.clear()
                        listener.onCallBack(mListPhoto)
                    }
                },
            )
    }

    @JvmOverloads
    fun openVideoGallery(
        activity: Activity,
        listener: PhotoCallBackListener,
        maxSize: Int = 9,
        spanCount: Int = 4,
    ) {
        mContext = activity
        openVideoGallery(PictureSelector.create(activity), activity, listener, maxSize, spanCount)
    }

    @JvmOverloads
    fun openVideoGallery(
        fragment: Fragment,
        listener: PhotoCallBackListener,
        maxSize: Int = 9,
        spanCount: Int = 4,
    ) {
        val ctx = fragment.context
        if (ctx != null) {
            mContext = ctx
        }
        openVideoGallery(PictureSelector.create(fragment), ctx, listener, maxSize, spanCount)
    }

    private fun openVideoGallery(
        pictureSelector: PictureSelector,
        context: Context?,
        listener: PhotoCallBackListener,
        maxSize: Int = 9,
        spanCount: Int = 4,
    ) {
        pictureSelector
            .openGallery(SelectMimeType.ofVideo())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setVideoPlayerEngine(ExoPlayerEngine())
            .isDisplayCamera(false)
            .isAutoVideoPlay(false)
            .isLoopAutoVideoPlay(false)
            .isUseSystemVideoPlayer(false)
            .setSelectionMode(if (maxSize > 1) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
            .isVideoPauseResumePlay(true)
            .isPreviewVideo(true)
            .setMaxSelectNum(maxSize)
            .setImageSpanCount(spanCount)
            .forResult(
                object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        mListPhoto.clear()
                        result?.forEach { media ->
                            media?.let {
                                mListPhoto.add(getPath(context, it))
                            }
                        }
                        listener.onCallBack(mListPhoto)
                    }

                    override fun onCancel() {
                        mListPhoto.clear()
                        listener.onCallBack(mListPhoto)
                    }
                },
            )
    }

    private fun isUri(uriString: String): Boolean =
        try {
            Uri.parse(uriString)
            true
        } catch (_: Exception) {
            false
        }

    private fun getPath(context: Context?, localMedia: LocalMedia): String {
        var path = ""

        if (localMedia.isCompressed) {
            path = localMedia.compressPath
        }
        if (TextUtils.isEmpty(path)) {
            path = localMedia.availablePath
        }
        if (TextUtils.isEmpty(path)) {
            path = localMedia.realPath
        }
        if (TextUtils.isEmpty(path)) {
            path = localMedia.path
        }

        if (TextUtils.isEmpty(path)) {
            return ""
        }

        val uri = isUri(path)
        if (uri) {
            val parse = Uri.parse(path)
            val scheme = parse.scheme
            if (TextUtils.isEmpty(scheme)) {
                parse.path?.let { path = it }
            } else if (TextUtils.equals(ContentResolver.SCHEME_FILE, scheme)) {
                parse.path?.let { path = it }
            } else if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
                path = context?.let { PictureFileUtils.getPath(it, parse) } ?: ""
            }
        }
        return path
    }

    companion object {
        /**
         * 从uri中获取path路径
         */
        @JvmStatic
        fun getPathForUri(
            context: Context,
            uri: Uri?,
        ): String {
            var tempPath = ""
            if (uri != null) {
                val scheme = uri.scheme
                if (TextUtils.isEmpty(scheme)) {
                    uri.path?.let { tempPath = it }
                } else if (TextUtils.equals(ContentResolver.SCHEME_FILE, scheme)) {
                    uri.path?.let { tempPath = it }
                } else if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
                    tempPath = PictureFileUtils.getPath(context, uri)
                }
            }
            return tempPath
        }

        /**
         * 鲁班压缩
         */
        @JvmStatic
        fun compress(
            context: Context,
            path: String,
            result: (Boolean, String?) -> Unit,
        ) {
            Luban
                .with(context)
                .load(path)
                .ignoreBy(100)
                .setCompressListener(
                    object : OnNewCompressListener {
                        override fun onStart() {}

                        override fun onSuccess(
                            source: String?,
                            compressFile: File?,
                        ) {
                            result(true, compressFile?.absolutePath)
                        }

                        override fun onError(
                            source: String?,
                            e: Throwable?,
                        ) {
                            result(false, e?.message)
                        }
                    },
                ).launch()
        }
    }
}
