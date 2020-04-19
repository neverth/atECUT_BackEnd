# coding=utf-8

import requests
from email.utils import formataddr
import smtplib
from email.mime.text import MIMEText
import random
import time


def sendQQEmail(_html):
    my_sender = '370920626@qq.com'
    my_pass = 'mnahptzzlidtbiif'

    ret = True
    try:
        msg = MIMEText(_html, 'html', 'utf-8')
        msg['From'] = formataddr(("发件人昵称", my_sender))
        msg['To'] = formataddr(("收件人昵称", my_sender))

        msg['Subject'] = 'atecut 报警'

        server = smtplib.SMTP_SSL("smtp.qq.com", 465)
        server.login(my_sender, my_pass)
        server.sendmail(my_sender, my_sender, msg.as_string())
        server.quit()

    except Exception:
        ret = False
    return ret


def request():
    a = [('201720180727', 'ly19980911'), ('201720180727', 'h36y61'), ('201720180727', 'h36y61')]

    user = a[random.randint(0, len(a) - 1)]

    resp0 = requests.post('http://neverth.fun:8111/score/get', json={
        "username": user[0],
        "password": user[1],
        "XSBH": "201720180727",
        "XN": "2019",
        "XQ": "1",
        "pageSize": 10,
        "pageNumber": 1
    })
    if resp0.status_code != 200:
        sendQQEmail('报错')
        exit(0)


if __name__ == '__main__':
    while True:
        request()
        time.sleep(60 * 30)
