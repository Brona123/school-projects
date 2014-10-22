#include "mainmenu.h"
#include "ui_mainmenu.h"
#include "singleplayersetup.h"

MainMenu::MainMenu(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainMenu)
{
    this->host = dynamic_cast<Home*>(parent);
    ui->setupUi(this);
}

MainMenu::~MainMenu()
{
    delete ui;
}

void MainMenu::on_exitButton_clicked()
{
    exit(0);
}

void MainMenu::on_spButton_clicked()
{
    host->setWidget(Home::SP_SETUP_WINDOW);
}

void MainMenu::on_mpButton_clicked()
{
    host->setWidget(Home::MP_SETUP_WINDOW);
}
