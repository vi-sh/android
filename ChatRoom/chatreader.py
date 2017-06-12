import json
import requests
import time
import signal
url = 'https://verifier.cloudant.com/chat/1'
headers = {
    'cache-control': "no-cache"
    }
uname=input('Enter your username to continue\n')
print('Welcome '+uname+' for VISHs CHATROOM')
print('!!!......Reader Window......!!!')
username='.........{'+uname+'}'
#gathering old data
olddata = requests.request("GET", url, headers=headers)
old_data_in_text=str(olddata.text)
old_data_in_json=json.loads(old_data_in_text.encode())
old_id=old_data_in_json['_id']
old_rev=old_data_in_json['_rev']
oldmessage=old_data_in_json['message']

try:
    while True:
        data = requests.request("GET", url, headers=headers)
        data_in_text=str(data.text)
        data_in_json=json.loads(data_in_text.encode())
        new_id=data_in_json['_id']
        new_rev=data_in_json['_rev']
        newmessage=data_in_json['message']
        if oldmessage != newmessage:
            print(newmessage+username)
            oldmessage=newmessage
            #payload = {'_id': new_id, '_rev':new_rev, 'message':''}
            #putresponse = requests.request("PUT", url, data=json.dumps(payload))
        continue
except:
    raise
