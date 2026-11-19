# Q26. Ansible Playbook using Modules

**Experiment:** Ansible roles and modules

## Question
Create an Ansible playbook for a simple application and use modules in Ansible.

## Aim
To create an Ansible playbook for a simple application using various Ansible modules.

## Procedure
1. Install Ansible and create inventory.
2. Try modules with ad-hoc commands.
3. Write `modules-playbook.yml` using ping, apt, user, file, copy, lineinfile, service, command, shell, uri, debug.
4. Run the playbook and verify the application.

## Project structure
```text
Q26_Ansible_Modules/
  hosts.ini
  modules-playbook.yml
```

## Files

### `hosts.ini`
```ini
[local]
localhost ansible_connection=local ansible_python_interpreter=/usr/bin/python3
```

### `modules-playbook.yml`
```yaml
---
# Simple application deployed using many Ansible modules
- name: Deploy a simple application using Ansible modules
  hosts: local
  become: true

  vars:
    app_name: simpleapp
    app_user: appuser
    app_dir: /var/www/html/simpleapp

  tasks:
    - name: 1. ping module - check connection
      ansible.builtin.ping:

    - name: 2. apt module - install required packages
      ansible.builtin.apt:
        name:
          - apache2
          - git
          - tree
        state: present
        update_cache: true

    - name: 3. user module - create application user
      ansible.builtin.user:
        name: "{{ app_user }}"
        shell: /bin/bash
        create_home: true

    - name: 4. file module - create application directory
      ansible.builtin.file:
        path: "{{ app_dir }}"
        state: directory
        owner: "{{ app_user }}"
        mode: "0755"

    - name: 5. copy module - deploy application page
      ansible.builtin.copy:
        dest: "{{ app_dir }}/index.html"
        content: |
          <!DOCTYPE html>
          <html>
          <head><title>{{ app_name }}</title></head>
          <body>
            <h1>{{ app_name }} deployed with Ansible modules</h1>
            <p>Server: {{ ansible_hostname }} | OS: {{ ansible_distribution }} {{ ansible_distribution_version }}</p>
          </body>
          </html>
        owner: "{{ app_user }}"
        mode: "0644"

    - name: 6. lineinfile module - write app config
      ansible.builtin.lineinfile:
        path: "{{ app_dir }}/app.conf"
        line: "APP_NAME={{ app_name }}"
        create: true
        mode: "0644"

    - name: 7. service module - start and enable Apache
      ansible.builtin.service:
        name: apache2
        state: started
        enabled: true

    - name: 8. command module - get uptime
      ansible.builtin.command: uptime
      register: up
      changed_when: false

    - name: 9. shell module - list deployed files
      ansible.builtin.shell: ls -l {{ app_dir }} | wc -l
      register: files
      changed_when: false

    - name: 10. uri module - verify application URL
      ansible.builtin.uri:
        url: "http://localhost/{{ app_name }}/"
        return_content: true
      register: page

    - name: 11. debug module - print results
      ansible.builtin.debug:
        msg:
          - "Uptime: {{ up.stdout }}"
          - "Files in app dir (lines): {{ files.stdout }}"
          - "App URL status: {{ page.status }}"
          - "Open http://localhost/{{ app_name }}/ in browser"
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

### 3. Ad-hoc commands (use modules directly)
```bash
ansible all -i hosts.ini -m ping
ansible all -i hosts.ini -m setup -a "filter=ansible_distribution*"
ansible all -i hosts.ini -m command -a "uptime"
ansible all -i hosts.ini -m shell -a "df -h | head -3"
ansible all -i hosts.ini -m apt -a "name=tree state=present update_cache=yes" --become -K
ansible all -i hosts.ini -m file -a "path=/tmp/demo state=directory mode=0755"
ansible all -i hosts.ini -m copy -a "content='Hello Ansible' dest=/tmp/demo/hello.txt"
ansible all -i hosts.ini -m command -a "cat /tmp/demo/hello.txt"
ansible all -i hosts.ini -m service -a "name=apache2 state=started" --become -K
ansible-doc -l | wc -l          # number of modules
ansible-doc apt                 # module documentation
```

### 4. Run the playbook
```bash
ansible-playbook -i hosts.ini modules-playbook.yml --syntax-check
ansible-playbook -i hosts.ini modules-playbook.yml -K
```

### 5. Verify
```bash
curl http://localhost/simpleapp/
id appuser
cat /var/www/html/simpleapp/app.conf
```

### Modules used
| Module | Purpose |
|---|---|
| ping | test connection |
| apt | install packages |
| user | create user |
| file | create directory / set permission |
| copy | copy file / content to host |
| lineinfile | add line to a file |
| service | start / enable service |
| command | run command (no shell features) |
| shell | run command with pipes |
| uri | HTTP request |
| debug | print messages |
| setup | gather facts (ad-hoc) |


## Result
The experiment was executed successfully and the output was verified.
