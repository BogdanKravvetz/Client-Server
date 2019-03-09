var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var allPlayers = [];
var allSpiders = [];
var allBullets = [];
var timer = 0;
var xVel = 0;
var yVel= 0;

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
    socket.emit('getSpiders', allSpiders);//cand jucatorul se conecteaza primeste lista cu toti paienjenii deja existenti.
    socket.emit('spiders', allSpiders);
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
    socket.on('spidersMove', function(data) //event primit de la socket
    {
        if(allSpiders.length==0)
        {
            for(var i = 0;i<data.length;i++)
            {
                 allSpiders.push(new spider(data[i].id,data[i].x,data[i].y,data[i].xv,data[i].yv,data[i].spawned,data[i].destroyed));
            }
        }
            for(var i = 0;i<data.length;i++)
            {
                allSpiders[i].id = data[i].id;
                allSpiders[i].x = data[i].x;
                allSpiders[i].y = data[i].y;
                allSpiders[i].xv = Math.random() * 2 - 1;
                allSpiders[i].yv = Math.random() * 2 - 1;
                allSpiders[i].spawned = data[i].spawned;
            }
            io.sockets.emit('spidersMove',allSpiders);//SERVERUL trimite catre toti clientii.
    });
//    socket.on('spidersDestroyed',function(data)
//    {
//        for(var i = 0;i<data.length;i++)
//        {
//            allSpiders[i].destroyed = data[i].destroyed;
//        }
//        io.sockets.emit('spidersDestroyed',allSpiders);
//    });
    socket.on('updateBullets', function(data) //event primit de la socket
            {
//              allBullets.push(new bullet(data[i].x,data[i].y,data[i].xv,data[i].yv));
                socket.broadcast.emit('updateBullets',{x:data.x,y:data.y,xv:data.xv,yv:data.yv});
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
setInterval(function()
{
    xVel =  Math.random() * 2 - 1;
    yVel =  Math.random() * 2 - 1;

},4000);

setInterval(function()
{
    io.sockets.emit('spidersStop',allSpiders);//SERVERUL trimite catre toti clientii.
},111);

function player(id,x,y,xv,yv) //obiectul jucator de pe server.
{
    this.id = id;
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
}
function spider (id,x,y,xv,yv,spawned,destroyed)
{
    this.id = id;
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
    this.spawned = spawned;
}
function bullet(x,y,xv,yv)
{
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
}