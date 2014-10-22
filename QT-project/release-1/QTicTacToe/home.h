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
    explicit Home(QWidget *parent = 0);
    void setWidget(int);
    QWidget* getGamefieldWidget();
    void addNewWidget(QWidget*);
signals:

public slots:

private:
    QStackedWidget *widgets;

};

#endif // HOME_H
