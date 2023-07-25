var stompClient = Stomp.client('ws://localhost:8080/channel');
stompClient.reconnect_delay = 100;

function connect(boardNo) {
     stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/board/' + boardNo, function (message) {
            console.log('message from topic: ' , message);
            // message.ack()
        });
    });
}

document.getElementById("btn-send").addEventListener("click",function (evt){
    let name = document.getElementById("input-name").val()
    let message = {
        name:name
    }
    stompClient.send("/app/wshello/b1",message);
    evt.preventDefault()
})