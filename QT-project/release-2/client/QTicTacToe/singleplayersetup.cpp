#include "singleplayersetup.h"
#include "ui_singleplayersetup.h"
#include "gamefield.h"
#include <QDebug>

SingleplayerSetup::SingleplayerSetup(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::SingleplayerSetup)
{
    this->host = dynamic_cast<Home*>(parent);
    ui->setupUi(this);
}

SingleplayerSetup::~SingleplayerSetup()
{
    delete ui;
}

void SingleplayerSetup::on_mainMenuButton_clicked()
{
    host->setWidget(Home::MAIN_MENU_WINDOW);
}

void SingleplayerSetup::on_startButton_clicked()
{
    int width = (ui->widthBox->currentText().toInt());
    int height = (ui->heightBox->currentText().toInt());
    int victoryCondition = (ui->vcComboBox->currentText().toInt());

    QString difficulty = ui->diffComboBox->currentText();

    int diff;
    if (difficulty == "Easy"){
        diff = 1;
    } else if (difficulty == "Normal"){
        diff = 2;
    } else if (difficulty == "Hard"){
        diff = 3;
    } else if (difficulty == "Impossible"){
        diff = 4;
    }

    qDebug() << diff;

    GameField *temp = dynamic_cast<GameField*>(host->getWidget(Home::GAMEFIELD_WINDOW));
    temp->setWidth(width);
    temp->setHeight(height);
    temp->setVictoryCondition(victoryCondition);
    temp->setDifficulty(diff);
    temp->setMultiplayerMode(false);
    host->setWidget(Home::GAMEFIELD_WINDOW);
}
