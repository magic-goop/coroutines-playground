package com.android.coroutines_playground.presentation.riddle.imageviewer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.coroutines_playground.GlideApp
import com.android.coroutines_playground.R
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.android.coroutines_playground.domain.model.entity.buildImgUrl
import com.android.coroutines_playground.utils.isVisible
import com.android.coroutines_playground.utils.lazyUi
import kotlinx.android.synthetic.main.dialog_fragment_image_viewer.*

class ImageViewerDialog : DialogFragment() {

    companion object {
        @JvmField
        val TAG: String = ImageViewerDialog::class.java.name

        private val EXTRA_IMAGES: String = ImageViewerDialog::class.java.name + "extra.IMAGES"
        private val EXTRA_POSITION: String = ImageViewerDialog::class.java.name + "extra.POSITION"

        fun createDialog(
            position: Int = 0,
            images: List<String>? = null,
            listener: ((page: Int) -> Unit)? = null
        ) =
            ImageViewerDialog().apply {
                onPageChangeListener = listener
                arguments = Bundle().apply {
                    images?.let {
                        putStringArrayList(EXTRA_IMAGES, ArrayList<String>(images))
                    }
                    putInt(EXTRA_POSITION, position)
                }
            }
    }

    private val adapter by lazyUi { ImageAdapter() }

    private var onPageChangeListener: ((page: Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ImageViewerDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_image_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViewerPager.offscreenPageLimit = 3
        imageViewerPager.adapter = adapter

        arguments?.apply {
            adapter.setData(getStringArrayList(EXTRA_IMAGES))
            imageViewerPager.currentItem = getInt(EXTRA_POSITION)
            imageViewerIndicator.isVisible(adapter.count > 1)
            imageViewerIndicator.setDotCount(adapter.count)
            imageViewerIndicator.attachToPager(imageViewerPager)
        }
        imageViewerPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                onPageChangeListener?.invoke(position)
            }
        })
        btnClose.setOnClickListener { dismiss() }
    }

    class ImageAdapter : PagerAdapter() {

        private var images: List<String> = emptyList()

        @SuppressLint("ClickableViewAccessibility")
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val img = AppCompatImageView(container.context)

            img.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            val imageMatrixTouchHandler = ImageMatrixTouchHandler(container.context)
            img.setOnTouchListener(imageMatrixTouchHandler)

            GlideApp
                .with(container.context).load(Uri.parse(images[position].buildImgUrl()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.mipmap.ic_launcher)
                .into(img)
            container.addView(img)
            return img
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
//            super.destroyItem(container, position, obj)
            if (obj is AppCompatImageView) {
                obj.setImageResource(0)
            }
            container.removeView(obj as View?)
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun getCount(): Int = images.size

        override fun getItemPosition(obj: Any): Int = POSITION_NONE

        fun setData(data: List<String>) {
            images = data
            notifyDataSetChanged()
        }
    }
}