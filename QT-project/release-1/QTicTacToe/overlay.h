#ifndef OVERLAY_H
#define OVERLAY_H

#include <QWidget>

class Overlay : public QWidget
{
    Q_OBJECT
public:
    explicit Overlay(QWidget *parent = 0);

protected:
    void paintEvent(QPaintEvent *);

};

#endif // OVERLAY_H
