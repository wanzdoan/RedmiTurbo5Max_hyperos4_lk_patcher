
/* WipeData(Device*, bool, bool) */

uint WipeData(Device *param_1,bool param_2,bool param_3)

{
  ostream *poVar1;
  ulong uVar2;
  void *pvVar3;
  long lVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  bool bVar7;
  bool bVar8;
  int iVar9;
  uint uVar10;
  uint uVar11;
  RecoveryUI *pRVar12;
  ulong uVar13;
  char *pcVar14;
  long lVar15;
  ostream *poVar16;
  ostream *poVar17;
  size_t sVar18;
  long lVar19;
  undefined8 *puVar20;
  ulong *puVar21;
  undefined4 *puVar22;
  undefined1 uVar23;
  undefined8 uVar24;
  undefined4 uVar25;
  char *pcVar26;
  void *pvVar27;
  undefined8 uVar28;
  undefined8 uVar29;
  byte local_150;
  char local_14f [8];
  undefined4 local_147;
  undefined1 local_143;
  void *local_140;
  ulong local_138;
  ostream *local_130;
  void *local_128;
  ulong local_120;
  undefined8 uStack_118;
  char *local_110;
  ulong local_108;
  undefined8 local_100;
  char *local_f8;
  ulong local_f0;
  undefined8 local_e8;
  char *local_e0;
  ulong local_d8;
  size_t local_d0;
  char *local_c8;
  ulong local_c0;
  ulong uStack_b8;
  void *local_b0;
  ulong local_a8;
  undefined8 local_a0;
  char *local_98;
  base local_90;
  undefined4 uStack_8f;
  undefined2 uStack_8b;
  char cStack_89;
  char cStack_88;
  undefined2 uStack_87;
  undefined5 uStack_85;
  char cStack_80;
  undefined1 uStack_7f;
  undefined6 uStack_7e;
  long local_78;

  lVar4 = tpidr_el0;
  local_78 = *(long *)(lVar4 + 0x28);
  uVar23 = param_3;
  pRVar12 = (RecoveryUI *)(**(code **)(*(long *)param_1 + 0x10))();
  (**(code **)(*(long *)pRVar12 + 0x158))(pRVar12,"\n-- Wiping data...\n");
  (**(code **)(*(long *)pRVar12 + 0x118))(pRVar12,3);
  (**(code **)(*(long *)pRVar12 + 0x128))(pRVar12,1);
  uVar13 = FinishPendingSnapshotMerges(param_1);
  if ((uVar13 & 1) == 0) {
    (**(code **)(*(long *)pRVar12 + 0x158))
              (pRVar12,"Unable to check update status or complete merge, cannot wipe partitions.\n")
    ;
    uVar10 = 0;
  }
  else {
    pcVar14 = operator_new(0x20);
    pcVar14[0x1b] = '\0';
    uVar29 = s_vendor_wipedata_savedataapp_0015695d._19_8_;
    uVar28 = s_vendor_wipedata_savedataapp_0015695d._0_8_;
    uVar24 = CONCAT35(s_vendor_wipedata_savedataapp_0015695d._16_3_,
                      s_vendor_wipedata_savedataapp_0015695d._11_5_);
    cStack_88 = 0x1b;
    uStack_87 = 0;
    uStack_85 = 0;
    cStack_80 = (char)pcVar14;
    uStack_7f = (undefined1)((ulong)pcVar14 >> 8);
    uStack_7e = (undefined6)((ulong)pcVar14 >> 0x10);
    local_90 = (base)0x21;
    uStack_8f = 0;
    uStack_8b = 0;
    cStack_89 = 0;
    *(ulong *)(pcVar14 + 8) =
         CONCAT53(s_vendor_wipedata_savedataapp_0015695d._11_5_,
                  s_vendor_wipedata_savedataapp_0015695d._8_3_);
    *(undefined8 *)pcVar14 = uVar28;
    *(undefined8 *)(pcVar14 + 0x13) = uVar29;
    *(undefined8 *)(pcVar14 + 0xb) = uVar24;
    uVar13 = android::base::GetBoolProperty(&local_90,(string *)0x0,(bool)uVar23);
    if ((uVar13 & 1) == 0) {
      pcVar14 = DAT_0057b6b8;
      if ((DAT_0057b6a8 & 1) == 0) {
        pcVar14 = &DAT_0057b6a9;
      }
      iVar9 = access(pcVar14,4);
      bVar7 = iVar9 == 0;
    }
    else {
      bVar7 = false;
    }
    if (((byte)local_90 & 1) != 0) {
      operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
    }
    if (bVar7 || param_3) {
      iVar9 = access("/cache/recovery/last_format_data",4);
      bVar8 = iVar9 != 0;
      if (!bVar7) goto LAB_0027002c;
LAB_0027000c:
      uVar13 = MiuiPreWipeData();
      if ((uVar13 & 1) == 0) {
        uVar13 = android::base::ShouldLog(3,0);
        if ((uVar13 & 1) == 0) goto LAB_0027002c;
        puVar22 = (undefined4 *)__errno();
        uVar25 = *puVar22;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x12d,3,0,
                   uVar25);
        poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar16,"Miui Pre WipeData failed!",0x19);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
        bVar7 = false;
        *puVar22 = uVar25;
      }
      else {
        bVar7 = true;
      }
    }
    else {
      bVar8 = false;
      if (bVar7) goto LAB_0027000c;
LAB_0027002c:
      bVar7 = false;
    }
    uStack_118 = 0;
    local_110 = (char *)0x0;
    local_120 = 0x65736c61660a;
    local_150 = 0x18;
    local_138 = 0;
    local_130 = (ostream *)0x0;
    local_128 = (void *)0x0;
    local_143 = 0;
    local_147 = 0x746f6873;
    local_14f[0] = s__ufssnapshot_001531bf[0];
    local_14f[1] = s__ufssnapshot_001531bf[1];
    local_14f[2] = s__ufssnapshot_001531bf[2];
    local_14f[3] = s__ufssnapshot_001531bf[3];
    local_14f[4] = s__ufssnapshot_001531bf[4];
    local_14f[5] = s__ufssnapshot_001531bf[5];
    local_14f[6] = s__ufssnapshot_001531bf[6];
    local_14f[7] = s__ufssnapshot_001531bf[7];
    getUfsDevicePath(&local_138,&local_150);
    if ((local_150 & 1) != 0) {
      operator_delete(local_140);
    }
    uVar13 = local_138;
    pcVar14 = (char *)(local_138 & 0xff);
    cStack_88 = '\0';
    uStack_87 = 0;
    uStack_85 = 0;
    cStack_80 = '\0';
    uStack_7f = 0;
    uStack_7e = 0;
    local_90 = (base)0x0;
    uStack_8f = 0;
    uStack_8b = 0;
    cStack_89 = '\0';
    poVar16 = (ostream *)(local_138 >> 1 & 0x7f);
    if ((local_138 & 1) != 0) {
      poVar16 = local_130;
    }
    poVar17 = poVar16 + 8;
    if ((ostream *)0xffffffffffffffef < poVar17) {
      if (*(long *)(lVar4 + 0x28) != local_78) goto LAB_002710e4;
      std::string::__throw_length_error_abi_ne180000_();
LAB_00270df0:
      if (*(long *)(lVar4 + 0x28) != local_78) goto LAB_002710e4;
      std::string::__throw_length_error_abi_ne180000_();
LAB_00270e08:
      if (*(long *)(lVar4 + 0x28) != local_78) goto LAB_002710e4;
      std::string::__throw_length_error_abi_ne180000_();
LAB_00270e20:
      if (*(long *)(lVar4 + 0x28) == local_78) {
        uVar24 = std::string::__throw_length_error_abi_ne180000_();
        *(int *)poVar16 = (int)pcVar14;
        if ((local_138 & 1) != 0) {
          operator_delete(local_128);
        }
        if ((local_120 & 1) != 0) {
          operator_delete(local_110);
        }
        if (*(long *)(lVar4 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
          _Unwind_Resume(uVar24);
        }
      }
      goto LAB_002710e4;
    }
    if (poVar17 < (ostream *)0x17) {
      pvVar27 = (void *)((ulong)&local_90 | 1);
      local_90 = SUB41((int)poVar17 << 1,0);
      if (poVar16 != (ostream *)0x0) goto LAB_0027010c;
    }
    else {
      uVar2 = ((ulong)poVar17 | 0xf) + 1;
      pvVar27 = operator_new(uVar2);
      cStack_88 = (char)poVar17;
      uStack_87 = (undefined2)((ulong)poVar17 >> 8);
      uStack_85 = (undefined5)((ulong)poVar17 >> 0x18);
      cStack_80 = (char)pvVar27;
      uStack_7f = (undefined1)((ulong)pvVar27 >> 8);
      uStack_7e = (undefined6)((ulong)pvVar27 >> 0x10);
      local_90 = (base)((byte)uVar2 | 1);
      uStack_8f = (undefined4)(uVar2 >> 8);
      uStack_8b = (undefined2)(uVar2 >> 0x28);
      cStack_89 = (char)(uVar2 >> 0x38);
LAB_0027010c:
      pvVar3 = (void *)((ulong)&local_138 | 1);
      if ((uVar13 & 1) != 0) {
        pvVar3 = local_128;
      }
      memmove(pvVar27,pvVar3,(size_t)poVar16);
    }
    ((ostream *)((long)pvVar27 + (long)poVar16))[8] = (ostream)0x0;
    *(undefined8 *)((long)pvVar27 + (long)poVar16) = 0x74726f707075732f;
    android::base::ReadFileToString((string *)&local_90,(string *)&local_120,false);
    if (((byte)local_90 & 1) != 0) {
      operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
    }
    uVar13 = set_disable_systemrestore();
    if ((uVar13 & 1) == 0) {
      pcVar14 = (char *)((ulong)&local_120 | 1);
      pcVar26 = pcVar14;
      if ((local_120 & 1) != 0) {
        pcVar26 = local_110;
      }
      iVar9 = strncmp(pcVar26,"true",4);
      uVar13 = local_138;
      if (iVar9 != 0) goto LAB_0027019c;
      cStack_88 = '\0';
      uStack_87 = 0;
      uStack_85 = 0;
      cStack_80 = '\0';
      uStack_7f = 0;
      uStack_7e = 0;
      local_90 = (base)0x0;
      uStack_8f = 0;
      uStack_8b = 0;
      cStack_89 = '\0';
      poVar16 = (ostream *)(local_138 >> 1 & 0x7f);
      if ((local_138 & 1) != 0) {
        poVar16 = local_130;
      }
      poVar17 = poVar16 + 7;
      if ((ostream *)0xffffffffffffffef < poVar17) goto LAB_00270e08;
      if (poVar17 < (ostream *)0x17) {
        pvVar27 = (void *)((ulong)&local_90 | 1);
        local_90 = SUB41((int)poVar17 << 1,0);
        if (poVar16 != (ostream *)0x0) goto LAB_00270448;
      }
      else {
        uVar2 = ((ulong)poVar17 | 0xf) + 1;
        pvVar27 = operator_new(uVar2);
        cStack_88 = (char)poVar17;
        uStack_87 = (undefined2)((ulong)poVar17 >> 8);
        uStack_85 = (undefined5)((ulong)poVar17 >> 0x18);
        cStack_80 = (char)pvVar27;
        uStack_7f = (undefined1)((ulong)pvVar27 >> 8);
        uStack_7e = (undefined6)((ulong)pvVar27 >> 0x10);
        local_90 = (base)((byte)uVar2 | 1);
        uStack_8f = (undefined4)(uVar2 >> 8);
        uStack_8b = (undefined2)(uVar2 >> 0x28);
        cStack_89 = (char)(uVar2 >> 0x38);
LAB_00270448:
        pvVar3 = (void *)((ulong)&local_138 | 1);
        if ((uVar13 & 1) != 0) {
          pvVar3 = local_128;
        }
        memmove(pvVar27,pvVar3,(size_t)poVar16);
      }
      poVar16 = (ostream *)((long)pvVar27 + (long)poVar16);
      *(undefined4 *)(poVar16 + 3) = 0x73757461;
      *(undefined4 *)poVar16 = 0x6174732f;
      poVar16[7] = (ostream)0x0;
      uVar13 = android::base::ReadFileToString((string *)&local_90,(string *)&local_120,false);
      poVar16 = (ostream *)(uVar13 & 0xffffffff);
      if (((byte)local_90 & 1) != 0) {
        operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
      }
      if ((uVar13 & 1) == 0) {
        uVar13 = android::base::ShouldLog(4,0);
        if ((uVar13 & 1) != 0) {
          puVar22 = (undefined4 *)__errno();
          uVar25 = *puVar22;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_a8,"bootable/recovery/install/wipe_data.cpp",0x14d,4,0,
                     uVar25);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a8);
          poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar16,"Failed to read ",0xf);
          std::operator+[abi_ne180000_<char,std::char_traits<char>,std::allocator<char>>
                    ((string *)&local_138,"/status");
          std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (poVar16,(string *)&local_90);
          if (((byte)local_90 & 1) != 0) {
            operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
          }
          lVar15 = -0x48;
          goto LAB_00270d18;
        }
LAB_00270710:
        uVar10 = 0;
      }
      else {
        pcVar26 = pcVar14;
        if ((local_120 & 1) != 0) {
          pcVar26 = local_110;
        }
        iVar9 = strncmp(pcVar26,"complete_backup",0xf);
        if (iVar9 != 0) goto LAB_0027019c;
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                  (&local_90,&DAT_00157af6);
        std::operator+[abi_ne180000_<char,std::char_traits<char>,std::allocator<char>>
                  ((string *)&local_138,"/ctrl");
        uVar13 = android::base::WriteStringToFile((string *)&local_90,(string *)&local_a8,false);
        if ((local_a8 & 1) == 0) {
          if (((byte)local_90 & 1) == 0) goto LAB_00270520;
LAB_002709e8:
          operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
          if ((uVar13 & 1) == 0) goto LAB_002709f4;
LAB_00270524:
          lVar15 = android::base::boot_clock::now();
          while (lVar19 = android::base::boot_clock::now(), uVar13 = local_138,
                lVar19 - lVar15 < 10000000000) {
            poVar16 = (ostream *)(local_138 & 0xff);
            cStack_88 = '\0';
            uStack_87 = 0;
            uStack_85 = 0;
            cStack_80 = '\0';
            uStack_7f = 0;
            uStack_7e = 0;
            local_90 = (base)0x0;
            uStack_8f = 0;
            uStack_8b = 0;
            cStack_89 = '\0';
            poVar17 = (ostream *)(local_138 >> 1 & 0x7f);
            if ((local_138 & 1) != 0) {
              poVar17 = local_130;
            }
            poVar1 = poVar17 + 7;
            if ((ostream *)0xffffffffffffffef < poVar1) goto LAB_00270df0;
            if (poVar1 < (ostream *)0x17) {
              local_90 = SUB41((int)poVar1 << 1,0);
              pvVar27 = (void *)((ulong)&local_90 | 1);
              if (poVar17 != (ostream *)0x0) goto LAB_002705cc;
            }
            else {
              uVar2 = ((ulong)poVar1 | 0xf) + 1;
              pvVar27 = operator_new(uVar2);
              cStack_88 = (char)poVar1;
              uStack_87 = (undefined2)((ulong)poVar1 >> 8);
              uStack_85 = (undefined5)((ulong)poVar1 >> 0x18);
              cStack_80 = (char)pvVar27;
              uStack_7f = (undefined1)((ulong)pvVar27 >> 8);
              uStack_7e = (undefined6)((ulong)pvVar27 >> 0x10);
              local_90 = (base)((byte)uVar2 | 1);
              uStack_8f = (undefined4)(uVar2 >> 8);
              uStack_8b = (undefined2)(uVar2 >> 0x28);
              cStack_89 = (char)(uVar2 >> 0x38);
LAB_002705cc:
              pvVar3 = (void *)((ulong)&local_138 | 1);
              if ((uVar13 & 1) != 0) {
                pvVar3 = local_128;
              }
              memmove(pvVar27,pvVar3,(size_t)poVar17);
            }
            poVar17 = (ostream *)((long)pvVar27 + (long)poVar17);
            poVar17[7] = (ostream)0x0;
            *(undefined4 *)(poVar17 + 3) = 0x73757461;
            *(undefined4 *)poVar17 = 0x6174732f;
            uVar13 = android::base::ReadFileToString((string *)&local_90,(string *)&local_120,false)
            ;
            poVar16 = (ostream *)(uVar13 & 0xffffffff);
            if (((byte)local_90 & 1) != 0) {
              operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
            }
            if (((uVar13 & 1) == 0) && (uVar13 = android::base::ShouldLog(4,0), (uVar13 & 1) != 0))
            {
              puVar22 = (undefined4 *)__errno();
              uVar25 = *puVar22;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_a8,"bootable/recovery/install/wipe_data.cpp",0x13e,4,0
                         ,uVar25);
              poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a8);
              poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar16,"Failed to read ",0xf);
              std::operator+[abi_ne180000_<char,std::char_traits<char>,std::allocator<char>>
                        ((string *)&local_138,"/status");
              std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
              operator<(poVar16,(string *)&local_90);
              if (((byte)local_90 & 1) != 0) {
                operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
              }
              android::base::LogMessage::~LogMessage((LogMessage *)&local_a8);
              *puVar22 = uVar25;
            }
            pcVar26 = pcVar14;
            if ((local_120 & 1) != 0) {
              pcVar26 = local_110;
            }
            iVar9 = strncmp(pcVar26,"idle",4);
            if (iVar9 == 0) {
              uVar13 = android::base::ShouldLog(4,0);
              if ((uVar13 & 1) != 0) {
                poVar16 = (ostream *)__errno();
                iVar9 = *(int *)poVar16;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x142,4
                           ,0,0xffffffff);
                poVar17 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar17,"Snapshot backup cleared successfully!",0x25);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
                *(int *)poVar16 = iVar9;
              }
              goto LAB_0027019c;
            }
            sleep(1);
          }
          uVar13 = android::base::ShouldLog(4,0);
          if ((uVar13 & 1) == 0) goto LAB_00270710;
          puVar22 = (undefined4 *)__errno();
          uVar25 = *puVar22;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x148,4,0,
                     uVar25);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar16,"Failed to clear snapshot backup",0x1f);
        }
        else {
          operator_delete(local_98);
          if (((byte)local_90 & 1) != 0) goto LAB_002709e8;
LAB_00270520:
          if ((uVar13 & 1) != 0) goto LAB_00270524;
LAB_002709f4:
          uVar13 = android::base::ShouldLog(4,0);
          if ((uVar13 & 1) == 0) goto LAB_00270710;
          puVar22 = (undefined4 *)__errno();
          uVar25 = *puVar22;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x138,4,0,
                     uVar25);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
          poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar16,"Failed to write ",0x10);
          std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (poVar16,(string *)&local_138);
        }
        lVar15 = -0x30;
LAB_00270d18:
        android::base::LogMessage::~LogMessage((LogMessage *)(&stack0xffffffffffffffa0 + lVar15));
        uVar10 = 0;
        *puVar22 = uVar25;
      }
    }
    else {
LAB_0027019c:
      uVar13 = (**(code **)(*(long *)param_1 + 0x60))(param_1);
      if ((uVar13 & 1) == 0) {
LAB_002702e8:
        uVar10 = 0;
      }
      else {
        uVar10 = EraseVolume("/data",pRVar12,param_2);
        cStack_89 = 0;
        local_90 = (base)0xc;
        uStack_8f = 0x6361632f;
        uStack_8b = 0x6568;
        lVar15 = volume_for_mount_point((string *)&local_90);
        if (((byte)local_90 & 1) != 0) {
          operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
        }
        if (lVar15 != 0) {
          uVar11 = EraseVolume("/cache",pRVar12,false);
          uVar10 = uVar10 & uVar11;
        }
        local_90 = (base)0x12;
        uStack_87 = 0x61;
        uStack_8f = (undefined4)s__metadata_001548c6._0_8_;
        uStack_8b = SUB82(s__metadata_001548c6._0_8_,4);
        cStack_89 = SUB81(s__metadata_001548c6._0_8_,6);
        cStack_88 = SUB81(s__metadata_001548c6._0_8_,7);
        poVar16 = (ostream *)volume_for_mount_point((string *)&local_90);
        if (((byte)local_90 & 1) != 0) {
          operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
        }
        if (poVar16 != (ostream *)0x0) {
          uVar11 = EraseVolume("/metadata",pRVar12,false);
          uVar10 = uVar10 & uVar11;
        }
        if ((uVar10 & 1) == 0) goto LAB_002702e8;
        uVar10 = (**(code **)(*(long *)param_1 + 0x68))(param_1);
      }
      if (bVar7) {
        local_90 = (base)0xa;
        uStack_8f = 0x7461642f;
        uStack_8b = 0x61;
        iVar9 = ensure_path_mounted((string *)&local_90);
        if (((byte)local_90 & 1) != 0) {
          operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
        }
        if (iVar9 == 0) {
          uStack_7f = 0;
          local_90 = (base)0x20;
          uStack_87 = (undefined2)s__data_preinstall_00157afb._8_8_;
          uStack_85 = SUB85(s__data_preinstall_00157afb._8_8_,2);
          cStack_80 = SUB81(s__data_preinstall_00157afb._8_8_,7);
          uStack_8f = (undefined4)s__data_preinstall_00157afb._0_8_;
          uStack_8b = SUB82(s__data_preinstall_00157afb._0_8_,4);
          cStack_89 = SUB81(s__data_preinstall_00157afb._0_8_,6);
          cStack_88 = SUB81(s__data_preinstall_00157afb._0_8_,7);
          iVar9 = mkdir_recursively((string *)&local_90,0x1a4,false,(selabel_handle *)0x0);
          if (((byte)local_90 & 1) != 0) {
            operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
          }
          if (iVar9 == 0) {
            chown("/data/preinstall",0,0);
            chmod("/data/preinstall",0x1a4);
            pcVar14 = DAT_0057b6a0;
            if ((DAT_0057b690 & 1) == 0) {
              pcVar14 = &DAT_0057b691;
            }
            iVar9 = access(pcVar14,0);
            if (iVar9 == -1) {
              uVar13 = android::base::ShouldLog(4,0);
              if ((uVar13 & 1) != 0) {
                puVar22 = (undefined4 *)__errno();
                uVar25 = *puVar22;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x10a,4
                           ,0,uVar25);
                poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar16,"[MiuiPostWipeData] cust tmp img not existent",0x2c);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
                *puVar22 = uVar25;
              }
              local_e0 = operator_new(0x30);
              local_e0[0x2d] = '\0';
              uVar6 = s__MiuiPostWipeData__cust_tmp_img_n_0015b883._37_8_;
              uVar5 = s__MiuiPostWipeData__cust_tmp_img_n_0015b883._16_8_;
              uVar29 = s__MiuiPostWipeData__cust_tmp_img_n_0015b883._0_8_;
              uVar24 = CONCAT35(s__MiuiPostWipeData__cust_tmp_img_n_0015b883._29_3_,
                                s__MiuiPostWipeData__cust_tmp_img_n_0015b883._24_5_);
              local_e8 = 0x2d;
              local_f0 = 0x31;
              uVar28 = CONCAT53(s__MiuiPostWipeData__cust_tmp_img_n_0015b883._32_5_,
                                s__MiuiPostWipeData__cust_tmp_img_n_0015b883._29_3_);
              *(undefined8 *)(local_e0 + 8) = s__MiuiPostWipeData__cust_tmp_img_n_0015b883._8_8_;
              *(undefined8 *)local_e0 = uVar29;
              *(undefined8 *)(local_e0 + 0x18) = uVar24;
              *(undefined8 *)(local_e0 + 0x10) = uVar5;
              *(undefined8 *)(local_e0 + 0x25) = uVar6;
              *(undefined8 *)(local_e0 + 0x1d) = uVar28;
              LogVirtualPartitionCriticalInfo(&local_f0);
              pcVar14 = local_e0;
              uVar13 = local_f0;
            }
            else {
              uVar13 = copyFile((string *)&DAT_0057b690,(string *)&DAT_0057b678);
              if ((uVar13 & 1) != 0) goto joined_r0x002708d0;
              uVar13 = android::base::ShouldLog(4,0);
              if ((uVar13 & 1) != 0) {
                puVar22 = (undefined4 *)__errno();
                uVar25 = *puVar22;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x110,4
                           ,0,uVar25);
                poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar16,"[MiuiPostWipeData] Copy fail failed!",0x24);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
                *puVar22 = uVar25;
              }
              local_f8 = operator_new(0x30);
              uVar5 = s__MiuiPostWipeData__Copy_fail_fai_00146b40._24_8_;
              uVar29 = s__MiuiPostWipeData__Copy_fail_fai_00146b40._16_8_;
              uVar28 = s__MiuiPostWipeData__Copy_fail_fai_00146b40._8_8_;
              uVar24 = s__MiuiPostWipeData__Copy_fail_fai_00146b40._0_8_;
              local_108 = 0x31;
              local_100 = 0x24;
              local_f8[0x20] = 'l';
              local_f8[0x21] = 'e';
              local_f8[0x22] = 'd';
              local_f8[0x23] = '!';
              local_f8[0x24] = '\0';
              *(undefined8 *)(local_f8 + 8) = uVar28;
              *(undefined8 *)local_f8 = uVar24;
              *(undefined8 *)(local_f8 + 0x18) = uVar5;
              *(undefined8 *)(local_f8 + 0x10) = uVar29;
              LogVirtualPartitionCriticalInfo(&local_108);
              pcVar14 = local_f8;
              uVar13 = local_108;
            }
          }
          else {
            uVar13 = android::base::ShouldLog(4,0);
            poVar17 = (ostream *)__errno();
            iVar9 = *(int *)poVar17;
            if ((uVar13 & 1) != 0) {
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x100,4,0
                         ,0xffffffff);
              poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
              poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar16,"[MiuiPostWipeData] failed to mkdir /data/preinstall (",
                                   0x35);
              pcVar14 = strerror(*(int *)poVar17);
              sVar18 = strlen(pcVar14);
              poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar16,pcVar14,sVar18);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar16,")",1);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
              *(int *)poVar17 = iVar9;
              poVar16 = poVar17;
            }
            pcVar14 = strerror(iVar9);
            sVar18 = strlen(pcVar14);
            if (0xffffffffffffffef < sVar18) goto LAB_00270e20;
            if (sVar18 < 0x17) {
              pcVar26 = (char *)((ulong)&local_d8 | 1);
              local_d8 = CONCAT71(local_d8._1_7_,(char)((int)sVar18 << 1));
              if (sVar18 != 0) goto LAB_00270804;
            }
            else {
              uVar13 = (sVar18 | 0xf) + 1;
              pcVar26 = operator_new(uVar13);
              local_d8 = uVar13 | 1;
              local_d0 = sVar18;
              local_c8 = pcVar26;
LAB_00270804:
              memmove(pcVar26,pcVar14,sVar18);
            }
            pcVar26[sVar18] = '\0';
            puVar20 = (undefined8 *)std::string::insert((ulong)&local_d8,(char *)0x0,0x146b0a);
            uVar24 = puVar20[2];
            uVar29 = puVar20[1];
            uVar28 = *puVar20;
            cStack_80 = (char)uVar24;
            uStack_7f = (undefined1)((ulong)uVar24 >> 8);
            uStack_7e = (undefined6)((ulong)uVar24 >> 0x10);
            cStack_88 = (char)uVar29;
            uStack_87 = (undefined2)((ulong)uVar29 >> 8);
            uStack_85 = (undefined5)((ulong)uVar29 >> 0x18);
            local_90 = SUB81(uVar28,0);
            uStack_8f = (undefined4)((ulong)uVar28 >> 8);
            uStack_8b = (undefined2)((ulong)uVar28 >> 0x28);
            cStack_89 = (char)((ulong)uVar28 >> 0x38);
            puVar20[1] = 0;
            puVar20[2] = 0;
            *puVar20 = 0;
            puVar21 = (ulong *)std::string::append((char *)&local_90,0x14b8d0);
            local_b0 = (void *)puVar21[2];
            uStack_b8 = puVar21[1];
            local_c0 = *puVar21;
            puVar21[1] = 0;
            puVar21[2] = 0;
            *puVar21 = 0;
            LogVirtualPartitionCriticalInfo(&local_c0);
            if ((local_c0 & 1) != 0) {
              operator_delete(local_b0);
            }
            pcVar14 = local_c8;
            uVar13 = local_d8;
            if (((byte)local_90 & 1) != 0) {
              operator_delete((void *)CONCAT62(uStack_7e,CONCAT11(uStack_7f,cStack_80)));
              pcVar14 = local_c8;
              uVar13 = local_d8;
            }
          }
        }
        else {
          uVar13 = android::base::ShouldLog(4,0);
          if ((uVar13 & 1) != 0) {
            puVar22 = (undefined4 *)__errno();
            uVar25 = *puVar22;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0xfa,4,0,
                       uVar25);
            poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar16,"[MiuiPostWipeData] failed to mount userdata",0x2b);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
            *puVar22 = uVar25;
          }
          local_98 = operator_new(0x30);
          local_98[0x2b] = '\0';
          uVar6 = s__MiuiPostWipeData__failed_to_mou_0015abd4._35_8_;
          uVar5 = s__MiuiPostWipeData__failed_to_mou_0015abd4._16_8_;
          uVar29 = s__MiuiPostWipeData__failed_to_mou_0015abd4._0_8_;
          uVar24 = CONCAT53(s__MiuiPostWipeData__failed_to_mou_0015abd4._27_5_,
                            s__MiuiPostWipeData__failed_to_mou_0015abd4._24_3_);
          local_a0 = 0x2b;
          local_a8 = 0x31;
          uVar28 = CONCAT35(s__MiuiPostWipeData__failed_to_mou_0015abd4._32_3_,
                            s__MiuiPostWipeData__failed_to_mou_0015abd4._27_5_);
          *(undefined8 *)(local_98 + 8) = s__MiuiPostWipeData__failed_to_mou_0015abd4._8_8_;
          *(undefined8 *)local_98 = uVar29;
          *(undefined8 *)(local_98 + 0x18) = uVar24;
          *(undefined8 *)(local_98 + 0x10) = uVar5;
          *(undefined8 *)(local_98 + 0x23) = uVar6;
          *(undefined8 *)(local_98 + 0x1b) = uVar28;
          LogVirtualPartitionCriticalInfo(&local_a8);
          pcVar14 = local_98;
          uVar13 = local_a8;
        }
        if ((uVar13 & 1) != 0) {
          operator_delete(pcVar14);
        }
        uVar13 = android::base::ShouldLog(4,0);
        if ((uVar13 & 1) != 0) {
          puVar22 = (undefined4 *)__errno();
          uVar25 = *puVar22;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_90,"bootable/recovery/install/wipe_data.cpp",0x164,4,0,
                     0xffffffff);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar16,"Miui Post wipe data failed!",0x1b);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
          *puVar22 = uVar25;
        }
      }
joined_r0x002708d0:
      if (bVar8) {
        (**(code **)(*(long *)pRVar12 + 0x158))(pRVar12,"Remove last format flag!\n");
        unlink("/cache/recovery/last_format_data");
      }
      pcVar14 = "complete";
      if ((uVar10 & 1) == 0) {
        pcVar14 = "failed";
      }
      (**(code **)(*(long *)pRVar12 + 0x158))(pRVar12,"Data wipe %s.\n",pcVar14);
    }
    if ((local_138 & 1) != 0) {
      operator_delete(local_128);
    }
    if ((local_120 & 1) != 0) {
      operator_delete(local_110);
    }
  }
  if (*(long *)(lVar4 + 0x28) == local_78) {
    return uVar10 & 1;
  }
LAB_002710e4:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
