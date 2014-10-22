#ifndef HOME_H
#define HOME_H

#include <QMainWindow>
#include <QStackedWidget>

class Home : public QMainWindow
{
    Q_OBJECT
public:
    static const int MAIN_MENU_WINDOW = 0;
    static const int SP_SETUP_WINDOW = 1;
    static const int GAMEFIELD_WINDOW = 2;
    static const int MP_SETUP_WINDOW = 3;
    static const int LOBBY_WINDOW = 4;
    static const int MP_SETTINGS_WINDOW = 5;
    explicit Home(QWidget *parent = 0);
    void setWidget(int);
    void addNewWidget(QWidget*);
    void setPlayerName(QString);
    QWidget* getWidget(int);
signals:

public slots:

private:
    QStackedWidget *widgets;

};

#endif // HOME_H
