import datetime
import os
import time
import json

import js2py
import requests
from bs4 import BeautifulSoup
import pymysql
from DBUtils.PooledDB import PooledDB

pool = PooledDB(pymysql, mincached=1, maxcached=10, host='121.36.21.125',
                user='atecut', passwd='pKSJzbnCjNC78rJ5',
                db='atecut', port=3306)


class AuthServer:
    @staticmethod
    def encryptPassword(_password, _aesKey):
        context = js2py.EvalJs()
        context.execute(open('js/aes.js', 'r', encoding='utf8').read())
        return context.eval("encryptAES('{}', '{}')".format(_password, _aesKey))

    @staticmethod
    def studentLogin(_studentNum, _password, _serviceUrl=''):

        _data = {}

        session = requests.session()

        _url = 'https://authserver.ecut.edu.cn/authserver/login'

        if _serviceUrl != '':
            _url = _serviceUrl

        res = {
            '_webvpn_key': 'eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiMjAxNzIwMTgwNzAyIiwiaWF0IjoxNTg2NTg0MTAzLCJleHAiOjE1ODY2NzA1MDN9.QRhE3Uisd93zm0ieGI7DyGXy27-T5ZBGvj1qKVFJ950',
            'webvpn_username': '201720180702%7C1586584103%7Cf163fc689a097cf5fe7793b251bb66ee80760f78',
            'SERVERID': 'Server1',
            '_astraeus_session': 'WDFLT3BReHNSc1U3SFA1WFkySjRLRXdCOFZCOUI3aWwzbXNQSDJWRUhQWkRlNy9rd3JuZG1vS3JwZStiRURPTkRvY2xtMGtNZk9neC9GMEZWUGh3NkpHMGZnbjRrQS9sakhwQ2ZjNkl1VVh0dmt1bXc0V3pjTk9vdHMrUDVXSTBMdUVXK3hET3kwSmlqV2habkxlbUZ6Q3ZDRlh1TDlZczZnTkZQY1RKQVg1OXB6bjJYQlBUTXRhSWxVZnBMUVF0QndXZU9sL1JmWHdvdCtlc0FZbHpHZDQvcWtKYUU3WGpKd1lFWkszTEx3cDAwSDk0TyswdFR4VVJLTUJ2MmN2SlkzVVBFN0hzMjFnTXpjN0s5bVJOczU1OFB4cjZNWllpVndSUytwZnRYNGxwNGRTalBRNDdJMTNpMlozUlVBeDctLVBuT1lMRExUT3Z6NlhzMmNkVkw3Tmc9PQ%3D%3D--d573bbb43f98ceb0d23925be62e490c6555fd498'
        }

        cookiesJar = requests.utils.cookiejar_from_dict(res, cookiejar=None, overwrite=True)
        session.cookies = cookiesJar
        resp0 = session.get(_serviceUrl)
        html = resp0.content.decode('utf-8')
        soup = BeautifulSoup(html, 'lxml')
        inputEles = soup.find_all(name='input')
        for inputEle in inputEles:

            if 'name' in inputEle.attrs.keys():
                name = inputEle.attrs['name']
            else:
                name = None

            if name in ('lt', 'dllt', 'execution', '_eventId', 'rmShown'):
                _data[inputEle.attrs['name']] = inputEle.attrs['value']

        _data['rememberMe'] = 'on'
        _start = html.find('pwdDefaultEncryptSalt') + len('pwdDefaultEncryptSalt = "')
        _end = _start + len('bElVrwm2AaEdWrYq')
        _data['pwdDefaultEncryptSalt'] = html[_start: _end]
        _data['username'] = _studentNum
        _data['password'] = AuthServer.encryptPassword(_password, _data['pwdDefaultEncryptSalt'])

        _data.pop('pwdDefaultEncryptSalt')

        resp0 = session.post(_serviceUrl,
                             data=_data)

        # html1 = resp0.content.decode('uf8-f')

        _cookies = requests.utils.dict_from_cookiejar(session.cookies)

        cookiesStr = ''
        for _cookieName in list(_cookies.keys()):
            cookiesStr += '{}={};'.format(_cookieName, _cookies[_cookieName])

        conn = pool.connection()
        cursor = conn.cursor()
        cursor.execute(
            'INSERT INTO cookies (user_number, user_cookies, type, creat_time, version) VALUES ({}, {}, {}, {}, {})'
            .format(_studentNum, cookiesStr, 'jwxt', cookiesStr, ))
        res = cursor.fetchall()

        return


if __name__ == '__main__':
    # AuthServer.studentLogin('201720180702', 'ly19980911',
    #                         'https://authserver-ecut-edu-cn-443.webvpn1.ecit.cn/authserver/login?service=https%3A%2F%2Fjwxt-ecut-edu-cn-18801.webvpn1.ecit.cn%3A443%2Fcaslogin.jsp')

    conn = pool.connection()
    cursor = conn.cursor()
    sql = "INSERT INTO cookies (user_number, user_cookies, type, creat_time, version) VALUES ('{}', '{}', '{}', '{}', 1)". \
        format('_studentNum', 'cookiesStr', 'jwxt', datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))

    a = cursor.execute(sql)

    print()

# res = {
#     'JSESSIONID': 'kO9noeTuQyIOoIi_t4qmenJeTdTXz0MxDonMJEtlsfckDaWg4fwg!-1900380123',
#     'route': '821f33a27f0005037b76f2cc013c01ea',
#     'CASTGC': 'TGT-44346-aNSctqCennDdD2v2WsyOoPGWWo0fQWXcdLsFd3MLi1lSnHdg5R1586581634903-LVAB-cas'
# }
