# Q19. Ansible - Apache + welcome page

**Experiment:** Ansible playbook: Apache web server + web page

## Question
Create an Ansible playbook to install the Apache web server on a Linux system, start and enable the service, deploy a simple HTML web page displaying a welcome message, and verify the application's availability using an HTTP request.

## Aim
To create an Ansible playbook that installs Apache, starts and enables it, deploys a welcome page and verifies availability using an HTTP request.

## Procedure
1. Install Ansible.
2. Create inventory `hosts.ini` for localhost.
3. Write `apache-playbook.yml` with apt, service, copy, uri and debug modules.
4. Run the playbook and verify with curl / browser.

## Project structure
```text
Q19_Apache_WelcomePage/
  hosts.ini
  apache-playbook.yml
```

## Files

### `hosts.ini`
```ini
[local]
localhost ansible_connection=local ansible_python_interpreter=/usr/bin/python3
```

### `apache-playbook.yml`
```yaml
---
- name: Install Apache and deploy a welcome page
  hosts: local
  become: true

  tasks:
    - name: Update apt cache and install Apache
      ansible.builtin.apt:
        name: apache2
        state: present
        update_cache: true

    - name: Start and enable Apache service
      ansible.builtin.service:
        name: apache2
        state: started
        enabled: true

    - name: Deploy welcome web page
      ansible.builtin.copy:
        dest: /var/www/html/index.html
        content: |
          <!DOCTYPE html>
          <html>
          <head>
              <title>Welcome</title>
          </head>
          <body>
              <h1>Welcome to Apache Web Server deployed using Ansible!</h1>
          </body>
          </html>
        owner: www-data
        group: www-data
        mode: "0644"

    - name: Verify web page using HTTP request
      ansible.builtin.uri:
        url: http://localhost
        return_content: true
        status_code: 200
      register: web

    - name: Show HTTP response
      ansible.builtin.debug:
        msg:
          - "HTTP status: {{ web.status }}"
          - "{{ 'Welcome message found - application is available' if 'Welcome' in web.content else 'Welcome message NOT found' }}"
```

## Commands / Steps

### 1. Install Ansible (Ubuntu / WSL)
```bash
sudo apt update
sudo apt install -y software-properties-common
sudo add-apt-repository --yes --update ppa:ansible/ansible
sudo apt install -y ansible
ansible --version
```
(PPA fails -> `sudo apt install -y ansible`.)

### 2. Copy project into Linux home (WSL: /mnt/c folders are world-writable, Ansible may complain)
```bash
mkdir -p ~/ansible-lab && cd ~/ansible-lab
# copy the files of this folder here, e.g.
# cp -r "/mnt/c/Users/HP/OneDrive/Desktop/jenk/Lab_Experiments/<exp>/<question>/"* .
ansible all -i hosts.ini -m ping          # localhost | SUCCESS => "ping": "pong"
```

### 3. Syntax check, dry run, run
```bash
ansible-playbook -i hosts.ini apache-playbook.yml --syntax-check
ansible-playbook -i hosts.ini apache-playbook.yml --check -K      # dry run (optional)
ansible-playbook -i hosts.ini apache-playbook.yml -K              # -K asks sudo (BECOME) password
```
Expected end: `PLAY RECAP  localhost : ok=6 changed=... failed=0`

### 4. Verify manually
```bash
systemctl status apache2 --no-pager     # active (running)
systemctl is-enabled apache2            # enabled
curl http://localhost
curl -I http://localhost                # HTTP/1.1 200 OK
```
Browser (Windows with WSL works too): http://localhost

### Errors
| Error | Fix |
|---|---|
| `Missing sudo password` | add `-K` |
| `Could not get lock /var/lib/dpkg/lock-frontend` | wait / `sudo killall apt apt-get; sudo dpkg --configure -a` |
| `System has not been booted with systemd` (WSL) | `echo -e "[boot]\nsystemd=true" \| sudo tee /etc/wsl.conf`, PowerShell `wsl --shutdown`, reopen |
| Port 80 in use / uri fails | `sudo ss -ltnp \| grep :80` -> stop other server (nginx/IIS), rerun |


## Result
The experiment was executed successfully and the output was verified.
