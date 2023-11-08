package com.android.selector.photo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

    fun openPhotoCamera(activity: FragmentActivity, listener: PhotoCallBackListener) {
        mContext = activity
        openPhotoCamera(PictureSelector.create(activity), listener)
    }

    fun openPhotoCamera(fragment: Fragment, listener: PhotoCallBackListener) {
        if (fragment.context != null) {
            mContext = fragment.context
        }
        openPhotoCamera(PictureSelector.create(fragment), listener)
    }

    private fun openPhotoCamera(pictureSelector: PictureSelector, listener: PhotoCallBackListener) {
        pictureSelector.openCamera(SelectMimeType.ofImage())
            .setCompressEngine(CompressFileEngine { context, source, call ->
                Luban.with(context)
                    .load(source)
                    .ignoreBy(100)
                    .setCompressListener(object : OnNewCompressListener {
                        override fun onStart() {

                        }

                        override fun onSuccess(source: String?, compressFile: File?) {
                            call.onCallback(source, compressFile?.absolutePath)
                        }

                        override fun onError(source: String?, e: Throwable?) {
                            call.onCallback(source, null);
                        }
                    })
                    .launch()
            }) // 压缩
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    if (result != null && result.isNotEmpty()) {
                        mListPhoto.add(0, getPath(result[0]!!))
                        listener.onCallBack(mListPhoto)
                    }
                }

                override fun onCancel() {
                    mListPhoto.clear()
                    listener.onCallBack(mListPhoto)
                }
            })
    }

    @JvmOverloads
    fun openPhotoGallery(activity: FragmentActivity, listener: PhotoCallBackListener, maxSize: Int = 9, spanCount: Int = 4) {
        mContext = activity
        openPhotoGallery(PictureSelector.create(activity), listener, maxSize, spanCount)
    }

    @JvmOverloads
    fun openPhotoGallery(fragment: Fragment, listener: PhotoCallBackListener, maxSize: Int = 9, spanCount: Int = 4) {
        if (fragment.context != null) {
            mContext = fragment.context
        }
        openPhotoGallery(PictureSelector.create(fragment), listener, maxSize, spanCount)
    }

    private fun openPhotoGallery(pictureSelector: PictureSelector, listener: PhotoCallBackListener, maxSize: Int, SpanCount: Int) {
        pictureSelector.openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setCompressEngine(CompressFileEngine { context, source, call ->
                Luban.with(context)
                    .load(source)
                    .ignoreBy(100)
                    .setCompressListener(object : OnNewCompressListener {
                        override fun onStart() {
                        }

                        override fun onSuccess(source: String?, compressFile: File?) {
                            call.onCallback(source, compressFile?.absolutePath)
                        }

                        override fun onError(source: String?, e: Throwable?) {
                            call.onCallback(source, null);
                        }
                    })
                    .launch()
            })
            .setSelectionMode(if (maxSize > 1) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
            .setMaxSelectNum(maxSize) // 图片最大选择数量
            .setImageSpanCount(SpanCount) // 相册列表每行显示个数
            .isPreviewImage(true) // 是否显示预览
            .isDisplayCamera(false) // 是否显示相机入口
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: java.util.ArrayList<LocalMedia?>?) {
                    if (result != null && result.isNotEmpty()) {
                        mListPhoto.clear()
                        result.forEach {
                            val path = getPath(it!!)
                            mListPhoto.add(path)
                        }
                        listener.onCallBack(mListPhoto)
                    }
                }

                override fun onCancel() {
                    mListPhoto.clear()
                    listener.onCallBack(mListPhoto)
                }
            })
    }

    /**
     * @param maxDuration 视频的录制时长
     * @param videoQuality 视频的录制质量，1：高质量 2：底质量
     */
    @JvmOverloads
    fun openVideoCamera(activity: FragmentActivity, listener: PhotoCallBackListener, maxDuration: Int = 60, videoQuality: Int = 1) {
        mContext = activity
        openVideoCamera(PictureSelector.create(activity), listener, maxDuration, videoQuality)
    }

    @JvmOverloads
    fun openVideoCamera(fragment: Fragment, listener: PhotoCallBackListener, maxDuration: Int = 60, videoQuality: Int = 1) {
        if (fragment.context != null) {
            mContext = fragment.context
        }
        openVideoCamera(PictureSelector.create(fragment), listener, maxDuration, videoQuality)
    }

    private fun openVideoCamera(pictureSelector: PictureSelector, listener: PhotoCallBackListener, maxDuration: Int, videoQuality: Int) {
        pictureSelector.openCamera(SelectMimeType.ofVideo())
            .setRecordVideoMaxSecond(maxDuration) //视频录制最大时长
            .setRecordVideoMinSecond(1) // 视频录制最小时长
            .setVideoQuality(videoQuality) //系统相机录制视频质量
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    if (result != null && result.isNotEmpty()) {
                        mListPhoto.add(0, getPath(result[0]!!))
                        listener.onCallBack(mListPhoto)
                    }
                }

                override fun onCancel() {
                    mListPhoto.clear()
                    listener.onCallBack(mListPhoto)
                }
            })
    }

    @JvmOverloads
    fun openVideoGallery(activity: FragmentActivity, listener: PhotoCallBackListener, maxSize: Int = 9, spanCount: Int = 4) {
        openVideoGallery(PictureSelector.create(activity), listener, maxSize, spanCount)
    }

    @JvmOverloads
    fun openVideoGallery(fragment: Fragment, listener: PhotoCallBackListener, maxSize: Int = 9, spanCount: Int = 4) {
        openVideoGallery(PictureSelector.create(fragment), listener, maxSize, spanCount)
    }

    private fun openVideoGallery(pictureSelector: PictureSelector, listener: PhotoCallBackListener, maxSize: Int = 9, spanCount: Int = 4) {
        pictureSelector.openGallery(SelectMimeType.ofVideo())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setVideoPlayerEngine(ExoPlayerEngine())
            .isDisplayCamera(false) //  是否显示相机入口
            .isAutoVideoPlay(false) //预览视频是否自动播放
            .isLoopAutoVideoPlay(false) // 预览视频是否循环播放
            .isUseSystemVideoPlayer(false) // 使用系统播放器
            .setSelectionMode(if (maxSize > 1) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
            .isVideoPauseResumePlay(true) // 视频支持暂停与播放
            .isPreviewVideo(true) // 是否支持预览视频
            .setMaxSelectNum(maxSize) // 视频最大选择数量
            .setImageSpanCount(spanCount) // 相册列表每行显示个数
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: java.util.ArrayList<LocalMedia>?) {
                    if (result != null && result.isNotEmpty()) {
                        mListPhoto.clear()
                        result.forEach {
                            val path = getPath(it)
                            mListPhoto.add(path)
                        }
                        listener.onCallBack(mListPhoto)
                    }
                }

                override fun onCancel() {
                    mListPhoto.clear()
                    listener.onCallBack(mListPhoto)
                }
            })
    }

    private fun isUri(uriString: String): Boolean {
        return try {
            Uri.parse(uriString)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getPath(localMedia: LocalMedia): String {
        var path = ""
        if (localMedia.isCompressed) {
            path = localMedia.compressPath // 压缩路径
        }
        if (TextUtils.isEmpty(path)) {
            path = localMedia.availablePath // 可用路径
        }

        if (TextUtils.isEmpty(path)) {
            path = localMedia.realPath // 真实路径，图片最大，没有压缩的那种
        }

        if (TextUtils.isEmpty(path)) {
            path = localMedia.path // content://media/external/images/media/1000013389
        }

        val uri = isUri(path)
        if (uri) {
            val parse = Uri.parse(path)
            val scheme = parse.scheme
            if (TextUtils.isEmpty(scheme)) {
                parse.path?.let {
                    path = it
                }
            } else if (TextUtils.equals(ContentResolver.SCHEME_FILE, scheme)) {
                // file:// 开头的
                parse.path?.let {
                    path = it
                }
            } else if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
                // 使用选择器的三方类库去获取
                path = PictureFileUtils.getPath(mContext, parse)
            }
        }
        return path
    }
}

