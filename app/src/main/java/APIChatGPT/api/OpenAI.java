package APIChatGPT.api;

import android.os.Build;

import com.example.capstone1.R;

import io.grpc.android.BuildConfig;

public class OpenAI {
    public static String API="https://api.openai.com/v1/engines/davinci/completions";
    public static String TOKEN= "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";
    public static String METHOD="POST";
    public static String CONTENT_TYPE="application/json";
}
