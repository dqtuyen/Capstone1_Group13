package APIChatGPT;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageDataAdapterTest extends RecyclerView.Adapter {

    // ... (Các biến khác)

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String ACCESS_TOKEN = "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"; // Thay bằng access token của bạn từ OpenAI

    // ... (Các phương thức khác)

    public void sendMessageToOpenAI(String messageText) {

        try {
            // Tạo JSON payload theo định dạng yêu cầu của OpenAI
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");

            JSONArray messagesArray = new JSONArray();
            JSONObject messageObject = new JSONObject();
            messageObject.put("role", "user");
            messageObject.put("content", messageText);
            messagesArray.put(messageObject);

            jsonBody.put("messages", messagesArray);
            jsonBody.put("temperature", 0);

            // Log chuỗi JSON
            String jsonString = jsonBody.toString();
            Log.d("LOG", jsonString);

            HttpUrl URl = HttpUrl.parse("https://api.openai.com/v1/chat/completions")
                    .newBuilder()
                    .build();
            RequestBody body = RequestBody.create(String.valueOf(jsonBody.toString()), MediaType.parse("application/json; charset=utf-8"));
            System.out.println("body: " + jsonBody.toString());

            Request request = new Request.Builder()
                    .url(URl)
                    .post(body)
                    .addHeader("Authorization", "Bearer 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")
                    .addHeader("Content-Type","application/json")
                    .build();


            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    int statusCode = response.code();
                    JSONObject data = null;
                    if(statusCode == 200)
                    {
                        try {
                            data = new JSONObject(response.body().string());
                        } catch (JSONException e) {
                            Log.d("LOG",e.getMessage());
                        }
                    }else {
                        Log.d("LOG", response.code() + response.message());
                    }
                }
            });
        } catch (JSONException e) {
            Log.d("LOG",e.getMessage());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}