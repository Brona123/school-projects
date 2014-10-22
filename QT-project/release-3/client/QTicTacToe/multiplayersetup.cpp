#include "multiplayersetup.h"
#include "ui_multiplayersetup.h"
#include <QMessageBox>
#include <QNetworkAccessManager>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonValue>
#include <QJsonArray>
#include "gamefield.h"
#include "lobby.h"

MultiplayerSetup::MultiplayerSetup(QWidget *parent) :
    QWidget(parent),
    networkSession(0),
    ui(new Ui::MultiplayerSetup)
{
    ui->setupUi(this);
    this->host = dynamic_cast<Home*>(parent);
    tcpSocket = new QTcpSocket(this);

    connect(tcpSocket, SIGNAL(error(QAbstractSocket::SocketError)),
            this,      SLOT(displayError(QAbstractSocket::SocketError)));
    connect(tcpSocket, SIGNAL(connected()),
            this,      SLOT(clientConnected()));
}

MultiplayerSetup::~MultiplayerSetup()
{
    delete ui;
}

void MultiplayerSetup::on_pushButton_clicked()
{
    if (ui->nickLineEdit->text() == ""){
        QMessageBox msgBox;
        msgBox.setText(tr("Please enter a nickname"));
        msgBox.exec();
    } else {
        tcpSocket->abort();
        tcpSocket->connectToHost(ui->IPLineEdit->text(),
                                 ui->portLineEdit->text().toInt());
    }
}

void MultiplayerSetup::clientConnected(){
    Lobby *temp = dynamic_cast<Lobby*>(host->getWidget(Home::LOBBY_WINDOW));

    temp->setSocket(tcpSocket);
    temp->setPlayer(ui->nickLineEdit->text());
    QString nameInfo = "/Name::" + ui->nickLineEdit->text();
    QByteArray block(nameInfo.toLatin1());
    tcpSocket->write(block);
    host->setWidget(Home::LOBBY_WINDOW);
}

void MultiplayerSetup::displayError(QAbstractSocket::SocketError socketError)
{
    switch (socketError) {
    case QAbstractSocket::RemoteHostClosedError:
        break;
    case QAbstractSocket::HostNotFoundError:
        QMessageBox::information(this, tr("QTicTacToe Client"),
                                 tr("The host was not found. Please check the "
                                    "host name and port settings."));
        break;
    case QAbstractSocket::ConnectionRefusedError:
        QMessageBox::information(this, tr("QTicTacToe Client"),
                                 tr("The connection was refused by the peer. "
                                    "Make sure the server is running, "
                                    "and check that the host name and port "
                                    "settings are correct."));
        break;
    default:
        QMessageBox::information(this, tr("QTicTacToe Client"),
                                 tr("The following error occurred: %1.")
                                 .arg(tcpSocket->errorString()));
    }
}

void MultiplayerSetup::showEvent(QShowEvent*){
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QObject::connect(manager, SIGNAL(finished(QNetworkReply*)),
                     this, SLOT(requestFinished(QNetworkReply*)));

    QString request = QString("http://koti.tamk.fi/~c2sjouts/QT/QTicTacToeServer/getLocation.php");
    QUrl url(request);
    manager->get(QNetworkRequest(url));
}

void MultiplayerSetup::requestFinished(QNetworkReply *reply){
    QString response(reply->readAll());
    QJsonDocument d = QJsonDocument::fromJson(response.toUtf8());
    QJsonArray arr = d.array();

    for (const QJsonValue value : arr){
        QJsonObject obj = value.toObject();
        ui->IPLineEdit->setText(obj["ip"].toString());
        ui->portLineEdit->setText(obj["port"].toString());
    }
}
