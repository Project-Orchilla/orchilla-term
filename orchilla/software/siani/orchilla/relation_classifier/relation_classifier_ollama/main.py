from ollama import chat
from ollama import ChatResponse

response: ChatResponse = chat(model='gemma3:4b', messages=[
    {
        'role': 'user',
        'content': (
            "Your only purpose is to say if two phrases are temporally related or not with events.\n"
            "Phrase 1: 'We have to go to the gym'\n"
            "Phrase 2: 'After school we go to the cinema'\n"
            "Answer only with 'related' or 'not related'."
        ),
    },
])

print(response['message']['content'])
