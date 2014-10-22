#include "singleplayersetup.h"
#include "ui_singleplayersetup.h"
#include "gamefield.h"

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

    GameField *temp = dynamic_cast<GameField*>(host->getGamefieldWidget());
    temp->setWidth(width);
    temp->setHeight(height);
    temp->setVictoryCondition(victoryCondition);
    host->addNewWidget(temp);
    host->setWidget(Home::GAMEFIELD_WINDOW);
}
