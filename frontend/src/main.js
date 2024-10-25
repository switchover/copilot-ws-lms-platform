import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import { initializeApp } from 'firebase/app';
import { getMessaging, onMessage } from 'firebase/messaging';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import adapter from 'webrtc-adapter';

const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID",
  measurementId: "YOUR_MEASUREMENT_ID"
};

initializeApp(firebaseConfig);

const messaging = getMessaging();
onMessage(messaging, (payload) => {
  console.log('Message received. ', payload);
  // Handle the received message
});

const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, (frame) => {
  console.log('Connected: ' + frame);
  stompClient.subscribe('/topic/messages', (message) => {
    console.log('Received: ' + message.body);
    // Handle the received message
  });
});

const app = createApp(App);
app.use(router);
app.use(store);
app.mount('#app');
