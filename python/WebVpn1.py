import os
import time
import json

import js2py
import requests
from bs4 import BeautifulSoup

if __name__ == '__main__':
    _data = {
        'utf8': '✓',
        'user[login]': '201720180702',
        'user[password]': 'ly19980911',
        'user[dymatice_code]': 'unknown',
        'commit': '登录+Login'
    }

    session = requests.session()

    resp0 = session.get('https://webvpn1.ecit.cn/users/sign_in')
    html = resp0.content.decode('utf-8')
    soup = BeautifulSoup(html, 'lxml')
    inputEles = soup.find_all(name='input')

    for inputEle in inputEles:

        if 'name' in inputEle.attrs.keys():
            name = inputEle.attrs['name']
        else:
            name = None

        if name in 'authenticity_token':
            _data[inputEle.attrs['name']] = inputEle.attrs['value']

    resp0 = session.post('https://webvpn1.ecit.cn/users/sign_in', data=_data)
    html1 = resp0.content.decode('utf-8')
    a = requests.utils.dict_from_cookiejar(session.cookies)

    res = {
        '_webvpn_key': 'eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiMjAxNzIwMTgwNzAyIiwiaWF0IjoxNTg2NTg0MTAzLCJleHAiOjE1ODY2NzA1MDN9.QRhE3Uisd93zm0ieGI7DyGXy27-T5ZBGvj1qKVFJ950',
        'webvpn_username': '201720180702%7C1586584103%7Cf163fc689a097cf5fe7793b251bb66ee80760f78',
        'SERVERID': 'Server1',
        '_astraeus_session': 'WDFLT3BReHNSc1U3SFA1WFkySjRLRXdCOFZCOUI3aWwzbXNQSDJWRUhQWkRlNy9rd3JuZG1vS3JwZStiRURPTkRvY2xtMGtNZk9neC9GMEZWUGh3NkpHMGZnbjRrQS9sakhwQ2ZjNkl1VVh0dmt1bXc0V3pjTk9vdHMrUDVXSTBMdUVXK3hET3kwSmlqV2habkxlbUZ6Q3ZDRlh1TDlZczZnTkZQY1RKQVg1OXB6bjJYQlBUTXRhSWxVZnBMUVF0QndXZU9sL1JmWHdvdCtlc0FZbHpHZDQvcWtKYUU3WGpKd1lFWkszTEx3cDAwSDk0TyswdFR4VVJLTUJ2MmN2SlkzVVBFN0hzMjFnTXpjN0s5bVJOczU1OFB4cjZNWllpVndSUytwZnRYNGxwNGRTalBRNDdJMTNpMlozUlVBeDctLVBuT1lMRExUT3Z6NlhzMmNkVkw3Tmc9PQ%3D%3D--d573bbb43f98ceb0d23925be62e490c6555fd498'
    }

    print()
