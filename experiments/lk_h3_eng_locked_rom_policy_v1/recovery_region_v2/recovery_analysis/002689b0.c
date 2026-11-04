
/* WARNING: Type propagation algorithm not settling */
/* SetupPackageMount(std::string const&, bool*) */

bool SetupPackageMount(string *param_1,bool *param_2)

{
  string *psVar1;
  string sVar2;
  string sVar3;
  long lVar4;
  bool bVar5;
  int iVar6;
  ulong uVar7;
  undefined4 *puVar8;
  ostream *poVar9;
  undefined8 uVar10;
  string *psVar11;
  long lVar12;
  ulong uVar13;
  undefined4 uVar14;
  undefined4 unaff_w22;
  char *pcVar15;
  LogMessage aLStack_98 [8];
  undefined8 local_90;
  size_t sStack_88;
  char *local_80;
  undefined8 local_70;
  undefined8 local_68;
  ulong local_60;
  ulong uStack_58;
  char *local_50;
  long local_48;

  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  if (param_2 == (bool *)0x0) {
    android::base::LogMessage::LogMessage
              ((LogMessage *)&local_60,"bootable/recovery/install/install.cpp",0x318,6,0,0xffffffff)
    ;
    poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_60);
    poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                       (poVar9,"Check failed: should_use_fuse != nullptr",0x28);
    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar9," ",1);
    android::base::LogMessage::~LogMessage((LogMessage *)&local_60);
  }
  uVar7 = (ulong)((byte)*param_1 >> 1);
  if (((byte)*param_1 & 1) != 0) {
    uVar7 = *(ulong *)(param_1 + 8);
  }
  if (uVar7 == 0) {
LAB_00268ae4:
    bVar5 = false;
  }
  else {
    *param_2 = true;
    sVar2 = *param_1;
    psVar11 = *(string **)(param_1 + 0x10);
    if (((byte)sVar2 & 1) == 0) {
      psVar11 = param_1 + 1;
    }
    if (*psVar11 == (string)0x40) {
      local_60 = 0;
      uStack_58 = 0;
      local_50 = (char *)0x0;
      uVar7 = (ulong)((byte)sVar2 >> 1);
      if (((byte)sVar2 & 1) != 0) {
        uVar7 = *(ulong *)(param_1 + 8);
      }
      if (uVar7 == 0) {
        if (*(long *)(lVar4 + 0x28) != local_48) goto LAB_002690f0;
        std::string::__throw_out_of_range_abi_ne180000_();
      }
      else {
        uVar13 = uVar7 - 1;
        if (uVar13 < 0xfffffffffffffff0) {
          if (uVar7 < 0x18) {
            pcVar15 = (char *)((ulong)&local_60 | 1);
            local_60 = (ulong)(byte)((int)uVar13 << 1);
            if (uVar13 != 0) goto LAB_00268b08;
          }
          else {
            pcVar15 = operator_new((uVar13 | 0xf) + 1);
            local_60 = (uVar13 | 0xf) + 2;
            uStack_58 = uVar13;
            local_50 = pcVar15;
LAB_00268b08:
            memmove(pcVar15,psVar11 + 1,uVar13);
          }
          pcVar15[uVar13] = '\0';
          iVar6 = ensure_path_mounted((string *)&local_60);
          bVar5 = iVar6 == 0;
          if (iVar6 == 0) {
LAB_00268de8:
            *param_2 = false;
          }
          else {
            uVar7 = android::base::ShouldLog(4,0);
            if ((uVar7 & 1) != 0) {
              puVar8 = (undefined4 *)__errno();
              uVar14 = *puVar8;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_90,"bootable/recovery/install/install.cpp",0x322,4,0,
                         0xffffffff);
              poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_90);
              poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                 (poVar9,"Failed to mount ",0x10);
              std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
              operator<(poVar9,(string *)&local_60);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_90);
              goto LAB_00268ca8;
            }
          }
          goto joined_r0x00268cb0;
        }
      }
      if (*(long *)(lVar4 + 0x28) == local_48) {
        uVar10 = std::string::__throw_length_error_abi_ne180000_();
        android::base::LogMessage::~LogMessage(aLStack_98);
        *(undefined4 *)param_2 = unaff_w22;
        if ((local_60 & 1) != 0) {
          operator_delete(local_50);
        }
        if (*(long *)(lVar4 + 0x28) == local_48) {
                    /* WARNING: Subroutine does not return */
          _Unwind_Resume(uVar10);
        }
      }
      goto LAB_002690f0;
    }
    uVar7 = (ulong)((byte)sVar2 >> 1);
    if (((byte)sVar2 & 1) != 0) {
      uVar7 = *(ulong *)(param_1 + 8);
    }
    uVar7 = android::base::StartsWith(psVar11,uVar7,"/sdcard/",8);
    if ((uVar7 & 1) != 0) {
      uStack_58 = uStack_58 & 0xffffffffffffff00;
      local_60 = 0x6472616364732f0e;
      ensure_dev_ready((string *)&local_60);
      if ((local_60 & 1) != 0) {
        operator_delete(local_50);
      }
    }
    iVar6 = ensure_path_mounted(param_1);
    if (iVar6 != 0) {
      uVar7 = android::base::ShouldLog(4,0);
      if ((uVar7 & 1) == 0) goto LAB_00268ae4;
      puVar8 = (undefined4 *)__errno();
      uVar14 = *puVar8;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_60,"bootable/recovery/install/install.cpp",0x331,4,0,
                 0xffffffff);
      poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_60);
      poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         (poVar9,"Failed to mount ",0x10);
      std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                (poVar9,param_1);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_60);
      bVar5 = false;
      *puVar8 = uVar14;
      goto LAB_00268dfc;
    }
    psVar11 = param_1 + 1;
    local_70 = 0;
    local_68 = 0;
    local_68 = std::system_category();
    local_60 = 0;
    uStack_58 = 0;
    local_50 = (char *)0x0;
    local_90 = 0;
    sStack_88 = 0;
    uVar7 = (ulong)((byte)*param_1 >> 1);
    psVar1 = psVar11;
    if (((byte)*param_1 & 1) != 0) {
      uVar7 = *(ulong *)(param_1 + 8);
      psVar1 = *(string **)(param_1 + 0x10);
    }
    local_80 = (char *)0x0;
    _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEE6appendB8ne180000IPKcTnNS_9enable_ifIXsr31__has_forward_iterator_categoryIT_EE5valueEiE4typeELi0EEERS5_SA_SA_
              (&local_90,psVar1,psVar1 + uVar7);
    std::__fs::filesystem::__canonical((path *)&local_90,(error_code *)&local_70);
    if ((local_90 & 1) != 0) {
      operator_delete(local_80);
    }
    if ((int)local_70 == 0) {
      if ((local_60 & 1) == 0) {
        sStack_88 = uStack_58;
        local_90 = local_60;
        local_80 = local_50;
      }
      else {
        std::string::__init_copy_ctor_external((string *)&local_90,local_50,uStack_58);
      }
      pcVar15 = local_80;
      sVar2 = *param_1;
      uVar7 = (ulong)((byte)local_90._0_1_ >> 1);
      if ((local_90 & 1) != 0) {
        uVar7 = sStack_88;
      }
      uVar13 = (ulong)((byte)sVar2 >> 1);
      if (((byte)sVar2 & 1) != 0) {
        uVar13 = *(ulong *)(param_1 + 8);
      }
      if (uVar7 == uVar13) {
        psVar1 = psVar11;
        if (((byte)sVar2 & 1) != 0) {
          psVar1 = *(string **)(param_1 + 0x10);
        }
        if ((local_90 & 1) == 0) {
          if (1 < (byte)local_90._0_1_) {
            lVar12 = 0;
            do {
              sVar3 = psVar1[lVar12];
              sVar2 = *(string *)((long)&local_90 + lVar12 + 1);
              bVar5 = (ulong)((byte)local_90._0_1_ >> 1) - 1 != lVar12;
              lVar12 = lVar12 + 1;
            } while (sVar2 == sVar3 && bVar5);
            if ((local_90 & 1) != 0) {
              operator_delete(local_80);
            }
            if (sVar2 != sVar3) goto LAB_00268d98;
          }
        }
        else {
          iVar6 = memcmp(local_80,psVar1,sStack_88);
          operator_delete(pcVar15);
          if (iVar6 != 0) goto LAB_00268d98;
        }
        uVar7 = (ulong)((byte)*param_1 >> 1);
        if (((byte)*param_1 & 1) != 0) {
          uVar7 = *(ulong *)(param_1 + 8);
          psVar11 = *(string **)(param_1 + 0x10);
        }
        uVar7 = android::base::StartsWith(psVar11,uVar7,"/cache",6);
        bVar5 = true;
        if ((uVar7 & 1) != 0) goto LAB_00268de8;
      }
      else {
        if ((local_90 & 1) != 0) {
          operator_delete(local_80);
        }
LAB_00268d98:
        uVar7 = android::base::ShouldLog(4,0);
        if ((uVar7 & 1) == 0) goto LAB_00268da8;
        puVar8 = (undefined4 *)__errno();
        uVar14 = *puVar8;
        android::base::LogMessage::LogMessage
                  (aLStack_98,"bootable/recovery/install/install.cpp",0x33e,4,0,0xffffffff);
        poVar9 = (ostream *)android::base::LogMessage::stream(aLStack_98);
        poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                           (poVar9,"Installation aborts. The canonical path ",0x28);
        if ((local_60 & 1) == 0) {
          sStack_88 = uStack_58;
          local_90 = local_60;
          local_80 = local_50;
        }
        else {
          std::string::__init_copy_ctor_external((string *)&local_90,local_50,uStack_58);
        }
        poVar9 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                 operator<(poVar9,(string *)&local_90);
        poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                           (poVar9," doesn\'t equal the original path ",0x21);
        std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                  (poVar9,param_1);
        if ((local_90 & 1) != 0) {
          operator_delete(local_80);
        }
        android::base::LogMessage::~LogMessage(aLStack_98);
        bVar5 = false;
        *puVar8 = uVar14;
      }
    }
    else {
      uVar7 = android::base::ShouldLog(4,0);
      if ((uVar7 & 1) == 0) {
LAB_00268da8:
        bVar5 = false;
        goto joined_r0x00268cb0;
      }
      puVar8 = (undefined4 *)__errno();
      uVar14 = *puVar8;
      android::base::LogMessage::LogMessage
                (aLStack_98,"bootable/recovery/install/install.cpp",0x33a,4,0,0xffffffff);
      poVar9 = (ostream *)android::base::LogMessage::stream(aLStack_98);
      poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         (poVar9,"Failed to get canonical of ",0x1b);
      poVar9 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
               operator<(poVar9,param_1);
      poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         (poVar9,", ",2);
      std::error_code::message();
      std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                (poVar9,(string *)&local_90);
      if ((local_90 & 1) != 0) {
        operator_delete(local_80);
      }
      android::base::LogMessage::~LogMessage(aLStack_98);
      bVar5 = false;
LAB_00268ca8:
      *puVar8 = uVar14;
    }
joined_r0x00268cb0:
    if ((local_60 & 1) != 0) {
      operator_delete(local_50);
    }
  }
LAB_00268dfc:
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return bVar5;
  }
LAB_002690f0:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
