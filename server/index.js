var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var allPlayers = [];
var timer = 0;

server.listen(8081,function()
{
    console.log("Server is up..");
});
//asta e o conexiune individuala intre client si server (socket?)
io.on('connection',function(socket)
{
    console.log("player connected");
    socket.emit('socketId', {id: socket.id}); //serverul trimite catre socket-ul curent id0ul propriu
    socket.emit('getPlayers', allPlayers); //cand jucatorul se conecteaza primeste lista cu toti jucatorii deja conectati
    socket.broadcast.emit('newPlayerConnected',{id: socket.id});  //trimite catre toate celelalte socket-uri conectate de nu  si celui curent.
    socket.on('playerMoved', function(data) //event primit de la socket
    {
        data.id = socket.id; //initializaeza id-ul pentru data cu id-ul socket-ului curent
        socket.broadcast.emit('playerMoved',data); //trimite catre toata lumea faptul ca socket-ul s-a miscat
        for(var i=0;i<allPlayers.length;i++) //itereaza printre toti jucatorii
        {
            if(allPlayers[i].id == data.id) //daca jucatorul este  socket-ul curent
            {
            //updateaza coordonatele lui pe server.
                allPlayers[i].x = data.x;
                allPlayers[i].y = data.y;
                allPlayers[i].xv = data.xv;
                allPlayers[i].yv = data.yv;
            }
        }
    });
    socket.on('disconnect',function()
    {
        socket.broadcast.emit('playerDisconnected',{id: socket.id});
        console.log("player disconnected");
        //itereaza peste toti jucatorii
        for(var i=0;i<allPlayers.length;i++)
        {
        //daca este gasit socekt-ul curent atunci scoate-l din array.
            if(allPlayers[i].id == socket.id)
            {
                allPlayers.splice(i,1);
            }
        }
    });
    allPlayers.push(new player(socket.id,0,0,0,0));//cand un jucator se conecteaza adauga-l in lista de jucatori
});
//in fiecare secunda serverul caculeaza si trimite statusul timer-ului catre clienti.
setInterval(function()
{
    timer++;
    io.sockets.emit('sendTimer',{inGameTimer: timer});//SERVERUL trimite catre toti clientii.
},1000);

function player(id,x,y,xv,yv) //obiectul jucator de pe server.
{
    this.id = id;
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
}