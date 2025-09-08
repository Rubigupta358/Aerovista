<!-- chatbot.jsp -->
<!-- This file intentionally has NO directive so it can be included safely -->

<!-- Floating chat button -->
<button id="chatButton" class="floating-chat" aria-label="Open chat">💬</button>

<!-- Modal chat window -->
<div id="chatModal" class="chat-modal">
  <div class="chat-card">
    <div class="chat-header">
      <span>AI Chatbot</span>
      <button id="closeChat" class="close-btn">&times;</button>
    </div>
    <div id="chatArea" class="chat-area"></div>
    <form id="chatForm" class="chat-form" onsubmit="return false;">
      <input id="msgInput" name="message" placeholder="Type a message..." autocomplete="off" />
      <button id="sendBtn" type="button">Send</button>
    </form>
  </div>
</div>

<style>
/* Floating button */
.floating-chat {
  position: fixed;
  right: 24px;
  bottom: 80px;
  width: 64px;
  height: 64px;
  border-radius: 50%;
  border: none;
  box-shadow: 0 6px 18px rgba(0,0,0,0.25);
  background: #007bff;
  color: #fff;
  font-size: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 9999;
}
.floating-chat:hover { transform: translateY(-4px); }

/* Modal container */
.chat-modal {
  position: fixed;
  right: 24px;
  bottom: 160px;
  width: 340px;
  max-height: 520px;
  z-index: 9999;
  display: none;
  font-family: Arial, sans-serif;
}
.chat-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.12);
  overflow: hidden;
  display:flex;
  flex-direction: column;
  height: 100%;
}
.chat-header {
  padding: 12px;
  background: linear-gradient(90deg,#0d6efd,#0b5ed7);
  color: #fff;
  display:flex;
  justify-content:space-between;
  align-items:center;
}
.close-btn {
  background: transparent;
  border: none;
  color: #fff;
  font-size: 18px;
  cursor: pointer;
}
.chat-area {
  padding: 12px;
  height: 360px;
  overflow-y: auto;
  background: #f8f9fa;
}
.chat-area .msg { margin: 8px 0; }
.chat-area .msg.user { text-align: right; }
.chat-area .bubble {
  display: inline-block;
  padding: 8px 12px;
  border-radius: 10px;
  max-width: 84%;
}
.chat-area .bubble.bot { background: #fff; border: 1px solid #e9ecef; }
.chat-area .bubble.user { background: #0d6efd; color: #fff; }

.chat-form {
  display:flex;
  gap:8px;
  padding: 12px;
  border-top: 1px solid #e9ecef;
}
#msgInput { flex:1; padding:8px 10px; border-radius:8px; border:1px solid #ced4da; }
#sendBtn { padding:8px 12px; border-radius:8px; border:none; background:#0d6efd; color:#fff; cursor:pointer; }
</style>

<script>
(function(){
  const chatBtn = document.getElementById('chatButton');
  const modal = document.getElementById('chatModal');
  const closeBtn = document.getElementById('closeChat');
  const sendBtn = document.getElementById('sendBtn');
  const msgInput = document.getElementById('msgInput');
  const chatArea = document.getElementById('chatArea');

  function openChat() {
    modal.style.display = 'block';
    msgInput.focus();
  }
  function closeChat() {
    modal.style.display = 'none';
  }

  chatBtn.addEventListener('click', openChat);
  closeBtn.addEventListener('click', closeChat);

  function appendMessage(who, text, html=false) {
    const div = document.createElement('div');
    div.className = 'msg ' + (who === 'You' ? 'user' : 'bot');
    const bubble = document.createElement('div');
    bubble.className = 'bubble ' + (who === 'You' ? 'user' : 'bot');
    if (html) bubble.innerHTML = text;
    else bubble.textContent = text;
    div.appendChild(bubble);
    chatArea.appendChild(div);
    chatArea.scrollTop = chatArea.scrollHeight;
  }

  function sendMessage() {
    const text = msgInput.value.trim();
    if (!text) return;
    appendMessage('You', text);
    msgInput.value = '';
    // Send to servlet
    fetch('AIChatServlet', {
      method: 'POST',
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      body: 'message=' + encodeURIComponent(text)
    })
    .then(r => r.json())
    .then(j => {
      appendMessage('Bot', j.reply || 'No reply', true);
    })
    .catch(err => {
      appendMessage('Bot', '⚠️ Error: ' + err.message);
      console.error(err);
    });
  }

  sendBtn.addEventListener('click', sendMessage);
  msgInput.addEventListener('keydown', (e) => { if (e.key === 'Enter') sendMessage(); });

  // initial greeting
  appendMessage('Bot', "Hi! I can help with: 'add product' or 'view product'. For add, give name/category/price/launch_date or I'll ask the missing fields.");
})();
</script>
