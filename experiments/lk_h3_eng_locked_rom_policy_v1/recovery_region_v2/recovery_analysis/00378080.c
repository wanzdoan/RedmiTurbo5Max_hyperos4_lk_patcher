
/* unSplicing(char*, std::string, std::string, unsigned long) */

uint unSplicing(uint *param_1,byte *param_2,ostream *param_3,ulong param_4)

{
  ulong uVar1;
  byte *pbVar2;
  uint uVar3;
  uint uVar4;
  undefined2 uVar5;
  ostream *__n;
  undefined1 *puVar6;
  int iVar7;
  void *__ptr;
  ostream *poVar8;
  void *pvVar9;
  undefined8 *puVar10;
  undefined **ppuVar11;
  int *piVar12;
  ostream *poVar13;
  size_t sVar14;
  ulong uVar15;
  long lVar16;
  undefined4 *puVar17;
  undefined8 uVar18;
  ostream *poVar19;
  ostream *poVar20;
  undefined4 uVar21;
  ulong *puVar22;
  ostream *poVar23;
  byte bVar24;
  char *pcVar25;
  ulong uVar26;
  ostream *poVar27;
  ostream *__n_00;
  undefined1 auStack_2c0 [8];
  long local_2b8;
  byte *local_2b0;
  undefined8 *local_2a8;
  void *local_2a0;
  undefined **local_298;
  byte *local_290;
  undefined8 *local_288;
  ostream *local_280;
  ostream *local_278;
  ostream *local_270;
  uint local_264;
  uint *local_260;
  ulong local_258;
  undefined1 *local_250;
  ostream *local_248;
  uint local_23c;
  ostream *local_238;
  ostream *local_230;
  ulong local_228;
  ulong local_220;
  void *local_218;
  ulong local_210;
  ulong uStack_208;
  ostream *local_200;
  ostream *poStack_1f8;
  undefined8 local_1f0;
  undefined8 uStack_1e8;
  undefined **local_1e0;
  ostream *poStack_1d8;
  undefined **local_1d0;
  undefined8 uStack_1c8;
  undefined8 local_1c0;
  undefined8 uStack_1b8;
  undefined8 uStack_1b0;
  undefined8 uStack_1a8;
  undefined8 local_1a0;
  undefined8 uStack_198;
  undefined8 local_190;
  undefined8 uStack_188;
  undefined8 uStack_180;
  undefined8 uStack_178;
  undefined8 local_170;
  undefined8 uStack_168;
  undefined8 uStack_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 uStack_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined **local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined6 local_88;
  undefined2 uStack_82;
  undefined6 uStack_80;
  long local_78;

  lVar16 = tpidr_el0;
  local_78 = *(long *)(lVar16 + 0x28);
  __ptr = malloc(0x400000);
  uStack_208 = 0;
  local_210 = 0;
  poStack_1f8 = (ostream *)0x0;
  local_200 = (ostream *)0x0;
  uStack_1e8 = 0;
  local_1f0 = 0;
  if (__ptr == (void *)0x0) {
    uVar26 = android::base::ShouldLog(4,0);
    if ((uVar26 & 1) == 0) {
      uVar3 = 0;
    }
    else {
      puVar17 = (undefined4 *)__errno();
      uVar21 = *puVar17;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x2f0,4,0,
                 0xffffffff);
      poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar13,"unSplicing malloc memory failed!",0x20);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_1e0);
      *puVar17 = uVar21;
      uVar3 = 0;
    }
  }
  else {
    local_248 = (ostream *)((ulong)&local_210 | 9);
    local_288 = &local_1c0;
    local_278 = param_3 + 1;
    local_298 = (undefined **)((ulong)&local_1e0 | 1);
    local_2b0 = param_2 + 1;
    local_2a0 = (void *)((ulong)&local_228 | 1);
    local_2a8 = (undefined8 *)((ulong)&local_210 | 10);
    puVar6 = auStack_2c0;
    local_2b8 = lVar16;
    local_290 = param_2;
    local_270 = param_3;
    do {
      poVar13 = local_270;
      local_264 = (uint)(param_4 == 0);
      if (param_4 == 0) {
        uVar26 = android::base::ShouldLog(2,0);
        if ((uVar26 & 1) != 0) {
          puVar17 = (undefined4 *)__errno();
          uVar21 = *puVar17;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x330,2,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar13,"UnSplicing of reserved apps completed. ",0x27);
LAB_00378940:
          android::base::LogMessage::~LogMessage((LogMessage *)&local_1e0);
          *puVar17 = uVar21;
        }
        break;
      }
      poStack_1f8 = (ostream *)0x0;
      local_200 = (ostream *)0x0;
      uStack_1e8 = 0;
      local_1f0 = 0;
      uStack_208 = 0;
      local_210 = 0;
      if (param_4 < 4) {
        uVar26 = android::base::ShouldLog(4,0);
        if ((uVar26 & 1) != 0) {
          puVar17 = (undefined4 *)__errno();
          uVar21 = *puVar17;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x2f7,4,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"failed to unsplicing: read file name size failed(need ",0x36
                              );
          poVar13 = (ostream *)std::ostream::operator<<(poVar13,4);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"): ",3);
          std::ostream::operator<<(poVar13,param_4);
          goto LAB_00378940;
        }
        break;
      }
      param_4 = param_4 - 4;
      uVar3 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
      uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
      local_210 = (ulong)uVar3;
      if (param_4 < uVar3) {
        uVar26 = android::base::ShouldLog(4,0);
        if ((uVar26 & 1) != 0) {
          puVar17 = (undefined4 *)__errno();
          uVar21 = *puVar17;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x2ff,4,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"failed to unsplicing: read file name failed(need ",0x31);
          poVar13 = (ostream *)std::ostream::operator<<(poVar13,(uint)local_210);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"): ",3);
          std::ostream::operator<<(poVar13,param_4);
          goto LAB_00378940;
        }
        break;
      }
      pcVar25 = puVar6 + -((ulong)(uVar3 + 1) + 0xf & 0x1fffffff0);
      local_258 = param_4;
      local_250 = puVar6;
      memset(pcVar25,0,(ulong)(uVar3 + 1));
      uVar26 = local_210 & 0xffffffff;
      memset(pcVar25,0,(ulong)((uint)local_210 + 1));
      local_260 = param_1 + 1;
      memcpy(pcVar25,param_1 + 1,uVar26);
      std::string::__assign_external((string *)((ulong)&local_210 | 8),pcVar25);
      poVar27 = poStack_1f8;
      poVar23 = local_200;
      bVar24 = (byte)uStack_208 & 1;
      __n_00 = (ostream *)(ulong)((byte)uStack_208 >> 1);
      poVar8 = local_248;
      poVar20 = __n_00;
      if ((uStack_208 & 1) != 0) {
        poVar8 = poStack_1f8;
        poVar20 = local_200;
      }
      local_238 = local_278;
      __n = (ostream *)(ulong)((byte)*poVar13 >> 1);
      if (((byte)*poVar13 & 1) != 0) {
        local_238 = *(ostream **)(poVar13 + 0x10);
        __n = *(ostream **)(poVar13 + 8);
      }
      if (__n == (ostream *)0x0) {
LAB_003782e0:
        poStack_1d8 = (ostream *)0x0;
        local_1d0 = (undefined **)0x0;
        local_1e0 = (undefined **)0x0;
        bVar24 = *local_290;
        puVar22 = (ulong *)(ulong)bVar24;
        sVar14 = (ulong)(bVar24 >> 1);
        if ((bVar24 & 1) != 0) {
          sVar14 = *(size_t *)(local_290 + 8);
        }
        poVar13 = (ostream *)(sVar14 + 1);
        local_280 = poVar8;
        if (poVar13 < (ostream *)0xfffffffffffffff0) {
          if (poVar13 < (ostream *)0x17) {
            local_1e0 = (undefined **)(ulong)(byte)((int)poVar13 << 1);
            ppuVar11 = local_298;
            if (sVar14 != 0) goto LAB_0037834c;
          }
          else {
            uVar15 = ((ulong)poVar13 | 0xf) + 1;
            ppuVar11 = operator_new(uVar15);
            local_1e0 = (undefined **)(uVar15 | 1);
            poStack_1d8 = poVar13;
            local_1d0 = ppuVar11;
LAB_0037834c:
            pbVar2 = local_2b0;
            if ((bVar24 & 1) != 0) {
              pbVar2 = *(byte **)(local_290 + 0x10);
            }
            memmove(ppuVar11,pbVar2,sVar14);
          }
          poVar13 = local_280;
          uVar15 = (long)poVar20 - (long)__n;
          *(undefined2 *)((long)ppuVar11 + sVar14) = 0x2f;
          if (__n <= poVar20) {
            if (uVar15 < 0xfffffffffffffff0) {
              if (uVar15 < 0x17) {
                local_228 = CONCAT71(local_228._1_7_,(char)((int)uVar15 << 1));
                pvVar9 = local_2a0;
                if (poVar20 != __n) goto LAB_003783c8;
              }
              else {
                uVar1 = (uVar15 | 0xf) + 1;
                pvVar9 = operator_new(uVar1);
                local_228 = uVar1 | 1;
                local_220 = uVar15;
                local_218 = pvVar9;
LAB_003783c8:
                memmove(pvVar9,poVar13 + (long)__n,uVar15);
              }
              *(undefined1 *)((long)pvVar9 + uVar15) = 0;
              pvVar9 = local_2a0;
              if ((local_228 & 1) != 0) {
                pvVar9 = local_218;
              }
              puVar10 = (undefined8 *)std::string::append((char *)&local_1e0,(ulong)pvVar9);
              uVar18 = puVar10[1];
              poVar13 = (ostream *)puVar10[2];
              puVar10[1] = 0;
              puVar10[2] = 0;
              local_88 = (undefined6)*(undefined8 *)((long)puVar10 + 2);
              uVar5 = *(undefined2 *)puVar10;
              *puVar10 = 0;
              uStack_82 = (undefined2)uVar18;
              uStack_80 = (undefined6)((ulong)uVar18 >> 0x10);
              if ((uStack_208 & 1) != 0) {
                operator_delete(poStack_1f8);
              }
              uStack_208 = CONCAT62(uStack_208._2_6_,uVar5);
              *local_2a8 = CONCAT26(uStack_82,local_88);
              *(ulong *)((long)local_2a8 + 6) = CONCAT62(uStack_80,uStack_82);
              poStack_1f8 = poVar13;
              if ((local_228 & 1) != 0) {
                operator_delete(local_218);
              }
              if (((ulong)local_1e0 & 1) != 0) {
                operator_delete(local_1d0);
              }
              bVar24 = (byte)uStack_208 & 1;
              __n_00 = (ostream *)(ulong)((byte)uStack_208 >> 1);
              poVar23 = local_200;
              poVar27 = poStack_1f8;
              goto LAB_00378488;
            }
            goto LAB_00378bcc;
          }
          goto LAB_00378be8;
        }
LAB_00378c04:
        if (*(long *)(local_2b8 + 0x28) == local_78) {
          uVar18 = std::string::__throw_length_error_abi_ne180000_();
          *(int *)puVar22 = (int)poVar13;
          if ((uStack_208 & 1) != 0) {
            operator_delete(poStack_1f8);
          }
          if (*(long *)(local_2b8 + 0x28) == local_78) {
                    /* WARNING: Subroutine does not return */
            _Unwind_Resume(uVar18);
          }
        }
        goto LAB_00378d9c;
      }
      if ((long)__n <= (long)poVar20) {
        poVar13 = poVar8 + (long)poVar20;
        local_23c = (uint)(byte)*local_238;
        poVar19 = poVar20;
        local_280 = poVar8;
        do {
          if ((0xfffffffffffffffe < (ulong)((long)poVar19 - (long)__n)) ||
             (poVar8 = memchr(poVar8,local_23c,((long)poVar19 - (long)__n) + 1),
             poVar8 == (ostream *)0x0)) break;
          local_230 = poVar8;
          iVar7 = memcmp(poVar8,local_238,(size_t)__n);
          if (iVar7 == 0) {
            if ((local_230 != poVar13) && (poVar8 = local_280, local_230 == local_280))
            goto LAB_003782e0;
            break;
          }
          poVar8 = local_230 + 1;
          poVar19 = poVar13 + -(long)poVar8;
        } while ((long)__n <= (long)poVar19);
      }
LAB_00378488:
      poVar8 = local_248;
      if (bVar24 != 0) {
        poVar8 = poVar27;
        __n_00 = poVar23;
      }
      if (__n_00 == (ostream *)0x0) {
        poVar20 = (ostream *)0xffffffffffffffff;
      }
      else {
        poVar20 = __n_00;
        do {
          if (poVar20 == (ostream *)0x0) {
            poVar20 = (ostream *)0xffffffffffffffff;
            break;
          }
          poVar23 = poVar8 + -1 + (long)poVar20;
          poVar20 = poVar20 + -1;
        } while (*poVar23 != (ostream)0x2f);
      }
      puVar22 = &local_210;
      if (poVar20 <= __n_00) {
        __n_00 = poVar20;
      }
      if ((ostream *)0xffffffffffffffef < __n_00) {
        if (*(long *)(local_2b8 + 0x28) == local_78) {
          std::string::__throw_length_error_abi_ne180000_();
LAB_00378bcc:
          if (*(long *)(local_2b8 + 0x28) == local_78) {
            std::string::__throw_length_error_abi_ne180000_();
LAB_00378be8:
            if (*(long *)(local_2b8 + 0x28) == local_78) {
              std::string::__throw_out_of_range_abi_ne180000_();
              goto LAB_00378c04;
            }
          }
        }
        goto LAB_00378d9c;
      }
      if (__n_00 < (ostream *)0x17) {
        local_1e0 = (undefined **)CONCAT71(local_1e0._1_7_,(char)((int)__n_00 << 1));
        ppuVar11 = local_298;
        if (__n_00 != (ostream *)0x0) goto LAB_00378530;
      }
      else {
        uVar15 = ((ulong)__n_00 | 0xf) + 1;
        ppuVar11 = operator_new(uVar15);
        local_1e0 = (undefined **)(uVar15 | 1);
        poStack_1d8 = __n_00;
        local_1d0 = ppuVar11;
LAB_00378530:
        memmove(ppuVar11,poVar8,(size_t)__n_00);
      }
      *(ostream *)((long)ppuVar11 + (long)__n_00) = (ostream)0x0;
      iVar7 = mkdir_recursively((string *)&local_1e0,0x1ed,false,(selabel_handle *)0x0);
      if (((ulong)local_1e0 & 1) != 0) {
        operator_delete(local_1d0);
      }
      if (iVar7 != 0) {
        uVar26 = android::base::ShouldLog(4,0);
        if ((uVar26 & 1) != 0) {
          piVar12 = (int *)__errno();
          iVar7 = *piVar12;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x30d,4,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"failed to mkdir ",0x10);
          poVar13 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                    operator<(poVar13,(string *)((ulong)&local_210 | 8));
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13," (",2);
          pcVar25 = strerror(*piVar12);
          sVar14 = strlen(pcVar25);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,pcVar25,sVar14);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar13,")",1);
LAB_00378ba0:
          android::base::LogMessage::~LogMessage((LogMessage *)&local_1e0);
          *piVar12 = iVar7;
        }
        break;
      }
      param_4 = local_258 - uVar26;
      if (param_4 < 4) {
        uVar26 = android::base::ShouldLog(4,0);
        if ((uVar26 & 1) != 0) {
          piVar12 = (int *)__errno();
          iVar7 = *piVar12;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x313,4,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"failed to unsplicing: read file content size failed(need ",
                               0x39);
          poVar13 = (ostream *)std::ostream::operator<<(poVar13,4);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"): ",3);
          std::ostream::operator<<(poVar13,param_4);
          goto LAB_00378ba0;
        }
        break;
      }
      param_4 = param_4 - 4;
      param_1 = (uint *)((long)local_260 + uVar26) + 1;
      uVar3 = *(uint *)((long)local_260 + uVar26);
      uVar4 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
      uVar26 = (ulong)(int)(uVar4 >> 0x10 | uVar4 << 0x10);
      if (param_4 < uVar26) {
        uVar15 = android::base::ShouldLog(4,0);
        if ((uVar15 & 1) != 0) {
          piVar12 = (int *)__errno();
          iVar7 = *piVar12;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_1e0,"bootable/recovery/miutil/mi_utils.cpp",0x31a,4,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_1e0);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"failed to unsplicing: read file content failed(need ",0x34);
          poVar13 = (ostream *)std::ostream::operator<<(poVar13,uVar26);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"): ",3);
          std::ostream::operator<<(poVar13,param_4);
          goto LAB_00378ba0;
        }
        break;
      }
      local_90 = 0;
      uStack_188 = 0;
      local_190 = 0;
      uStack_178 = 0;
      uStack_180 = 0;
      uStack_168 = 0;
      local_170 = 0;
      uStack_158 = 0;
      uStack_160 = 0;
      uStack_148 = 0;
      local_150 = 0;
      uStack_138 = 0;
      uStack_140 = 0;
      uStack_128 = 0;
      local_130 = 0;
      uStack_118 = 0;
      local_120 = (undefined **)0x0;
      uStack_108 = 0;
      local_110 = 0;
      uStack_f8 = 0;
      uStack_100 = 0;
      uStack_e8 = 0;
      local_f0 = 0;
      uStack_d8 = 0;
      uStack_e0 = 0;
      uStack_c8 = 0;
      local_d0 = 0;
      uStack_b8 = 0;
      uStack_c0 = 0;
      uStack_a8 = 0;
      local_b0 = 0;
      uStack_98 = 0;
      uStack_a0 = 0;
      uStack_198 = 0;
      local_1a0 = 0;
      poStack_1d8 = (ostream *)0x0;
      local_1e0 = (undefined **)0x0;
      uStack_1c8 = 0;
      local_1d0 = (undefined **)0x0;
      uStack_1b8 = 0;
      local_1c0 = 0;
      uStack_1a8 = 0;
      uStack_1b0 = 0;
      std::fstream::fstream((fstream *)&local_1e0,(string *)((ulong)puVar22 | 8),0x14);
      uVar4 = *(uint *)((long)local_288 + (long)local_1e0[-3]) & 5;
      if (uVar4 == 0) {
        uVar15 = android::base::ShouldLog(2,0);
        if ((uVar15 & 1) != 0) {
          puVar17 = (undefined4 *)__errno();
          uVar21 = *puVar17;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_228,"bootable/recovery/miutil/mi_utils.cpp",0x327,2,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_228);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13,"unSplice Info: File name: ",0x1a);
          poVar13 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                    operator<(poVar13,(string *)((ulong)&local_210 | 8));
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar13," File size: ",0xc);
          std::ostream::operator<<(poVar13,uVar26);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_228);
          *puVar17 = uVar21;
        }
        if (uVar3 != 0) {
          do {
            uVar15 = uVar26;
            if (0x3fffff < uVar26) {
              uVar15 = 0x400000;
            }
            __memcpy_chk(__ptr,param_1,uVar15,0x400000);
            std::ostream::write((char *)&local_1d0,(long)__ptr);
            param_4 = param_4 - uVar15;
            param_1 = (uint *)((long)param_1 + uVar15);
            uVar26 = uVar26 - uVar15;
          } while (uVar26 != 0);
        }
        lVar16 = std::filebuf::close();
        if (lVar16 == 0) {
          std::ios_base::clear((int)&local_1e0 + (int)local_1e0[-3]);
        }
      }
      else {
        uVar26 = android::base::ShouldLog(4,0);
        if ((uVar26 & 1) != 0) {
          piVar12 = (int *)__errno();
          iVar7 = *piVar12;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_228,"bootable/recovery/miutil/mi_utils.cpp",0x323,4,0,
                     0xffffffff);
          poVar13 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_228);
          local_230 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar13,"open copy dest file error: ",0x1b);
          pbVar2 = local_2b0;
          if ((*local_290 & 1) != 0) {
            pbVar2 = *(byte **)(local_290 + 0x10);
          }
          sVar14 = strlen((char *)pbVar2);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (local_230,(char *)pbVar2,sVar14);
          local_230 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar13," (",2);
          pcVar25 = strerror(*piVar12);
          sVar14 = strlen(pcVar25);
          poVar13 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (local_230,pcVar25,sVar14);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar13,")",1);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_228);
          *piVar12 = iVar7;
        }
      }
      local_120 = &PTR__fstream_00425da8;
      local_1e0 = &PTR__fstream_00425d58;
      local_1d0 = &PTR__fstream_00425d80;
      std::filebuf::~filebuf((filebuf *)&uStack_1c8);
      std::iostream::~iostream((iostream *)&local_1e0);
      std::ios::~ios((ios *)&local_120);
      puVar6 = local_250;
    } while (uVar4 == 0);
    free(__ptr);
    lVar16 = local_2b8;
    uVar3 = local_264;
  }
  if ((uStack_208 & 1) != 0) {
    operator_delete(poStack_1f8);
  }
  if (*(long *)(lVar16 + 0x28) == local_78) {
    return uVar3;
  }
LAB_00378d9c:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
