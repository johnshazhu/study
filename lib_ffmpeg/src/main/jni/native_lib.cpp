//
// Created by YXY-JS-XJF on 2020/8/6.
//
#include <jni.h>
#include <android/log.h>
#define XDEBUG "xdebug"
#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO, XDEBUG, FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR, XDEBUG, FORMAT,##__VA_ARGS__);
extern "C"
{
    //封装格式
    #include "libavformat/avformat.h"
    //解码
    #include "libavcodec/avcodec.h"
    //缩放
    #include "libswscale/swscale.h"

    #include "libavutil/imgutils.h"
    #include "libavutil/frame.h"
}

int YUV_2_JPG2(uint8_t* data[8], FILE* out_file);

extern "C"
JNIEXPORT void JNICALL
Java_com_study_doc_third_FFmpegUtil_video_1decode(JNIEnv *env, jobject thiz, jstring input,
                                                  jstring output) {
    jboolean isCopy = false;
    const char *inputStr = env->GetStringUTFChars(input, &isCopy);
    const char *outputStr = env->GetStringUTFChars(output, &isCopy);

    //封装格式上下文
    AVFormatContext *pFormatCtx = avformat_alloc_context();

    LOGI("%s", inputStr)
    LOGI("%s", outputStr)
    //2.打开输入视频文件
    int ret = 0;
    if ((ret = avformat_open_input(&pFormatCtx, inputStr, nullptr, nullptr)) != 0) {
        char *errorBuf = static_cast<char *>(malloc(1024));
        av_strerror(ret, errorBuf, 1024);
        LOGE("%s%s, %d", "打开视频文件失败 : ", errorBuf, ret)
        free(errorBuf);
        return;
    }
    //3.获取视频信息
    if (avformat_find_stream_info(pFormatCtx,nullptr) < 0) {
        LOGE("%s", "获取视频信息失败")
        return;
    }
    //视频解码，需要找到视频对应的AVStream所在pFormatCtx->streams的索引位置
    int video_stream_idx = -1;
    int i = 0;
    LOGI("%s : %d", "number of streams", pFormatCtx->nb_streams)
    while (i < pFormatCtx->nb_streams) {
        //根据类型判断，是否是视频流
        if (pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            if (video_stream_idx < 0) {
                video_stream_idx = i;
            }
            //break;
        }
        LOGI("%d, type is : %d", i, pFormatCtx->streams[i]->codecpar->codec_type)
        ++i;
    }

    //4.获取视频解码器
    AVCodecID codecId = pFormatCtx->streams[video_stream_idx]->codecpar->codec_id;
    AVCodec *pCodec = avcodec_find_decoder(codecId);
    if (pCodec == nullptr) {
        LOGE("%s", "无法解码")
        return;
    }

    //5.打开解码器
    AVCodecContext *pCodeCtx = avcodec_alloc_context3(nullptr);
    if (pCodeCtx == nullptr) {
        LOGI("%s", "context 分配失败")
        return;
    }
    avcodec_parameters_to_context(pCodeCtx, pFormatCtx->streams[video_stream_idx]->codecpar);
    if (avcodec_open2(pCodeCtx, pCodec, nullptr) < 0) {
        LOGE("%s", "解码器无法打开")
        return;
    }

    //编码数据
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));

    //像素数据（解码数据）
    AVFrame *frame = av_frame_alloc();
    AVFrame *yuvFrame = av_frame_alloc();

    //只有指定了AVFrame的像素格式、画面大小才能真正分配内存
    //缓冲区分配内存
    uint8_t *out_buffer = (uint8_t *) av_malloc(av_image_get_buffer_size(AV_PIX_FMT_YUVJ420P,
            pCodeCtx->width, pCodeCtx->height, 1));
    //初始化缓冲区
    av_image_fill_arrays(yuvFrame->data, yuvFrame->linesize, out_buffer,
            AV_PIX_FMT_YUVJ420P, pCodeCtx->width, pCodeCtx->height, 1);

    //输出文件
    FILE* fp_yuv = fopen(outputStr, "wb");
    //用于像素格式转换或者缩放
    struct SwsContext *sws_ctx = sws_getContext(
            pCodeCtx->width, pCodeCtx->height, pCodeCtx->pix_fmt,
            pCodeCtx->width, pCodeCtx->height, AV_PIX_FMT_YUVJ420P,
            SWS_BILINEAR, nullptr, nullptr, nullptr);

    bool saved = false;
    //6.一阵一阵读取压缩的视频数据AVPacket
    // 0 if OK, < 0 on error or end of file
    while (av_read_frame(pFormatCtx, packet) >= 0) {
        //解码AVPacket->AVFrame
        if (packet->stream_index == video_stream_idx) {
            // 0 on success, otherwise negative error code:
            ret = avcodec_send_packet(pCodeCtx, packet);
            if (ret != 0) {
                char *errorBuf = static_cast<char *>(malloc(1024));
                av_strerror(ret, errorBuf, 1024);
                LOGE("%s%s, %d", "avcodec_send_packet 失败 : ", errorBuf, ret)
                free(errorBuf);
                break;
            }

            // 0 success, a frame was returned
            while (avcodec_receive_frame(pCodeCtx, frame) == 0) {
                //frame->yuvFrame (YUV420P)
                //转为指定的YUV420P像素帧
                sws_scale(sws_ctx,
                          frame->data, frame->linesize, 0, frame->height,
                          yuvFrame->data, yuvFrame->linesize);
                //向YUV文件保存解码之后的帧数据
                //AVFrame->YUV
                //一个像素包含一个Y

                if (frame->key_frame > 0) {
                    LOGI("channels : %d, key_frame : %d, format: %d", frame->channels, frame->key_frame, frame->format)
                    int y_size = pCodeCtx->width * pCodeCtx->height;
                    /*fwrite(yuvFrame->data[0], 1, y_size, fp_yuv);
                    fwrite(yuvFrame->data[1], 1, y_size / 4, fp_yuv);
                    fwrite(yuvFrame->data[2], 1, y_size / 4, fp_yuv);*/
                    int value = YUV_2_JPG2(yuvFrame->data, fp_yuv);
                    LOGI("%s : %d", "value is ", value)
                    saved = true;
                    break;
                }
            }

            if (saved) {
                break;
            }
        }
        av_packet_unref(packet);
    }
    fclose(fp_yuv);
    if (sws_ctx != nullptr) {
        sws_freeContext(sws_ctx);
    }

    av_frame_free(&frame);
    av_frame_free(&yuvFrame);

    av_free(packet);

    avcodec_free_context(&pCodeCtx);
    avcodec_close(pCodeCtx);

    avformat_close_input(&pFormatCtx);
    avformat_free_context(pFormatCtx);

    env->ReleaseStringUTFChars(input, inputStr);
    env->ReleaseStringUTFChars(output, outputStr);
    LOGI("%s", "video_1decode 结束")
}

int YUV_2_JPG2(uint8_t* data[8], FILE* out_file) {
    AVFormatContext* pFormatCtx;
    AVStream* pStream;
    AVCodec* pCodec;
    AVFrame* pFrame;
    AVPacket pkt;

    uint8_t* picture_buf;
    int y_size = 0;
    int size = 0;
    int width = 1920;
    int height = 1080;

    int ret = 0;

    pFormatCtx = avformat_alloc_context();   //分配AVFormatCtx
    pStream = avformat_new_stream(pFormatCtx, nullptr);
    if (pStream == nullptr) {
        LOGI("%s", "pStream 分配失败")
        return -1;
    }

    AVCodecContext *pCodeCtx = avcodec_alloc_context3(nullptr);
    if (pCodeCtx == nullptr) {
        LOGI("%s", "context 分配失败")
        return -1;
    }
    avcodec_parameters_to_context(pCodeCtx, pStream->codecpar);
    /*设置相关信息*/
    pCodeCtx->codec_id = AV_CODEC_ID_MJPEG;
    pCodeCtx->codec_type = AVMEDIA_TYPE_VIDEO;
    pCodeCtx->pix_fmt = AV_PIX_FMT_YUVJ420P;
    pCodeCtx->width = width;
    pCodeCtx->height = height;
    pCodeCtx->time_base.num = 1;
    pCodeCtx->time_base.den = 25;

    //av_dump_format(pFormatCtx, 0, out_file, 1);

    LOGI("codec_id is %d", pCodeCtx->codec_id)
    pCodec = avcodec_find_encoder(pCodeCtx->codec_id); //查找编码器
    if (pCodec == nullptr) {
        LOGI("%s", "can not find codec!")
        return -1;
    }
    if (avcodec_open2(pCodeCtx, pCodec, nullptr) < 0) {
        LOGI("%s", "can not open codec!")
        return -1;
    }

    pFrame = av_frame_alloc();
    size = av_image_get_buffer_size(pCodeCtx->pix_fmt, pCodeCtx->width, pCodeCtx->height, 1);//获取图片的大小
    picture_buf = (uint8_t*)av_malloc(size);
    if (picture_buf == nullptr) {
        printf("malloc picture buf error!\n");
        return -1;
    }
    av_image_fill_arrays(pFrame->data, pFrame->linesize, picture_buf, pCodeCtx->pix_fmt, pCodeCtx->width, pCodeCtx->height, 1);


    y_size = pCodeCtx->width * pCodeCtx->height;
    av_new_packet(&pkt, y_size * 2);  //给AVPacket申请空间
    memcpy(picture_buf, data[0], y_size);
    memcpy(picture_buf + y_size, data[1], y_size / 4);
    memcpy(picture_buf + y_size + y_size / 4, data[2], y_size / 4);

    pFrame->width = pCodeCtx->width;
    pFrame->height =  pCodeCtx->height;
    pFrame->format = pCodeCtx->pix_fmt;
    pFrame->data[0] = picture_buf;
    pFrame->data[1] = picture_buf + y_size;
    pFrame->data[2] = picture_buf + y_size * 5 / 4;
    ret = av_dict_set(&pFrame->metadata, "rotate", "90", 0);
    if (ret != 0) {
        LOGE("av_dict_set %d", ret)
    }

    // 编码 avcodec_encode_video2(pCodeCtx, &pkt, pFrame, &got_picture);
    ret = avcodec_send_frame(pCodeCtx, pFrame);
    if (ret != 0) {
        LOGE("avcodec_send_frame %d", ret)
        return -1;
    }

    ret = avcodec_receive_packet(pCodeCtx, &pkt);
    if (ret == 0) {
        fwrite(pkt.buf->data, 1, pkt.size, out_file);
    } else {
        char *errorBuf = static_cast<char *>(malloc(1024));
        av_strerror(ret, errorBuf, 1024);
        LOGE("%s%s, %d", "avcodec_send_packet 失败 : ", errorBuf, ret)
        free(errorBuf);
    }

    av_packet_unref(&pkt);
    av_free(picture_buf);

    av_frame_free(&pFrame);
    av_free(pFrame);

    avcodec_free_context(&pCodeCtx);
    avcodec_close(pCodeCtx);
    avformat_free_context(pFormatCtx);

    LOGI("%s", "save end")
    return 0;
}