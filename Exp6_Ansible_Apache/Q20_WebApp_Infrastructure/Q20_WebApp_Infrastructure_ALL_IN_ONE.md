# Q20. Ansible - Web application infrastructure

**Experiment:** Ansible playbook: Apache web server + web page

## Question
Create an Ansible playbook to configure a simple web application infrastructure on a Linux system by installing the Apache web server, starting and enabling the service, deploying an HTML page containing a heading and a description, and verifying that the page is accessible through http://localhost.

## Aim
To configure a simple web application infrastructure using an Ansible playbook with Apache and an HTML page containing a heading and a description, accessible at http://localhost.

## Procedure
1. Install Ansible.
2. Create inventory `hosts.ini`.
3. Write `webapp-playbook.yml` (install, start/enable, deploy page, verify with uri + assert).
4. Run the playbook and open http://localhost.

## Project structure
```text
Q20_WebApp_Infrastructure/
  hosts.ini
  webapp-playbook.yml
```

## Files

### `hosts.ini`
```ini
[local]
localhost ansible_connection=local ansible_python_interpreter=/usr/bin/python3
```

### `webapp-playbook.yml`
```yaml
---
- name: Configure simple web application infrastructure
  hosts: local
  become: true

  vars:
    page_heading: "My Simple Web Application"
    page_description: "This web application infrastructure was configured automatically using an Ansible playbook with the Apache web server."

  tasks:
    - name: Install Apache web server
      ansible.builtin.apt:
        name: apache2
        state: present
        update_cache: true

    - name: Start and enable Apache
      ansible.builtin.service:
        name: apache2
        state: started
        enabled: true

    - name: Deploy HTML page with heading and description
      ansible.builtin.copy:
        dest: /var/www/html/index.html
        content: |
          <!DOCTYPE html>
          <html>
          <head>
              <title>{{ page_heading }}</title>
          </head>
          <body>
              <h1>{{ page_heading }}</h1>
              <p>{{ page_description }}</p>
          </body>
          </html>
        mode: "0644"

    - name: Verify page is accessible at http://localhost
      ansible.builtin.uri:
        url: http://localhost
        return_content: true
        status_code: 200
      register: page

    - name: Check heading is present in the page
      ansible.builtin.assert:
        that:
          - page_heading in page.content
        success_msg: "Page is accessible at http://localhost (HTTP {{ page.status }})"
        fail_msg: "Page content is wrong"
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
ansible-playbook -i hosts.ini webapp-playbook.yml --syntax-check
ansible-playbook -i hosts.ini webapp-playbook.yml --check -K      # dry run (optional)
ansible-playbook -i hosts.ini webapp-playbook.yml -K              # -K asks sudo (BECOME) password
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
