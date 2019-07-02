var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server, { wsEngine: 'ws' });

var allPlayers = [];
var allSpiders = [];
var allBullets = [];
var timer = 120;
var xVel = 0;
var yVel= 0;
var readyCounter = 0;
var readyStatus = [];
var started = false;
var finished = false;
var globalScore = 0;
server.listen(8081,function()
{
    console.log("Serverul este pornit...");
});
                                    //asta e o conexiune individuala intre client si server
//schimba asta in "connect" in loc de connection ? bug dublu deconectat.
io.on('connection',function(socket)
{
    console.log("player connected");
    var name;
    socket.once('myName', function(data)
    {
        name = data.name;
        console.log("Nume "+ name);
        console.log("ID "+socket.id)
        allPlayers.push(new player(socket.id,0,0,0,0,false,data.name,0));
        socket.broadcast.emit('newPlayerConnected',{id: socket.id});
        socket.emit('getPlayers', allPlayers);
        console.log("I GOT PLAYERS");
        socket.broadcast.emit('newPlayerName',{name : data.name});

    });
    socket.on('givePlayers',function(data)
    {
        socket.emit('getPlayers', allPlayers);
    });

    //allPlayers.push(new player(socket.id,0,0,0,0,false,name));//cand un jucator se conecteaza adauga-l in lista de jucatori

    socket.emit('socketId', {id: socket.id});   //serverul trimite catre socket-ul curent id0ul propriu
    socket.emit('getPlayers', allPlayers);      //cand jucatorul se conecteaza primeste lista cu toti jucatorii deja conectati

    socket.emit('getSpiders', allSpiders);//cand jucatorul se conecteaza primeste lista cu toti paienjenii deja existenti.
    socket.emit('spiders', allSpiders);
    //socket.broadcast.emit('newPlayerConnected',{id: socket.id});  //trimite catre toate celelalte socket-uri conectate de nu  si celui curent.
//    socket.on('hp', function(data)
//    {
//    for(var i=0;i<allPlayers.length;i++) //itereaza printre toti jucatorii
//    {
//        if(allPlayers[i].id == socket.id)
//        {
//            if(allPlayers[i].hp > 100)
//            {
//                allPlayers[i].hp = 100;
//            }
//            allPlayers[i].hp+=10;
//            socket.emit("gainHp",{hp:allPlayers[i].hp,id:socket.id});
//        }
//    }
//    });
    socket.on('scoreUp', function(data)
    {
         for(var i=0;i<allPlayers.length;i++)
         {
            if(allPlayers[i].id == data.id)
            {
                allPlayers[i].score ++;
                globalScore++;
                console.log("SCORE");
                socket.emit('score',{id: data.id, score: allPlayers[i].score});
                if(globalScore == allSpiders.length)
                {
                     var scores = [];
                     for(var j=0;j<allPlayers.length;j++){
                        scores.push(allPlayers[j].score);
                     }
                     var max = Math.max(...scores);
                     winnerIndex = scores.indexOf(max);
                     //console.log("Index Castigator" + winnerIndex + "Nume" + allPlayers[winnerIndex].name);
                     finished = true;
                     io.sockets.emit('gameOver',{gameOver : finished, winner : allPlayers[winnerIndex].name});
                }
            }
         }
    });
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
                //allSpiders[i].xv = xVel;                 //Math.random() * 2 - 1;
                //allSpiders[i].yv =  yVel;                //Math.random() * 2 - 1;
                allSpiders[i].destroyed = data[i].destroyed;
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

//              allBullets.push(new bullet(data[i].x,data[i].y,data[i].xv,data[i].yv));

    socket.on('updateBullets', function(data)                                                       //event primit de la socket
    {
        socket.broadcast.emit('updateBullets',{x:data.x,y:data.y,xv:data.xv,yv:data.yv,playerId:data.playerId});
    });

    socket.on('ready', function(data) //event primit de la socket
        {
            for(var i=0;i<allPlayers.length;i++) //itereaza printre toti jucatorii
            {
                if(allPlayers[i].id == socket.id  && allPlayers[i].ready == false) //daca jucatorul este  socket-ul curenst si exista cel putin 2 jucatori
                {
                    allPlayers[i].ready = true;
                    readyCounter ++;
                    //readyStatus[i] = true;

                }
            }
             console.log("counter" + readyCounter);
             if(allPlayers.length == readyCounter && allPlayers.length>1)
             {
                started = true
                console.log("Started");
                io.sockets.emit('start',{startBool: started});
                readyCounter=0;
                for(var i=0;i<allPlayers.length;i++)
                {
                    if(allPlayers[i].id == socket.id)
                    {
                        allPlayers[i].ready = false;
                    }
                }
             }

        });
        socket.on('disconnect',function()
            {
                socket.broadcast.emit('playerDisconnected',{id: socket.id, nameC: name});
                console.log("player disconnected");
                //itereaza peste toti jucatorii
                for(var i=0;i<allPlayers.length;i++)
                {
                //daca este gasit socekt-ul curent atunci scoate-l din array.
                    if(allPlayers[i].id == socket.id)
                    {
                        if(allPlayers[i].ready == true && readyCounter>0)
                        {
                            readyCounter--;
                        }
                        allPlayers.splice(i,1);
                        console.log(allPlayers);
                    }
                }
                //reseteaza statusul serverului
                if(allPlayers.length == 0 && finished == true)
                {
                //reseteaza starea jocului.
                      allPlayers = [];
                      allSpiders = [];
                      allBullets = [];
                      timer = 120;
                      xVel = 0;
                      yVel= 0;
                      readyCounter = 0;
                      readyStatus = [];
                      started = false;
                      finished = false;
                      globalScore = 0;
                }
            });

    if(started == true)
    {
        socket.emit('started',{startBool: started});
        //return;
    }
});

//in fiecare secunda serverul caculeaza si trimite statusul timer-ului catre clienti.
setInterval(function()
{
    if(!finished){
        if(started){
        timer--;
        io.sockets.emit('sendTimer',{inGameTimer: timer});//SERVERUL trimite catre toti clientii.
        }
        if(timer == 0){
            var scores = [];
            for(var j=0;j<allPlayers.length;j++){
                scores.push(allPlayers[j].score);
            }
            var max = Math.max(...scores);
            winnerIndex = scores.indexOf(max);
            //console.log("Index Castigator" + winnerIndex + "Nume" + allPlayers[winnerIndex].name);
            finished = true;
            if(allPlayers.length>0){
                io.sockets.emit('gameOver',{gameOver : finished, winner : allPlayers[winnerIndex].name});
            }
        }
    }
},1000);
setInterval(function()
{
    if(allSpiders.length>0){
    //for(var i=0; i<allSpiders.length;i++){
        xVel =  Math.random() * 2 - 1;
        yVel =  Math.random() * 2 - 1;
        spiderIndex = Math.floor(Math.random() * allSpiders.length);
        allSpiders[spiderIndex].xv = xVel;
        allSpiders[spiderIndex].yv = yVel;
        //}
    }
},200);

setInterval(function(){
    if(allSpiders.length>0){
    var allEnemyBullets = [];
        for(var i=0; i<allSpiders.length;i++){
            if(allSpiders[i].destroyed == false){
                var distArray = [];
                for(var j=0;j<allPlayers.length;j++){
                    var distance = Math.sqrt( Math.pow(allSpiders[i].x-allPlayers[j].x,2) + Math.pow(allSpiders[i].y-allPlayers[j].y,2));
                   // if(distance<11){
                        distArray.push(distance);
                   //}
                }
                playerIndex = distArray.indexOf(Math.min(...distArray));
                if(allSpiders[playerIndex]!=null){
                    var angle = Math.atan2(allPlayers[playerIndex].y-allSpiders[i].y, allPlayers[playerIndex].x-allSpiders[i].x);
                    bulVelX = Math.cos(angle);
                    bulVelY = Math.sin(angle);
                    allEnemyBullets.push(new enemyBullet(allSpiders[i].x,allSpiders[i].y,bulVelX,bulVelY));
                }
            }
        }
        io.sockets.emit('enemyShoot',allEnemyBullets);

    }
},2000);

setInterval(function()
{
    io.sockets.emit('spidersStop',allSpiders);//SERVERUL trimite catre toti clientii.
},1111);

function player(id,x,y,xv,yv,ready,name,score) //obiectul jucator de pe server.
{
    this.id = id;
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
    this.ready = ready
    this.name = name;
    this.score = score;
}
function spider (id,x,y,xv,yv,spawned,destroyed)
{
    this.id = id;
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
    this.spawned = spawned;
    this.destroyed = destroyed;
}
function bullet(x,y,xv,yv)
{
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
}
function enemyBullet(x,y,xv,yv)
{
    this.x = x;
    this.y = y;
    this.xv = xv;
    this.yv = yv;
}