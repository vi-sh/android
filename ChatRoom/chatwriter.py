import json
import requests
import time
_id=1
url = 'https://verifier:admin123@verifier.cloudant.com/chat/1'
headers = {
    'cache-control': "no-cache",
    'content-type': "application/json",
    'accept': "application/json"
    }
uname=input('Enter your username to continue\n')
print('Welcome '+uname+' for VISHs CHATROOM')
print('!!!......Writer Window......!!!')
username='.........{'+uname+'}'

while True:
    data = requests.request("GET", url, headers=headers)
    data_in_text=str(data.text)
    data_in_json=json.loads(data_in_text.encode())
    old_id=data_in_json['_id']
    old_rev=data_in_json['_rev']
    message=data_in_json['message']

    newmessage=input()
    payload = {'_id': old_id, '_rev':old_rev, 'message':newmessage}
    putresponse = requests.request("PUT", url, data=json.dumps(payload))
    #print(putresponse.text)
    #print(newmessage+username)





    #data = requests.request("GET", url, headers=headers)
    #data_in_text=str(data.text)
    #data_in_json=json.loads(data_in_text.encode())
    #new_id=data_in_json['_id']
    #new_rev=data_in_json['_rev']
    #message=data_in_json['message']
