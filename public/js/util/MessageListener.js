function MessageListener(gameCode, serverCommunication, handler){
    var timeMarker = (new Date).getTime();

    function getMessages(){
        serverCommunication.getMessagesByTime(gameCode, timeMarker, function(response){
            response.data.forEach(function(msg){
                if (msg.time > timeMarker) timeMarker = msg.time + 1
            });
            response.data.forEach(handler.handleMessage)
        }, handler.handleMessageError);
    }

    return {
        getMessages: getMessages
    }
}