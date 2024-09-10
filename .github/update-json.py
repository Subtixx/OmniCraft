# Generate the updates.json file from github releases
# File looks like this:
# {
#   "homepage": "<homepage/download page for your mod>",
#   "<mcversion>": {
#     "<modversion>": "<changelog for this version>", 
#     // List all versions of your mod for the given Minecraft version, along with their changelogs
#     // ...
#   },
#   "promos": {
#     "<mcversion>-latest": "<modversion>",
#     // Declare the latest "bleeding-edge" version of your mod for the given Minecraft version
#     "<mcversion>-recommended": "<modversion>",
#     // Declare the latest "stable" version of your mod for the given Minecraft version
#     // ...
#   }
# }

import json
import requests
import os
import sys
import re
import datetime

class GenerateUpdatesJson:
    def __init__(self, mod_id, github_user, github_repo, mc_version):
        self.mod_id = mod_id
        self.github_user = github_user
        self.github_repo = github_repo
        self.mc_version = mc_version
        self.homepage = "https://github.com/{}/{}".format(self.github_user, self.github_repo)
        self.releases = []
        self.promos = {}
        self.updates = {}
        self.updates["homepage"] = self.homepage
        self.updates[self.mc_version] = {}
        self.updates["promos"] = self.promos

    def get_releases(self):
        url = "https://api.github.com/repos/{}/{}/releases".format(self.github_user, self.github_repo)
        response = requests.get(url)
        if response.status_code != 200:
            print("Error: Could not fetch releases from github")
            sys.exit(1)
        releases = response.json()
        print("Found {} releases".format(len(releases)))
        # Pretty print the releases
        print(json.dumps(releases, indent=2))
        for release in releases:
            if release["prerelease"]:
                print("Skipping prerelease: {}".format(release["tag_name"]))
                continue
            version = release["tag_name"]
            if not re.match(r"^\d+\.\d+\.\d+$", version):
                if not re.match(r"^\d+\.\d+-SNAPSHOT$", version):
                    print("Skipping invalid version: {}".format(version))
                    continue
                version = "0." + version[:-9]
            changelog = release["body"]
            self.releases.append((version, changelog))

    def generate_updates(self):
        for version, changelog in self.releases:
            self.updates[self.mc_version][version] = changelog
        self.promos["{}-latest".format(self.mc_version)] = self.releases[0][0]
        self.promos["{}-recommended".format(self.mc_version)] = self.releases[0][0]

    def write_updates(self):
        with open("updates.json", "w") as f:
            json.dump(self.updates, f, indent=2)
            
if __name__ == "__main__":
    mod_id = os.environ.get("MODID") or "omnicraft"
    github_user = os.environ.get("GITHUB_USER") or "subtixx"
    github_repo = os.environ.get("GITHUB_REPO") or "OmniCraft"
    mc_version = os.environ.get("MCVERSION") or "1.21.1"
    if not mod_id or not github_user or not github_repo or not mc_version:
        print("Error: Environment variables not set")
        sys.exit(1)
    gen = GenerateUpdatesJson(mod_id, github_user, github_repo, mc_version)
    gen.get_releases()
    gen.generate_updates()
    gen.write_updates()