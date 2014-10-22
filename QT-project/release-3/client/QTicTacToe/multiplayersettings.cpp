#include "multiplayersettings.h"
#include "ui_multiplayersettings.h"
#include "gamefield.h"

MultiplayerSettings::MultiplayerSettings(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::MultiplayerSettings)
{
    ui->setupUi(this);
    host = dynamic_cast<Home*>(parent);
    tcpSocket = NULL;
}

MultiplayerSettings::~MultiplayerSettings()
{
    delete ui;
}

void MultiplayerSettings::on_widthBox_currentTextChanged(const QString &arg1)
{
    emit settingsChanged();
}

void MultiplayerSettings::on_heightBox_currentTextChanged(const QString &arg1)
{
    emit settingsChanged();
}

void MultiplayerSettings::on_vcBox_currentTextChanged(const QString &arg1)
{
    emit settingsChanged();
}

void MultiplayerSettings::sendNewSettings(){
    QString width = ui->widthBox->currentText();
    QString height = ui->heightBox->currentText();
    QString vc = ui->vcBox->currentText();
    QString player2 = ui->listWidget->item(1)->text();

    tcpSocket->write("/Settings::"
                     + player2.toLatin1()
                     + "|"
                     + width.toUtf8()
                     + "|"
                     + height.toUtf8()
                     + "|"
                     + vc.toUtf8());
}

void MultiplayerSettings::setSocket(QTcpSocket *socket){
    this->tcpSocket = socket;
}

void MultiplayerSettings::textRead(){
    QString text = tcpSocket->readAll();
    QString settingsPrefix = "/Settings::";
    QString startPrefix = "/Start::";

    if (text.startsWith(settingsPrefix)){
        changeSettings(text);
    } else if (text.startsWith(startPrefix)){
        startGame();
    } else {
        ui->textBrowser->append(text);
    }
}

void MultiplayerSettings::setPlayers(QString player1, QString player2){
    ui->listWidget->addItem(player1);
    ui->listWidget->item(0)->setForeground(QColor(0, 0, 220));
    ui->listWidget->addItem(player2);
    this->player1 = player1;
    this->player2 = player2;
}

void MultiplayerSettings::on_lineEdit_returnPressed()
{
    QString line = ui->lineEdit->text();
    ui->lineEdit->clear();

    if (!line.startsWith(("/"))){
        ui->textBrowser->append("<" + this->player1 + "> " + line);
        QString p2 = player2.toLatin1();
        tcpSocket->write("/MultiplayerMatch::" + QString::number(p2.length()).toLatin1() + p2.toLatin1() + "<" + player1.toLatin1() + "> " + line.toLatin1());
    }
}

void MultiplayerSettings::changeSettings(QString text){
    QString settingsPrefix = "/Settings::";
    QStringRef values(&text, settingsPrefix.length(), text.length() - settingsPrefix.length());
    QStringList valueList = values.toString().split("|");

    ui->widthBox->setCurrentText(valueList[1]);
    ui->heightBox->setCurrentText(valueList[2]);
    ui->vcBox->setCurrentText(valueList[3]);
}

void MultiplayerSettings::on_goButton_clicked()
{
    QString startPrefix = "/Start::";
    tcpSocket->write(startPrefix.toLatin1() + ui->listWidget->item(1)->text().toLatin1());
    startGame();
}

void MultiplayerSettings::startGame(){
    int width = ui->widthBox->currentText().toInt();
    int height = ui->heightBox->currentText().toInt();
    int victoryCondition = ui->vcBox->currentText().toInt();



    GameField *temp = dynamic_cast<GameField*>(host->getWidget(Home::GAMEFIELD_WINDOW));
    temp->setWidth(width);
    temp->setHeight(height);
    temp->setVictoryCondition(victoryCondition);
    temp->setMultiplayerMode(true);
    temp->setSocket(tcpSocket);
    temp->setPlayers(player1, player2);
    temp->setPlayerID(myID);
    host->setWidget(Home::GAMEFIELD_WINDOW);
}

void MultiplayerSettings::setID(int ID){
    this->myID = ID;
}

void MultiplayerSettings::showEvent(QShowEvent *){
    connect(this, SIGNAL(settingsChanged()),
            this, SLOT(sendNewSettings()));

    connect(tcpSocket, SIGNAL(readyRead()),
            this,      SLOT(textRead()));
}

void MultiplayerSettings::hideEvent(QHideEvent *){
    if (tcpSocket != NULL){
        disconnect(tcpSocket, SIGNAL(readyRead()),
                   this,      SLOT(textRead()));
    }

    ui->listWidget->clear();
    ui->textBrowser->clear();
}
