
/* WipeAbDevice(Device*, unsigned long) */

bool WipeAbDevice(Device *param_1,ulong param_2)

{
  string *psVar1;
  long lVar2;
  undefined *puVar3;
  Package *pPVar4;
  string *psVar5;
  int iVar6;
  RecoveryUI *pRVar7;
  ulong uVar8;
  undefined1 *puVar9;
  string **ppsVar10;
  undefined1 *puVar11;
  ZipArchive *pZVar12;
  int *piVar13;
  undefined4 *puVar14;
  ostream *poVar15;
  undefined8 uVar16;
  LogMessage *this;
  long lVar17;
  bool bVar18;
  undefined4 uVar19;
  string *psVar20;
  Package *local_e0;
  undefined1 *local_d8;
  undefined1 *local_d0;
  undefined1 *local_c8;
  int local_c0 [3];
  int local_b4;
  string *local_b0;
  string *local_a8;
  undefined1 *local_a0;
  string **local_90;
  ulong local_80;
  undefined4 *local_78;
  undefined1 *local_70;
  long local_68;

  lVar2 = tpidr_el0;
  local_68 = *(long *)(lVar2 + 0x28);
  pRVar7 = (RecoveryUI *)(**(code **)(*(long *)param_1 + 0x10))();
  (**(code **)(*(long *)pRVar7 + 0x118))(pRVar7,3);
  (**(code **)(*(long *)pRVar7 + 0x128))(pRVar7,1);
  local_e0 = (Package *)0x0;
  if (param_2 == 0) {
    uVar8 = android::base::ShouldLog(4,0);
    if ((uVar8 & 1) != 0) {
      puVar14 = (undefined4 *)__errno();
      uVar19 = *puVar14;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_b0,"bootable/recovery/install/wipe_device.cpp",0x88,4,0,
                 0xffffffff);
      poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_b0);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar15,"wipe_package_size is zero",0x19);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_b0);
      *puVar14 = uVar19;
    }
    local_e0 = (Package *)0x0;
LAB_0027315c:
    uVar8 = android::base::ShouldLog(4,0);
    if ((uVar8 & 1) == 0) {
LAB_002737ec:
      bVar18 = false;
    }
    else {
      puVar14 = (undefined4 *)__errno();
      uVar19 = *puVar14;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_b0,"bootable/recovery/install/wipe_device.cpp",0xb6,4,0,
                 0xffffffff);
      poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_b0);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar15,"Failed to open wipe package",0x1b);
LAB_00273938:
      android::base::LogMessage::~LogMessage((LogMessage *)&local_b0);
      bVar18 = false;
      *puVar14 = uVar19;
    }
  }
  else {
    local_80 = 0;
    local_78 = (undefined4 *)0x0;
    local_70 = (undefined1 *)0x0;
    local_b0 = (string *)0x0;
    local_a8 = (string *)0x0;
    local_a0 = (undefined1 *)0x0;
    uVar8 = read_wipe_package((string *)&local_80,param_2,(string *)&local_b0);
    if ((uVar8 & 1) == 0) {
      uVar8 = android::base::ShouldLog(4,0);
      if ((uVar8 & 1) != 0) {
        puVar14 = (undefined4 *)__errno();
        uVar19 = *puVar14;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)local_c0,"bootable/recovery/install/wipe_device.cpp",0x8e,4,0,
                   uVar19);
        poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_c0);
        poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"Failed to read wipe package",0x1b);
        std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                  (poVar15,(string *)&local_b0);
        android::base::LogMessage::~LogMessage((LogMessage *)local_c0);
        *puVar14 = uVar19;
      }
      local_e0 = (Package *)0x0;
      puVar11 = local_a0;
      if (((ulong)local_b0 & 1) != 0) goto LAB_002730a4;
    }
    else {
      if (((ulong)local_b0 & 1) != 0) {
        operator_delete(local_a0);
      }
      local_d0 = (undefined1 *)0x0;
      local_c8 = (undefined1 *)0x0;
      local_d8 = (undefined1 *)0x0;
      puVar11 = (undefined1 *)((ulong)&local_80 | 1);
      puVar14 = (undefined4 *)(local_80 >> 1 & 0x7f);
      if ((local_80 & 1) != 0) {
        puVar11 = local_70;
        puVar14 = local_78;
      }
      if (puVar14 != (undefined4 *)0x0) {
        if ((long)puVar14 < 0) {
          if (*(long *)(lVar2 + 0x28) == local_68) {
            uVar16 = std::vector<unsigned_short,std::allocator<unsigned_short>>::
                     __throw_length_error_abi_ne180000_();
            *puVar14 = (int)puVar11;
            std::vector<std::string,std::allocator<std::string>>::~vector_abi_ne180000_
                      ((vector<std::string,std::allocator<std::string>> *)&local_b0);
            pPVar4 = local_e0;
            local_e0 = (Package *)0x0;
            if (pPVar4 != (Package *)0x0) {
              (**(code **)(*(long *)pPVar4 + 8))();
            }
            if (*(long *)(lVar2 + 0x28) == local_68) {
                    /* WARNING: Subroutine does not return */
              _Unwind_Resume(uVar16);
            }
          }
          goto LAB_00273cd0;
        }
        local_d8 = operator_new((ulong)puVar14);
        local_c8 = local_d8 + (long)puVar14;
        puVar9 = local_d8;
        do {
          puVar14 = (undefined4 *)((long)puVar14 - 1);
          local_d0 = puVar9 + 1;
          *puVar9 = *puVar11;
          puVar9 = local_d0;
          puVar11 = puVar11 + 1;
        } while (puVar14 != (undefined4 *)0x0);
      }
      local_90 = (string **)0x0;
      Package::CreateMemoryPackage((Package *)&local_e0,&local_d8,&local_b0);
      if (local_90 == &local_b0) {
        lVar17 = 4;
        ppsVar10 = &local_b0;
LAB_0027308c:
        (**(code **)(*ppsVar10 + lVar17 * 8))();
      }
      else if (local_90 != (string **)0x0) {
        lVar17 = 5;
        ppsVar10 = local_90;
        goto LAB_0027308c;
      }
      if (local_d8 != (undefined1 *)0x0) {
        local_d0 = local_d8;
        puVar11 = local_d8;
LAB_002730a4:
        operator_delete(puVar11);
      }
    }
    if ((local_80 & 1) != 0) {
      operator_delete(local_70);
    }
    pPVar4 = local_e0;
    if (local_e0 == (Package *)0x0) goto LAB_0027315c;
    uVar8 = verify_package(local_e0,pRVar7,false);
    if ((uVar8 & 1) == 0) {
      uVar8 = android::base::ShouldLog(4,0);
      if ((uVar8 & 1) != 0) {
        puVar14 = (undefined4 *)__errno();
        uVar19 = *puVar14;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_b0,"bootable/recovery/install/wipe_device.cpp",0x9c,4,0,
                   0xffffffff);
        poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_b0);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar15,"Failed to verify package",0x18);
LAB_002737b0:
        android::base::LogMessage::~LogMessage((LogMessage *)&local_b0);
        *puVar14 = uVar19;
      }
LAB_002737dc:
      uVar8 = android::base::ShouldLog(4,0);
      if ((uVar8 & 1) != 0) {
        puVar14 = (undefined4 *)__errno();
        uVar19 = *puVar14;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_b0,"bootable/recovery/install/wipe_device.cpp",0xbb,4,0,
                   0xffffffff);
        poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_b0);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar15,"Failed to verify wipe package",0x1d);
        goto LAB_00273938;
      }
      goto LAB_002737ec;
    }
    pZVar12 = (ZipArchive *)(**(code **)(*(long *)pPVar4 + 0x48))(pPVar4);
    if (pZVar12 == (ZipArchive *)0x0) {
      uVar8 = android::base::ShouldLog(4,0);
      if ((uVar8 & 1) != 0) {
        puVar14 = (undefined4 *)__errno();
        uVar19 = *puVar14;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_b0,"bootable/recovery/install/wipe_device.cpp",0xa2,4,0,
                   0xffffffff);
        poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_b0);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar15,"Failed to get ZipArchiveHandle",0x1e);
        goto LAB_002737b0;
      }
      goto LAB_002737dc;
    }
    local_a8 = (string *)0x0;
    local_a0 = (undefined1 *)0x0;
    local_b0 = (string *)&local_a8;
    uVar8 = ReadMetadataFromPackage(pZVar12,(map *)&local_b0);
    if ((uVar8 & 1) == 0) {
      uVar8 = android::base::ShouldLog(4,0);
      if ((uVar8 & 1) != 0) {
        puVar14 = (undefined4 *)__errno();
        uVar19 = *puVar14;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_80,"bootable/recovery/install/wipe_device.cpp",0xa8,4,0,
                   0xffffffff);
        poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_80);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar15,"Failed to parse metadata in the zip file",0x28);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_80);
        *puVar14 = uVar19;
      }
      std::
      __tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
      ::destroy((__tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
                 *)&local_b0,(__tree_node *)local_a8);
      goto LAB_002737dc;
    }
    uVar8 = CheckPackageMetadata(&local_b0,2);
    std::
    __tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
    ::destroy((__tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
               *)&local_b0,(__tree_node *)local_a8);
    if ((uVar8 & 1) == 0) goto LAB_002737dc;
    local_b0 = (string *)0x0;
    local_a8 = (string *)0x0;
    local_a0 = (undefined1 *)0x0;
    GetWipePartitionList(local_e0);
    psVar5 = local_a8;
    puVar3 = PTR_android_fdsan_close_with_tag_00429ae8;
    bVar18 = local_b0 != local_a8;
    psVar20 = local_b0;
    if (local_b0 == local_a8) {
      uVar8 = android::base::ShouldLog(4,0);
      psVar1 = local_b0;
      if ((uVar8 & 1) != 0) {
        puVar14 = (undefined4 *)__errno();
        uVar19 = *puVar14;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_80,"bootable/recovery/install/wipe_device.cpp",0xc1,4,0,
                   0xffffffff);
        poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_80);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar15,"Empty wipe ab partition list",0x1c);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_80);
        *puVar14 = uVar19;
        psVar1 = local_b0;
      }
    }
    else {
      do {
        local_c0[0] = 0;
        do {
          psVar1 = psVar20 + 1;
          if (((byte)*psVar20 & 1) != 0) {
            psVar1 = *(string **)(psVar20 + 0x10);
          }
          iVar6 = __open_2(psVar1,1);
        } while ((iVar6 == -1) && (piVar13 = (int *)__errno(), *piVar13 == 4));
        local_c0[0] = -1;
        android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                  ((unique_fd_impl<android::base::DefaultCloser> *)local_c0,iVar6,(void *)0x0);
        if (local_c0[0] == -1) {
          uVar8 = android::base::ShouldLog(4,0);
          if ((uVar8 & 1) == 0) goto LAB_002733c4;
          puVar14 = (undefined4 *)__errno();
          uVar19 = *puVar14;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_80,"bootable/recovery/install/wipe_device.cpp",0x62,4,0,
                     uVar19);
          poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_80);
          poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar15,"Failed to open \"",0x10);
          poVar15 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                    operator<(poVar15,psVar20);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar15,"\"",1);
          this = (LogMessage *)&local_80;
LAB_00273574:
          android::base::LogMessage::~LogMessage(this);
          *puVar14 = uVar19;
        }
        else {
          local_80 = 0;
          local_78 = (undefined4 *)0x0;
          iVar6 = ioctl(local_c0[0],0x80081272,&local_78);
          if ((iVar6 != -1) && (local_78 != (undefined4 *)0x0)) {
            uVar8 = android::base::ShouldLog(2,0);
            if ((uVar8 & 1) != 0) {
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x6b,2,
                         0,0xffffffff);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
              poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar15,"Secure-wiping \"",0xf);
              poVar15 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std
                        ::operator<(poVar15,psVar20);
              poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar15,"\" from ",7);
              poVar15 = (ostream *)std::ostream::operator<<(poVar15,local_80);
              poVar15 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar15," to ",4);
              std::ostream::operator<<(poVar15,(ulong)local_78);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_d8);
              *puVar14 = uVar19;
            }
            uVar8 = android::base::ShouldLog(2,0);
            if ((uVar8 & 1) != 0) {
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x6d,2,
                         0,0xffffffff);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"  Trying BLKSECDISCARD...",0x19);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_d8);
              *puVar14 = uVar19;
            }
            iVar6 = ioctl(local_c0[0],0x127d,&local_80);
            if (iVar6 == -1) {
              uVar8 = android::base::ShouldLog(3,0);
              if ((uVar8 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x6f,
                           3,0,uVar19);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"  Failed",8);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_d8);
                *puVar14 = uVar19;
              }
              local_b4 = 0;
              iVar6 = ioctl(local_c0[0],0x127c,&local_b4);
              if ((iVar6 != 0) || (local_b4 == 0)) {
                uVar8 = android::base::ShouldLog(2,0);
                if ((uVar8 & 1) != 0) {
                  puVar14 = (undefined4 *)__errno();
                  uVar19 = *puVar14;
                  android::base::LogMessage::LogMessage
                            ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",
                             0x7a,2,0,0xffffffff);
                  poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
                  std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar15,"  Trying BLKZEROOUT...",0x16);
                  android::base::LogMessage::~LogMessage((LogMessage *)&local_d8);
                  *puVar14 = uVar19;
                }
                iVar6 = ioctl(local_c0[0],0x127f,&local_80);
                if (iVar6 == -1) {
                  uVar8 = android::base::ShouldLog(4,0);
                  if ((uVar8 & 1) != 0) {
                    puVar14 = (undefined4 *)__errno();
                    uVar19 = *puVar14;
                    android::base::LogMessage::LogMessage
                              ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",
                               0x7c,4,0,uVar19);
                    poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
                    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar15,"  Failed",8);
                    goto LAB_00273570;
                  }
                  goto LAB_002733c4;
                }
                goto LAB_002732e0;
              }
              uVar8 = android::base::ShouldLog(2,0);
              if ((uVar8 & 1) != 0) {
                puVar14 = (undefined4 *)__errno();
                uVar19 = *puVar14;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x74,
                           2,0,0xffffffff);
                poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar15,"  Trying BLKDISCARD...",0x16);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_d8);
                *puVar14 = uVar19;
              }
              iVar6 = ioctl(local_c0[0],0x1277,&local_80);
              if (iVar6 != -1) goto LAB_002732e0;
              uVar8 = android::base::ShouldLog(4,0);
              if ((uVar8 & 1) == 0) goto LAB_002733c4;
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x76,4,
                         0,uVar19);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"  Failed",8);
            }
            else {
LAB_002732e0:
              uVar8 = android::base::ShouldLog(2,0);
              if ((uVar8 & 1) == 0) goto LAB_002733c4;
              puVar14 = (undefined4 *)__errno();
              uVar19 = *puVar14;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x82,2,
                         0,0xffffffff);
              poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar15,"  Done",6);
            }
LAB_00273570:
            this = (LogMessage *)&local_d8;
            goto LAB_00273574;
          }
          uVar8 = android::base::ShouldLog(4,0);
          if ((uVar8 & 1) != 0) {
            puVar14 = (undefined4 *)__errno();
            uVar19 = *puVar14;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_d8,"bootable/recovery/install/wipe_device.cpp",0x68,4,0,
                       uVar19);
            poVar15 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_d8);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar15,"Failed to get partition size",0x1c);
            goto LAB_00273570;
          }
        }
LAB_002733c4:
        puVar14 = (undefined4 *)__errno();
        iVar6 = local_c0[0];
        uVar19 = *puVar14;
        if (local_c0[0] != -1) {
          if (puVar3 == (undefined *)0x0) {
            close(local_c0[0]);
          }
          else {
            uVar16 = android_fdsan_create_owner_tag(3,local_c0);
            android_fdsan_close_with_tag(iVar6,uVar16);
          }
        }
        psVar20 = psVar20 + 0x18;
        *puVar14 = uVar19;
        psVar1 = local_b0;
      } while (psVar20 != psVar5);
    }
    local_b0 = psVar1;
    psVar20 = local_a8;
    if (psVar1 != (string *)0x0) {
      while (psVar5 = psVar20, psVar5 != psVar1) {
        psVar20 = psVar5 + -0x18;
        if (((byte)*psVar20 & 1) != 0) {
          operator_delete(*(string **)(psVar5 + -8));
        }
      }
      local_a8 = psVar1;
      operator_delete(local_b0);
    }
  }
  pPVar4 = local_e0;
  local_e0 = (Package *)0x0;
  if (pPVar4 != (Package *)0x0) {
    (**(code **)(*(long *)pPVar4 + 8))();
  }
  if (*(long *)(lVar2 + 0x28) == local_68) {
    return bVar18;
  }
LAB_00273cd0:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
