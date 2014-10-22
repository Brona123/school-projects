#-------------------------------------------------
#
# Project created by QtCreator 2014-04-11T12:35:44
#
#-------------------------------------------------

QT       += core gui
QT       += network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = QTicTacToe
TEMPLATE = app


SOURCES += main.cpp\
        mainmenu.cpp \
    singleplayersetup.cpp \
    home.cpp \
    gamefield.cpp \
    multiplayersetup.cpp \
    lobby.cpp \
    multiplayersettings.cpp

HEADERS  += mainmenu.h \
    singleplayersetup.h \
    home.h \
    gamefield.h \
    multiplayersetup.h \
    lobby.h \
    multiplayersettings.h

FORMS    += mainmenu.ui \
    singleplayersetup.ui \
    gamefield.ui \
    multiplayersetup.ui \
    lobby.ui \
    multiplayersettings.ui

RESOURCES +=
