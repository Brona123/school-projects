#include "home.h"
#include <QVBoxLayout>
#include "mainmenu.h"
#include "gamefield.h"
#include "singleplayersetup.h"
#include "multiplayersetup.h"
#include "lobby.h"
#include "multiplayersettings.h"

Home::Home(QWidget *parent) :
    QMainWindow(parent)
{
    widgets = new QStackedWidget;

    widgets->setMinimumSize(800, 600);
    widgets->addWidget(new MainMenu(this));
    widgets->addWidget(new SingleplayerSetup(this));
    widgets->addWidget(new GameField(this));
    widgets->addWidget(new MultiplayerSetup(this));
    widgets->addWidget(new Lobby(this));
    widgets->addWidget(new MultiplayerSettings(this));

    setCentralWidget(widgets);
    widgets->setCurrentIndex(0);

}

void Home::setWidget(int i){
    widgets->setCurrentIndex(i);
}

QWidget* Home::getWidget(int index){
    return widgets->widget(index);
}

void Home::addNewWidget(QWidget *widget){
    widgets->addWidget(widget);
}
