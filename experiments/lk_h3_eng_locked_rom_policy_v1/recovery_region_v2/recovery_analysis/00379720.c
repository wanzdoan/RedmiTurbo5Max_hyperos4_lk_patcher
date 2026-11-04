
/* WARNING: Removing unreachable block (ram,0x00379c2c) */
/* GetPropertyFromMiextPartition(std::string const&) */

void GetPropertyFromMiextPartition(string *param_1)

{
  byte *pbVar1;
  size_t sVar2;
  size_t sVar3;
  string sVar4;
  long lVar5;
  undefined7 uVar6;
  char cVar7;
  undefined7 uVar8;
  filebuf fVar9;
  char cVar10;
  int iVar11;
  long lVar12;
  ulong *puVar13;
  ulong uVar14;
  undefined4 *puVar15;
  ostream *poVar16;
  long *plVar17;
  istream *piVar18;
  void *pvVar19;
  void *pvVar20;
  string *psVar21;
  undefined8 uVar22;
  ulong *in_x8;
  string *psVar23;
  undefined4 *puVar24;
  undefined4 uVar25;
  byte *pbVar26;
  void *__s;
  ulong local_238;
  void *pvStack_230;
  void *local_228;
  id local_220;
  undefined7 uStack_21f;
  char cStack_218;
  undefined7 uStack_217;
  filebuf fStack_210;
  undefined2 uStack_20f;
  undefined1 uStack_20d;
  undefined4 uStack_20c;
  ulong local_208;
  undefined4 *local_200;
  void *local_1f8;
  ulong local_1f0;
  ulong uStack_1e8;
  void *local_1e0;
  string local_1d0;
  undefined7 uStack_1cf;
  char cStack_1c8;
  undefined7 uStack_1c7;
  filebuf fStack_1c0;
  undefined2 uStack_1bf;
  undefined1 uStack_1bd;
  undefined4 uStack_1bc;
  undefined8 uStack_1b8;
  undefined8 local_1b0;
  undefined8 uStack_1a8;
  undefined8 local_1a0;
  undefined8 uStack_198;
  undefined8 uStack_190;
  undefined8 uStack_188;
  undefined8 local_180;
  undefined8 uStack_178;
  undefined8 uStack_170;
  undefined8 uStack_168;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 uStack_150;
  long local_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  long local_78;

  lVar5 = tpidr_el0;
  local_78 = *(long *)(lVar5 + 0x28);
  *(byte *)in_x8 = 0xe;
  in_x8[1] = 0;
  in_x8[2] = 0;
  pbVar26 = (byte *)((long)in_x8 + 1);
  pbVar26[0] = 0x75;
  pbVar26[1] = 0x6e;
  pbVar26[2] = 0x6b;
  pbVar26[3] = 0x6e;
  local_1d0 = (string)0x24;
  pbVar1 = (byte *)((long)in_x8 + 4);
  pbVar1[0] = 0x6e;
  pbVar1[1] = 0x6f;
  pbVar1[2] = 0x77;
  pbVar1[3] = 0x6e;
  uStack_1bd = 0;
  uStack_1bf = 0x7478;
  uStack_1c7 = (undefined7)s__mnt_vendor_mi_ext_00146f39._8_8_;
  uVar8 = uStack_1c7;
  fStack_1c0 = SUB81(s__mnt_vendor_mi_ext_00146f39._8_8_,7);
  fVar9 = fStack_1c0;
  uStack_1cf = (undefined7)s__mnt_vendor_mi_ext_00146f39._0_8_;
  uVar6 = uStack_1cf;
  cStack_1c8 = SUB81(s__mnt_vendor_mi_ext_00146f39._0_8_,7);
  cVar7 = cStack_1c8;
  lVar12 = volume_for_mount_point(&local_1d0);
  if (((byte)local_1d0 & 1) != 0) {
    operator_delete((void *)CONCAT44(uStack_1bc,CONCAT13(uStack_1bd,CONCAT21(uStack_1bf,fStack_1c0))
                                    ));
  }
  if (lVar12 == 0) {
    uVar14 = android::base::ShouldLog(4,0);
    if ((uVar14 & 1) != 0) {
      puVar15 = (undefined4 *)__errno();
      uVar25 = *puVar15;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_1d0,"bootable/recovery/miutil/mi_utils.cpp",0x35b,4,0,
                 0xffffffff);
      poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1d0);
      poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar16,"Unknown volume for ",0x13);
      poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar16,"/mnt/vendor/mi_ext",0x12);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar16,". Check fstab!",0xe);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_1d0);
      *puVar15 = uVar25;
    }
    goto LAB_00379e28;
  }
  local_1f0 = 0;
  uStack_1e8 = 0;
  local_1e0 = (void *)0x0;
  fs_mgr_get_slot_suffix();
  puVar13 = (ulong *)std::string::insert((ulong)&local_1d0,(char *)0x0,0x149652);
  local_1e0 = (void *)puVar13[2];
  uStack_1e8 = puVar13[1];
  local_1f0 = *puVar13;
  puVar13[1] = 0;
  puVar13[2] = 0;
  *puVar13 = 0;
  if (((byte)local_1d0 & 1) != 0) {
    operator_delete((void *)CONCAT44(uStack_1bc,CONCAT13(uStack_1bd,CONCAT21(uStack_1bf,fStack_1c0))
                                    ));
  }
  uVar14 = CreateDeviceMapperForPartition((string *)&local_1f0);
  if ((uVar14 & 1) == 0) {
    uVar14 = android::base::ShouldLog(4,0);
    if ((uVar14 & 1) != 0) {
      puVar15 = (undefined4 *)__errno();
      uVar25 = *puVar15;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_1d0,"bootable/recovery/miutil/mi_utils.cpp",0x360,4,0,
                 0xffffffff);
      poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1d0);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar16,"Failed to create device mapper",0x1e);
LAB_0037999c:
      android::base::LogMessage::~LogMessage((LogMessage *)&local_1d0);
      *puVar15 = uVar25;
    }
  }
  else {
    uStack_1bd = 0;
    local_1d0 = (string)0x24;
    uStack_1bf = 0x7478;
    uStack_1cf = uVar6;
    cStack_1c8 = cVar7;
    uStack_1c7 = uVar8;
    fStack_1c0 = fVar9;
    iVar11 = ensure_path_mounted(&local_1d0);
    if (((byte)local_1d0 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_1bc,
                                       CONCAT13(uStack_1bd,CONCAT21(uStack_1bf,fStack_1c0))));
    }
    if (iVar11 == 0) {
      uStack_198 = 0;
      local_1a0 = 0;
      uStack_188 = 0;
      uStack_190 = 0;
      uStack_178 = 0;
      local_180 = 0;
      uStack_168 = 0;
      uStack_170 = 0;
      uStack_158 = 0;
      local_160 = 0;
      local_148 = 0;
      uStack_150 = 0;
      uStack_138 = 0;
      local_140 = 0;
      uStack_128 = 0;
      uStack_130 = 0;
      uStack_118 = 0;
      local_120 = 0;
      uStack_108 = 0;
      uStack_110 = 0;
      uStack_f8 = 0;
      local_100 = 0;
      uStack_e8 = 0;
      uStack_f0 = 0;
      uStack_d8 = 0;
      local_e0 = 0;
      uStack_c8 = 0;
      uStack_d0 = 0;
      uStack_b8 = 0;
      local_c0 = 0;
      uStack_a8 = 0;
      uStack_b0 = 0;
      uStack_98 = 0;
      local_a0 = 0;
      uStack_88 = 0;
      uStack_90 = 0;
      uStack_1a8 = 0;
      local_1b0 = 0;
      cStack_1c8 = '\0';
      uStack_1c7 = 0;
      local_1d0 = (string)0x0;
      uStack_1cf = 0;
      uStack_1b8 = 0;
      fStack_1c0 = (filebuf)0x0;
      uStack_1bf = 0;
      uStack_1bd = 0;
      uStack_1bc = 0;
      std::ifstream::ifstream((ifstream *)&local_1d0,"/mnt/vendor/mi_ext/etc/build.prop",8);
      if (local_148 == 0) {
        uVar14 = android::base::ShouldLog(4,0);
        if ((uVar14 & 1) != 0) {
          puVar15 = (undefined4 *)__errno();
          uVar25 = *puVar15;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_208,"bootable/recovery/miutil/mi_utils.cpp",0x382,4,0,
                     uVar25);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_208);
          poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar16,"Error opening ",0xe);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar16,"/mnt/vendor/mi_ext/etc/build.prop",0x21);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_208);
          *puVar15 = uVar25;
        }
      }
      else {
        local_208 = 0;
        local_200 = (undefined4 *)0x0;
        local_1f8 = (void *)0x0;
LAB_00379a44:
        do {
          do {
            std::ios_base::getloc();
            plVar17 = (long *)std::locale::use_facet(&local_220);
            cVar10 = (**(code **)(*plVar17 + 0x38))(plVar17,10);
            std::locale::~locale((locale *)&local_220);
            piVar18 = std::getline_abi_ne180000_<char,std::char_traits<char>,std::allocator<char>>
                                ((istream *)&local_1d0,(string *)&local_208,cVar10);
            if (((byte)piVar18[*(long *)(*(long *)piVar18 + -0x18) + 0x20] & 5) != 0)
            goto LAB_00379d20;
            puVar15 = (undefined4 *)(local_208 >> 1 & 0x7f);
            if ((local_208 & 1) != 0) {
              puVar15 = local_200;
            }
          } while (puVar15 == (undefined4 *)0x0);
          __s = (void *)((ulong)&local_208 | 1);
          if ((local_208 & 1) != 0) {
            __s = local_1f8;
          }
          pvVar19 = memchr(__s,0x3d,(size_t)puVar15);
        } while ((pvVar19 == (void *)0x0) ||
                (puVar24 = (undefined4 *)((long)pvVar19 - (long)__s),
                puVar24 == (undefined4 *)0xffffffffffffffff));
        local_220 = (id)0x0;
        uStack_21f = 0;
        cStack_218 = '\0';
        uStack_217 = 0;
        if (puVar24 <= puVar15) {
          puVar15 = puVar24;
        }
        fStack_210 = (filebuf)0x0;
        uStack_20f = 0;
        uStack_20d = 0;
        uStack_20c = 0;
        if ((undefined4 *)0xffffffffffffffef < puVar15) {
          if (*(long *)(lVar5 + 0x28) == local_78) {
            std::string::__throw_length_error_abi_ne180000_();
LAB_00379f94:
            if (*(long *)(lVar5 + 0x28) == local_78) {
              std::string::__throw_out_of_range_abi_ne180000_();
LAB_00379fac:
              if (*(long *)(lVar5 + 0x28) == local_78) {
                uVar22 = std::string::__throw_length_error_abi_ne180000_();
                *puVar24 = (int)__s;
                if ((local_208 & 1) != 0) {
                  operator_delete(local_1f8);
                }
                std::ifstream::~ifstream((ifstream *)&local_1d0);
                if ((local_1f0 & 1) != 0) {
                  operator_delete(local_1e0);
                }
                if ((*in_x8 & 1) != 0) {
                  operator_delete((void *)in_x8[2]);
                }
                if (*(long *)(lVar5 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
                  _Unwind_Resume(uVar22);
                }
              }
            }
          }
          goto LAB_0037a170;
        }
        if (puVar15 < (undefined4 *)0x17) {
          local_220 = SUB41((int)puVar15 << 1,0);
          pvVar20 = (void *)((ulong)&local_220 | 1);
          if (pvVar19 != __s) goto LAB_00379b44;
        }
        else {
          uVar14 = ((ulong)puVar15 | 0xf) + 1;
          pvVar20 = operator_new(uVar14);
          local_220 = (id)((byte)uVar14 | 1);
          cStack_218 = (char)puVar15;
          uStack_217 = (undefined7)((ulong)puVar15 >> 8);
          fStack_210 = SUB81(pvVar20,0);
          uStack_20f = (undefined2)((ulong)pvVar20 >> 8);
          uStack_20d = (undefined1)((ulong)pvVar20 >> 0x18);
          uStack_20c = (undefined4)((ulong)pvVar20 >> 0x20);
          uStack_21f = (undefined7)(uVar14 >> 8);
LAB_00379b44:
          memmove(pvVar20,__s,(size_t)puVar15);
        }
        *(undefined1 *)((long)pvVar20 + (long)puVar15) = 0;
        sVar4 = *param_1;
        sVar2 = (ulong)((byte)local_220 >> 1);
        if (((byte)local_220 & 1) != 0) {
          sVar2 = CONCAT71(uStack_217,cStack_218);
        }
        sVar3 = (ulong)((byte)sVar4 >> 1);
        if (((byte)sVar4 & 1) != 0) {
          sVar3 = *(size_t *)(param_1 + 8);
        }
        if (sVar2 == sVar3) {
          psVar21 = param_1 + 1;
          if (((byte)sVar4 & 1) != 0) {
            psVar21 = *(string **)(param_1 + 0x10);
          }
          if (((byte)local_220 & 1) == 0) {
            if (1 < (byte)local_220) {
              uVar14 = 0;
              do {
                if (*(string *)((long)&uStack_21f + uVar14) != psVar21[uVar14]) goto LAB_00379a44;
                uVar14 = uVar14 + 1;
              } while ((byte)local_220 >> 1 != uVar14);
            }
          }
          else {
            iVar11 = memcmp((void *)CONCAT44(uStack_20c,
                                             CONCAT13(uStack_20d,CONCAT21(uStack_20f,fStack_210))),
                            psVar21,CONCAT71(uStack_217,cStack_218));
            if (iVar11 != 0) {
              operator_delete((void *)CONCAT44(uStack_20c,
                                               CONCAT13(uStack_20d,CONCAT21(uStack_20f,fStack_210)))
                             );
              goto LAB_00379a44;
            }
          }
          puVar15 = (undefined4 *)(local_208 >> 1 & 0x7f);
          if ((local_208 & 1) != 0) {
            puVar15 = local_200;
          }
          if (puVar15 <= puVar24) goto LAB_00379f94;
          puVar24 = (undefined4 *)((long)puVar24 + 1);
          __s = (void *)((long)puVar15 - (long)puVar24);
          pvVar19 = (void *)((ulong)&local_208 | 1);
          if ((local_208 & 1) != 0) {
            pvVar19 = local_1f8;
          }
          if ((void *)0xffffffffffffffef < __s) goto LAB_00379fac;
          if (__s < (void *)0x17) {
            local_238 = CONCAT71(local_238._1_7_,(char)((int)__s << 1));
            pvVar20 = (void *)((ulong)&local_238 | 1);
            if (puVar15 != puVar24) goto LAB_00379cac;
          }
          else {
            uVar14 = ((ulong)__s | 0xf) + 1;
            pvVar20 = operator_new(uVar14);
            local_238 = uVar14 | 1;
            pvStack_230 = __s;
            local_228 = pvVar20;
LAB_00379cac:
            memmove(pvVar20,(void *)((long)pvVar19 + (long)puVar24),(size_t)__s);
          }
          uVar14 = *in_x8;
          *(undefined1 *)((long)pvVar20 + (long)__s) = 0;
          if ((uVar14 & 1) != 0) {
            operator_delete((void *)in_x8[2]);
          }
          sVar4 = *param_1;
          in_x8[1] = (ulong)pvStack_230;
          *in_x8 = local_238;
          psVar23 = *(string **)(param_1 + 0x10);
          in_x8[2] = (ulong)local_228;
          psVar21 = param_1 + 1;
          if (((byte)sVar4 & 1) != 0) {
            psVar21 = psVar23;
          }
          pbVar1 = pbVar26;
          if ((*in_x8 & 1) != 0) {
            pbVar1 = (byte *)in_x8[2];
          }
          property_set(psVar21,pbVar1);
          iVar11 = 3;
        }
        else {
          iVar11 = 0;
        }
        if (((byte)local_220 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_20c,
                                           CONCAT13(uStack_20d,CONCAT21(uStack_20f,fStack_210))));
        }
        if ((sVar2 != sVar3) || (iVar11 == 0)) goto LAB_00379a44;
LAB_00379d20:
        lVar12 = std::filebuf::close();
        if (lVar12 == 0) {
          std::ios_base::clear
                    ((int)&local_1d0 + (int)*(undefined8 *)(CONCAT71(uStack_1cf,local_1d0) + -0x18))
          ;
        }
        uStack_20d = 0;
        local_220 = (id)0x24;
        uStack_20f = 0x7478;
        uStack_21f = uVar6;
        cStack_218 = cVar7;
        uStack_217 = uVar8;
        fStack_210 = fVar9;
        iVar11 = ensure_path_unmounted((string *)&local_220);
        if (((byte)local_220 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_20c,
                                           CONCAT13(uStack_20d,CONCAT21(uStack_20f,fStack_210))));
        }
        if ((iVar11 != 0) && (uVar14 = android::base::ShouldLog(4,0), (uVar14 & 1) != 0)) {
          puVar15 = (undefined4 *)__errno();
          uVar25 = *puVar15;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_220,"bootable/recovery/miutil/mi_utils.cpp",0x39b,4,0,
                     0xffffffff);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_220);
          poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar16,"Failed to unmount ",0x12);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar16,"/mnt/vendor/mi_ext",0x12);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_220);
          *puVar15 = uVar25;
        }
        psVar21 = (string *)android::dm::DeviceMapper::Instance();
        uVar14 = android::dm::DeviceMapper::DeleteDevice(psVar21);
        if (((uVar14 & 1) == 0) && (uVar14 = android::base::ShouldLog(4,0), (uVar14 & 1) != 0)) {
          puVar15 = (undefined4 *)__errno();
          uVar25 = *puVar15;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_220,"bootable/recovery/miutil/mi_utils.cpp",0x39f,4,0,
                     0xffffffff);
          poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_220);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar16,"Remove mi_ext device mapper failed",0x22);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_220);
          *puVar15 = uVar25;
        }
        if ((local_208 & 1) != 0) {
          operator_delete(local_1f8);
        }
      }
      lVar12 = *(long *)PTR_VTT_0042a050;
      local_1d0 = SUB81(lVar12,0);
      uStack_1cf = (undefined7)((ulong)lVar12 >> 8);
      *(undefined8 *)(&local_1d0 + *(long *)(lVar12 + -0x18)) =
           *(undefined8 *)(PTR_VTT_0042a050 + 0x18);
      std::filebuf::~filebuf(&fStack_1c0);
      std::istream::~istream((istream *)&local_1d0);
      std::ios::~ios((ios *)&uStack_118);
    }
    else {
      uVar14 = android::base::ShouldLog(4,0);
      if ((uVar14 & 1) != 0) {
        puVar15 = (undefined4 *)__errno();
        uVar25 = *puVar15;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_1d0,"bootable/recovery/miutil/mi_utils.cpp",0x37d,4,0,
                   0xffffffff);
        poVar16 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1d0);
        poVar16 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar16,"Failed to mount ",0x10);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar16,"/mnt/vendor/mi_ext",0x12);
        goto LAB_0037999c;
      }
    }
  }
  if ((local_1f0 & 1) != 0) {
    operator_delete(local_1e0);
  }
LAB_00379e28:
  if (*(long *)(lVar5 + 0x28) == local_78) {
    return;
  }
LAB_0037a170:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
