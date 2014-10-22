#include "lobby.h"
#include "ui_lobby.h"
#include <QDebug>
#include <QMessageBox>
#include "multiplayersettings.h"

Lobby::Lobby(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Lobby)
{
    this->host = dynamic_cast<Home*>(parent);
    ui->setupUi(this);
    tcpSocket = NULL;
}

Lobby::~Lobby()
{
    delete ui;
}

void Lobby::setSocket(QTcpSocket *socket){
    this->tcpSocket = socket;
}

void Lobby::setPlayer(QString player){
    this->player = player;
}

void Lobby::on_lineEdit_returnPressed()
{
    QString line = "<" + this->player + "> " + ui->lineEdit->text();
    ui->lineEdit->clear();

    if (!line.startsWith(("/"))){
        tcpSocket->write(line.toLatin1());
    }
}

void Lobby::textRead(){
    QString loginPrefix = "/Name::";
    QString dcPrefix = "/Disconnected::";
    QString challengePrefix = "/Challenge::";
    QString challengeAcceptedPrefix = "/ChallengeAccepted::";
    QString text = tcpSocket->readAll();

    if (text.startsWith(loginPrefix)){
        setLobbyPlayerList(text, loginPrefix);
    } else if (text.startsWith(dcPrefix)){
        announceDisconnection(text, dcPrefix);
    } else if (text.startsWith(challengePrefix)){
        challengeReceived(text, challengePrefix);
    } else if (text.startsWith(challengeAcceptedPrefix)){
        QStringRef name(&text, challengeAcceptedPrefix.length(), text.length() - challengeAcceptedPrefix.length());
        if (name == this->player){
            createSettingsMenu(challengedPlayer, MultiplayerSettings::PLAYER_ONE);
        }
    } else {
        ui->textEdit->append(text);
    }
}

void Lobby::setLobbyPlayerList(QString list, QString delim){
    QStringList names = list.split(delim, QString::SkipEmptyParts);
    ui->playerList->clear();
    for (int i = 0; i < names.length(); ++i){
        ui->playerList->addItem(names.at(i));

        if (names.at(i) == this->player){
            ui->playerList->item(i)->setForeground(QColor(0, 0, 220));
        }
    }
}

void Lobby::announceDisconnection(QString text, QString prefix){
    QStringRef name(&text, prefix.length(), text.length() - prefix.length());
    for (int i = 0; i < ui->playerList->count(); ++i){
        if (ui->playerList->item(i)->text() == name){
            delete ui->playerList->item(i);
            ui->textEdit->append(name.toString() + " has disconnected");
        }
    }
}

void Lobby::on_challengeButton_clicked()
{
    if (ui->playerList->currentItem() != NULL){
        this->challengedPlayer = ui->playerList->selectedItems().at(0)->text();
        if (challengedPlayer != NULL){
            QString challengePrefix = "/Challenge::";
            if (challengedPlayer != ""){
                tcpSocket->write(challengePrefix.toLatin1() + challengedPlayer.toLatin1());
            }
        }
    }
}

void Lobby::challengeReceived(QString text, QString prefix){
    QStringRef name(&text, prefix.length(), text.length() - prefix.length());

    QWidget *temp = this->parentWidget()->parentWidget();
    QMessageBox msgBox;
    int x = ((temp->width() / 2) + temp->x()) - (msgBox.widthMM() / 2);
    int y = ((temp->height() / 2) + temp->y()) - (msgBox.heightMM() / 2);

    msgBox.move(QPoint(x, y));
    msgBox.setWindowTitle("Challenge");
    msgBox.setText("Player " + name.toString() + " has challenged you!"
                   + "\nAccept?");
    msgBox.setStandardButtons(QMessageBox::Yes|QMessageBox::No);
    if (msgBox.exec() == QMessageBox::Yes){
        tcpSocket->write("/ChallengeAccepted::" + name.toLatin1());
        createSettingsMenu(name.toString(), MultiplayerSettings::PLAYER_TWO);
    }
}

void Lobby::createSettingsMenu(QString name, int ID){
    MultiplayerSettings *tempSettings = dynamic_cast<MultiplayerSettings*>(host->getWidget(Home::MP_SETTINGS_WINDOW));
    tempSettings->setPlayers(this->player, name);
    tempSettings->setSocket(tcpSocket);
    tempSettings->setID(ID);
    host->setWidget(Home::MP_SETTINGS_WINDOW);
}

void Lobby::showEvent(QShowEvent *event){
    connect(tcpSocket, SIGNAL(readyRead()),
            this,      SLOT(textRead()));
}

void Lobby::hideEvent(QHideEvent *event){
    if (tcpSocket != NULL){
        tcpSocket->disconnect(tcpSocket, SIGNAL(readyRead()),
                              this,      SLOT(textRead()));
    }

    ui->textEdit->clear();
}

void Lobby::on_pushButton_clicked()
{
    host->setWidget(Home::MAIN_MENU_WINDOW);
    tcpSocket->disconnectFromHost();
}
