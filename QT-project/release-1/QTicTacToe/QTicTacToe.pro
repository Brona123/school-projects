#-------------------------------------------------
#
# Project created by QtCreator 2014-04-11T12:35:44
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = QTicTacToe
TEMPLATE = app


SOURCES += main.cpp\
        mainmenu.cpp \
    singleplayersetup.cpp \
    home.cpp \
    gamefield.cpp

HEADERS  += mainmenu.h \
    singleplayersetup.h \
    home.h \
    gamefield.h

FORMS    += mainmenu.ui \
    singleplayersetup.ui \
    gamefield.ui
