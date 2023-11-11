package com.example.capstone1.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.Activity.MainActivity;
import com.example.capstone1.R;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import APIChatGPT.adapter.MessageDataAdapter;
import APIChatGPT.api.OpenAI;
import APIChatGPT.model.MessageData;
import APIChatGPT.model.OpenAIInput;
import APIChatGPT.model.OpenAIOutput;
import APIChatGPT.model.TypeTalking;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatBotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatBotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView rvMessageData;
    EditText edtChatBox;
    TextView txtOpenAIStatus;
    ImageView imgChatBoxSend;

    TextView txt_q1, txt_q2, txt_hello;
    ImageView img_avtchat;

    private MessageDataAdapter messageDataAdapter;
    private List<MessageData> messageList;


    public ChatBotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatBotFragment newInstance(String param1, String param2) {
        ChatBotFragment fragment = new ChatBotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);
        addControls(view);
        addEvents();
        return view;
    }
    private void addControls(View view) {
        edtChatBox = view.findViewById(R.id.edtChatBox);
        rvMessageData = view.findViewById(R.id.rvMessageData);
        txtOpenAIStatus = view.findViewById(R.id.txtOpenAIStatus);
        imgChatBoxSend = view.findViewById(R.id.imgChatBoxSend);
        txt_q1 = view.findViewById(R.id.txt_q1);
        txt_q2 = view.findViewById(R.id.txt_q2);
        txt_hello = view.findViewById(R.id.txt_hello);
        img_avtchat = view.findViewById(R.id.img_avtchat);

        messageList = new ArrayList<>();

        messageDataAdapter = new MessageDataAdapter(getActivity(), messageList);
        rvMessageData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMessageData.setAdapter(messageDataAdapter);
    }

    private void addEvents() {


        imgChatBoxSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSendMessage();
            }
        });
    }

    private void invisible() {
        txt_q1.setVisibility(View.INVISIBLE);
        txt_q2.setVisibility(View.INVISIBLE);
        txt_hello.setVisibility(View.INVISIBLE);
        img_avtchat.setVisibility(View.INVISIBLE);
    }
    private void processSendMessage() {
        String message = edtChatBox.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(getActivity(), "Nhập nội dung.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            invisible();
            sendMessageByMe(message);
        }


    }

    private void sendMessageByMe(String msg) {
        //Khi người dùng nhấn nút send
        //ta khởi tạo MessageData
        //và gán TypeTalking.HUMAN
        MessageData userMessage = new MessageData();
        userMessage.setUserName("Trần Duy Thanh");
        userMessage.setTypeTalking(TypeTalking.HUMAN);

        userMessage.setCreated(System.currentTimeMillis());
        userMessage.setText(msg);

        messageList.add(userMessage);
        //Cập nhật lại giao diện
        refreshMessageList();
        edtChatBox.setText("");
        //gửi thông tin này cho OpenAI
        getMessageByOpenAI(msg);
    }

    private void getMessageByOpenAI(String msg) {
        txtOpenAIStatus.setVisibility(View.VISIBLE);
        //dùng AsyncTask để tạo tiểu trình
        AsyncTask<OpenAIInput,Void, OpenAIOutput> task=new AsyncTask<OpenAIInput, Void, OpenAIOutput>() {
            @Override
            protected OpenAIOutput doInBackground(OpenAIInput... openAIInputs) {
                try {
                    //gọi HttpURLConnection
                    //và coding như bên dưới đây:
                    URL url = new URL(OpenAI.API);
                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization","Bearer "+OpenAI.TOKEN);
                    conn.setRequestProperty("Content-Type",OpenAI.CONTENT_TYPE);
                    conn.setRequestMethod(OpenAI.METHOD);
                    conn.setDoOutput(true);
                    OpenAIInput aiInput=openAIInputs[0];
                    String myData=new Gson().toJson(aiInput);
                    conn.getOutputStream().write(myData.getBytes());
                    InputStream responseBody = conn.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                    //lấy dữ liệu trả về là OpenAIOutput
                    OpenAIOutput data= new Gson().fromJson(responseBodyReader, OpenAIOutput.class);
                    return data;
                }
                catch (Exception ex)
                {
                    Log.e(TAG,ex.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(OpenAIOutput openAIOutput) {
                super.onPostExecute(openAIOutput);
                if(openAIOutput!=null)
                {
                    if(openAIOutput.getChoices()!=null)
                    {
                        //nếu có dữ liệu, ta lấy phần tử đầu tiên trong mảng choices
                        MessageData openAIMessage=openAIOutput.getChoices().get(0);
                        //cập nhật lại tên
                        openAIMessage.setUserName("OpenAI");
                        //cập nhật lại loại OpenAI để show box cho đúng
                        openAIMessage.setTypeTalking(TypeTalking.OPENAI);
                        //cập nhật thời gian để show
                        openAIMessage.setCreated(openAIOutput.getCreated());
                        messageList.add(openAIMessage);
                        //cập nhật giao diện
                        refreshMessageList();

                        txtOpenAIStatus.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
        //Kích hoạt tiểu trình:
        OpenAIInput input=new OpenAIInput(msg);
        task.execute(input);
    }
    private void refreshMessageList() {
        messageDataAdapter.notifyDataSetChanged();

        rvMessageData.scrollToPosition(messageList.size() - 1);
    }
}