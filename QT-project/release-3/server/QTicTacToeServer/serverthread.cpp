#include "serverthread.h"
#include <QDebug>
#include "server.h"

ServerThread::ServerThread(int socketDescriptor, QObject *parent) :
    QThread(parent), socketDescriptor(socketDescriptor)
{
    qDebug() << "ServerThread created";
    this->parent = parent;
}

void ServerThread::run(){
    /*
    tcpSocket = new QTcpSocket();
    connect(tcpSocket, SIGNAL(readyRead()),
            this,      SLOT(textRead()), Qt::DirectConnection);
    connect(tcpSocket, SIGNAL(disconnected()), this, SLOT(disconnected()));

    Server *host = dynamic_cast<Server*>(parent);
    connect(host,   SIGNAL(textReceived(QByteArray)),
            this,   SLOT(sendBytes(QByteArray)));

    if (!tcpSocket->setSocketDescriptor(socketDescriptor)){
        emit error(tcpSocket->error());
        return;
    }

    this->clientThread = this->currentThread();
    exec();*/
}

void ServerThread::textRead(){

}

void ServerThread::sendText(QByteArray text){
    qDebug() << "SENDTEXT";

}

void ServerThread::disconnected(){
    /*
    tcpSocket->deleteLater();
    qDebug() << socketDescriptor << " Disconnected";
    exit(0);
    */
}
