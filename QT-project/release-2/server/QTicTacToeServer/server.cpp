#include "server.h"
#include <QDebug>
#include <QDateTime>

Server::Server(QObject *parent) :
    QTcpServer(parent)
{
    clientContainer = new QList<QTcpSocket*>();
    nickContainer = new QList<QString>();
    connect(this, SIGNAL(newConnection()),
            this, SLOT(connectionReceived()));
}

void Server::connectionReceived(){
    QTcpSocket *clientConnection = this->nextPendingConnection();
    clientContainer->append(clientConnection);

    connect(clientConnection, SIGNAL(readyRead()),
            this,             SLOT(textRead()), Qt::DirectConnection);

    connect(clientConnection, SIGNAL(disconnected()),
            this,             SLOT(disconnected()));
}

void Server::textRead(){
    QTcpSocket *source = dynamic_cast<QTcpSocket*>(sender());
    QByteArray data = source->readAll();

    QByteArray namePrefix = "/Name::";
    QByteArray challengePrefix = "/Challenge::";
    QByteArray challengeAcceptedPrefix = "/ChallengeAccepted::";
    QByteArray settingsPrefix = "/Settings::";
    QByteArray startPrefix = "/Start::";
    QByteArray turnPrefix = "/Turn::";
    QByteArray locationPrefix = "/Location::";

    if (data.startsWith(namePrefix)){
        QString tempNick(data);
        QStringRef name(&tempNick, namePrefix.length(), tempNick.length() - namePrefix.length());
        nickContainer->append(name.toString());
        sendClientList();
    } else if (data.startsWith(challengePrefix)){
        QString tempNick(data);
        QStringRef name(&tempNick, challengePrefix.length(), tempNick.length() - challengePrefix.length());
        sendChallenge(source, name);
    } else if (data.startsWith(challengeAcceptedPrefix)){
        QString tempNick(data);
        QStringRef name(&tempNick, challengeAcceptedPrefix.length(), tempNick.length() - challengeAcceptedPrefix.length());
        sendChallengeAccepted(name);
    } else if (data.startsWith(settingsPrefix)){
        QString tempString(data);
        QStringRef values(&tempString, settingsPrefix.length(), tempString.length() - settingsPrefix.length());
        sendSettingsData(values.toString());
    } else if (data.startsWith(startPrefix)){
        QString tempString(data);
        QStringRef values(&tempString, startPrefix.length(), tempString.length() - startPrefix.length());
        sendStartMessage(values);
    } else if (data.startsWith(turnPrefix)){
        QString tempString(data);
        QStringRef name(&tempString, turnPrefix.length(), tempString.length() - turnPrefix.length());
        sendTurnChangeMessage(name);
    } else if (data.startsWith(locationPrefix)){
        QString tempString(data);
        QStringRef values(&tempString, locationPrefix.length(), tempString.length() - locationPrefix.length());
        sendLocationMessage(values);
    } else {
        QTime time = QTime::currentTime();
        QString timeString = time.toString() + " ";
        for (int i = 0; i < clientContainer->size(); ++i){
            clientContainer->at(i)->write(timeString.toLatin1() + data);
        }
    }
}

void Server::disconnected(){
    QTcpSocket *source = dynamic_cast<QTcpSocket*>(sender());
    QString disconnectedClient;

    for (int i = 0; i < clientContainer->size(); ++i){
        if (clientContainer->at(i) == source){
            disconnectedClient = nickContainer->at(i);
            nickContainer->removeAt(i);
            clientContainer->removeAt(i);
        }
    }

    QString dcPrefix = "/Disconnected::";
    for (int i = 0; i < clientContainer->size(); ++i){
        clientContainer->at(i)->write(dcPrefix.toLatin1() + disconnectedClient.toLatin1());
    }
}

void Server::sendClientList(){
    QByteArray prefix = "/Name::";
    for (int i = 0; i < clientContainer->size(); ++i){
        for (int j = 0; j < nickContainer->size(); ++j){
            clientContainer->at(i)->write(prefix + nickContainer->at(j).toLatin1());
        }
    }
}

void Server::sendChallenge(QTcpSocket *source, QStringRef target){
    QString challenger = "";
    for (int i = 0; i < clientContainer->size(); ++i){
        if (clientContainer->at(i) == source){
            challenger = nickContainer->at(i);
        }
    }
    int index = nickContainer->indexOf(target.toString());
    clientContainer->at(index)->write("/Challenge::" + challenger.toLatin1());
}

void Server::sendChallengeAccepted(QStringRef source){
    for (int i = 0; i < nickContainer->size(); ++i){
        if (nickContainer->at(i) == source){
            clientContainer->at(i)->write("/ChallengeAccepted::" + source.toLatin1());
        }
    }
}

void Server::sendSettingsData(QString text){
    QStringList valueArray = text.split("|");
    QString name = valueArray[0];

    for (int i = 0; i < nickContainer->size(); ++i){
        if (nickContainer->at(i) == name){
            clientContainer->at(i)->write("/Settings::" + text.toLatin1());
        }
    }
}

void Server::sendStartMessage(QStringRef name){
    for (int i = 0; i < nickContainer->size(); ++i){
        if (nickContainer->at(i) == name){
            clientContainer->at(i)->write("/Start::" + name.toLatin1());
        }
    }
}

void Server::sendTurnChangeMessage(QStringRef name){
    for (int i = 0; i < nickContainer->size(); ++i){
        if (nickContainer->at(i) == name){
            clientContainer->at(i)->write("/Turn::" + name.toLatin1());
        }
    }
}

void Server::sendLocationMessage(QStringRef values){
    QStringList valueArray = values.toString().split("|");
    QString name = valueArray[0];

    for (int i = 0; i < nickContainer->size(); ++i){
        if (nickContainer->at(i) == name){
            clientContainer->at(i)->write("/Location::" + values.toString().toLatin1());
        }
    }
}
