# Q25. Ansible Roles - prerequisites, PostgreSQL, apache

**Experiment:** Ansible roles and modules

## Question
Install Ansible on a Linux system and create three Ansible roles named prerequisites, PostgreSQL, and apache to install Git, PostgreSQL, and the Apache web server, respectively. Define the required tasks in each role, create a stack.yml playbook to execute all three roles on localhost, and verify the successful installation of the required packages.

## Aim
To install Ansible and create three roles (prerequisites, PostgreSQL, apache) that install Git, PostgreSQL and Apache, executed by a stack.yml playbook on localhost.

## Procedure
1. Install Ansible.
2. Create roles with `ansible-galaxy init`.
3. Write tasks in each role's `tasks/main.yml`.
4. Write `stack.yml` that calls the three roles.
5. Run the playbook and verify the packages.

## Project structure
```text
Q25_Ansible_Roles/
  hosts.ini
  roles/prerequisites/tasks/main.yml
  roles/PostgreSQL/tasks/main.yml
  roles/apache/tasks/main.yml
  stack.yml
```

## Files

### `hosts.ini`
```ini
[local]
localhost ansible_connection=local ansible_python_interpreter=/usr/bin/python3
```

### `roles/prerequisites/tasks/main.yml`
```yaml
---
# roles/prerequisites/tasks/main.yml
- name: Update apt cache
  ansible.builtin.apt:
    update_cache: true
    cache_valid_time: 3600

- name: Install Git
  ansible.builtin.apt:
    name: git
    state: present

- name: Check Git version
  ansible.builtin.command: git --version
  register: git_ver
  changed_when: false

- name: Show Git version
  ansible.builtin.debug:
    msg: "{{ git_ver.stdout }}"
```

### `roles/PostgreSQL/tasks/main.yml`
```yaml
---
# roles/PostgreSQL/tasks/main.yml
- name: Install PostgreSQL
  ansible.builtin.apt:
    name:
      - postgresql
      - postgresql-contrib
    state: present

- name: Start and enable PostgreSQL service
  ansible.builtin.service:
    name: postgresql
    state: started
    enabled: true

- name: Check PostgreSQL version
  ansible.builtin.command: psql --version
  register: pg_ver
  changed_when: false

- name: Show PostgreSQL version
  ansible.builtin.debug:
    msg: "{{ pg_ver.stdout }}"
```

### `roles/apache/tasks/main.yml`
```yaml
---
# roles/apache/tasks/main.yml
- name: Install Apache web server
  ansible.builtin.apt:
    name: apache2
    state: present

- name: Start and enable Apache service
  ansible.builtin.service:
    name: apache2
    state: started
    enabled: true

- name: Check Apache version
  ansible.builtin.command: apache2 -v
  register: apache_ver
  changed_when: false

- name: Show Apache version
  ansible.builtin.debug:
    msg: "{{ apache_ver.stdout_lines[0] }}"
```

### `stack.yml`
```yaml
---
# stack.yml - runs all three roles on localhost
- name: Install full stack (Git, PostgreSQL, Apache) using roles
  hosts: localhost
  connection: local
  become: true

  roles:
    - prerequisites
    - PostgreSQL
    - apache

  post_tasks:
    - name: Gather installed package facts
      ansible.builtin.package_facts:
        manager: auto

    - name: Verify all packages are installed
      ansible.builtin.assert:
        that:
          - "'git' in ansible_facts.packages"
          - "'postgresql' in ansible_facts.packages"
          - "'apache2' in ansible_facts.packages"
        success_msg: "Git, PostgreSQL and Apache are installed successfully"
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

### 3. Create the roles skeleton
```bash
cd ~/ansible-lab
mkdir roles
ansible-galaxy init roles/prerequisites
ansible-galaxy init roles/PostgreSQL
ansible-galaxy init roles/apache
tree roles            # (sudo apt install tree)  shows tasks/ handlers/ vars/ defaults/ meta/ ...
```

### 4. Write tasks
```bash
nano roles/prerequisites/tasks/main.yml     # paste prerequisites tasks
nano roles/PostgreSQL/tasks/main.yml        # paste PostgreSQL tasks
nano roles/apache/tasks/main.yml            # paste apache tasks
nano stack.yml                              # paste stack.yml
```

### 5. Run the playbook
```bash
ansible-playbook -i hosts.ini stack.yml --syntax-check
ansible-playbook -i hosts.ini stack.yml -K
```
Expected: tasks prefixed `prerequisites : ...`, `PostgreSQL : ...`, `apache : ...`, assert message
"Git, PostgreSQL and Apache are installed successfully", `failed=0` in PLAY RECAP.

### 6. Verify installation manually
```bash
git --version
psql --version
apache2 -v
systemctl status postgresql --no-pager
systemctl status apache2 --no-pager
dpkg -l | grep -E "git |postgresql|apache2"
sudo -u postgres psql -c "SELECT version();"
curl -I http://localhost
ansible-galaxy role list --roles-path roles     # lists the 3 roles
```

### Errors
| Error | Fix |
|---|---|
| `the role 'PostgreSQL' was not found` | run from the folder containing `roles/` (or add `roles_path = ./roles` in ansible.cfg) |
| `Missing sudo password` | add `-K` |
| dpkg lock | `sudo killall apt apt-get; sudo dpkg --configure -a` |


## Result
The experiment was executed successfully and the output was verified.
