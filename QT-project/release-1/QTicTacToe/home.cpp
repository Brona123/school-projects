#include "home.h"
#include <QVBoxLayout>
#include "mainmenu.h"
#include "gamefield.h"
#include "singleplayersetup.h"

Home::Home(QWidget *parent) :
    QMainWindow(parent)
{
    setMinimumSize(800, 600);
    widgets = new QStackedWidget;

    widgets->addWidget(new MainMenu(this));
    widgets->addWidget(new SingleplayerSetup(this));
    widgets->addWidget(new GameField(this));

    setCentralWidget(widgets);
    widgets->setCurrentIndex(0);

}

void Home::setWidget(int i){
    widgets->setCurrentIndex(i);

}

QWidget* Home::getGamefieldWidget(){
    return widgets->widget(GAMEFIELD_WINDOW);
}

void Home::addNewWidget(QWidget *widget){
    widgets->addWidget(widget);
}
