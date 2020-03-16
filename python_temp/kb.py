from flask import Flask, request
from flask_cors import *
import json
import requests

app = Flask(__name__)
CORS(app, supports_credentials=True)


def getAuthServerCookies(_user):
    resp0 = requests.post('http://neverth.fun:8080/atecut/authServer/cookies/{}'
                          .format(_user['username'])
                          , json={"password": _user['password']})
    resp0 = json.loads(resp0.content.decode('utf-8'))
    return resp0


def getKbCookies(_user):
    authserverCookies = getAuthServerCookies(_user=_user)

    kbCookies = {}

    resp0 = requests.get(
        'https://authserver.ecut.edu.cn/authserver/login?service=https%3A%2F%2Fehall.ecut.edu.cn%3A443%2Fpsfw%2Fsys%2Fpswdkbapp%2F*default%2Findex.do'
        , cookies=authserverCookies['data'], allow_redirects=False)

    ticketUrl = resp0.headers.get("Location")
    resp0 = requests.get(ticketUrl, allow_redirects=False)

    for i in resp0.cookies:
        print(i.name)
        kbCookies[i.name] = i.value

    newUrl = resp0.headers.get("Location")
    resp0 = requests.get(newUrl, allow_redirects=False, cookies=kbCookies)

    for i in resp0.cookies:
        kbCookies[i.name] = i.value

    resp0 = requests.post(
        "https://ehall.ecut.edu.cn/psfw/sys/xgpspubapp/indexmenu/getAppConfig.do?appId=5395950742020172&appName=pswdkbapp",
        cookies=kbCookies)

    for i in resp0.cookies:
        kbCookies[i.name] = i.value

    # resp0 = requests.post(
    #     'https://ehall.ecut.edu.cn/psfw/sys/funauthapp/api/changeAppRole/pswdkbapp/20190327165300850.do',
    #     cookies=newCookies)
    #
    # for i in resp0.cookies:
    #     newCookies[i.name] = i.value

    # resp0 = requests.post('https://ehall.ecut.edu.cn/psfw/sys/xgpspubapp/userinfo/setXgCommonAppRole.do',
    #                       cookies=newCookies)
    #
    # for i in resp0.cookies:
    #     newCookies[i.name] = i.value

    # resp0 = requests.get("https://ehall.ecut.edu.cn/psfw/sys/emappagelog/config/pswdkbapp.do",
    #                      cookies=newCookies)
    #
    # for i in resp0.cookies:
    #     newCookies[i.name] = i.value
    return kbCookies


def getKbData(_user, _data, **kwargs):
    _kbCookies = getKbCookies(_user=_user)

    myheader = {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    }

    resp0 = requests.post("https://ehall.ecut.edu.cn/psfw/sys/pswdkbapp/wdkbcx/getWdkbxx.do",
                          headers=myheader,
                          cookies=_kbCookies, data='data=' + str(_data))

    a = resp0.content.decode('utf-8')
    resp0 = json.loads(resp0.content.decode('utf-8'))
    return resp0


@app.route('/kb/get', methods=['POST'])
def kb():
    """
    {
        "username": "123",
        "password": "XXX",
        "XN": "", // 学年
        "XQ": "", // 学期
        "ZC": "", // 周次
    }
    """
    res = request

    requestData = json.loads(str(request.data, 'utf-8'))

    ehallParams = {
        "XN": requestData['XN'],
        "XQ": requestData['XQ'],
        "ZC": requestData['ZC'],
        "SJBZ": "1"
    }

    user = {
        'username': requestData['username'],
        'password': requestData['password'],
    }

    kbData = getKbData(_user=user, _data=ehallParams)

    return kbData


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8111)
