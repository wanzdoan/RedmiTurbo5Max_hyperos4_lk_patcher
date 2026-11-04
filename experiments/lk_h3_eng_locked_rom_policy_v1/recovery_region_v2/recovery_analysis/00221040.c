
undefined8 main(uint param_1,char **param_2)

{
  ulong __n;
  string sVar1;
  long lVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined *puVar5;
  undefined *puVar6;
  bootloader_message bVar7;
  bool bVar8;
  undefined1 uVar9;
  int iVar10;
  int iVar11;
  uint uVar12;
  __pid_t _Var13;
  bootloader_message *pbVar14;
  ulong uVar15;
  char *pcVar16;
  size_t sVar17;
  undefined8 *puVar18;
  string *psVar19;
  istream *piVar20;
  __tree_node_base **pp_Var21;
  int *piVar22;
  code *pcVar23;
  RecoveryUI *this;
  long *plVar24;
  string *psVar25;
  FILE *__stream;
  FILE *__stream_00;
  __ssize_t _Var26;
  long lVar27;
  selabel_handle *psVar28;
  undefined4 *puVar29;
  ostream *poVar30;
  __tree_node_base *p_Var31;
  string *extraout_x1;
  char *pcVar32;
  long lVar33;
  undefined8 uVar34;
  string *psVar35;
  filebuf *this_00;
  string *psVar36;
  string *psVar37;
  ulong uVar38;
  undefined4 uVar39;
  void *pvVar40;
  __tree_node_base *p_Var41;
  int *piVar42;
  void *unaff_x28;
  undefined8 uVar43;
  double dVar44;
  undefined1 auVar45 [16];
  void *local_b00;
  void *local_af0;
  LogMessage aLStack_ad8 [8];
  int local_ad0;
  int local_acc;
  string *local_ac8;
  string *local_ac0;
  string *local_ab8;
  ulong local_ab0;
  ulong uStack_aa8;
  void *local_aa0;
  time_t local_a90;
  undefined1 local_a81;
  ulong local_a80;
  ulong uStack_a78;
  void *local_a70;
  ulong local_a68;
  ulong uStack_a60;
  void *local_a58;
  ulong local_a50;
  ulong uStack_a48;
  long *local_a40;
  string *local_a38;
  string *local_a30;
  char *local_a28;
  size_t local_a20;
  ulong uStack_a18;
  long *local_a10;
  char *local_a00;
  char *local_9f8;
  void *local_9f0;
  __tree_end_node *local_9e8;
  string local_9e0;
  undefined7 uStack_9df;
  char cStack_9d8;
  undefined7 uStack_9d7;
  char local_9d0;
  undefined2 uStack_9cf;
  undefined5 uStack_9cd;
  undefined8 uStack_9c8;
  string *psStack_9c0;
  undefined8 local_9b8;
  undefined8 uStack_9b0;
  undefined8 local_9a8;
  undefined8 uStack_9a0;
  undefined8 local_998;
  undefined8 uStack_990;
  undefined8 local_988;
  undefined8 uStack_980;
  undefined8 local_978;
  undefined8 uStack_970;
  undefined8 local_968;
  undefined8 uStack_960;
  undefined8 local_958;
  undefined8 uStack_950;
  undefined8 local_948;
  undefined8 uStack_940;
  undefined8 local_938;
  undefined8 uStack_930;
  undefined8 local_928;
  undefined **ppuStack_920;
  undefined8 local_918;
  undefined8 uStack_910;
  undefined8 local_908;
  undefined8 uStack_900;
  undefined8 local_8f8;
  undefined8 uStack_8f0;
  undefined8 local_8e8;
  undefined8 uStack_8e0;
  undefined8 local_8d8;
  undefined8 uStack_8d0;
  undefined8 local_8c8;
  undefined8 uStack_8c0;
  undefined8 uStack_8b8;
  undefined8 uStack_8b0;
  undefined8 local_8a8;
  undefined8 uStack_8a0;
  undefined8 local_898;
  undefined8 local_890;
  bootloader_message local_880;
  char cStack_87f;
  undefined2 uStack_87e;
  int iStack_87c;
  char cStack_878;
  undefined7 uStack_877;
  char local_870;
  undefined2 uStack_86f;
  undefined5 uStack_86d;
  long lStack_868;
  bootloader_message *local_860;
  undefined8 uStack_858;
  undefined8 uStack_850;
  undefined8 uStack_848;
  char local_840 [767];
  undefined1 local_541;
  char acStack_540 [1224];
  long local_78;

  lVar2 = tpidr_el0;
  this_00 = (filebuf *)(ulong)param_1;
  local_78 = *(long *)(lVar2 + 0x28);
  local_860 = &local_880;
  cStack_878 = -0x60;
  uStack_877 = 0x2248;
  local_880 = (bootloader_message)0x80;
  cStack_87f = 'A';
  uStack_87e = 0x42;
  iStack_87c = 0;
  local_9e0 = (string)0xc0;
  uStack_9df = 0x427b;
  cStack_9d8 = '\x10';
  uStack_9d7 = 0x40c4;
  psStack_9c0 = &local_9e0;
  android::base::InitLogging(param_2,&local_880,&local_9e0);
  if (psStack_9c0 == &local_9e0) {
    lVar33 = 4;
    psVar25 = &local_9e0;
LAB_002210ec:
    (**(code **)(*(long *)psVar25 + lVar33 * 8))();
  }
  else if (psStack_9c0 != (string *)0x0) {
    lVar33 = 5;
    psVar25 = psStack_9c0;
    goto LAB_002210ec;
  }
  if (local_860 == &local_880) {
    lVar33 = 4;
    pbVar14 = &local_880;
LAB_0022111c:
    (**(code **)(*(long *)pbVar14 + lVar33 * 8))();
  }
  else if (local_860 != (bootloader_message *)0x0) {
    lVar33 = 5;
    pbVar14 = local_860;
    goto LAB_0022111c;
  }
  printf("%d\n",0);
  local_a81 = 0;
  __android_log_pmsg_file_read(3,4,"recovery/",logbasename,&local_a81);
  __android_log_pmsg_file_read(3,4,"recovery/",logrotate,&local_a81);
  load_volume_table();
  if ((param_1 == 4) && (iVar10 = strcmp(param_2[1],"--adbd"), iVar10 == 0)) {
    iVar10 = atoi(param_2[2]);
    iVar11 = atoi(param_2[3]);
    minadbd_main(iVar10,iVar11);
    uVar34 = 0;
  }
  else {
    local_a90 = 0;
    local_a90 = time((time_t *)0x0);
    lVar33 = Paths::Get();
    if ((*(byte *)(lVar33 + 0x90) & 1) == 0) {
      uVar43 = *(undefined8 *)(lVar33 + 0x90);
      uVar34 = *(undefined8 *)(lVar33 + 0xa0);
      cStack_9d8 = (char)*(undefined8 *)(lVar33 + 0x98);
      uStack_9d7 = (undefined7)((ulong)*(undefined8 *)(lVar33 + 0x98) >> 8);
      local_9e0 = SUB81(uVar43,0);
      uStack_9df = (undefined7)((ulong)uVar43 >> 8);
      local_9d0 = (char)uVar34;
      uStack_9cf = (undefined2)((ulong)uVar34 >> 8);
      uStack_9cd = (undefined5)((ulong)uVar34 >> 0x18);
    }
    else {
      std::string::__init_copy_ctor_external
                (&local_9e0,*(char **)(lVar33 + 0xa0),*(ulong *)(lVar33 + 0x98));
    }
    local_880 = (bootloader_message)0x0;
    cStack_87f = '\0';
    uStack_87e = 0;
    iStack_87c = 0;
    pcVar16 = (char *)((ulong)&local_9e0 | 1);
    if (((byte)local_9e0 & 1) != 0) {
      pcVar16 = (char *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0));
    }
    local_a38 = (string *)CONCAT44(local_a38._4_4_,0xffffffff);
    local_a50 = CONCAT44(local_a50._4_4_,0xffffffff);
    iVar10 = pipe2((int *)&local_880,0x80000);
    puVar5 = PTR_android_fdsan_close_with_tag_00429ae8;
    if (iVar10 == 0) {
      android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                ((unique_fd_impl<android::base::DefaultCloser> *)&local_a38,
                 CONCAT22(uStack_87e,CONCAT11(cStack_87f,local_880)),(void *)0x0);
      android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                ((unique_fd_impl<android::base::DefaultCloser> *)&local_a50,iStack_87c,(void *)0x0);
      _Var13 = fork();
      if (_Var13 == -1) {
        uVar15 = android::base::ShouldLog(4,0);
        if ((uVar15 & 1) != 0) {
          puVar29 = (undefined4 *)__errno();
          uVar39 = *puVar29;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x122,4,0,uVar39
                    );
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30,"Failed to fork for redirecting stdio",0x24);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
          *puVar29 = uVar39;
        }
        puVar6 = PTR_stdout_00429b30;
        freopen(pcVar16,"a",*(FILE **)PTR_stdout_00429b30);
        setbuf(*(FILE **)puVar6,(char *)0x0);
        puVar18 = (undefined8 *)PTR_stderr_00429b38;
        freopen(pcVar16,"a",*(FILE **)PTR_stderr_00429b38);
        goto LAB_00221364;
      }
      if (_Var13 != 0) {
        puVar29 = (undefined4 *)__errno();
        psVar25 = local_a38;
        uVar39 = *puVar29;
        if ((int)local_a38 != -1) {
          if (puVar5 == (undefined *)0x0) {
            close((int)local_a38);
          }
          else {
            uVar34 = android_fdsan_create_owner_tag(3,&local_a38);
            android_fdsan_close_with_tag((ulong)psVar25 & 0xffffffff,uVar34);
          }
        }
        puVar6 = PTR_stdout_00429b30;
        local_a38 = (string *)CONCAT44(local_a38._4_4_,0xffffffff);
        *puVar29 = uVar39;
        setbuf(*(FILE **)puVar6,(char *)0x0);
        setbuf(*(FILE **)PTR_stderr_00429b38,(char *)0x0);
        iVar10 = dup2((int)local_a50,1);
        if ((iVar10 == -1) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
          uVar39 = *puVar29;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x15e,4,0,uVar39
                    );
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30,"dup2 stdout failed",0x12);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
          *puVar29 = uVar39;
        }
        iVar10 = dup2((int)local_a50,2);
        if ((iVar10 == -1) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
          uVar39 = *puVar29;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x161,4,0,uVar39
                    );
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30,"dup2 stderr failed",0x12);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
          *puVar29 = uVar39;
        }
        goto LAB_00221370;
      }
LAB_002231dc:
      android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                ((unique_fd_impl<android::base::DefaultCloser> *)&local_a50,-1,(void *)0x0);
      lVar33 = std::chrono::steady_clock::now();
      __stream = fopen(pcVar16,"ae");
      if (__stream == (FILE *)0x0) {
        uVar15 = android::base::ShouldLog(4,0);
        if ((uVar15 & 1) != 0) {
          puVar29 = (undefined4 *)__errno();
          uVar39 = *puVar29;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x137,4,0,uVar39
                    );
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,"fopen \"",7);
          sVar17 = strlen(pcVar16);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,pcVar16,sVar17);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30,"\" failed",8);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
          *puVar29 = uVar39;
        }
        if (*(long *)(lVar2 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
          _exit(1);
        }
      }
      else {
        __stream_00 = (FILE *)android::base::Fdopen((unique_fd_impl *)&local_a38,"r");
        if (__stream_00 == (FILE *)0x0) {
          uVar15 = android::base::ShouldLog(4,0);
          if ((uVar15 & 1) != 0) {
            puVar29 = (undefined4 *)__errno();
            uVar39 = *puVar29;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x13d,4,0,
                       uVar39);
            poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar30,"fdopen failed",0xd);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
            *puVar29 = uVar39;
          }
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_880,pcVar16);
          check_and_fclose((__sFILE *)__stream,(string *)&local_880);
          if (((byte)local_880 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
          }
          if (*(long *)(lVar2 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
            _exit(1);
          }
        }
        else {
          local_a00 = (char *)0x0;
          local_a20 = 0;
          while (_Var26 = getline(&local_a00,&local_a20,__stream_00), _Var26 != -1) {
            lVar27 = std::chrono::steady_clock::now();
            dVar44 = (double)(lVar27 - lVar33) / 1000000000.0;
            if (*local_a00 == '\n') {
              fprintf(__stream,"[%12.6lf]\n",dVar44);
            }
            else {
              fprintf(__stream,"[%12.6lf] %s",dVar44);
            }
            fflush(__stream);
          }
          uVar15 = android::base::ShouldLog(4,0);
          if ((uVar15 & 1) != 0) {
            puVar29 = (undefined4 *)__errno();
            uVar39 = *puVar29;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x150,4,0,
                       uVar39);
            poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar30,"getline failed",0xe);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
            *puVar29 = uVar39;
          }
          fclose(__stream_00);
          free(local_a00);
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_880,pcVar16);
          check_and_fclose((__sFILE *)__stream,(string *)&local_880);
          if (((byte)local_880 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
          }
          if (*(long *)(lVar2 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
            _exit(1);
          }
        }
      }
      goto LAB_0022489c;
    }
    uVar15 = android::base::ShouldLog(4,0);
    if ((uVar15 & 1) != 0) {
      puVar29 = (undefined4 *)__errno();
      uVar39 = *puVar29;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x114,4,0,uVar39);
      poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar30,"Failed to create pipe for redirecting stdio",0x2b);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
      *puVar29 = uVar39;
    }
    puVar6 = PTR_stdout_00429b30;
    freopen(pcVar16,"a",*(FILE **)PTR_stdout_00429b30);
    setbuf(*(FILE **)puVar6,(char *)0x0);
    puVar18 = (undefined8 *)PTR_stderr_00429b38;
    freopen(pcVar16,"a",*(FILE **)PTR_stderr_00429b38);
LAB_00221364:
    setbuf((FILE *)*puVar18,(char *)0x0);
LAB_00221370:
    pcVar16 = (char *)__errno();
    uVar15 = local_a50;
    uVar39 = *(undefined4 *)pcVar16;
    if ((int)local_a50 != -1) {
      if (puVar5 == (undefined *)0x0) {
        close((int)local_a50);
      }
      else {
        uVar34 = android_fdsan_create_owner_tag(3,&local_a50);
        android_fdsan_close_with_tag(uVar15 & 0xffffffff,uVar34);
      }
    }
    psVar25 = local_a38;
    *(undefined4 *)pcVar16 = uVar39;
    if ((int)local_a38 != -1) {
      if (puVar5 == (undefined *)0x0) {
        close((int)local_a38);
      }
      else {
        uVar34 = android_fdsan_create_owner_tag(3,&local_a38);
        android_fdsan_close_with_tag((ulong)psVar25 & 0xffffffff,uVar34);
      }
    }
    *(undefined4 *)pcVar16 = uVar39;
    if (((byte)local_9e0 & 1) != 0) {
      operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
    }
    local_ab0 = 0;
    uStack_aa8 = 0;
    local_aa0 = (void *)0x0;
    local_ac8 = (string *)0x0;
    local_ac0 = (string *)0x0;
    local_ab8 = (string *)0x0;
    if ((int)param_1 < 1) {
      do {
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x6b,6,0,
                   0xffffffff);
        poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,"Check failed: ",0xe);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,"argc",4);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30," ",1);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,">",1);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30," ",1);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,"0",1);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30," (argc=",7);
        poVar30 = (ostream *)std::ostream::operator<<(poVar30,param_1);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,", 0=",4);
        poVar30 = (ostream *)std::ostream::operator<<(poVar30,0);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar30,") ",2);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
      } while( true );
    }
    memset(&local_880,0,0x800);
    local_a00 = (char *)0x0;
    local_9f8 = (char *)0x0;
    local_9f0 = (void *)0x0;
    uVar15 = read_bootloader_message(&local_880,(string *)&local_a00);
    if ((uVar15 & 1) == 0) {
      uVar15 = android::base::ShouldLog(4,0);
      if ((uVar15 & 1) != 0) {
        uVar39 = *(undefined4 *)pcVar16;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_9e0,"bootable/recovery/recovery_main.cpp",0x70,4,0,
                   0xffffffff);
        poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_9e0);
        std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                  (poVar30,(string *)&local_a00);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_9e0);
        *(undefined4 *)pcVar16 = uVar39;
      }
      memset(&local_880,0,0x800);
    }
    sVar17 = strlen(acStack_540);
    if (0xffffffffffffffef < sVar17) {
LAB_00222fa8:
      if (*(long *)(lVar2 + 0x28) != local_78) goto LAB_0022489c;
      std::string::__throw_length_error_abi_ne180000_();
LAB_00222fc4:
      if (*(long *)(lVar2 + 0x28) == local_78) {
        std::string::__throw_length_error_abi_ne180000_();
        goto LAB_00222fe0;
      }
      goto LAB_0022489c;
    }
    if (sVar17 < 0x17) {
      pvVar40 = (void *)((ulong)&local_9e0 | 1);
      local_9e0 = SUB41((int)sVar17 << 1,0);
      if (sVar17 != 0) goto LAB_002214b8;
    }
    else {
      uVar15 = (sVar17 | 0xf) + 1;
      pvVar40 = operator_new(uVar15);
      local_9e0 = (string)((byte)uVar15 | 1);
      cStack_9d8 = (char)sVar17;
      uStack_9d7 = (undefined7)(sVar17 >> 8);
      local_9d0 = (char)pvVar40;
      uStack_9cf = (undefined2)((ulong)pvVar40 >> 8);
      uStack_9cd = (undefined5)((ulong)pvVar40 >> 0x18);
      uStack_9df = (undefined7)(uVar15 >> 8);
LAB_002214b8:
      memcpy(pvVar40,acStack_540,sVar17);
    }
    *(undefined1 *)((long)pvVar40 + sVar17) = 0;
    if ((local_ab0 & 1) != 0) {
      operator_delete(local_aa0);
    }
    uStack_aa8 = CONCAT71(uStack_9d7,cStack_9d8);
    local_ab0 = CONCAT71(uStack_9df,local_9e0);
    local_a20 = 0;
    uStack_a18 = 0;
    local_aa0 = (void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0));
    local_a10 = (long *)0x0;
    if (local_880 == (bootloader_message)0x0) {
LAB_002215d8:
    }
    else {
      pvVar40 = memchr(&local_880,0,0x20);
      if (pvVar40 == (void *)0x0) {
        local_a10 = operator_new(0x30);
        *(char *)(local_a10 + 4) = '\0';
        local_a20 = 0x31;
        uStack_a18 = 0x20;
        local_a10[1] = CONCAT71(uStack_877,cStack_878);
        *local_a10 = CONCAT44(iStack_87c,CONCAT22(uStack_87e,CONCAT11(cStack_87f,local_880)));
        local_a10[3] = lStack_868;
        local_a10[2] = CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870));
      }
      else {
        sVar17 = strlen((char *)&local_880);
        if (0xffffffffffffffef < sVar17) {
          if (*(long *)(lVar2 + 0x28) != local_78) goto LAB_0022489c;
          std::string::__throw_length_error_abi_ne180000_();
          goto LAB_002231dc;
        }
        if (sVar17 < 0x17) {
          pvVar40 = (void *)((ulong)&local_9e0 | 1);
          local_9e0 = SUB41((int)sVar17 << 1,0);
          if (sVar17 != 0) goto LAB_00221590;
        }
        else {
          uVar15 = (sVar17 | 0xf) + 1;
          pvVar40 = operator_new(uVar15);
          local_9e0 = (string)((byte)uVar15 | 1);
          cStack_9d8 = (char)sVar17;
          uStack_9d7 = (undefined7)(sVar17 >> 8);
          local_9d0 = (char)pvVar40;
          uStack_9cf = (undefined2)((ulong)pvVar40 >> 8);
          uStack_9cd = (undefined5)((ulong)pvVar40 >> 0x18);
          uStack_9df = (undefined7)(uVar15 >> 8);
LAB_00221590:
          memcpy(pvVar40,&local_880,sVar17);
        }
        *(undefined1 *)((long)pvVar40 + sVar17) = 0;
        if ((local_a20 & 1) != 0) {
          operator_delete(local_a10);
        }
        local_a10 = (long *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0));
        uStack_a18 = CONCAT71(uStack_9d7,cStack_9d8);
        local_a20 = CONCAT71(uStack_9df,local_9e0);
      }
      uVar15 = android::base::ShouldLog(2,0);
      if ((uVar15 & 1) == 0) goto LAB_002215d8;
      uVar39 = *(undefined4 *)pcVar16;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_9e0,"bootable/recovery/recovery_main.cpp",0x7f,2,0,0xffffffff)
      ;
      poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_9e0);
      poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar30,"Boot command: ",0xe);
      std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                (poVar30,(string *)&local_a20);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_9e0);
      *(undefined4 *)pcVar16 = uVar39;
    }
    if ((char)local_860 != '\0') {
      puVar18 = operator_new(0x30);
      *(undefined1 *)(puVar18 + 4) = 0;
      cStack_9d8 = 0x20;
      uStack_9d7 = 0;
      local_9d0 = (char)puVar18;
      uStack_9cf = (undefined2)((ulong)puVar18 >> 8);
      uStack_9cd = (undefined5)((ulong)puVar18 >> 0x18);
      local_9e0 = (string)0x31;
      uStack_9df = 0;
      puVar18[1] = uStack_858;
      *puVar18 = local_860;
      puVar18[3] = uStack_848;
      puVar18[2] = uStack_850;
      uVar15 = android::base::ShouldLog(2,0);
      if ((uVar15 & 1) != 0) {
        uVar39 = *(undefined4 *)pcVar16;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_a38,"bootable/recovery/recovery_main.cpp",0x84,2,0,
                   0xffffffff);
        poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a38);
        poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,"Boot status: ",0xd);
        std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                  (poVar30,&local_9e0);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_a38);
        *(undefined4 *)pcVar16 = uVar39;
      }
      if (((byte)local_9e0 & 1) != 0) {
        operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
      }
    }
    uVar15 = (ulong)param_1;
    local_ac8 = (string *)0x0;
    local_ac0 = (string *)0x0;
    local_ab8 = (string *)0x0;
    local_9e0 = SUB81(&local_ac8,0);
    uStack_9df = (undefined7)((ulong)&local_ac8 >> 8);
    cStack_9d8 = '\0';
    uStack_9d7 = 0;
    local_ac8 = operator_new((uVar15 + (ulong)param_1 * 2) * 8);
    psVar25 = (string *)(param_2 + uVar15);
    local_ab8 = local_ac8 + uVar15 * 0x18;
    local_ac0 = local_ac8;
    local_ac0 = std::
                __uninitialized_allocator_copy_impl_abi_ne180000_<std::allocator<std::string>,char**,char**,std::string*>
                          ((allocator *)&local_ab8,param_2,(char **)psVar25,local_ac8);
    if ((long)local_ac0 - (long)local_ac8 == 0x18) {
      local_541 = 0;
      local_9e0 = (string)0x0;
      uStack_9df = 0;
      cStack_9d8 = '\0';
      uStack_9d7 = 0;
      local_9d0 = '\0';
      uStack_9cf = 0;
      uStack_9cd = 0;
      psVar19 = (string *)strlen(local_840);
      if ((string *)0xffffffffffffffef < psVar19) goto LAB_00222fc4;
      if (psVar19 < (string *)0x17) {
        pvVar40 = (void *)((ulong)&local_9e0 | 1);
        local_9e0 = SUB41((int)psVar19 << 1,0);
        if (psVar19 != (string *)0x0) goto LAB_002216ec;
      }
      else {
        uVar15 = ((ulong)psVar19 | 0xf) + 1;
        pvVar40 = operator_new(uVar15);
        local_9e0 = (string)((byte)uVar15 | 1);
        cStack_9d8 = (char)psVar19;
        uStack_9d7 = (undefined7)((ulong)psVar19 >> 8);
        local_9d0 = (char)pvVar40;
        uStack_9cf = (undefined2)((ulong)pvVar40 >> 8);
        uStack_9cd = (undefined5)((ulong)pvVar40 >> 0x18);
        uStack_9df = (undefined7)(uVar15 >> 8);
LAB_002216ec:
        psVar25 = psVar19;
        memcpy(pvVar40,local_840,(size_t)psVar19);
      }
      *(string *)((long)pvVar40 + (long)psVar19) = (string)0x0;
      local_a38 = (string *)0x0;
      local_a30 = (string *)0x0;
      local_a28 = (void *)0x0;
      local_a50 = CONCAT53(local_a50._3_5_,0xa02);
      android::base::Split((base *)&local_9e0,(string *)&local_a50,psVar25);
      if ((local_a50 & 1) != 0) {
        operator_delete(local_a40);
      }
      if (local_a38 == local_a30) {
LAB_00221784:
        if ((local_840[0] != '\0') && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
          uVar39 = *(undefined4 *)pcVar16;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_a50,"bootable/recovery/recovery_main.cpp",0x95,4,0,
                     0xffffffff);
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a50);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,"Bad boot message: \"",0x13);
          poVar30 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                    operator<(poVar30,&local_9e0);
          psVar25 = (string *)0x1;
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar30,"\"",1);
LAB_00222d5c:
          android::base::LogMessage::~LogMessage((LogMessage *)&local_a50);
          *(undefined4 *)pcVar16 = uVar39;
        }
      }
      else {
        sVar1 = *local_a38;
        uVar15 = (ulong)((byte)sVar1 >> 1);
        if (((byte)sVar1 & 1) != 0) {
          uVar15 = *(ulong *)(local_a38 + 8);
        }
        if (uVar15 != 8) goto LAB_00221784;
        psVar19 = *(string **)(local_a38 + 0x10);
        if (((byte)sVar1 & 1) == 0) {
          psVar19 = local_a38 + 1;
        }
        psVar37 = local_a30;
        psVar36 = local_a38;
        if (*(long *)psVar19 != 0x797265766f636572) goto LAB_00221784;
        while (psVar19 = psVar36, psVar36 = psVar19 + 0x18, psVar36 != psVar37) {
          bVar8 = ((byte)*psVar36 & 1) != 0;
          uVar15 = (ulong)((byte)*psVar36 >> 1);
          if (bVar8) {
            uVar15 = *(ulong *)(psVar19 + 0x20);
          }
          if (uVar15 != 0) {
            psVar35 = *(string **)(psVar19 + 0x28);
            if (!bVar8) {
              psVar35 = psVar19 + 0x19;
            }
            if (*psVar35 != (string)0x0) {
              if (local_ac0 < local_ab8) {
                uVar43 = *(undefined8 *)(psVar19 + 0x20);
                uVar34 = *(undefined8 *)psVar36;
                *(undefined8 *)(local_ac0 + 0x10) = *(undefined8 *)(psVar19 + 0x28);
                *(undefined8 *)(local_ac0 + 8) = uVar43;
                *(undefined8 *)local_ac0 = uVar34;
                *(undefined8 *)(psVar19 + 0x20) = 0;
                *(undefined8 *)(psVar19 + 0x28) = 0;
                *(undefined8 *)psVar36 = 0;
                psVar37 = local_a30;
                local_ac0 = local_ac0 + 0x18;
              }
              else {
                local_ac0 = std::vector<std::string,std::allocator<std::string>>::
                            __push_back_slow_path<std::string>
                                      ((vector<std::string,std::allocator<std::string>> *)&local_ac8
                                       ,psVar36);
                psVar37 = local_a30;
              }
            }
          }
        }
        uVar15 = android::base::ShouldLog(2,0);
        if ((uVar15 & 1) != 0) {
          uVar39 = *(undefined4 *)pcVar16;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_a50,"bootable/recovery/recovery_main.cpp",0x93,2,0,
                     0xffffffff);
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a50);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,"Got ",4);
          poVar30 = (ostream *)
                    std::ostream::operator<<
                              (poVar30,((long)local_ac0 - (long)local_ac8 >> 3) *
                                       -0x5555555555555555);
          psVar25 = (string *)0x1c;
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30," arguments from boot message",0x1c);
          goto LAB_00222d5c;
        }
      }
      psVar19 = local_a38;
      psVar37 = local_a30;
      if (local_a38 != (string *)0x0) {
        while (psVar36 = psVar37, psVar36 != psVar19) {
          psVar37 = psVar36 + -0x18;
          if (((byte)*psVar37 & 1) != 0) {
            operator_delete(*(void **)(psVar36 + -8));
          }
        }
        local_a30 = psVar19;
        operator_delete(local_a38);
      }
      if (((byte)local_9e0 & 1) != 0) {
        operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
      }
    }
    if (((long)local_ac0 - (long)local_ac8 == 0x18) && (uVar15 = HasCache(), (uVar15 & 1) != 0)) {
      local_9e0 = (string)0x0;
      uStack_9df = 0;
      cStack_9d8 = '\0';
      uStack_9d7 = 0;
      local_9d0 = '\0';
      uStack_9cf = 0;
      uStack_9cd = 0;
      local_a28 = operator_new(0x20);
      local_a28[0x17] = '\0';
      uVar43 = s__cache_recovery_command_0014b87e._0_8_;
      lVar33 = CONCAT17(s__cache_recovery_command_0014b87e[0xf],
                        s__cache_recovery_command_0014b87e._8_7_);
      uVar34 = CONCAT71(s__cache_recovery_command_0014b87e._16_7_,
                        s__cache_recovery_command_0014b87e[0xf]);
      local_a30 = (string *)0x17;
      local_a38 = (string *)&DAT_00000021;
      *(long *)(local_a28 + 8) = lVar33;
      *(undefined8 *)local_a28 = uVar43;
      *(undefined8 *)(local_a28 + 0xf) = uVar34;
      iVar10 = ensure_path_mounted((string *)&local_a38);
      if (iVar10 == 0) {
        local_a40 = operator_new(0x20);
        *(char *)((long)local_a40 + 0x17) = '\0';
        local_a50 = 0x21;
        local_a40[1] = lVar33;
        *local_a40 = uVar43;
        uStack_a48 = 0x17;
        *(undefined8 *)((long)local_a40 + 0xf) = uVar34;
        psVar25 = (string *)0x0;
        uVar12 = android::base::ReadFileToString((string *)&local_a50,&local_9e0,false);
        if ((local_a50 & 1) != 0) {
          operator_delete(local_a40);
        }
      }
      else {
        uVar12 = 0;
      }
      if (((ulong)local_a38 & 1) != 0) {
        operator_delete(local_a28);
      }
      if ((uVar12 & 1) != 0) {
        local_a38 = (string *)0x0;
        local_a30 = (string *)0x0;
        local_a28 = (char *)0x0;
        uVar15 = local_a50 >> 0x10;
        local_a50 = CONCAT62((uint6)uVar15 & 0xffffffffff00,0xa02);
        android::base::Split((base *)&local_9e0,(string *)&local_a50,psVar25);
        psVar25 = local_a30;
        psVar19 = local_a38;
        if ((local_a50 & 1) != 0) {
          operator_delete(local_a40);
          psVar25 = local_a30;
          psVar19 = local_a38;
        }
        for (; psVar19 != psVar25; psVar19 = psVar19 + 0x18) {
          bVar8 = ((byte)*psVar19 & 1) != 0;
          uVar15 = (ulong)((byte)*psVar19 >> 1);
          if (bVar8) {
            uVar15 = *(ulong *)(psVar19 + 8);
          }
          if (uVar15 != 0) {
            psVar37 = *(string **)(psVar19 + 0x10);
            if (!bVar8) {
              psVar37 = psVar19 + 1;
            }
            if (*psVar37 != (string)0x0) {
              if (local_ac0 < local_ab8) {
                uVar43 = *(undefined8 *)(psVar19 + 8);
                uVar34 = *(undefined8 *)psVar19;
                *(undefined8 *)(local_ac0 + 0x10) = *(undefined8 *)(psVar19 + 0x10);
                *(undefined8 *)(local_ac0 + 8) = uVar43;
                *(undefined8 *)local_ac0 = uVar34;
                *(undefined8 *)(psVar19 + 8) = 0;
                *(undefined8 *)(psVar19 + 0x10) = 0;
                *(undefined8 *)psVar19 = 0;
                psVar25 = local_a30;
                local_ac0 = local_ac0 + 0x18;
              }
              else {
                local_ac0 = std::vector<std::string,std::allocator<std::string>>::
                            __push_back_slow_path<std::string>
                                      ((vector<std::string,std::allocator<std::string>> *)&local_ac8
                                       ,psVar19);
                psVar25 = local_a30;
              }
            }
          }
        }
        uVar15 = android::base::ShouldLog(2,0);
        psVar25 = local_a38;
        if ((uVar15 & 1) != 0) {
          uVar39 = *(undefined4 *)pcVar16;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_a50,"bootable/recovery/recovery_main.cpp",0xa5,2,0,
                     0xffffffff);
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a50);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,"Got ",4);
          poVar30 = (ostream *)
                    std::ostream::operator<<
                              (poVar30,((long)local_ac0 - (long)local_ac8 >> 3) *
                                       -0x5555555555555555);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30," arguments from ",0x10);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30,"/cache/recovery/command",0x17);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_a50);
          *(undefined4 *)pcVar16 = uVar39;
          psVar25 = local_a38;
        }
        local_a38 = psVar25;
        psVar19 = local_a30;
        if (psVar25 != (string *)0x0) {
          while (psVar37 = psVar19, psVar37 != psVar25) {
            psVar19 = psVar37 + -0x18;
            if (((byte)*psVar19 & 1) != 0) {
              operator_delete(*(void **)(psVar37 + -8));
            }
          }
          local_a30 = psVar25;
          operator_delete(local_a38);
        }
      }
      if (((byte)local_9e0 & 1) != 0) {
        operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
      }
    }
    local_a38 = (string *)0x0;
    local_a30 = (string *)0x0;
    local_a28 = (void *)0x0;
    _ZNSt3__16vectorINS_12basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEENS4_IS6_EEEC2INS_11__wrap_iterIPKS6_EETnNS_9enable_ifIXaasr31__has_forward_iterator_categoryIT_EE5valuesr16is_constructibleIS6_NS_15iterator_traitsISF_E9referenceEEE5valueEiE4typeELi0EEESF_SF_
              (&local_a38,local_ac8 + 0x18,local_ac0);
    uVar15 = update_bootloader_message((vector *)&local_a38,(string *)&local_a00);
    if (((uVar15 & 1) == 0) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
      uVar39 = *(undefined4 *)pcVar16;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_9e0,"bootable/recovery/recovery_main.cpp",0xae,4,0,0xffffffff)
      ;
      poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_9e0);
      poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar30,"Failed to set BCB message: ",0x1b);
      std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                (poVar30,(string *)&local_a00);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_9e0);
      *(undefined4 *)pcVar16 = uVar39;
    }
    uVar34 = s___fastboot_001499f9._0_8_;
    if ((long)local_ac0 - (long)local_ac8 == 0x18) {
      uVar15 = local_a20 >> 1 & 0x7f;
      if ((local_a20 & 1) != 0) {
        uVar15 = uStack_a18;
      }
      if (uVar15 == 0xb) {
        plVar24 = (long *)((ulong)&local_a20 | 1);
        if ((local_a20 & 1) != 0) {
          plVar24 = local_a10;
        }
        uVar34._0_1_ = '-';
        uVar34._1_1_ = '-';
        uVar34._2_1_ = 'r';
        uVar34._3_1_ = 'e';
        uVar34._4_1_ = 's';
        uVar34._5_1_ = 'c';
        uVar34._6_1_ = 'u';
        uVar34._7_1_ = 'e';
        if (*plVar24 == 0x7365722d746f6f62 && *(long *)((long)plVar24 + 3) == 0x6575637365722d74) {
          if (local_ac0 < local_ab8) {
            psVar25 = local_ac0 + 9;
            *local_ac0 = (string)0x10;
LAB_00221cfc:
            *(undefined8 *)(local_ac0 + 1) = uVar34;
            local_ac0 = local_ac0 + 0x18;
            *psVar25 = (string)0x0;
          }
          else {
            local_ac0 = std::vector<std::string,std::allocator<std::string>>::
                        __emplace_back_slow_path<char_const(&)[9]>
                                  ((vector<std::string,std::allocator<std::string>> *)&local_ac8,
                                   "--rescue");
          }
        }
      }
      else if (uVar15 == 0xd) {
        plVar24 = (long *)((ulong)&local_a20 | 1);
        if ((local_a20 & 1) != 0) {
          plVar24 = local_a10;
        }
        if (*plVar24 == 0x7361662d746f6f62 && *(long *)((long)plVar24 + 5) == 0x746f6f6274736166) {
          if (local_ac0 < local_ab8) {
            psVar25 = local_ac0 + 0xb;
            *local_ac0 = (string)0x14;
            *(undefined2 *)(local_ac0 + 9) = 0x746f;
            goto LAB_00221cfc;
          }
          local_ac0 = std::vector<std::string,std::allocator<std::string>>::
                      __emplace_back_slow_path<char_const(&)[11]>
                                ((vector<std::string,std::allocator<std::string>> *)&local_ac8,
                                 "--fastboot");
        }
      }
    }
    this_00 = (filebuf *)&uStack_9c8;
    uStack_8c0 = 0;
    local_8c8 = 0;
    uStack_8b0 = 0;
    uStack_8b8 = 0;
    uStack_8a0 = 0;
    local_8a8 = 0;
    local_890 = 0;
    local_898 = 0;
    uStack_8d0 = 0;
    local_8d8 = 0;
    uStack_8e0 = 0;
    local_8e8 = 0;
    uStack_8f0 = 0;
    local_8f8 = 0;
    uStack_900 = 0;
    local_908 = 0;
    uStack_910 = 0;
    local_918 = 0;
    local_928 = 0;
    uStack_930 = 0;
    local_938 = 0;
    uStack_940 = 0;
    local_948 = 0;
    uStack_950 = 0;
    local_958 = 0;
    uStack_960 = 0;
    local_968 = 0;
    uStack_970 = 0;
    local_978 = 0;
    uStack_980 = 0;
    local_988 = 0;
    uStack_990 = 0;
    local_998 = 0;
    uStack_9a0 = 0;
    local_9a8 = 0;
    uStack_9b0 = 0;
    local_9b8 = 0;
    psStack_9c0 = (string *)0x0;
    uStack_9c8 = 0;
    cStack_9d8 = '\0';
    uStack_9d7 = 0;
    local_9d0 = 0x80;
    uStack_9cf = 0x425d;
    uStack_9cd = 0;
    ppuStack_920 = (undefined **)0x425e70;
    local_9e0 = (string)0x48;
    uStack_9df = 0x425e;
    std::ios_base::init(&ppuStack_920);
    local_898 = 0;
    local_9d0 = -0x80;
    uStack_9cf = 0x425d;
    uStack_9cd = 0;
    local_890 = CONCAT44(local_890._4_4_,0xffffffff);
    ppuStack_920 = &PTR__fstream_00425da8;
    local_9e0 = (string)0x58;
    uStack_9df = 0x425d;
    std::filebuf::filebuf(this_00);
    local_a40 = operator_new(0x30);
    *(char *)((long)local_a40 + 0x29) = '\0';
    uVar4 = s__cache_recovery_last_extension_p_00142ff7._33_8_;
    uVar3 = s__cache_recovery_last_extension_p_00142ff7._16_8_;
    uVar43 = s__cache_recovery_last_extension_p_00142ff7._0_8_;
    lVar33 = CONCAT71(s__cache_recovery_last_extension_p_00142ff7._25_7_,
                      s__cache_recovery_last_extension_p_00142ff7[0x18]);
    uStack_a48 = 0x29;
    local_a50 = 0x31;
    uVar34 = CONCAT17(s__cache_recovery_last_extension_p_00142ff7[0x20],
                      s__cache_recovery_last_extension_p_00142ff7._25_7_);
    local_a40[1] = s__cache_recovery_last_extension_p_00142ff7._8_8_;
    *local_a40 = uVar43;
    local_a40[3] = lVar33;
    local_a40[2] = uVar3;
    *(undefined8 *)((long)local_a40 + 0x21) = uVar4;
    *(undefined8 *)((long)local_a40 + 0x19) = uVar34;
    iVar10 = ensure_path_mounted((string *)&local_a50);
    if ((local_a50 & 1) != 0) {
      operator_delete(local_a40);
    }
    if ((iVar10 != 0) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
      uVar39 = *(undefined4 *)pcVar16;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_a50,"bootable/recovery/recovery_main.cpp",0xbe,4,0,0xffffffff)
      ;
      poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a50);
      poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar30,"Can\'t mount ",0xc);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar30,"/cache/recovery/last_extension_parameters",0x29);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_a50);
      *(undefined4 *)pcVar16 = uVar39;
    }
    std::filebuf::open((char *)this_00,0x142ff7);
    std::ios_base::clear
              ((int)&local_9e0 + (int)*(undefined8 *)(CONCAT71(uStack_9df,local_9e0) + -0x18));
    if ((*(byte *)((long)&psStack_9c0 + *(long *)(CONCAT71(uStack_9df,local_9e0) + -0x18)) & 5) == 0
       ) {
      uVar15 = android::base::ShouldLog(2,0);
      if ((uVar15 & 1) != 0) {
        uVar39 = *(undefined4 *)pcVar16;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_a50,"bootable/recovery/recovery_main.cpp",0xc2,2,0,
                   0xffffffff);
        poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a50);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar30,"Start to read extension parameters",0x22);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_a50);
        *(undefined4 *)pcVar16 = uVar39;
      }
      local_af0 = (void *)((ulong)&local_a68 | 1);
      local_a50 = 0;
      uStack_a48 = 0;
      local_a40 = (long *)0x0;
LAB_00221f04:
      unaff_x28 = (void *)0x0;
      piVar20 = std::operator>>[abi:ne180000]<char,std::char_traits<char>,std::allocator<char>>
                          ((istream *)&local_9e0,(string *)&local_a50);
      if (((byte)piVar20[*(long *)(*(long *)piVar20 + -0x18) + 0x20] & 5) == 0) {
        uVar15 = android::base::ShouldLog(2,0);
        if ((uVar15 & 1) != 0) {
          uVar39 = *(undefined4 *)pcVar16;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_a68,"bootable/recovery/recovery_main.cpp",0xc5,2,0,
                     0xffffffff);
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a68);
          std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (poVar30,(string *)&local_a50);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_a68);
          *(undefined4 *)pcVar16 = uVar39;
        }
        uVar15 = local_a50 >> 1 & 0x7f;
        if ((local_a50 & 1) != 0) {
          uVar15 = uStack_a48;
        }
        if (uVar15 != 0) {
          uVar38 = 0;
          plVar24 = (long *)((ulong)&local_a50 | 1);
          if ((local_a50 & 1) != 0) {
            plVar24 = local_a40;
          }
          do {
            if (*(char *)((long)plVar24 + uVar38) == '=') {
              __n = ~uVar38 + uVar15;
              if (uVar15 <= ~uVar38 + uVar15) {
                __n = uVar15;
              }
              if (__n < 0xfffffffffffffff0) {
                if (__n < 0x17) {
                  local_a68 = CONCAT71(local_a68._1_7_,(char)((int)__n << 1));
                  unaff_x28 = local_af0;
                  if (uVar15 - 1 != uVar38) goto LAB_00221fdc;
                }
                else {
                  uVar15 = (__n | 0xf) + 1;
                  unaff_x28 = operator_new(uVar15);
                  local_a68 = uVar15 | 1;
                  uStack_a60 = __n;
                  local_a58 = unaff_x28;
LAB_00221fdc:
                  memmove(unaff_x28,(char *)((long)plVar24 + uVar38 + 1),__n);
                }
                *(undefined1 *)((long)unaff_x28 + __n) = 0;
                plVar24 = (long *)((ulong)&local_a50 | 1);
                uVar15 = local_a50 >> 1 & 0x7f;
                if ((local_a50 & 1) != 0) {
                  plVar24 = local_a40;
                  uVar15 = uStack_a48;
                }
                if (uVar38 <= uVar15) {
                  uVar15 = uVar38;
                }
                if (uVar15 < 0xfffffffffffffff0) {
                  if (uVar15 < 0x17) {
                    local_a80 = CONCAT71(local_a80._1_7_,(char)((int)uVar15 << 1));
                    pvVar40 = (void *)((ulong)&local_a80 | 1);
                    if (uVar15 != 0) goto LAB_00222068;
                  }
                  else {
                    uVar38 = (uVar15 | 0xf) + 1;
                    pvVar40 = operator_new(uVar38);
                    local_a80 = uVar38 | 1;
                    uStack_a78 = uVar15;
                    local_a70 = pvVar40;
LAB_00222068:
                    memmove(pvVar40,plVar24,uVar15);
                  }
                  *(undefined1 *)((long)pvVar40 + uVar15) = 0;
                  local_9e8 = (__tree_end_node *)0x0;
                  pp_Var21 = std::
                             __tree<std::string,std::less<std::string>,std::allocator<std::string>>
                             ::__find_equal<std::string>
                                       ((__tree<std::string,std::less<std::string>,std::allocator<std::string>>
                                         *)&DAT_0042ce98,&local_9e8,(string *)&local_a80);
                  p_Var41 = *pp_Var21;
                  if (p_Var41 == (__tree_node_base *)0x0) {
                    p_Var41 = operator_new(0x50);
                    pvVar40 = local_a70;
                    uVar38 = uStack_a78;
                    uVar15 = local_a80;
                    *(undefined8 *)(p_Var41 + 0x40) = 0;
                    *(undefined8 *)(p_Var41 + 0x48) = 0;
                    local_a80 = 0;
                    uStack_a78 = 0;
                    local_a70 = (void *)0x0;
                    *(void **)(p_Var41 + 0x30) = pvVar40;
                    *(undefined8 *)(p_Var41 + 0x38) = 0;
                    *(ulong *)(p_Var41 + 0x28) = uVar38;
                    *(ulong *)(p_Var41 + 0x20) = uVar15;
                    *(undefined8 *)p_Var41 = 0;
                    *(undefined8 *)(p_Var41 + 8) = 0;
                    *(__tree_end_node **)(p_Var41 + 0x10) = local_9e8;
                    *pp_Var21 = p_Var41;
                    p_Var31 = p_Var41;
                    if ((long *)*DAT_0042ce98 != (long *)0x0) {
                      p_Var31 = *pp_Var21;
                      DAT_0042ce98 = (long *)*DAT_0042ce98;
                    }
                    std::__tree_balance_after_insert_abi_nn180000_<std::__tree_node_base<void*>*>
                              (DAT_0042cea0,p_Var31);
                    DAT_0042cea8 = DAT_0042cea8 + 1;
                  }
                  if (((byte)p_Var41[0x38] & 1) != 0) {
                    operator_delete(*(void **)(p_Var41 + 0x48));
                  }
                  *(ulong *)(p_Var41 + 0x40) = uStack_a60;
                  *(ulong *)(p_Var41 + 0x38) = local_a68;
                  *(void **)(p_Var41 + 0x48) = local_a58;
                  if ((local_a80 & 1) != 0) {
                    operator_delete(local_a70);
                  }
                  break;
                }
              }
              else {
                if (*(long *)(lVar2 + 0x28) != local_78) goto LAB_0022489c;
                std::string::__throw_length_error_abi_ne180000_();
              }
              if (*(long *)(lVar2 + 0x28) != local_78) goto LAB_0022489c;
              std::string::__throw_length_error_abi_ne180000_();
              goto LAB_00222fa8;
            }
            uVar38 = uVar38 + 1;
          } while (uVar15 != uVar38);
        }
        goto LAB_00221f04;
      }
      lVar33 = std::filebuf::close();
      if (lVar33 == 0) {
        std::ios_base::clear
                  ((int)&local_9e0 + (int)*(undefined8 *)(CONCAT71(uStack_9df,local_9e0) + -0x18));
      }
      if ((local_a50 & 1) != 0) {
        operator_delete(local_a40);
      }
    }
    ppuStack_920 = &PTR__fstream_00425da8;
    local_9e0 = (string)0x58;
    uStack_9df = 0x425d;
    local_9d0 = -0x80;
    uStack_9cf = 0x425d;
    uStack_9cd = 0;
    std::filebuf::~filebuf(this_00);
    std::iostream::~iostream((iostream *)&local_9e0);
    std::ios::~ios((ios *)&ppuStack_920);
    psVar25 = local_a38;
    psVar19 = local_a30;
    if (local_a38 != (string *)0x0) {
      while (psVar37 = psVar19, psVar37 != psVar25) {
        psVar19 = psVar37 + -0x18;
        if (((byte)*psVar19 & 1) != 0) {
          operator_delete(*(void **)(psVar37 + -8));
        }
      }
      local_a30 = psVar25;
      operator_delete(local_a38);
    }
    if ((local_a20 & 1) != 0) {
      operator_delete(local_a10);
    }
    if (((ulong)local_a00 & 1) != 0) {
      operator_delete(local_9f0);
    }
    local_a00 = (char *)0x0;
    local_9f8 = (char *)0x0;
    local_9f0 = (void *)0x0;
    StringVectorToNullTerminatedArray((vector *)&local_ac8);
    local_af0 = (void *)0x0;
    local_a20 = 0;
    uStack_a18 = 0;
    piVar42 = (int *)((ulong)&local_880 | 1);
    local_a10 = (long *)0x0;
    unaff_x28 = (void *)0x0;
    local_b00 = (void *)((ulong)&local_a38 | 1);
    *(undefined4 *)PTR_opterr_00429b00 = 0;
    local_a38 = (string *)0x0;
    local_a30 = (string *)0x0;
    local_a28 = (void *)0x0;
    local_acc = 0;
LAB_002222e8:
    do {
      while (psVar25 = (string *)&DAT_0014826c,
            iVar10 = getopt_long((int)((ulong)((long)local_9f8 - (long)local_a00) >> 3) + -1,
                                 local_a00,&DAT_0014826c,&PTR_s_fastboot_00427e88,&local_acc),
            iVar10 == 0) {
        cStack_878 = '\0';
        uStack_877 = 0;
        local_880 = (bootloader_message)0x0;
        cStack_87f = '\0';
        uStack_87e = 0;
        iStack_87c = 0;
        local_870 = '\0';
        uStack_86f = 0;
        uStack_86d = 0;
        pcVar32 = (&PTR_s_fastboot_00427e88)[(long)local_acc * 4];
        sVar17 = strlen(pcVar32);
        if (0xffffffffffffffef < sVar17) {
          if (*(long *)(lVar2 + 0x28) != local_78) goto LAB_0022489c;
          std::string::__throw_length_error_abi_ne180000_();
          goto LAB_00222f14;
        }
        if (sVar17 < 0x17) {
          local_880 = SUB41((int)sVar17 << 1,0);
          piVar22 = piVar42;
          if (sVar17 != 0) goto LAB_0022238c;
        }
        else {
          uVar15 = (sVar17 | 0xf) + 1;
          piVar22 = operator_new(uVar15);
          local_880 = (bootloader_message)((byte)uVar15 | 1);
          local_870 = (char)piVar22;
          uStack_86f = (undefined2)((ulong)piVar22 >> 8);
          uStack_86d = (undefined5)((ulong)piVar22 >> 0x18);
          cStack_878 = (char)sVar17;
          uStack_877 = (undefined7)(sVar17 >> 8);
          cStack_87f = (char)(uVar15 >> 8);
          uStack_87e = (undefined2)(uVar15 >> 0x10);
          iStack_87c = (int)(uVar15 >> 0x20);
LAB_0022238c:
          memmove(piVar22,pcVar32,sVar17);
        }
        bVar7 = local_880;
        *(undefined1 *)((long)piVar22 + sVar17) = 0;
        uVar15 = (ulong)((byte)local_880 >> 1);
        if (((byte)local_880 & 1) != 0) {
          uVar15 = CONCAT71(uStack_877,cStack_878);
        }
        if (uVar15 == 8) {
          piVar22 = piVar42;
          if (((byte)local_880 & 1) != 0) {
            piVar22 = (int *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870));
          }
          iVar10 = memcmp(piVar22,"fastboot",8);
          uVar9 = (undefined1)uVar15;
          if (iVar10 == 0) {
            pcVar32 = operator_new(0x20);
            pcVar32[0x1a] = '\0';
            uVar3 = s_ro_boot_dynamic_partitions_00143d54._18_8_;
            uVar43 = s_ro_boot_dynamic_partitions_00143d54._0_8_;
            uVar34 = CONCAT26(s_ro_boot_dynamic_partitions_00143d54._16_2_,
                              s_ro_boot_dynamic_partitions_00143d54._10_6_);
            *(ulong *)(pcVar32 + 8) =
                 CONCAT62(s_ro_boot_dynamic_partitions_00143d54._10_6_,
                          s_ro_boot_dynamic_partitions_00143d54._8_2_);
            *(undefined8 *)pcVar32 = uVar43;
            local_9e0 = (string)0x21;
            uStack_9df = 0;
            *(undefined8 *)(pcVar32 + 0x12) = uVar3;
            *(undefined8 *)(pcVar32 + 10) = uVar34;
            cStack_9d8 = '\x1a';
            uStack_9d7 = 0;
            local_9d0 = (char)pcVar32;
            uStack_9cf = (undefined2)((ulong)pcVar32 >> 8);
            uStack_9cd = (undefined5)((ulong)pcVar32 >> 0x18);
            uVar12 = android::base::GetBoolProperty((base *)&local_9e0,(string *)0x0,(bool)uVar9);
            if (((byte)local_9e0 & 1) != 0) {
              operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
            }
            goto LAB_002224dc;
          }
        }
        else {
          uVar12 = 0;
          if (uVar15 == 6) {
            piVar22 = piVar42;
            if (((byte)local_880 & 1) != 0) {
              piVar22 = (int *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870));
            }
            if (*piVar22 == 0x61636f6c && (short)piVar22[1] == 0x656c) {
              std::string::__assign_external((string *)&local_a20,*(char **)PTR_optarg_00429b08);
              std::string::replace((ulong)&local_a20,2,(char *)0x1,0x150517);
            }
            else if (*piVar22 == 0x73616572 && (short)piVar22[1] == 0x6e6f) {
              std::string::__assign_external((string *)&local_a38,*(char **)PTR_optarg_00429b08);
            }
          }
          else {
LAB_002224dc:
            unaff_x28 = (void *)(ulong)(uVar12 | (uint)unaff_x28);
          }
        }
        if (((byte)bVar7 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
        }
      }
      if (iVar10 == 0x74) {
        local_af0 = (void *)0x1;
        goto LAB_002222e8;
      }
    } while (iVar10 != -1);
    *(undefined4 *)PTR_optind_00429b10 = 1;
    uVar15 = local_a20 >> 1 & 0x7f;
    if ((local_a20 & 1) != 0) {
      uVar15 = uStack_a18;
    }
    *(undefined4 *)PTR_opterr_00429b00 = 1;
    if (uVar15 == 0) {
      uVar15 = HasCache();
      if ((uVar15 & 1) != 0) {
        pcVar32 = operator_new(0x20);
        uVar3 = s__cache_recovery_last_locale_0015535e._0_8_;
        local_870 = (char)pcVar32;
        uStack_86f = (undefined2)((ulong)pcVar32 >> 8);
        uStack_86d = (undefined5)((ulong)pcVar32 >> 0x18);
        uVar34 = CONCAT53(s__cache_recovery_last_locale_0015535e._11_5_,
                          s__cache_recovery_last_locale_0015535e._8_3_);
        pcVar32[0x1b] = '\0';
        uVar4 = s__cache_recovery_last_locale_0015535e._19_8_;
        uVar43 = CONCAT35(s__cache_recovery_last_locale_0015535e._16_3_,
                          s__cache_recovery_last_locale_0015535e._11_5_);
        local_880 = (bootloader_message)0x21;
        cStack_87f = '\0';
        uStack_87e = 0;
        iStack_87c = 0;
        cStack_878 = '\x1b';
        uStack_877 = 0;
        *(undefined8 *)(pcVar32 + 8) = uVar34;
        *(undefined8 *)pcVar32 = uVar3;
        *(undefined8 *)(pcVar32 + 0x13) = uVar4;
        *(undefined8 *)(pcVar32 + 0xb) = uVar43;
        iVar10 = ensure_path_mounted((string *)&local_880);
        if (((byte)local_880 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
        }
        if (iVar10 == 0) {
          cStack_878 = '\0';
          uStack_877 = 0;
          local_880 = (bootloader_message)0x0;
          cStack_87f = '\0';
          uStack_87e = 0;
          iStack_87c = 0;
          local_870 = '\0';
          uStack_86f = 0;
          uStack_86d = 0;
          pcVar32 = operator_new(0x20);
          pcVar32[0x1b] = '\0';
          *(undefined8 *)(pcVar32 + 8) = uVar34;
          *(undefined8 *)pcVar32 = uVar3;
          local_9e0 = (string)0x21;
          uStack_9df = 0;
          cStack_9d8 = '\x1b';
          uStack_9d7 = 0;
          local_9d0 = (char)pcVar32;
          uStack_9cf = (undefined2)((ulong)pcVar32 >> 8);
          uStack_9cd = (undefined5)((ulong)pcVar32 >> 0x18);
          *(undefined8 *)(pcVar32 + 0x13) = uVar4;
          *(undefined8 *)(pcVar32 + 0xb) = uVar43;
          psVar25 = (string *)0x0;
          auVar45 = android::base::ReadFileToString(&local_9e0,(string *)&local_880,false);
          psVar19 = auVar45._8_8_;
          if (((byte)local_9e0 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
            psVar19 = extraout_x1;
          }
          if ((auVar45._0_8_ & 1) == 0) {
            uVar15 = android::base::ShouldLog(4,0);
            if ((uVar15 & 1) != 0) {
LAB_00222f14:
              uVar39 = *(undefined4 *)pcVar16;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_9e0,"bootable/recovery/recovery_main.cpp",0xdd,4,0,
                         uVar39);
              poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_9e0);
              poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar30,"Can\'t read ",0xb);
              psVar25 = (string *)0x1b;
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar30,"/cache/recovery/last_locale",0x1b);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_9e0);
              *(undefined4 *)pcVar16 = uVar39;
            }
            local_a50 = local_a50 & 0xffffffffffff0000;
          }
          else {
            android::base::Trim<std::string&>((base *)&local_880,psVar19);
          }
          if (((byte)local_880 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
          }
        }
        else {
          uVar15 = android::base::ShouldLog(4,0);
          if ((uVar15 & 1) != 0) {
            uVar39 = *(undefined4 *)pcVar16;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0xd7,4,0,
                       0xffffffff);
            poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
            poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar30,"Can\'t mount ",0xc);
            psVar25 = (string *)0x1b;
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar30,"/cache/recovery/last_locale",0x1b);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
            *(undefined4 *)pcVar16 = uVar39;
          }
          local_a50 = local_a50 & 0xffffffffffff0000;
        }
        if ((local_a20 & 1) != 0) {
          operator_delete(local_a10);
        }
        local_a10 = local_a40;
        uStack_a18 = uStack_a48;
        local_a20 = local_a50;
      }
      uVar38 = (ulong)((byte)local_a20._0_1_ >> 1);
      uVar15 = uVar38;
      if ((local_a20 & 1) != 0) {
        uVar15 = uStack_a18;
      }
      if (uVar15 == 0) {
        local_9e0 = (string)0x22;
        uStack_9cf = 0x65;
        uStack_9d7 = (undefined7)s_ro_product_locale_0014d15a._8_8_;
        local_9d0 = SUB81(s_ro_product_locale_0014d15a._8_8_,7);
        uStack_9df = (undefined7)s_ro_product_locale_0014d15a._0_8_;
        cStack_9d8 = SUB81(s_ro_product_locale_0014d15a._0_8_,7);
        local_a50 = CONCAT17(local_a50._7_1_,0x53555f6e650a);
        android::base::GetProperty((base *)&local_9e0,(string *)&local_a50,psVar25);
        if ((local_a20 & 1) != 0) {
          operator_delete(local_a10);
        }
        uStack_a18 = CONCAT71(uStack_877,cStack_878);
        local_a20 = CONCAT44(iStack_87c,CONCAT22(uStack_87e,CONCAT11(cStack_87f,local_880)));
        local_880 = (bootloader_message)0x0;
        cStack_87f = '\0';
        local_a10 = (long *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870));
        if ((local_a50 & 1) != 0) {
          operator_delete(local_a40);
        }
        if (((byte)local_9e0 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_9cd,CONCAT21(uStack_9cf,local_9d0)));
        }
        psVar25 = (string *)0x1;
        std::string::replace((ulong)&local_a20,2,(char *)0x1,0x150517);
        uVar38 = (ulong)((byte)local_a20._0_1_ >> 1);
      }
      if (((byte)local_a20._0_1_ & 1) != 0) {
        uVar38 = uStack_a18;
      }
      if (uVar38 == 0) {
        if (((byte)local_a20._0_1_ & 1) == 0) {
          local_a20 = CONCAT71(local_a20._1_7_,10);
          plVar24 = (long *)((ulong)&local_a20 | 1);
        }
        else {
          uStack_a18 = 5;
          plVar24 = local_a10;
        }
        ((char *)((long)plVar24 + 4))[0] = 'S';
        ((char *)((long)plVar24 + 4))[1] = '\0';
        *(undefined4 *)plVar24 = 0x552d6e65;
      }
    }
    uVar9 = SUB81(psVar25,0);
    lVar33 = dlopen("librecovery_ui_ext.so",2);
    if (lVar33 == 0) {
      uVar9 = dlerror();
      printf("Failed to dlopen %s: %s\n","librecovery_ui_ext.so");
LAB_002227e8:
      iVar10 = puts("Falling back to the default make_device() instead");
      this_00 = (filebuf *)make_device(iVar10);
    }
    else {
      pcVar23 = (code *)dlsym(lVar33,"make_device");
      if (pcVar23 == (code *)0x0) {
        uVar34 = dlerror();
        printf("Failed to dlsym make_device: %s\n",uVar34);
        goto LAB_002227e8;
      }
      iVar10 = printf("Loading make_device from %s\n","librecovery_ui_ext.so");
      this_00 = (filebuf *)(*pcVar23)(iVar10);
    }
    local_880 = (bootloader_message)0x22;
    uStack_86f = 0x74;
    uStack_877 = (undefined7)s_ro_boot_quiescent_00152079._8_8_;
    local_870 = SUB81(s_ro_boot_quiescent_00152079._8_8_,7);
    cStack_87f = (char)s_ro_boot_quiescent_00152079._0_8_;
    uStack_87e = SUB82(s_ro_boot_quiescent_00152079._0_8_,1);
    iStack_87c = SUB84(s_ro_boot_quiescent_00152079._0_8_,3);
    cStack_878 = SUB81(s_ro_boot_quiescent_00152079._0_8_,7);
    uVar15 = android::base::GetBoolProperty((base *)&local_880,(string *)0x0,(bool)uVar9);
    if (((byte)local_880 & 1) != 0) {
      operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
    }
    if ((uVar15 & 1) == 0) {
      plVar24 = (long *)(**(code **)(*(long *)this_00 + 0x10))(this_00);
      uVar15 = (**(code **)(*plVar24 + 0x100))(plVar24,&local_a20);
      if ((uVar15 & 1) != 0) {
LAB_00222fe0:
        psVar25 = (string *)((ulong)local_a38 >> 1 & 0x7f);
        if (((ulong)local_a38 & 1) != 0) {
          local_b00 = local_a28;
          psVar25 = local_a30;
        }
        bVar8 = (local_ab0 & 1) != 0;
        pvVar40 = (void *)((ulong)&local_ab0 | 1);
        if (bVar8) {
          pvVar40 = local_aa0;
        }
        uVar15 = local_ab0 >> 1 & 0x7f;
        if (bVar8) {
          uVar15 = uStack_aa8;
        }
        uStack_9c8 = 0;
        local_9d0 = '\0';
        uStack_9cf = 0;
        uStack_9cd = 0;
        local_9b8 = 0;
        psStack_9c0 = (string *)0x0;
        cStack_9d8 = '\0';
        uStack_9d7 = 0;
        local_9e0 = (string)0x0;
        uStack_9df = 0;
        BootState::BootState((BootState *)&local_9e0,local_b00,psVar25,pvVar40,uVar15);
        Device::SetBootState((Device *)this_00,(BootState *)&local_9e0);
        DAT_0042ceb0 = (long *)(**(code **)(*(long *)this_00 + 0x10))(this_00);
        uVar15 = HasCache();
        if ((uVar15 & 1) == 0) {
          (**(code **)(*(long *)this_00 + 0x58))(this_00,6);
        }
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_880,"secure_password");
        psVar25 = (string *)
                  std::
                  map<std::string,std::string,std::less<std::string>,std::allocator<std::pair<std::string_const,std::string>>>
                  ::operator[]((map<std::string,std::string,std::less<std::string>,std::allocator<std::pair<std::string_const,std::string>>>
                                *)&DAT_0042ce98,(string *)&local_880);
        bVar8 = std::operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                          (psVar25,"1");
        if (((byte)local_880 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
        }
        if (bVar8) {
          (**(code **)(*DAT_0042ceb0 + 0xa0))(DAT_0042ceb0,1);
        }
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_880,"disable_factoryreset");
        psVar25 = (string *)
                  std::
                  map<std::string,std::string,std::less<std::string>,std::allocator<std::pair<std::string_const,std::string>>>
                  ::operator[]((map<std::string,std::string,std::less<std::string>,std::allocator<std::pair<std::string_const,std::string>>>
                                *)&DAT_0042ce98,(string *)&local_880);
        bVar8 = std::operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                          (psVar25,"1");
        if (((byte)local_880 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
        }
        if (bVar8) {
          set_disable_factoryreset();
        }
        (**(code **)(*DAT_0042ceb0 + 0x118))(DAT_0042ceb0,2);
        if (((ulong)local_af0 & 1) != 0) {
          (**(code **)(*DAT_0042ceb0 + 0x140))(DAT_0042ceb0,1);
        }
        uVar15 = android::base::ShouldLog(2,0);
        if ((uVar15 & 1) != 0) {
          uVar39 = *(undefined4 *)pcVar16;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x215,2,0,
                     0xffffffff);
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,"Starting recovery (pid ",0x17);
          _Var13 = getpid();
          poVar30 = (ostream *)std::ostream::operator<<(poVar30,_Var13);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,") on ",5);
          pcVar32 = ctime(&local_a90);
          sVar17 = strlen(pcVar32);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar30,pcVar32,sVar17);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
          *(undefined4 *)pcVar16 = uVar39;
        }
        uVar15 = android::base::ShouldLog(2,0);
        if ((uVar15 & 1) != 0) {
          uVar39 = *(undefined4 *)pcVar16;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x216,2,0,
                     0xffffffff);
          poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
          poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar30,"locale is [",0xb);
          poVar30 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                    operator<(poVar30,(string *)&local_a20);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar30,"]",1);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
          *(undefined4 *)pcVar16 = uVar39;
        }
        local_880 = SUB81(&local_a20,0);
        cStack_87f = (char)((ulong)&local_a20 >> 8);
        uStack_87e = (undefined2)((ulong)&local_a20 >> 0x10);
        iStack_87c = (int)((ulong)&local_a20 >> 0x20);
        lVar33 = std::
                 __tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
                 ::
                 __emplace_unique_key_args<std::string,std::piecewise_construct_t_const&,std::tuple<std::string_const&>,std::tuple<>>
                           ((string *)&DAT_0042ce80,(piecewise_construct_t *)&local_a20,
                            (tuple *)&std::piecewise_construct,(tuple *)&local_880);
        bVar8 = std::operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                          ((string *)(lVar33 + 0x38),"");
        plVar24 = DAT_0042ceb0;
        if (bVar8) {
          (**(code **)(*DAT_0042ceb0 + 0x18))(DAT_0042ceb0,&DAT_001511c4);
        }
        else {
          local_880 = SUB81(&local_a20,0);
          cStack_87f = (char)((ulong)&local_a20 >> 8);
          uStack_87e = (undefined2)((ulong)&local_a20 >> 0x10);
          iStack_87c = (int)((ulong)&local_a20 >> 0x20);
          lVar27 = std::
                   __tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
                   ::
                   __emplace_unique_key_args<std::string,std::piecewise_construct_t_const&,std::tuple<std::string_const&>,std::tuple<>>
                             ((string *)&DAT_0042ce80,(piecewise_construct_t *)&local_a20,
                              (tuple *)&std::piecewise_construct,(tuple *)&local_880);
          lVar33 = lVar27 + 0x39;
          if ((*(byte *)(lVar27 + 0x38) & 1) != 0) {
            lVar33 = *(long *)(lVar27 + 0x48);
          }
          (**(code **)(*plVar24 + 0x18))(plVar24,lVar33);
        }
        psVar28 = (selabel_handle *)selinux_android_file_context_handle();
        selinux_android_set_sehandle();
        if (psVar28 == (selabel_handle *)0x0) {
          (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,"Warning: No file_contexts\n");
        }
        SetLoggingSehandle(psVar28);
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_880,"/persist");
        iVar10 = ensure_path_mounted((string *)&local_880);
        if (((byte)local_880 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
        }
        if (iVar10 == 0) {
          iVar10 = selinux_android_restorecon("/persist",0xc);
          if (iVar10 != 0) {
            printf("restore persist secontext label fail, ret: %d\n");
          }
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_880,"/persist");
          ensure_path_unmounted((string *)&local_880);
          if (((byte)local_880 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
          }
        }
        else {
          puts("mount persist fail");
        }
        local_ad0 = 0;
        local_9e8 = (__tree_end_node *)0x0;
        local_880 = SUB81(&local_ad0,0);
        cStack_87f = (char)((ulong)&local_ad0 >> 8);
        uStack_87e = (undefined2)((ulong)&local_ad0 >> 0x10);
        iStack_87c = (int)((ulong)&local_ad0 >> 0x20);
        psVar25 = (string *)&DAT_0042ceb0;
        std::thread::
        thread<void(&)(RecoveryUI*,std::atomic<Device::BuiltinAction>&),RecoveryUI*&,std::reference_wrapper<std::atomic<Device::BuiltinAction>>,void>
                  ((thread *)&local_9e8,ListenRecoverySocket,(RecoveryUI **)&DAT_0042ceb0,
                   (reference_wrapper *)&local_880);
        std::thread::detach();
LAB_00223608:
        local_a50 = 0;
        uStack_a48 = 0;
        local_a40 = (long *)0x0;
        pcVar32 = "fastboot";
        if ((((ulong)unaff_x28 & 1) == 0) &&
           ((uVar15 = IsRoDebuggable(), (uVar15 & 1) != 0 ||
            (uVar15 = IsDeviceUnlocked(), pcVar32 = "none", (uVar15 & 1) != 0)))) {
          pcVar32 = "adb";
        }
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_a50,pcVar32);
        local_a68 = 0;
        uStack_a60 = 0;
        local_a58 = (void *)0x0;
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_880,"sys.usb.state");
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_a80,&DAT_00153c47);
        android::base::GetProperty((base *)&local_880,(string *)&local_a80,psVar25);
        if ((local_a80 & 1) != 0) {
          operator_delete(local_a70);
        }
        if (((byte)local_880 & 1) != 0) {
          operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
        }
        lVar33 = 0x30;
        if (((ulong)unaff_x28 & 1) == 0) {
          lVar33 = 0x20;
        }
        (**(code **)(*(long *)this_00 + lVar33))(this_00);
        bVar8 = std::operator==[abi_ne180000_<std::allocator<char>>
                          ((string *)&local_a50,(string *)&local_a68);
        if (!bVar8) {
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_880,&DAT_00153c47);
          uVar15 = SetUsbConfig((string *)&local_880);
          if (((byte)local_880 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
          }
          if (((uVar15 & 1) == 0) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
            uVar39 = *(undefined4 *)pcVar16;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x245,4,0,
                       0xffffffff);
            poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
            psVar25 = (string *)0x1a;
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar30,"Failed to clear USB config",0x1a);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
            *(undefined4 *)pcVar16 = uVar39;
          }
          uVar15 = SetUsbConfig((string *)&local_a50);
          if (((uVar15 & 1) == 0) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)) {
            uVar39 = *(undefined4 *)pcVar16;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x248,4,0,
                       0xffffffff);
            poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
            psVar25 = (string *)0x1c;
            poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar30,"Failed to set USB config to ",0x1c);
            std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                      (poVar30,(string *)&local_a50);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
            *(undefined4 *)pcVar16 = uVar39;
          }
        }
        *(byte *)((long)DAT_0042ceb0 + 0x59) = (byte)unaff_x28 & 1;
        if (((ulong)unaff_x28 & 1) == 0) {
          iVar10 = start_recovery((Device *)this_00,(vector *)&local_ac8);
        }
        else {
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_880,"/cache");
          ensure_path_unmounted((string *)&local_880);
          if (((byte)local_880 & 1) != 0) {
            operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
          }
          iVar10 = StartFastboot((Device *)this_00,(vector *)&local_ac8);
        }
        iVar11 = local_ad0;
        plVar24 = DAT_0042ceb0;
        if (iVar10 == 0xd) {
          LOAcquire();
          local_ad0 = 0xd;
          LORelease();
          iVar10 = iVar11;
          if (iVar11 != 0) goto LAB_00223788;
        }
        else {
LAB_00223788:
          if (iVar10 < 0x11) {
            if (iVar10 < 0xe) {
              if (iVar10 != 8) {
LAB_00223db8:
                if (iVar10 == 1) {
                  (**(code **)(*plVar24 + 0x158))(plVar24,"Rebooting...\n");
                  MiScreenRecoveryUI::SafeCloseDisplayDevice();
                  uVar34 = 0x16;
                  pcVar16 = "userrequested,recovery";
                }
                else {
                  if (iVar10 != 7) goto LAB_00223f54;
                  (**(code **)(*plVar24 + 0x158))(plVar24,"Rebooting to bootloader...\n");
                  MiScreenRecoveryUI::SafeCloseDisplayDevice();
                  uVar34 = 10;
                  pcVar16 = "bootloader";
                }
                goto LAB_00223f3c;
              }
              (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,"Shutting down...\n");
              MiScreenRecoveryUI::SafeCloseDisplayDevice();
              pcVar32 = "userrequested,recovery";
LAB_0022397c:
              Shutdown(pcVar32,0x16);
            }
            else if (iVar10 == 0xe) {
              uVar15 = android::fs_mgr::LogicalPartitionsMapped();
              if ((uVar15 & 1) != 0) goto LAB_00223df8;
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_880,"ro.boot.dynamic_partitions");
              uVar15 = android::base::GetBoolProperty
                                 ((base *)&local_880,(string *)0x0,SUB81(psVar25,0));
              if (((byte)local_880 & 1) != 0) {
                operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
              }
              if ((uVar15 & 1) != 0) {
                uVar15 = android::base::ShouldLog(2,0);
                if ((uVar15 & 1) == 0) {
                  unaff_x28 = (void *)0x1;
                }
                else {
                  uVar39 = *(undefined4 *)pcVar16;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x29c,2,
                             0,0xffffffff);
                  poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
                  psVar25 = (string *)0x11;
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar30,"Entering fastboot",0x11);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
                  unaff_x28 = (void *)0x1;
                  *(undefined4 *)pcVar16 = uVar39;
                }
              }
            }
            else {
              if (iVar10 != 0xf) goto LAB_00223f54;
              uVar15 = android::base::ShouldLog(2,0);
              if ((uVar15 & 1) != 0) {
                uVar39 = *(undefined4 *)pcVar16;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x2a2,2,0,
                           0xffffffff);
                poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar30,"Entering recovery",0x11);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
                *(undefined4 *)pcVar16 = uVar39;
              }
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_880,"sys.usb.config_disable");
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_a80,&DAT_0015531e);
              android::base::SetProperty((string *)&local_880,(string *)&local_a80);
              if ((local_a80 & 1) != 0) {
                operator_delete(local_a70);
              }
              if (((byte)local_880 & 1) != 0) {
                operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
              }
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_880,"sys.usb.config_disable");
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_a80,&DAT_0015531e);
              psVar25 = (string *)0x7fffffffffffffff;
              uVar15 = android::base::WaitForProperty(&local_880,&local_a80);
              if ((local_a80 & 1) != 0) {
                operator_delete(local_a70);
              }
              if (((byte)local_880 & 1) != 0) {
                operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
              }
              if (((uVar15 & 1) == 0) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)
                 ) {
                uVar39 = *(undefined4 *)pcVar16;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x2a6,4,0,
                           0xffffffff);
                poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
                psVar25 = (string *)&DAT_00000031;
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar30,"failed to setproperty sys.usb.config_disable to 0",0x31);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
                *(undefined4 *)pcVar16 = uVar39;
              }
              (**(code **)(*DAT_0042ceb0 + 0x10))(DAT_0042ceb0,0);
              unaff_x28 = (void *)0x0;
            }
          }
          else {
            if (0x13 < iVar10) {
              if (iVar10 == 0x15) {
                (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,"Shutting down...\n");
                MiScreenRecoveryUI::SafeCloseDisplayDevice();
                pcVar32 = "userrequested,fastboot";
                goto LAB_0022397c;
              }
              if (iVar10 != 0x14) goto LAB_00223f54;
              (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,"Rebooting...\n");
              MiScreenRecoveryUI::SafeCloseDisplayDevice();
              uVar34 = 0x16;
              pcVar16 = "userrequested,fastboot";
              goto LAB_00223f3c;
            }
            if (iVar10 != 0x13) {
              if (iVar10 == 0x11) {
                pcVar16 = "Rebooting to recovery/fastboot...\n";
                goto LAB_00223e4c;
              }
              if (iVar10 != 0x12) goto LAB_00223f54;
              (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,"Rebooting to recovery...\n");
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_880,"sys.usb.config_disable");
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_a80,&DAT_0015531e);
              android::base::SetProperty((string *)&local_880,(string *)&local_a80);
              if ((local_a80 & 1) != 0) {
                operator_delete(local_a70);
              }
              if (((byte)local_880 & 1) != 0) {
                operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
              }
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_880,"sys.usb.config_disable");
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_a80,&DAT_0015531e);
              uVar15 = android::base::WaitForProperty(&local_880,&local_a80,0x7fffffffffffffff);
              if ((local_a80 & 1) != 0) {
                operator_delete(local_a70);
              }
              if (((byte)local_880 & 1) != 0) {
                operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
              }
              if (((uVar15 & 1) == 0) && (uVar15 = android::base::ShouldLog(4,0), (uVar15 & 1) != 0)
                 ) {
                uVar39 = *(undefined4 *)pcVar16;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_880,"bootable/recovery/recovery_main.cpp",0x27b,4,0,
                           0xffffffff);
                poVar30 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_880);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar30,"failed to setproperty sys.usb.config_disable to 0",0x31);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_880);
                *(undefined4 *)pcVar16 = uVar39;
              }
              (**(code **)(*DAT_0042ceb0 + 0x10))(DAT_0042ceb0,0);
              MiScreenRecoveryUI::SafeCloseDisplayDevice();
              uVar34 = 8;
              pcVar16 = "recovery";
              goto LAB_00223f3c;
            }
            memset(&local_880,0,0x800);
            psVar25 = (string *)0x20;
            __strlcpy_chk(&local_880,"boot-rescue",0x20,0x20);
            local_a80 = 0;
            uStack_a78 = 0;
            local_a70 = (void *)0x0;
            uVar15 = write_bootloader_message(&local_880,(string *)&local_a80);
            if ((uVar15 & 1) != 0) {
              (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,"Rebooting to recovery/rescue...\n")
              ;
              MiScreenRecoveryUI::SafeCloseDisplayDevice();
              if (*(long *)(lVar2 + 0x28) == local_78) {
                Reboot("recovery",8);
                goto LAB_00223db8;
              }
              goto LAB_0022489c;
            }
            uVar15 = android::base::ShouldLog(4,0);
            if ((uVar15 & 1) != 0) {
              uVar39 = *(undefined4 *)pcVar16;
              android::base::LogMessage::LogMessage
                        (aLStack_ad8,"bootable/recovery/recovery_main.cpp",0x289,4,0,0xffffffff);
              poVar30 = (ostream *)android::base::LogMessage::stream(aLStack_ad8);
              psVar25 = (string *)0x24;
              poVar30 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar30,"Failed to write bootloader message: ",0x24);
              std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
              operator<(poVar30,(string *)&local_a80);
              android::base::LogMessage::~LogMessage(aLStack_ad8);
              *(undefined4 *)pcVar16 = uVar39;
            }
            if ((local_a80 & 1) != 0) {
              operator_delete(local_a70);
            }
          }
        }
        if ((local_a68 & 1) != 0) {
          operator_delete(local_a58);
        }
        if ((local_a50 & 1) != 0) {
          operator_delete(local_a40);
        }
        goto LAB_00223608;
      }
      puts("Failed to initialize UI; using stub UI instead.");
      this = operator_new(0x838);
      memset(this,0,0x838);
      RecoveryUI::RecoveryUI(this);
    }
    else {
      puts("Quiescent recovery mode.");
      this = operator_new(0x838);
      memset(this,0,0x838);
      RecoveryUI::RecoveryUI(this);
    }
    *(undefined ***)this = &PTR__RecoveryUI_00426ec8;
    (**(code **)(*(long *)this_00 + 0x18))(this_00,this);
    if (((ulong)local_a38 & 1) != 0) {
      operator_delete(local_a28);
    }
    if ((local_a20 & 1) != 0) {
      operator_delete(local_a10);
    }
    if (local_a00 != (char *)0x0) {
      local_9f8 = local_a00;
      operator_delete(local_a00);
    }
    psVar25 = local_ac8;
    psVar19 = local_ac0;
    if (local_ac8 != (string *)0x0) {
      while (psVar37 = psVar19, psVar37 != psVar25) {
        psVar19 = psVar37 + -0x18;
        if (((byte)*psVar19 & 1) != 0) {
          operator_delete(*(void **)(psVar37 + -8));
        }
      }
      local_ac0 = psVar25;
      operator_delete(local_ac8);
    }
    if ((local_ab0 & 1) != 0) {
      operator_delete(local_aa0);
    }
    uVar34 = 1;
  }
  if (*(long *)(lVar2 + 0x28) == local_78) {
    return uVar34;
  }
LAB_0022489c:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
LAB_00223df8:
  pcVar16 = "Partitions may be mounted - rebooting to enter fastboot.";
LAB_00223e4c:
  (**(code **)(*DAT_0042ceb0 + 0x158))(DAT_0042ceb0,pcVar16);
  MiScreenRecoveryUI::SafeCloseDisplayDevice();
  uVar34 = 8;
  pcVar16 = "fastboot";
LAB_00223f3c:
  if (*(long *)(lVar2 + 0x28) == local_78) {
    Reboot(pcVar16,uVar34);
LAB_00223f54:
    (**(code **)(*plVar24 + 0x158))(plVar24,"Rebooting...\n");
    MiScreenRecoveryUI::SafeCloseDisplayDevice();
    std::to_string(iVar10);
    std::operator+[abi_ne180000_<char,std::char_traits<char>,std::allocator<char>>
              ("unknown",(string *)&local_a80);
    uVar15 = (ulong)((byte)local_880 >> 1);
    uVar38 = (ulong)&local_880 | 1;
    if (((byte)local_880 & 1) != 0) {
      uVar15 = CONCAT71(uStack_877,cStack_878);
      uVar38 = CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870));
    }
    if (*(long *)(lVar2 + 0x28) == local_78) {
      uVar34 = Reboot(uVar38,uVar15);
      if (((byte)local_880 & 1) != 0) {
        operator_delete((void *)CONCAT53(uStack_86d,CONCAT21(uStack_86f,local_870)));
      }
      if ((local_a80 & 1) != 0) {
        operator_delete(local_a70);
      }
      if ((local_a68 & 1) != 0) {
        operator_delete(local_a58);
      }
      if ((local_a50 & 1) != 0) {
        operator_delete(local_a40);
      }
      std::thread::~thread((thread *)&local_9e8);
      std::pair<std::string_const,std::string>::~pair
                ((pair<std::string_const,std::string> *)&local_9e0);
      if (((ulong)local_a38 & 1) != 0) {
        operator_delete(local_a28);
      }
      if ((local_a20 & 1) != 0) {
        operator_delete(local_a10);
      }
      if (local_a00 != (char *)0x0) {
        local_9f8 = local_a00;
        operator_delete(local_a00);
      }
      std::vector<std::string,std::allocator<std::string>>::~vector_abi_ne180000_
                ((vector<std::string,std::allocator<std::string>> *)&local_ac8);
      if ((local_ab0 & 1) != 0) {
        operator_delete(local_aa0);
      }
      if (*(long *)(lVar2 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
        _Unwind_Resume(uVar34);
      }
    }
  }
  goto LAB_0022489c;
}
