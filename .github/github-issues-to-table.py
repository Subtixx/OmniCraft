# Convert Github issues to table

import requests
import json
import datetime

url = "https://api.github.com/repos/subtixx/omnicraft/issues?labels=enhancement"
response = requests.get(url)
data = response.json()

with open("issue_list.md", "w") as f:
    f.write("| Title | Status | Created |\n")
    f.write("| --- | --- | --- |\n")
    for issue in data:
        status = "Done"
        if issue["state"] == "open":
            status = "Planned"
        url = issue["html_url"]
        title = issue["title"]
        created_at = issue["created_at"]
        # Parse date
        created = datetime.datetime.fromisoformat(created_at).strftime("%d/%m/%Y")

        f.write(f"| [{title}]({url}) | {status} | {created} |\n")