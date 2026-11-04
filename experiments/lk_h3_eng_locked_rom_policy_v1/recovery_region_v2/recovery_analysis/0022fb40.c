
/* prompt_and_wait_adb_install(RecoveryUI*, char const*) */

undefined8 prompt_and_wait_adb_install(RecoveryUI *param_1,char *param_2)

{
  long lVar1;
  bool bVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  long lVar5;
  bool bVar6;
  __pid_t __pid;
  int iVar7;
  __pid_t _Var8;
  int iVar9;
  uint uVar10;
  ulong uVar11;
  FILE *__stream;
  FILE *__stream_00;
  ssize_t sVar12;
  char *pcVar13;
  undefined4 *puVar14;
  ostream *poVar15;
  size_t sVar16;
  char *__nptr;
  int *piVar17;
  undefined8 uVar18;
  undefined4 uVar19;
  uint local_5ac;
  timeval local_5a8;
  void *local_598;
  char *local_590;
  char *pcStack_588;
  fd_set *local_580;
  stat *psStack_578;
  undefined8 local_570;
  stat local_190;
  fd_set local_100;
  long local_78;

  lVar1 = tpidr_el0;
  local_78 = *(long *)(lVar1 + 0x28);
  stop_adbd(param_1);
  set_usb_function(param_1,"adb");
  uVar11 = android::base::ShouldLog(4,0);
  if ((uVar11 & 1) != 0) {
    puVar14 = (undefined4 *)__errno();
    uVar19 = *puVar14;
    android::base::LogMessage::LogMessage
              ((LogMessage *)&local_590,"bootable/recovery/mi_adb_install.cpp",0x2e2,4,0,0xffffffff)
    ;
    poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_590);
    poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"Adb start sideload <filename> ",0x1e);
    sVar16 = strlen(param_2);
    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar15,param_2,sVar16)
    ;
    android::base::LogMessage::~LogMessage((LogMessage *)&local_590);
    *puVar14 = uVar19;
  }
  local_190.__unused[1] = 0;
  local_190.__unused[2] = 0;
  pipe((int *)(local_190.__unused + 2));
  pipe((int *)(local_190.__unused + 1));
  __pid = fork();
  if (__pid == 0) {
    local_580 = &local_100;
    psStack_578 = &local_190;
    local_100.fds_bits[1] = local_100.fds_bits[1] & 0xffffffffffff0000;
    local_100.fds_bits[0] = 0;
    local_190.st_ino = local_190.st_ino & 0xffffffffffff0000;
    local_190.st_dev = 0;
    local_590 = "/system/bin/recovery";
    pcStack_588 = "--adbd";
    local_570 = 0;
    sprintf((char *)&local_100,(char *)0xa,&DAT_00153c8f,(ulong)local_190.__unused[2]._4_4_);
    sprintf((char *)&local_190,(char *)0xa,&DAT_00153c8f,local_190.__unused[1] & 0xffffffff);
    close((int)local_190.__unused[2]);
    close(local_190.__unused[1]._4_4_);
    signal(1,handle_signal);
    prctl(1,1);
    execv("/system/bin/recovery",&local_590);
    uVar11 = android::base::ShouldLog(4,0);
    if ((uVar11 & 1) != 0) {
      piVar17 = (int *)__errno();
      iVar7 = *piVar17;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x2f8,4,0,
                 0xffffffff);
      poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
      poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"Can\'t run ",10);
      poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"/system/bin/recovery",0x14);
      poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,":",1);
      pcVar13 = strerror(*piVar17);
      sVar16 = strlen(pcVar13);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar15,pcVar13,sVar16);
      uVar11 = android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
      *piVar17 = iVar7;
    }
    if (*(long *)(lVar1 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
      _exit(-1);
    }
  }
  else {
    close(local_190.__unused[2]._4_4_);
    close((int)local_190.__unused[1]);
    local_5ac = 0;
    local_190.st_ino = 0;
    local_190.st_dev = 0;
    local_190.st_mode = 0;
    local_190.st_uid = 0;
    local_190.st_nlink = 0;
    local_190.st_rdev = 0;
    local_190.st_gid = 0;
    local_190.__pad0 = 0;
    local_190.st_blksize = 0;
    local_190.st_size = 0;
    local_190.st_atim.tv_sec = 0;
    local_190.st_blocks = 0;
    local_190.st_mtim.tv_sec = 0;
    local_190.st_atim.tv_nsec = 0;
    local_190.st_ctim.tv_sec = 0;
    local_190.st_mtim.tv_nsec = 0;
    local_190.__unused[0] = 0;
    local_190.st_ctim.tv_nsec = 0;
    __stream = fdopen(local_190.__unused[1]._4_4_,"wb");
    setlinebuf(__stream);
    memset(&local_590,0,0x400);
    __stream_00 = fdopen((int)local_190.__unused[2],"r");
    bVar2 = false;
LAB_0022fc44:
    do {
      lVar5 = local_190.__unused[2];
      iVar7 = (int)local_190.__unused[2];
      local_100.fds_bits[1] = 0;
      local_100.fds_bits[0] = 0;
      local_100.fds_bits[3] = 0;
      local_100.fds_bits[2] = 0;
      local_100.fds_bits[5] = 0;
      local_100.fds_bits[4] = 0;
      local_100.fds_bits[7] = 0;
      local_100.fds_bits[6] = 0;
      local_100.fds_bits[9] = 0;
      local_100.fds_bits[8] = 0;
      local_100.fds_bits[0xb] = 0;
      local_100.fds_bits[10] = 0;
      local_100.fds_bits[0xd] = 0;
      local_100.fds_bits[0xc] = 0;
      local_100.fds_bits[0xf] = 0;
      local_100.fds_bits[0xe] = 0;
      __FD_SET_chk(local_190.__unused[2] & 0xffffffff,&local_100,0x80);
      local_5a8.tv_sec = 0x3c;
      local_5a8.tv_usec = 0;
      iVar7 = select(iVar7 + 1,&local_100,(fd_set *)0x0,(fd_set *)0x0,&local_5a8);
      if (iVar7 < 1) {
        if (iVar7 != 0) {
          uVar11 = android::base::ShouldLog(4,0);
          if ((uVar11 & 1) != 0) {
            puVar14 = (undefined4 *)__errno();
            uVar19 = *puVar14;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x30f,4,0,
                       0xffffffff);
            poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar15,"Error read_state.",0x11);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
            *puVar14 = uVar19;
          }
          break;
        }
      }
      else {
        iVar7 = __FD_ISSET_chk(lVar5 & 0xffffffff,&local_100,0x80);
        if (iVar7 != 0) {
          pcVar13 = fgets((char *)&local_590,0x400,__stream_00);
          if (pcVar13 == (char *)0x0) {
            uVar18 = 0;
            goto LAB_00230cfc;
          }
          pcVar13 = strtok((char *)&local_590," \n");
          if (pcVar13 == (char *)0x0) {
            puts("Commond is NULL.");
            goto LAB_0022fc44;
          }
          iVar7 = strncmp(pcVar13,"install_package:",0x10);
          if (iVar7 == 0) {
            uVar11 = android::base::ShouldLog(4,0);
            if ((uVar11 & 1) != 0) {
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x31c,4,0,
                         0xffffffff);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"We will install package scan file",0x21);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
              *puVar14 = uVar19;
            }
            local_100.fds_bits[0] = 0;
            strtok_r(pcVar13,":",(char **)&local_100);
            pcVar13 = strtok_r((char *)0x0,":",(char **)&local_100);
            __nptr = strtok_r((char *)0x0,":",(char **)&local_100);
            uVar10 = atoi(__nptr);
            printf("Verifier msg :%s\nreboot : %d\n",pcVar13,(ulong)uVar10);
            iVar7 = 299;
            while (_Var8 = waitpid(__pid,(int *)&local_5ac,1), _Var8 == 0) {
              iVar9 = stat("/sideload/package.zip",&local_190);
              if (iVar9 == 0) {
                uVar11 = android::base::ShouldLog(4,0);
                if ((uVar11 & 1) != 0) {
                  puVar14 = (undefined4 *)__errno();
                  uVar19 = *puVar14;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x337,4
                             ,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"Start install package.",0x16);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
                  *puVar14 = uVar19;
                }
                local_5a8.tv_sec = local_5a8.tv_sec & 0xffffffffffffff00;
                iVar7 = (**(code **)(*(long *)param_1 + 0x88))
                                  (param_1,"/sideload/package.zip",&local_5a8,param_2,0,pcVar13,1);
joined_r0x0023023c:
                bVar6 = !bVar2;
                bVar2 = true;
                if (bVar6) {
                  stat("/sideload/exit",&local_190);
                  bVar2 = false;
                }
                goto LAB_00230254;
              }
              piVar17 = (int *)__errno();
              bVar6 = iVar7 == 0;
              iVar7 = iVar7 + -1;
              if ((bVar6) || (*piVar17 != 2)) {
                uVar11 = android::base::ShouldLog(4,0);
                if ((uVar11 & 1) != 0) {
                  iVar7 = *piVar17;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x331,4
                             ,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"Timed out waiting for package.",0x1e);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
                  *piVar17 = iVar7;
                }
                kill(__pid,9);
                iVar7 = 1;
                goto joined_r0x0023023c;
              }
              sleep(1);
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                iVar9 = *piVar17;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x32e,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"Install package is scaning",0x1a);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
                *piVar17 = iVar9;
              }
            }
            uVar11 = android::base::ShouldLog(4,0);
            if ((uVar11 & 1) == 0) {
              iVar7 = 1;
              bVar2 = true;
            }
            else {
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x327,4,0,
                         0xffffffff);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"Install package failed child aborted",0x24);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
              iVar7 = 1;
              bVar2 = true;
              *puVar14 = uVar19;
            }
LAB_00230254:
            if ((local_5ac & 0xff7f) != 0) {
              if ((local_5ac & 0xff00) == 0x300) {
                uVar11 = android::base::ShouldLog(4,0);
                if ((uVar11 & 1) != 0) {
                  puVar14 = (undefined4 *)__errno();
                  uVar19 = *puVar14;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x343,4
                             ,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"You need adb 1.0.32 or newer to sideload to this device.",0x38
                            );
LAB_00230970:
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
                  *puVar14 = uVar19;
                }
              }
              else if (((local_5ac + 1 & 0x7e) == 0) &&
                      (uVar11 = android::base::ShouldLog(4,0), (uVar11 & 1) != 0)) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x345,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                    (poVar15,"(Adbd status ",0xd);
                poVar15 = (ostream *)std::ostream::operator<<(poVar15,local_5ac >> 8 & 0xff);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,")",1);
                goto LAB_00230970;
              }
            }
            if (-1 < iVar7) {
              save_install_status(iVar7);
              if (iVar7 == 0) {
                uVar11 = android::base::ShouldLog(4,0);
                if ((uVar11 & 1) != 0) {
                  puVar14 = (undefined4 *)__errno();
                  uVar19 = *puVar14;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x34f,4
                             ,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"Install from ADB complete.",0x1a);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
                  *puVar14 = uVar19;
                }
                fwrite("Installation_complete.\n",0x17,1,__stream);
                if (uVar10 != 0) {
                  sleep(3);
                  set_reboot_message(0);
                  sync();
                  kill(__pid,9);
                  goto LAB_00230cf8;
                }
                (**(code **)(*(long *)param_1 + 0x118))(param_1,4);
              }
              else {
                (**(code **)(*(long *)param_1 + 0x118))(param_1,4);
                uVar11 = android::base::ShouldLog(4,0);
                if ((uVar11 & 1) != 0) {
                  puVar14 = (undefined4 *)__errno();
                  uVar19 = *puVar14;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_5a8,"bootable/recovery/mi_adb_install.cpp",0x34c,4
                             ,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_5a8);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"Installation aborted.",0x15);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_5a8);
                  *puVar14 = uVar19;
                }
                fwrite("Installation_aborted.\n",0x16,1,__stream);
              }
            }
            goto LAB_0022fc44;
          }
          iVar7 = strcmp(pcVar13,"format_data");
          if (iVar7 == 0) {
            uVar11 = android::base::ShouldLog(4,0);
            if ((uVar11 & 1) == 0) {
              uVar11 = disable_format();
            }
            else {
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x35d,4,0,
                         0xffffffff);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"format_data start",0x11);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
              *puVar14 = uVar19;
              uVar11 = disable_format();
            }
            if ((uVar11 & 1) != 0) goto LAB_00230440;
            iVar7 = (**(code **)(*(long *)param_1 + 0x70))(param_1);
            if (iVar7 == 0) {
              pcVar13 = "Format_data_success.\n";
              DAT_0042cfa0 = 1;
LAB_00230420:
              sVar16 = 0x15;
            }
            else {
              pcVar13 = "Format_data_failed.\n";
              sVar16 = 0x14;
            }
LAB_0023044c:
            fwrite(pcVar13,sVar16,1,__stream);
          }
          else {
            iVar7 = strcmp(pcVar13,"format_cache");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) == 0) {
                uVar11 = disable_format();
              }
              else {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x361,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"format_cache start",0x12);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
                uVar11 = disable_format();
              }
              if ((uVar11 & 1) != 0) goto LAB_00230440;
              local_100.fds_bits[2] = (__fd_mask)operator_new(0x20);
              *(char *)(local_100.fds_bits[2] + 0x1f) = '\0';
              uVar4 = s_vendor_wipecache_menu_confirmed_00143d93._23_8_;
              uVar3 = s_vendor_wipecache_menu_confirmed_00143d93._0_8_;
              local_5a8.tv_sec._0_5_ = (uint5)(uint)local_5a8.tv_sec;
              local_100.fds_bits[0] = (__fd_mask)&DAT_00000021;
              uVar18 = CONCAT71(s_vendor_wipecache_menu_confirmed_00143d93._16_7_,
                                s_vendor_wipecache_menu_confirmed_00143d93[0xf]);
              local_100.fds_bits[1] = 0x1f;
              *(ulong *)(local_100.fds_bits[2] + 8) =
                   CONCAT17(s_vendor_wipecache_menu_confirmed_00143d93[0xf],
                            s_vendor_wipecache_menu_confirmed_00143d93._8_7_);
              *(undefined8 *)local_100.fds_bits[2] = uVar3;
              *(undefined8 *)(local_100.fds_bits[2] + 0x17) = uVar4;
              *(undefined8 *)(local_100.fds_bits[2] + 0xf) = uVar18;
              local_5a8.tv_sec = CONCAT71(local_5a8.tv_sec._1_7_,6);
              *(undefined2 *)((ulong)&local_5a8 | 1) = 0x6579;
              *(undefined1 *)((undefined2 *)((ulong)&local_5a8 | 1) + 1) = 0x73;
              android::base::SetProperty((string *)&local_100,(string *)&local_5a8);
              if ((local_5a8.tv_sec & 1U) != 0) {
                operator_delete(local_598);
              }
              if ((local_100.fds_bits[0] & 1U) != 0) {
                operator_delete((void *)local_100.fds_bits[2]);
              }
              iVar7 = (**(code **)(*(long *)param_1 + 0x60))(param_1);
              if (iVar7 != 0) {
                pcVar13 = "Format_cache_failed.\n";
                goto LAB_00230420;
              }
              pcVar13 = "Format_cache_success.\n";
              sVar16 = 0x16;
              goto LAB_0023044c;
            }
            iVar7 = strcmp(pcVar13,"format_storage");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) == 0) {
                uVar11 = disable_format();
              }
              else {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x365,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"format_storage start",0x14);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
                uVar11 = disable_format();
              }
              if ((uVar11 & 1) != 0) goto LAB_00230440;
              iVar7 = (**(code **)(*(long *)param_1 + 0x78))(param_1);
              if (iVar7 == 0) {
                pcVar13 = "Format_storage_success.\n";
                sVar16 = 0x18;
              }
              else {
                pcVar13 = "Format_storage_failed.\n";
                sVar16 = 0x17;
              }
              goto LAB_0023044c;
            }
            iVar7 = strcmp(pcVar13,"format_data_and_storage");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) == 0) {
                uVar11 = disable_format();
                if ((uVar11 & 1) != 0) goto LAB_00230440;
LAB_002303d4:
                iVar7 = (**(code **)(*(long *)param_1 + 0x70))(param_1);
                iVar9 = (**(code **)(*(long *)param_1 + 0x78))(param_1);
                if (iVar9 == 0 && iVar7 == 0) {
                  pcVar13 = "Format_data_and_storage_success.\n";
                  sVar16 = 0x21;
                  goto LAB_0023044c;
                }
                pcVar13 = "Format_data_and_storage_failed.\n";
              }
              else {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x369,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"format_data_and_storage satrt",0x1d);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
                uVar11 = disable_format();
                if ((uVar11 & 1) == 0) goto LAB_002303d4;
LAB_00230440:
                pcVar13 = "Format_data_is_already_disable.\n";
              }
              sVar16 = 0x20;
              goto LAB_0023044c;
            }
            iVar7 = strcmp(pcVar13,"wipe_data_and_storage");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) == 0) {
                uVar11 = disable_format();
              }
              else {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x36d,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"wipe_data_and_storage start",0x1b);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
                uVar11 = disable_format();
              }
              if ((uVar11 & 1) != 0) goto LAB_00230440;
              iVar7 = (**(code **)(*(long *)param_1 + 0x68))(param_1,0);
              iVar9 = (**(code **)(*(long *)param_1 + 0x78))(param_1);
              if (iVar9 == 0 && iVar7 == 0) {
                pcVar13 = "Wipe_data_and_storage_success.\n";
                sVar16 = 0x1f;
              }
              else {
                pcVar13 = "Wipe_data_and_storage_failed.\n";
                sVar16 = 0x1e;
              }
              goto LAB_0023044c;
            }
            iVar7 = strcmp(pcVar13,"reboot");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x371,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"reboot: start",0xd);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
              }
              set_reboot_message(0);
              sync();
              fwrite("reboot.\n",8,1,__stream);
              local_100.fds_bits[0] = 0x65686361632f0c;
              ensure_path_unmounted((string *)&local_100);
              if ((local_100.fds_bits[0] & 1U) != 0) {
                operator_delete((void *)local_100.fds_bits[2]);
              }
              goto LAB_00230cf8;
            }
            iVar7 = strcmp(pcVar13,"rebootbootloader");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x37a,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"reboot:bootloader start",0x17);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
              }
              fwrite("reboot-bootloader.\n",0x13,1,__stream);
              uVar18 = 7;
              goto LAB_00230cfc;
            }
            iVar7 = strcmp(pcVar13,"rebootfastboot");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x37f,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"reboot:fastboot start",0x15);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
              }
              fwrite("reboot-fastboot.\n",0x11,1,__stream);
              uVar18 = 0xe;
              goto LAB_00230cfc;
            }
            iVar7 = strcmp(pcVar13,"rebootrecovery");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",900,4,0,
                           0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"reboot:recovery start",0x15);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
              }
              fwrite("reboot-recovery.\n",0x11,1,__stream);
              uVar18 = 0x12;
              goto LAB_00230cfc;
            }
            iVar7 = strcmp(pcVar13,"shutdown");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x389,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"shutdown: start",0xf);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
              }
              fwrite("shutdown.\n",10,1,__stream);
              uVar18 = 8;
              goto LAB_00230cfc;
            }
            iVar7 = strcmp(pcVar13,"wipe_efs");
            if (iVar7 == 0) {
              uVar11 = android::base::ShouldLog(4,0);
              if ((uVar11 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x38e,4,0
                           ,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"wipe_efs start",0xe);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                *puVar14 = uVar19;
              }
              wipe_efs(param_1,(__sFILE *)__stream);
            }
            else {
              iVar7 = strcmp(pcVar13,"enablemtp");
              if (iVar7 == 0) {
                uVar11 = android::base::ShouldLog(2,0);
                if ((uVar11 & 1) != 0) {
                  puVar14 = (undefined4 *)__errno();
                  uVar19 = *puVar14;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x392,2
                             ,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"enable mtp start",0x10);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                  *puVar14 = uVar19;
                }
                enable_mtp(param_1,(__sFILE *)__stream);
              }
              else {
                iVar7 = strcmp(pcVar13,"disablemtp");
                if (iVar7 == 0) {
                  uVar11 = android::base::ShouldLog(2,0);
                  if ((uVar11 & 1) != 0) {
                    puVar14 = (undefined4 *)__errno();
                    uVar19 = *puVar14;
                    android::base::LogMessage::LogMessage
                              ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x396
                               ,2,0,0xffffffff);
                    poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100)
                    ;
                    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar15,"disable mtp start",0x11);
                    android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                    *puVar14 = uVar19;
                  }
                  disable_mtp(param_1,(__sFILE *)__stream);
                }
                else {
                  iVar7 = strncmp(pcVar13,"mtpinstall:",0xb);
                  if (iVar7 == 0) {
                    uVar11 = android::base::ShouldLog(2,0);
                    if ((uVar11 & 1) != 0) {
                      puVar14 = (undefined4 *)__errno();
                      uVar19 = *puVar14;
                      android::base::LogMessage::LogMessage
                                ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",
                                 0x39a,2,0,0xffffffff);
                      poVar15 = (ostream *)
                                android::base::LogMessage::stream((LogMessage *)&local_100);
                      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar15,"mtp install start",0x11);
                      android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                      *puVar14 = uVar19;
                    }
                    mtp_install(param_1,(__sFILE *)__stream,pcVar13 + 0xb);
                  }
                  else {
                    iVar7 = strcmp(pcVar13,"isenable");
                    if (iVar7 == 0) {
                      uVar11 = android::base::ShouldLog(2,0);
                      if ((uVar11 & 1) != 0) {
                        puVar14 = (undefined4 *)__errno();
                        uVar19 = *puVar14;
                        android::base::LogMessage::LogMessage
                                  ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",
                                   0x39f,2,0,0xffffffff);
                        poVar15 = (ostream *)
                                  android::base::LogMessage::stream((LogMessage *)&local_100);
                        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar15,"judge whether the mtp is enabled",0x20);
                        android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                        *puVar14 = uVar19;
                      }
                      is_enabled((__sFILE *)__stream);
                    }
                    else {
                      iVar7 = strncmp(pcVar13,"format-frp:",0xb);
                      if (iVar7 == 0) {
                        uVar11 = android::base::ShouldLog(2,0);
                        if ((uVar11 & 1) != 0) {
                          puVar14 = (undefined4 *)__errno();
                          uVar19 = *puVar14;
                          android::base::LogMessage::LogMessage
                                    ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp"
                                     ,0x3a3,2,0,0xffffffff);
                          poVar15 = (ostream *)
                                    android::base::LogMessage::stream((LogMessage *)&local_100);
                          poVar15 = std::
                                    __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                              (poVar15,"format the partition frp:",0x19);
                          sVar16 = strlen(pcVar13);
                          poVar15 = std::
                                    __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                              (poVar15,pcVar13,sVar16);
                          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                    (poVar15,"\n",1);
                          android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                          *puVar14 = uVar19;
                        }
                        local_100.fds_bits[0] = 0;
                        strtok_r(pcVar13,":",(char **)&local_100);
                        format_frp(param_1,(__sFILE *)__stream,(uchar *)local_100.fds_bits[0]);
                      }
                      else {
                        iVar7 = strcmp(pcVar13,"getmitoken");
                        if (iVar7 != 0) {
                          iVar7 = strncmp(pcVar13,"printlog:",9);
                          if (iVar7 == 0) {
                            printf("miassistant: %s\n",pcVar13 + 9);
                          }
                          else {
                            fprintf(__stream,"%s\n",pcVar13);
                            uVar11 = android::base::ShouldLog(2,0);
                            if ((uVar11 & 1) != 0) {
                              puVar14 = (undefined4 *)__errno();
                              uVar19 = *puVar14;
                              android::base::LogMessage::LogMessage
                                        ((LogMessage *)&local_100,
                                         "bootable/recovery/mi_adb_install.cpp",0x3b1,2,0,0xffffffff
                                        );
                              poVar15 = (ostream *)
                                        android::base::LogMessage::stream((LogMessage *)&local_100);
                              poVar15 = std::
                                        __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                                  (poVar15,"unknown command [",0x11);
                              sVar16 = strlen(pcVar13);
                              poVar15 = std::
                                        __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                                  (poVar15,pcVar13,sVar16);
                              std::
                              __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                        (poVar15,"]",1);
                              android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                              *puVar14 = uVar19;
                            }
                          }
                          goto LAB_0022fc44;
                        }
                        uVar11 = android::base::ShouldLog(2,0);
                        if ((uVar11 & 1) != 0) {
                          puVar14 = (undefined4 *)__errno();
                          uVar19 = *puVar14;
                          android::base::LogMessage::LogMessage
                                    ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp"
                                     ,0x3a9,2,0,0xffffffff);
                          poVar15 = (ostream *)
                                    android::base::LogMessage::stream((LogMessage *)&local_100);
                          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                    (poVar15,"get mitoken",0xb);
                          android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
                          *puVar14 = uVar19;
                        }
                        transport_ersfrp_verity_message((__sFILE *)__stream);
                      }
                    }
                  }
                }
              }
            }
          }
          (**(code **)(*(long *)param_1 + 0x118))(param_1,4);
          goto LAB_0022fc44;
        }
      }
      iVar7 = __open_2("/sys/class/android_usb/android0/state",0);
      if (iVar7 < 0) {
        piVar17 = (int *)__errno();
        pcVar13 = strerror(*piVar17);
        printf("failed to open /sys/class/android_usb/android0/state: %s\n",pcVar13);
        break;
      }
      local_100.fds_bits[0] = local_100.fds_bits[0] & 0xffffffffffffff00;
      sVar12 = read(iVar7,&local_100,1);
      bVar6 = local_100.fds_bits[0]._0_1_ == (LogMessage)0x43;
      iVar7 = close(iVar7);
      if (iVar7 < 0) {
        piVar17 = (int *)__errno();
        pcVar13 = strerror(*piVar17);
        printf("failed to close /sys/class/android_usb/android0/state: %s\n",pcVar13);
      }
    } while (sVar12 == 1 && bVar6);
    uVar11 = android::base::ShouldLog(4,0);
    if ((uVar11 & 1) != 0) {
      puVar14 = (undefined4 *)__errno();
      uVar19 = *puVar14;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_100,"bootable/recovery/mi_adb_install.cpp",0x310,4,0,
                 0xffffffff);
      poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_100);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar15,
                 "Time out after WAIT_COMMENT_TIMEOUT_SEC, unless a USB cable is  plugged in.",0x4b)
      ;
      android::base::LogMessage::~LogMessage((LogMessage *)&local_100);
      *puVar14 = uVar19;
    }
    set_reboot_message(0);
    sync();
LAB_00230cf8:
    uVar18 = 1;
LAB_00230cfc:
    fclose(__stream);
    uVar10 = fclose(__stream_00);
    uVar11 = (ulong)uVar10;
    if (*(long *)(lVar1 + 0x28) == local_78) {
      return uVar18;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar11);
}
