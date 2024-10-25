<template>
  <div class="live-class">
    <div class="video-conference">
      <video id="localVideo" autoplay muted></video>
      <video id="remoteVideo" autoplay></video>
    </div>
    <div class="chat">
      <div class="messages">
        <div v-for="message in messages" :key="message.id" class="message">
          <span class="sender">{{ message.sender }}:</span>
          <span class="text">{{ message.text }}</span>
        </div>
      </div>
      <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message...">
    </div>
    <div class="file-sharing">
      <input type="file" @change="shareFile">
      <div class="shared-files">
        <div v-for="file in sharedFiles" :key="file.id" class="file">
          <a :href="file.url" target="_blank">{{ file.name }}</a>
        </div>
      </div>
    </div>
    <div class="screen-sharing">
      <button @click="startScreenSharing">Share Screen</button>
    </div>
    <div class="notifications">
      <div v-for="notification in notifications" :key="notification.id" class="notification">
        <span class="title">{{ notification.title }}</span>
        <span class="body">{{ notification.body }}</span>
      </div>
    </div>
    <div class="recording">
      <button @click="startRecording">Start Recording</button>
      <button @click="stopRecording">Stop Recording</button>
      <button @click="uploadRecording">Upload Recording</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { getMessaging, onMessage } from 'firebase/messaging';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import adapter from 'webrtc-adapter';

export default {
  name: 'LiveClass',
  setup() {
    const localVideo = ref(null);
    const remoteVideo = ref(null);
    const messages = ref([]);
    const newMessage = ref('');
    const sharedFiles = ref([]);
    const notifications = ref([]);
    const mediaRecorder = ref(null);
    const recordedChunks = ref([]);

    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/topic/messages', (message) => {
        messages.value.push(JSON.parse(message.body));
      });
    });

    const messaging = getMessaging();
    onMessage(messaging, (payload) => {
      notifications.value.push(payload.notification);
    });

    const startScreenSharing = async () => {
      try {
        const stream = await navigator.mediaDevices.getDisplayMedia({ video: true });
        localVideo.value.srcObject = stream;
        // Send the stream to the remote peer
      } catch (err) {
        console.error('Error sharing screen:', err);
      }
    };

    const startRecording = () => {
      const stream = localVideo.value.srcObject;
      mediaRecorder.value = new MediaRecorder(stream);
      mediaRecorder.value.ondataavailable = (event) => {
        if (event.data.size > 0) {
          recordedChunks.value.push(event.data);
        }
      };
      mediaRecorder.value.start();
    };

    const stopRecording = () => {
      mediaRecorder.value.stop();
    };

    const uploadRecording = async () => {
      const blob = new Blob(recordedChunks.value, { type: 'video/webm' });
      const formData = new FormData();
      formData.append('file', blob, 'recording.webm');
      await fetch('/api/live-class/upload', {
        method: 'POST',
        body: formData,
      });
    };

    const sendMessage = () => {
      if (newMessage.value.trim() !== '') {
        stompClient.send('/app/chat', {}, JSON.stringify({ sender: 'User', text: newMessage.value }));
        newMessage.value = '';
      }
    };

    const shareFile = (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        sharedFiles.value.push({ id: Date.now(), name: file.name, url: e.target.result });
      };
      reader.readAsDataURL(file);
    };

    onMounted(() => {
      const localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
      localVideo.value.srcObject = localStream;
      // Initialize WebRTC connection and set remoteVideo.srcObject to the remote stream
    });

    return {
      localVideo,
      remoteVideo,
      messages,
      newMessage,
      sharedFiles,
      notifications,
      startScreenSharing,
      startRecording,
      stopRecording,
      uploadRecording,
      sendMessage,
      shareFile,
    };
  },
};
</script>

<style scoped>
.live-class {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.video-conference {
  display: flex;
  gap: 1rem;
}

.chat {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.file-sharing {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.screen-sharing {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.notifications {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.recording {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
</style>
