
/* WARNING: Type propagation algorithm not settling */
/* start_recovery(Device*, std::vector<std::string, std::allocator<std::string > > const&) */

ulong start_recovery(Device *param_1,vector *param_2)

{
  string *psVar1;
  undefined1 *puVar2;
  RecoveryUI *pRVar3;
  void *pvVar4;
  long lVar5;
  ulong uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  undefined8 *puVar9;
  undefined1 uVar10;
  undefined1 uVar11;
  undefined1 uVar12;
  undefined1 uVar13;
  undefined1 uVar14;
  undefined1 uVar15;
  bool bVar16;
  undefined1 uVar17;
  bool bVar18;
  bool bVar19;
  bool bVar20;
  bool bVar21;
  bool bVar22;
  bool bVar23;
  bool bVar24;
  bool bVar25;
  bool bVar26;
  bool bVar27;
  bool bVar28;
  double dVar29;
  Device *pDVar30;
  undefined7 uVar31;
  char cVar32;
  undefined4 uVar33;
  bool bVar34;
  bool bVar35;
  byte bVar36;
  undefined4 uVar37;
  int iVar38;
  uint uVar39;
  uint uVar40;
  RecoveryUI *pRVar41;
  ulong uVar42;
  undefined4 *puVar43;
  ostream *poVar44;
  RecoveryUI *pRVar45;
  ulonglong uVar46;
  long *plVar47;
  void *pvVar48;
  long lVar49;
  size_t sVar50;
  MiScreenRecoveryUI *this;
  long *plVar51;
  uint *puVar52;
  ulong *puVar53;
  undefined8 uVar54;
  int *piVar55;
  string *extraout_x1;
  string *psVar56;
  ulong uVar57;
  long lVar58;
  byte *pbVar59;
  ulong extraout_x10;
  int iVar60;
  char *pcVar61;
  byte *pbVar62;
  byte *pbVar63;
  string *psVar64;
  RecoveryUI *pRVar65;
  char *pcVar66;
  string *psVar67;
  undefined8 uVar68;
  float fVar69;
  ulong local_2c8;
  char *local_2c0;
  int local_2a4;
  undefined4 local_290;
  ulong local_280;
  byte local_240 [16];
  void *local_230;
  byte local_228 [16];
  void *local_218;
  byte local_210 [16];
  void *local_200;
  byte local_1f8 [16];
  void *local_1e8;
  undefined1 *local_1e0;
  size_t sStack_1d8;
  char *local_1d0;
  ulong local_1c8;
  ulong local_1c0;
  void *local_1b8;
  byte *local_1b0;
  byte *local_1a8;
  undefined8 local_1a0;
  uint local_194;
  undefined4 local_190;
  int local_18c;
  void *local_188;
  void *local_180;
  undefined8 local_178;
  string *local_170 [2];
  void *local_160;
  string *local_158;
  string *local_150;
  Device *local_148;
  undefined8 uStack_140;
  string *local_138;
  string *local_130;
  undefined8 local_128;
  uint local_120;
  char cStack_11c;
  char cStack_11b;
  undefined2 uStack_11a;
  char cStack_118;
  undefined7 uStack_117;
  char local_110;
  undefined1 uStack_10f;
  undefined1 uStack_10e;
  undefined1 uStack_10d;
  undefined4 uStack_10c;
  Device *pDStack_108;
  uint *local_100;
  char *local_f8;
  ulong local_f0;
  uint *puStack_e8;
  char *local_e0;
  undefined4 uStack_d8;
  undefined4 local_d4;
  undefined4 uStack_d0;
  undefined8 uStack_cc;
  string local_c0;
  char cStack_bf;
  char cStack_be;
  char cStack_bd;
  undefined4 uStack_bc;
  undefined8 uStack_b8;
  char local_b0;
  undefined4 uStack_af;
  undefined1 uStack_ab;
  undefined2 uStack_aa;
  undefined8 local_a8;
  string *local_a0;
  long local_88;

  lVar5 = tpidr_el0;
  local_88 = *(long *)(lVar5 + 0x28);
  local_188 = (void *)0x0;
  local_180 = (void *)0x0;
  local_178 = 0;
  StringVectorToNullTerminatedArray(param_2);
  uVar68 = 0;
  bVar34 = false;
  pcVar66 = (char *)0x0;
  pRVar65 = (RecoveryUI *)((ulong)&local_120 | 1);
  local_2a4 = 0;
  local_2c8 = 0;
  local_2c0 = (char *)0x0;
  uVar11 = false;
  uVar17 = false;
  uVar15 = false;
  uVar12 = false;
  local_290 = 0;
  uVar13 = false;
  uVar10 = false;
  uVar14 = false;
  bVar16 = false;
  bVar22 = false;
  bVar21 = false;
  bVar20 = false;
  bVar27 = false;
  bVar28 = false;
  bVar19 = false;
  bVar18 = false;
  bVar25 = false;
  bVar24 = false;
  bVar23 = false;
  bVar26 = false;
  local_18c = 0;
  pRVar45 = (RecoveryUI *)&local_120;
  do {
    pRVar41 = (RecoveryUI *)&DAT_0014826c;
    uVar37 = getopt_long((int)((ulong)((long)local_180 - (long)local_188) >> 3) + -1,local_188,
                         &DAT_0014826c,&PTR_s_fastboot_00428040,&local_18c);
    switch(uVar37) {
    case 0:
      local_120 = 0;
      cStack_11c = '\0';
      cStack_11b = '\0';
      uStack_11a = 0;
      cStack_118 = '\0';
      uStack_117 = 0;
      local_110 = '\0';
      uStack_10f = 0;
      uStack_10e = 0;
      uStack_10d = 0;
      uStack_10c = 0;
      pcVar61 = (&PTR_s_fastboot_00428040)[(long)local_18c * 4];
      pRVar41 = (RecoveryUI *)strlen(pcVar61);
      if ((RecoveryUI *)0xffffffffffffffef < pRVar41) {
        if (*(long *)(lVar5 + 0x28) != local_88) goto LAB_0022cbd4;
        std::string::__throw_length_error_abi_ne180000_();
        goto LAB_0022bb40;
      }
      if (pRVar41 < (RecoveryUI *)0x17) {
        local_120 = CONCAT31(local_120._1_3_,(char)((int)pRVar41 << 1));
        pRVar45 = pRVar65;
        if (pRVar41 != (RecoveryUI *)0x0) goto LAB_00229388;
      }
      else {
        local_280 = ((ulong)pRVar41 | 0xf) + 1;
        pRVar45 = operator_new(local_280);
        cStack_118 = (char)pRVar41;
        uStack_117 = (undefined7)((ulong)pRVar41 >> 8);
        local_110 = (char)pRVar45;
        uStack_10f = (undefined1)((ulong)pRVar45 >> 8);
        uStack_10e = (undefined1)((ulong)pRVar45 >> 0x10);
        uStack_10d = (undefined1)((ulong)pRVar45 >> 0x18);
        uStack_10c = (undefined4)((ulong)pRVar45 >> 0x20);
        local_120 = (uint)local_280 | 1;
        cStack_11c = (char)(local_280 >> 0x20);
        cStack_11b = (char)(local_280 >> 0x28);
        uStack_11a = (undefined2)(local_280 >> 0x30);
LAB_00229388:
        memmove(pRVar45,pcVar61,(size_t)pRVar41);
      }
      pRVar45[(long)pRVar41] = (RecoveryUI)0x0;
      uVar42 = (ulong)((byte)local_120._0_1_ >> 1);
      if ((local_120 & 1) != 0) {
        uVar42 = CONCAT71(uStack_117,cStack_118);
      }
      uVar57 = local_2c8;
      pcVar61 = local_2c0;
      if ((long)uVar42 < 0xe) {
        if (uVar42 == 6) {
          pRVar41 = pRVar65;
          if ((local_120 & 1) != 0) {
            pRVar41 = (RecoveryUI *)
                      CONCAT44(uStack_10c,
                               CONCAT13(uStack_10d,
                                        CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
          }
          iVar38 = memcmp(pRVar41,"locale",6);
          pRVar45 = pRVar41;
          if (((iVar38 != 0) && (iVar38 = memcmp(pRVar41,"reason",6), iVar38 != 0)) &&
             (iVar38 = memcmp(pRVar41,"rescue",6), iVar38 != 0)) goto LAB_002295fc;
        }
        else if (uVar42 == 8) {
          pRVar41 = pRVar65;
          if ((local_120 & 1) != 0) {
            pRVar41 = (RecoveryUI *)
                      CONCAT44(uStack_10c,
                               CONCAT13(uStack_10d,
                                        CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
          }
          iVar38 = memcmp(pRVar41,"fastboot",8);
          pRVar45 = pRVar41;
          if ((iVar38 != 0) && (iVar38 = memcmp(pRVar41,"security",8), iVar38 != 0)) {
            if (*(long *)pRVar41 != 0x64616f6c65646973) goto LAB_002295fc;
            uVar15 = true;
          }
        }
        else if (uVar42 == 0xb) {
          pRVar45 = pRVar65;
          if ((local_120 & 1) != 0) {
            pRVar45 = (RecoveryUI *)
                      CONCAT44(uStack_10c,
                               CONCAT13(uStack_10d,
                                        CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
          }
          iVar38 = memcmp(pRVar45,"retry_count",0xb);
          if (iVar38 != 0) goto LAB_002295fc;
          pbVar62 = (byte *)(*(long *)PTR_optarg_00429b08 + -1);
          do {
            pbVar63 = pbVar62;
            pbVar62 = pbVar63 + 1;
            bVar36 = *pbVar62;
          } while (bVar36 == 0x20 || 0xfffffffa < bVar36 - 0xe);
          if (bVar36 == 0x30) {
            bVar36 = pbVar63[2];
            if (bVar36 == 0x78) {
              iVar38 = 0x10;
            }
            else {
              iVar38 = 0x10;
              if (bVar36 != 0x58) {
                iVar38 = 10;
              }
            }
          }
          else {
            iVar38 = 10;
          }
          pRVar45 = (RecoveryUI *)__errno();
          *(uint *)pRVar45 = 0;
          local_c0 = (string)0x0;
          cStack_bf = 0;
          cStack_be = '\0';
          cStack_bd = '\0';
          uStack_bc = 0;
          uVar42 = strtoll((char *)pbVar62,(char **)&local_c0,iVar38);
          if (*(uint *)pRVar45 != 0) goto joined_r0x002296b8;
          pbVar63 = (byte *)CONCAT44(uStack_bc,
                                     CONCAT13(cStack_bd,
                                              CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0))));
          if ((pbVar62 == pbVar63) || (*pbVar63 != 0)) {
LAB_002297dc:
            uVar39 = 0x16;
          }
          else {
            if (uVar42 >> 0x1f == 0) {
              local_2a4 = (int)uVar42;
              goto joined_r0x002296b8;
            }
            uVar39 = 0x22;
          }
          *(uint *)pRVar45 = uVar39;
          uVar57 = local_2c8;
        }
        else {
LAB_002295fc:
          bVar35 = std::operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                             ((string *)&local_120,"update_package");
          pRVar45 = pRVar41;
          if (bVar35) {
            pcVar66 = *(char **)PTR_optarg_00429b08;
          }
          else {
            bVar35 = std::operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                               ((string *)&local_120,"wipe_ab");
            if (bVar35) {
              uVar14 = true;
            }
            else {
              bVar35 = std::
                       operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                                 ((string *)&local_120,"wipe_cache");
              if (bVar35) {
                uVar13 = true;
              }
              else {
                bVar35 = std::
                         operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                                   ((string *)&local_120,"wipe_data");
                if (bVar35) {
                  uVar11 = true;
                }
                else {
                  bVar35 = std::
                           operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                                     ((string *)&local_120,"wipe_package_size");
                  if (bVar35) {
                    pbVar62 = (byte *)(*(long *)PTR_optarg_00429b08 + -1);
                    do {
                      pbVar63 = pbVar62;
                      pbVar62 = pbVar63 + 1;
                      bVar36 = *pbVar62;
                    } while (bVar36 == 0x20 || 0xfffffffa < bVar36 - 0xe);
                    if (bVar36 == 0x30) {
                      bVar36 = pbVar63[2];
                      if (bVar36 == 0x78) {
                        iVar38 = 0x10;
                      }
                      else {
                        iVar38 = 0x10;
                        if (bVar36 != 0x58) {
                          iVar38 = 10;
                        }
                      }
                    }
                    else {
                      if (bVar36 == 0x2d) {
                        puVar43 = (undefined4 *)__errno();
                        *puVar43 = 0x16;
                        goto joined_r0x002296b8;
                      }
                      iVar38 = 10;
                    }
                    pRVar45 = (RecoveryUI *)__errno();
                    *(uint *)pRVar45 = 0;
                    local_c0 = (string)0x0;
                    cStack_bf = 0;
                    cStack_be = '\0';
                    cStack_bd = '\0';
                    uStack_bc = 0;
                    uVar46 = strtoull((char *)pbVar62,(char **)&local_c0,iVar38);
                    if ((*(uint *)pRVar45 == 0) &&
                       ((pbVar63 = (byte *)CONCAT44(uStack_bc,
                                                    CONCAT13(cStack_bd,
                                                             CONCAT12(cStack_be,
                                                                      CONCAT11(cStack_bf,local_c0)))
                                                   ), pbVar62 == pbVar63 ||
                        (uVar57 = uVar46, *pbVar63 != 0)))) goto LAB_002297dc;
                  }
                  else {
                    bVar35 = std::
                             operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                                       ((string *)&local_120,"export_validate");
                    if (bVar35) {
                      uVar68 = *(undefined8 *)PTR_optarg_00429b08;
                    }
                    else {
                      bVar35 = std::
                               operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                                         ((string *)&local_120,"apps_block");
                      pcVar61 = *(char **)PTR_optarg_00429b08;
                      if (!bVar35) {
                        pcVar61 = local_2c0;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      else {
        pRVar45 = pRVar41;
        if (uVar42 == 0xe) {
          pRVar3 = pRVar65;
          if ((local_120 & 1) != 0) {
            pRVar3 = (RecoveryUI *)
                     CONCAT44(uStack_10c,
                              CONCAT13(uStack_10d,
                                       CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
          }
          iVar38 = memcmp(pRVar3,"shutdown_after",0xe);
          if (iVar38 != 0) goto LAB_002295fc;
          uVar10 = true;
        }
        else {
          if (uVar42 != 0x14) {
            if (uVar42 == 0x11) {
              pRVar3 = pRVar65;
              if ((local_120 & 1) != 0) {
                pRVar3 = (RecoveryUI *)
                         CONCAT44(uStack_10c,
                                  CONCAT13(uStack_10d,
                                           CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
              }
              if ((*(long *)pRVar3 == 0x5f6c6c6174736e69 &&
                  *(long *)(pRVar3 + 8) == 0x7375665f68746977) && pRVar3[0x10] == (RecoveryUI)0x65)
              {
                local_290 = 1;
                goto joined_r0x002296b8;
              }
            }
            goto LAB_002295fc;
          }
          pRVar41 = pRVar65;
          if ((local_120 & 1) != 0) {
            pRVar41 = (RecoveryUI *)
                      CONCAT44(uStack_10c,
                               CONCAT13(uStack_10d,
                                        CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
          }
          iVar38 = memcmp(pRVar41,"prompt_and_wipe_data",0x14);
          pRVar45 = pRVar41;
          if (iVar38 == 0) {
            uVar12 = true;
          }
          else {
            iVar38 = memcmp(pRVar41,"sideload_auto_reboot",0x14);
            if (iVar38 != 0) goto LAB_002295fc;
            uVar17 = true;
            uVar15 = true;
          }
        }
      }
joined_r0x002296b8:
      local_2c0 = pcVar61;
      local_2c8 = uVar57;
      if ((local_120 & 1) != 0) {
        operator_delete((void *)CONCAT44(uStack_10c,
                                         CONCAT13(uStack_10d,
                                                  CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)
                                                          ))));
      }
      break;
    case 0x3f:
      uVar42 = android::base::ShouldLog(4,0);
      if ((uVar42 & 1) != 0) {
        puVar43 = (undefined4 *)__errno();
        uVar37 = *puVar43;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x3cc,4,0,0xffffffff);
        poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar44,"Invalid command argument",0x18);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_120);
        *puVar43 = uVar37;
        pRVar45 = (RecoveryUI *)&DAT_0014826c;
      }
      break;
    case 0x44:
      bVar26 = true;
      break;
    case 0x45:
      bVar28 = true;
      break;
    case 0x46:
      bVar18 = true;
      break;
    case 0x4e:
      bVar24 = true;
      break;
    case 0x52:
      bVar25 = true;
      break;
    case 0x53:
      bVar23 = true;
      break;
    case 0x57:
      bVar19 = true;
      break;
    case 100:
      bVar27 = true;
      break;
    case 0x66:
      bVar20 = true;
      break;
    case 0x6b:
      bVar22 = true;
      break;
    case 0x6d:
      bVar21 = true;
      break;
    case 0x76:
      uVar68 = *(undefined8 *)PTR_optarg_00429b08;
      break;
    case 0x78:
      bVar16 = true;
      break;
    case 0x7a:
      bVar34 = true;
      break;
    case 0xffffffff:
      goto switchD_0022920c_caseD_ffffffff;
    }
  } while( true );
code_r0x0022a660:
  uVar39 = (**(code **)(*(string **)local_148 + 0x50))(local_148,(long)iVar38);
  pDVar30 = local_148;
  switch(uVar39) {
  case 1:
    iVar38 = (**(code **)(*(long *)this + 0x30))(this,local_148);
    goto joined_r0x0022a9e8;
  default:
    goto switchD_0022a698_caseD_2;
  case 4:
  case 0x17:
    goto switchD_0022a698_caseD_4;
  case 5:
    DAT_0042cf18 = 1;
    uVar42 = (**(code **)(*(long *)this + 0x148))(this);
    pDVar30 = local_148;
    if ((uVar42 & 1) == 0) {
      WipeData(local_148,false,false);
      goto LAB_0022b78c;
    }
    pcVar61 = operator_new(0x20);
    pcVar61[0x19] = '\0';
    uVar7 = s_THIS_CAN_NOT_BE_UNDONE__0014aa37._17_8_;
    uVar54 = s_THIS_CAN_NOT_BE_UNDONE__0014aa37._0_8_;
    uStack_b8._0_4_ = 0;
    uStack_b8._4_4_ = 0;
    local_b0 = '\0';
    uStack_af = 0;
    uStack_ab = 0;
    uStack_aa = 0;
    local_c0 = (string)0x0;
    cStack_bf = 0;
    cStack_be = 0;
    cStack_bd = 0;
    uStack_bc = 0;
    uVar68 = CONCAT17(s_THIS_CAN_NOT_BE_UNDONE__0014aa37[0x10],
                      s_THIS_CAN_NOT_BE_UNDONE__0014aa37._9_7_);
    local_130 = (string *)0x0;
    *(ulong *)(pcVar61 + 8) =
         CONCAT71(s_THIS_CAN_NOT_BE_UNDONE__0014aa37._9_7_,s_THIS_CAN_NOT_BE_UNDONE__0014aa37[8]);
    *(undefined8 *)pcVar61 = uVar54;
    *(undefined8 *)(pcVar61 + 0x11) = uVar7;
    *(undefined8 *)(pcVar61 + 9) = uVar68;
    local_138 = &local_c0;
    local_1e0 = operator_new(0x30);
    puVar2 = local_1e0 + 0x30;
    local_c0 = SUB81(local_1e0,0);
    cStack_bf = (char)((ulong)local_1e0 >> 8);
    cStack_be = (char)((ulong)local_1e0 >> 0x10);
    cStack_bd = (char)((ulong)local_1e0 >> 0x18);
    uStack_bc = (undefined4)((ulong)local_1e0 >> 0x20);
    uStack_b8._0_4_ = SUB84(local_1e0,0);
    local_b0 = (char)puVar2;
    uStack_af = (undefined4)((ulong)puVar2 >> 8);
    uStack_ab = (undefined1)((ulong)puVar2 >> 0x28);
    uStack_aa = (undefined2)((ulong)puVar2 >> 0x30);
    local_120 = (uint)&local_b0;
    cStack_11c = (char)((ulong)&local_b0 >> 0x20);
    cStack_11b = (char)((ulong)&local_b0 >> 0x28);
    uStack_11a = (undefined2)((ulong)&local_b0 >> 0x30);
    cStack_118 = (char)&local_1e0;
    uStack_117 = (undefined7)((ulong)&local_1e0 >> 8);
    local_110 = (char)local_170;
    uStack_10f = (undefined1)((ulong)local_170 >> 8);
    uStack_10e = (undefined1)((ulong)local_170 >> 0x10);
    uStack_10d = (undefined1)((ulong)local_170 >> 0x18);
    uStack_10c = (undefined4)((ulong)local_170 >> 0x20);
    pDStack_108 = (Device *)0x0;
    *local_1e0 = 0x26;
    *(undefined4 *)(local_1e0 + 1) = 0x65706957;
    *(undefined4 *)(local_1e0 + 4) = 0x6c612065;
    local_170[0] = (string *)(local_1e0 + 0x18);
    *(undefined8 *)(local_1e0 + 8) = 0x642072657375206c;
    *(undefined8 *)(local_1e0 + 0x10) = 0x3f617461;
    uStack_b8._4_4_ = uStack_bc;
    std::string::__init_copy_ctor_external(local_170[0],pcVar61,0x19);
    uStack_b8 = local_170[0] + 0x18;
    operator_delete(pcVar61);
    local_138 = (string *)0x0;
    local_130 = (string *)0x0;
    cStack_118 = 0;
    uStack_117 = 0;
    local_128 = (string *)0x0;
    local_120 = (uint)&local_138;
    cStack_11c = (char)((ulong)&local_138 >> 0x20);
    cStack_11b = (char)((ulong)&local_138 >> 0x28);
    uStack_11a = (undefined2)((ulong)&local_138 >> 0x30);
    local_138 = operator_new(0x30);
    local_130 = local_138 + 0x30;
    *(undefined8 *)(local_138 + 8) = 0;
    *local_138 = (string)0xe;
    *(undefined4 *)(local_138 + 1) = 0x6e614320;
    *(undefined4 *)(local_138 + 4) = 0x6c65636e;
    local_138[0x18] = (string)0x26;
    *(undefined4 *)(local_138 + 0x19) = 0x63614620;
    *(undefined4 *)(local_138 + 0x1c) = 0x726f7463;
    *(undefined8 *)(local_138 + 0x20) = 0x7220617461642079;
    *(undefined8 *)(local_138 + 0x28) = 0x74657365;
    local_128 = local_130;
    plVar51 = (long *)(**(code **)(*(long *)pDVar30 + 0x10))(pDVar30);
    local_120 = 0x424918;
    cStack_11c = '\0';
    cStack_11b = '\0';
    uStack_11a = 0;
    cStack_118 = '@';
    uStack_117 = 0;
    local_110 = '\x01';
    uStack_10f = 0;
    uStack_10e = 0;
    uStack_10d = 0;
    uStack_10c = 0;
    pDStack_108 = pDVar30;
    local_100 = &local_120;
    lVar49 = (**(code **)(*plVar51 + 0x1e0))(plVar51,&local_c0,&local_138,&local_120);
    if (local_100 == &local_120) {
      puVar52 = &local_120;
      lVar58 = 4;
    }
    else {
      if (local_100 == (uint *)0x0) goto LAB_0022ad4c;
      lVar58 = 5;
      puVar52 = local_100;
    }
    (**(code **)(*(long *)puVar52 + lVar58 * 8))();
LAB_0022ad4c:
    psVar56 = local_138;
    psVar64 = local_130;
    if (local_138 != (string *)0x0) {
      while (psVar67 = psVar64, psVar67 != psVar56) {
        psVar64 = psVar67 + -0x18;
        if (((byte)*psVar64 & 1) != 0) {
          operator_delete(*(void **)(psVar67 + -8));
        }
      }
      local_130 = psVar56;
      operator_delete(local_138);
    }
    uVar33 = uStack_bc;
    uVar37 = CONCAT13(cStack_bd,CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0)));
    psVar56 = (string *)CONCAT44(uStack_bc,uVar37);
    if (psVar56 != (string *)0x0) {
      psVar64 = uStack_b8;
      if (uStack_b8 != psVar56) {
        do {
          psVar67 = uStack_b8 + -0x18;
          if (((byte)*psVar67 & 1) != 0) {
            psVar1 = uStack_b8 + -8;
            uStack_b8 = psVar64;
            operator_delete(*(void **)psVar1);
            psVar64 = uStack_b8;
          }
          uStack_b8 = psVar67;
        } while (psVar67 != psVar56);
        psVar56 = (string *)
                  CONCAT44(uStack_bc,
                           CONCAT13(cStack_bd,CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0))));
      }
      uStack_b8._4_4_ = uVar33;
      uStack_b8._0_4_ = uVar37;
      operator_delete(psVar56);
    }
    if (lVar49 == 1) {
      WipeData(local_148,false,false);
    }
    goto switchD_0022a698_caseD_2;
  case 6:
    local_100 = &local_120;
    pDStack_108 = (Device *)0x0;
    local_110 = '\0';
    uStack_10f = 0;
    uStack_10e = 0;
    uStack_10d = 0;
    uStack_10c = 0;
    local_f8 = (char *)0x0;
    local_120 = 0x424138;
    cStack_11c = '\0';
    cStack_11b = '\0';
    uStack_11a = 0;
    cStack_118 = (char)&local_148;
    uStack_117 = (undefined7)((ulong)&local_148 >> 8);
    DAT_0042cf18 = 1;
    uVar42 = (**(code **)(*(long *)this + 0x148))(this);
    if ((uVar42 & 1) == 0) {
      psVar56 = (string *)0x0;
    }
    else {
      psVar56 = (string *)0x0;
      if (local_100 != (uint *)0x0) {
        if (local_100 == &local_120) {
          local_a0 = &local_c0;
          (**(code **)(CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120))) +
                      0x18))(&local_120,&local_c0);
          psVar56 = local_a0;
        }
        else {
          psVar56 = (string *)(**(code **)(*(long *)local_100 + 0x10))();
        }
      }
    }
    local_a0 = psVar56;
    WipeCache((RecoveryUI *)this,&local_c0);
    if (local_a0 == &local_c0) {
      psVar56 = &local_c0;
      lVar49 = 4;
LAB_0022ace0:
      (**(code **)(*(long *)psVar56 + lVar49 * 8))();
    }
    else if (local_a0 != (string *)0x0) {
      lVar49 = 5;
      psVar56 = local_a0;
      goto LAB_0022ace0;
    }
    uVar42 = (**(code **)(*(long *)this + 0x148))(this);
    if (local_100 == &local_120) {
      puVar52 = &local_120;
      lVar49 = 4;
    }
    else {
      if (local_100 == (uint *)0x0) goto LAB_0022ad30;
      lVar49 = 5;
      puVar52 = local_100;
    }
    (**(code **)(*(long *)puVar52 + lVar49 * 8))();
LAB_0022ad30:
    if ((uVar42 & 1) == 0) goto LAB_0022b78c;
    goto switchD_0022a698_caseD_2;
  case 7:
  case 0xe:
  case 0xf:
  case 0x11:
  case 0x12:
  case 0x13:
    goto switchD_0022a698_caseD_7;
  case 8:
switchD_0022a698_caseD_8:
    bVar36 = (**(code **)(*(long *)this + 0x148))(this);
    if ((bVar34 & bVar36) == 1) {
      uVar42 = AskToReboot(local_148,8);
joined_r0x0022ac7c:
      if ((uVar42 & 1) == 0) goto switchD_0022a698_caseD_2;
    }
    break;
  case 9:
    local_c0 = (string)0x0;
    cStack_bf = 0;
    cStack_be = '\0';
    cStack_bd = '\0';
    uStack_bc = 0;
    uStack_b8._0_4_ = 0;
    uStack_b8._4_4_ = 0;
    local_b0 = '\0';
    uStack_af = 0;
    uStack_ab = 0;
    uStack_aa = 0;
    uVar42 = HasCache();
    if ((uVar42 & 1) == 0) {
      lVar49 = Paths::Get();
      if ((*(byte *)(lVar49 + 0x90) & 1) == 0) {
        uVar54 = *(undefined8 *)(lVar49 + 0x90);
        uVar68 = *(undefined8 *)(lVar49 + 0xa0);
        cStack_118 = (char)*(undefined8 *)(lVar49 + 0x98);
        uStack_117 = (undefined7)((ulong)*(undefined8 *)(lVar49 + 0x98) >> 8);
        local_120 = (uint)uVar54;
        cStack_11c = (char)((ulong)uVar54 >> 0x20);
        cStack_11b = (char)((ulong)uVar54 >> 0x28);
        uStack_11a = (undefined2)((ulong)uVar54 >> 0x30);
        local_110 = (char)uVar68;
        uStack_10f = (undefined1)((ulong)uVar68 >> 8);
        uStack_10e = (undefined1)((ulong)uVar68 >> 0x10);
        uStack_10d = (undefined1)((ulong)uVar68 >> 0x18);
        uStack_10c = (undefined4)((ulong)uVar68 >> 0x20);
      }
      else {
        std::string::__init_copy_ctor_external
                  ((string *)&local_120,*(char **)(lVar49 + 0xa0),*(ulong *)(lVar49 + 0x98));
      }
      pcVar61 = (char *)((ulong)&local_120 | 1);
      if ((local_120 & 1) != 0) {
        pcVar61 = (char *)CONCAT44(uStack_10c,
                                   CONCAT13(uStack_10d,
                                            CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
      }
      iVar38 = access(pcVar61,4);
      if ((local_120 & 1) != 0) {
        operator_delete((void *)CONCAT44(uStack_10c,
                                         CONCAT13(uStack_10d,
                                                  CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)
                                                          ))));
      }
      if (iVar38 == -1) goto LAB_0022b0ec;
      lVar49 = Paths::Get();
      if ((*(byte *)(lVar49 + 0x90) & 1) == 0) {
        uVar54 = *(undefined8 *)(lVar49 + 0x90);
        uVar68 = *(undefined8 *)(lVar49 + 0xa0);
        cStack_118 = (char)*(undefined8 *)(lVar49 + 0x98);
        uStack_117 = (undefined7)((ulong)*(undefined8 *)(lVar49 + 0x98) >> 8);
        local_120 = (uint)uVar54;
        cStack_11c = (char)((ulong)uVar54 >> 0x20);
        cStack_11b = (char)((ulong)uVar54 >> 0x28);
        uStack_11a = (undefined2)((ulong)uVar54 >> 0x30);
        local_110 = (char)uVar68;
        uStack_10f = (undefined1)((ulong)uVar68 >> 8);
        uStack_10e = (undefined1)((ulong)uVar68 >> 0x10);
        uStack_10d = (undefined1)((ulong)uVar68 >> 0x18);
        uStack_10c = (undefined4)((ulong)uVar68 >> 0x20);
      }
      else {
        std::string::__init_copy_ctor_external
                  ((string *)&local_120,*(char **)(lVar49 + 0xa0),*(ulong *)(lVar49 + 0x98));
      }
      puVar9 = (undefined8 *)CONCAT44(uStack_b8._4_4_,(undefined4)uStack_b8);
      if (puVar9 < (undefined8 *)
                   CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)))) {
        uStack_b8 = (string *)(puVar9 + 3);
        puVar9[2] = CONCAT44(uStack_10c,
                             CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))
                                     ));
        puVar9[1] = CONCAT71(uStack_117,cStack_118);
        *puVar9 = CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120)));
      }
      else {
        uStack_b8 = std::vector<std::string,std::allocator<std::string>>::
                    __push_back_slow_path<std::string>
                              ((vector<std::string,std::allocator<std::string>> *)&local_c0,
                               (string *)&local_120);
        if ((local_120 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110)))));
        }
      }
    }
    else {
      local_138 = (string *)((ulong)local_138 & 0xffffffff00000000);
      do {
        local_120 = (uint)&local_138;
        cStack_11c = (char)((ulong)&local_138 >> 0x20);
        cStack_11b = (char)((ulong)&local_138 >> 0x28);
        uStack_11a = (undefined2)((ulong)&local_138 >> 0x30);
        cStack_118 = (char)&local_c0;
        uStack_117 = (undefined7)((ulong)&local_c0 >> 8);
        choose_recovery_file(Device*)::$_0::operator()((__0 *)&local_120,"/cache/recovery/last_log")
        ;
        choose_recovery_file(Device*)::$_0::operator()
                  ((__0 *)&local_120,"/cache/recovery/last_kmsg");
        iVar38 = (int)local_138;
        local_138 = (string *)CONCAT44(local_138._4_4_,(int)local_138 + 1);
      } while (iVar38 < 9);
    }
    cStack_11b = 0;
    local_120 = 0x63614208;
    cStack_11c = 0x6b;
    if (uStack_b8 < (string *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))))
    {
      *(ulong *)(uStack_b8 + 0x10) =
           CONCAT44(uStack_10c,
                    CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
      *(ulong *)(uStack_b8 + 8) = CONCAT71(uStack_117,cStack_118);
      *(ulong *)uStack_b8 = CONCAT26(uStack_11a,0x6b63614208);
      uStack_b8 = uStack_b8 + 0x18;
    }
    else {
      psVar56 = std::vector<std::string,std::allocator<std::string>>::
                __push_back_slow_path<std::string>
                          ((vector<std::string,std::allocator<std::string>> *)&local_c0,
                           (string *)&local_120);
      uStack_b8 = psVar56;
      if ((local_120 & 1) != 0) {
        operator_delete((void *)CONCAT44(uStack_10c,
                                         CONCAT13(uStack_10d,
                                                  CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)
                                                          ))));
      }
    }
    local_138 = (string *)0x0;
    local_130 = (string *)0x0;
    cStack_118 = '\0';
    uStack_117 = 0;
    local_128 = (string *)0x0;
    local_120 = (uint)&local_138;
    cStack_11c = (char)((ulong)&local_138 >> 0x20);
    cStack_11b = (char)((ulong)&local_138 >> 0x28);
    uStack_11a = (undefined2)((ulong)&local_138 >> 0x30);
    local_138 = operator_new(0x18);
    lVar49 = 0;
    local_130 = local_138 + 0x18;
    *local_138 = (string)0x26;
    *(undefined4 *)(local_138 + 1) = 0x656c6553;
    *(undefined4 *)(local_138 + 4) = 0x20746365;
    *(undefined8 *)(local_138 + 8) = 0x206f7420656c6966;
    *(undefined8 *)(local_138 + 0x10) = 0x77656976;
    local_128 = local_130;
    do {
      plVar51 = (long *)(**(code **)(*(string **)pDVar30 + 0x10))(pDVar30);
      local_110 = '\x01';
      uStack_10f = 0;
      uStack_10e = 0;
      uStack_10d = 0;
      uStack_10c = 0;
      pDStack_108 = pDVar30;
      local_120 = 0x424918;
      cStack_11c = '\0';
      cStack_11b = '\0';
      uStack_11a = 0;
      cStack_118 = '@';
      uStack_117 = 0;
      local_100 = &local_120;
      lVar49 = (**(code **)(*plVar51 + 0x1d0))(plVar51,&local_138,&local_c0,lVar49,1,&local_120);
      if (local_100 == &local_120) {
        puVar52 = &local_120;
        lVar58 = 4;
LAB_0022b00c:
        (**(code **)(*(long *)puVar52 + lVar58 * 8))();
      }
      else if (local_100 != (uint *)0x0) {
        lVar58 = 5;
        puVar52 = local_100;
        goto LAB_0022b00c;
      }
      psVar56 = local_138;
      if (lVar49 == -2) goto LAB_0022b098;
      pbVar62 = (byte *)(CONCAT44(uStack_bc,
                                  CONCAT13(cStack_bd,
                                           CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0)))) +
                        lVar49 * 0x18);
      bVar36 = *pbVar62;
      uVar42 = (ulong)(bVar36 >> 1);
      if ((bVar36 & 1) != 0) {
        uVar42 = *(ulong *)(pbVar62 + 8);
      }
      if (uVar42 == 4) {
        pbVar63 = *(byte **)(pbVar62 + 0x10);
        if ((bVar36 & 1) == 0) {
          pbVar63 = pbVar62 + 1;
        }
        if (*(int *)pbVar63 == 0x6b636142) goto LAB_0022b098;
      }
      plVar51 = (long *)(**(code **)(*(string **)pDVar30 + 0x10))(pDVar30);
      (**(code **)(*plVar51 + 0x168))
                (plVar51,CONCAT44(uStack_bc,
                                  CONCAT13(cStack_bd,
                                           CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0)))) +
                         lVar49 * 0x18);
    } while( true );
  case 10:
    uVar42 = CreateSnapshotPartitions();
    pcVar61 = "Virtual A/B: snapshot partitions creation failed.\n";
    if ((uVar42 & 1) != 0) {
      android::fs_mgr::GetSystemRoot();
      uVar68 = s__mnt_system_0014c65d._0_8_;
      uStack_b8._4_4_ = uStack_b8._4_4_ & 0xffffff00;
      local_c0 = (string)0x16;
      pcVar66[7] = 's';
      pcVar66[8] = 't';
      pcVar66[9] = 'e';
      pcVar66[10] = 'm';
      *(undefined8 *)pcVar66 = uVar68;
      iVar38 = ensure_path_mounted_at((string *)&local_120,&local_c0);
      if (((byte)local_c0 & 1) != 0) {
        operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)))
                       );
      }
      if ((local_120 & 1) != 0) {
        operator_delete((void *)CONCAT44(uStack_10c,
                                         CONCAT13(uStack_10d,
                                                  CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)
                                                          ))));
      }
      pcVar61 = "Mounted /system.\n";
      if (iVar38 == -1) goto switchD_0022a698_caseD_2;
    }
    (**(code **)(*(long *)this + 0x158))(this,pcVar61);
    goto switchD_0022a698_caseD_2;
  case 0xb:
    puts("run_graphics_test");
    (**(code **)(*(long *)this + 0x140))(this,1);
    MiScreenRecoveryUI::StartProgress(this,0.0,11.0);
    (**(code **)(*(long *)this + 0x118))(this,1);
    (**(code **)(*(long *)this + 0x128))(this,2);
    (**(code **)(*(long *)this + 0x130))(0x3dcccccd,0x40a00000,this);
    sleep(5);
    (**(code **)(*(long *)this + 0x130))(0x3f000000,0,this);
    fVar69 = 0.0;
    lVar49 = 100;
    do {
      fVar69 = (float)((double)fVar69 + dVar29);
      (**(code **)(*(long *)this + 0x138))(fVar69,this);
      printf("set_progress %f\n",(double)fVar69);
      usleep(100000);
      lVar49 = lVar49 + -1;
    } while (lVar49 != 0);
    puts("first stage end+++++++++++++++++++++++++");
    (**(code **)(*(long *)this + 0x130))(0x3e99999a,0,this);
    fVar69 = 0.0;
    lVar49 = 100;
    do {
      fVar69 = (float)((double)fVar69 + dVar29);
      (**(code **)(*(long *)this + 0x138))(fVar69,this);
      printf("set_progress %f\n",(double)fVar69);
      usleep(20000);
      lVar49 = lVar49 + -1;
    } while (lVar49 != 0);
    puts("second stage end++++++++++++++++++++++++");
    (**(code **)(*(long *)this + 0x130))(0x3dcccccd,0x41200000,this);
    sleep(1);
    (**(code **)(*(long *)this + 0x138))(0x3f800000,this);
    sleep(4);
    (**(code **)(*(long *)this + 0x140))(this,0);
    MiScreenRecoveryUI::EndProgress(this);
    puts("end recovery log");
    goto switchD_0022a698_caseD_2;
  case 0xc:
    ScreenRecoveryUI::CheckBackgroundTextImages((ScreenRecoveryUI *)this);
    goto switchD_0022a698_caseD_2;
  case 0xd:
switchD_0022a698_caseD_d:
    uVar39 = 0xd;
    goto switchD_0022a698_caseD_7;
  case 0x16:
    iVar38 = (**(code **)(*(long *)this + 0x58))(this);
    if ((iVar38 != 1) && (iVar38 != -1)) goto switchD_0022a698_caseD_2;
    set_reboot_message(0);
    sync();
    bVar36 = (**(code **)(*(long *)this + 0x148))(this);
    if ((bVar34 & bVar36) == 1) {
      uVar42 = AskToReboot(local_148,0x16);
      goto joined_r0x0022ac7c;
    }
    break;
  case 0x18:
    cStack_11b = '\0';
    local_120 = 0x75727408;
    cStack_11c = 'e';
    pcVar66 = operator_new(0x20);
    pcVar66[0x1d] = '\0';
    uVar7 = s__cache_recovery_last_safemode_0014b8d2._21_8_;
    uVar54 = s__cache_recovery_last_safemode_0014b8d2._0_8_;
    uVar68 = CONCAT53(s__cache_recovery_last_safemode_0014b8d2._16_5_,
                      s__cache_recovery_last_safemode_0014b8d2._13_3_);
    uStack_b8._0_4_ = 0x1d;
    uStack_b8._4_4_ = 0;
    local_b0 = (char)pcVar66;
    uStack_af = (undefined4)((ulong)pcVar66 >> 8);
    uStack_ab = (undefined1)((ulong)pcVar66 >> 0x28);
    uStack_aa = (undefined2)((ulong)pcVar66 >> 0x30);
    local_c0 = (string)0x21;
    cStack_bf = 0;
    cStack_be = '\0';
    cStack_bd = '\0';
    uStack_bc = 0;
    *(ulong *)(pcVar66 + 8) =
         CONCAT35(s__cache_recovery_last_safemode_0014b8d2._13_3_,
                  s__cache_recovery_last_safemode_0014b8d2._8_5_);
    *(undefined8 *)pcVar66 = uVar54;
    *(undefined8 *)(pcVar66 + 0x15) = uVar7;
    *(undefined8 *)(pcVar66 + 0xd) = uVar68;
    uVar39 = android::base::WriteStringToFile((string *)&local_120,&local_c0,false);
    pcVar66 = "enter safemode, write result %d\n";
    goto LAB_0022b740;
  case 0x19:
    iVar38 = (**(code **)(*(long *)this + 0xf0))(this);
joined_r0x0022a9e8:
    if (iVar38 == 0) goto switchD_0022a698_caseD_2;
    break;
  case 0x1a:
    cStack_11b = '\0';
    local_120 = 0x75727408;
    cStack_11c = 'e';
    pcVar66 = operator_new(0x30);
    pcVar66[0x20] = '\0';
    uVar7 = s__cache_recovery_last_maintenance_00152e50._24_8_;
    uVar54 = s__cache_recovery_last_maintenance_00152e50._16_8_;
    uVar68 = s__cache_recovery_last_maintenance_00152e50._0_8_;
    uStack_b8._0_4_ = 0x20;
    uStack_b8._4_4_ = 0;
    local_b0 = (char)pcVar66;
    uStack_af = (undefined4)((ulong)pcVar66 >> 8);
    uStack_ab = (undefined1)((ulong)pcVar66 >> 0x28);
    uStack_aa = (undefined2)((ulong)pcVar66 >> 0x30);
    local_c0 = (string)0x31;
    cStack_bf = 0;
    cStack_be = '\0';
    cStack_bd = '\0';
    uStack_bc = 0;
    *(undefined8 *)(pcVar66 + 8) = s__cache_recovery_last_maintenance_00152e50._8_8_;
    *(undefined8 *)pcVar66 = uVar68;
    *(undefined8 *)(pcVar66 + 0x18) = uVar7;
    *(undefined8 *)(pcVar66 + 0x10) = uVar54;
    uVar39 = android::base::WriteStringToFile((string *)&local_120,&local_c0,false);
    pcVar66 = "enter maintenance mode, write result %d\n";
LAB_0022b740:
    printf(pcVar66,(ulong)(uVar39 & 1));
    if (((byte)local_c0 & 1) != 0) {
      operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
    }
    if ((local_120 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_10c,
                                       CONCAT13(uStack_10d,
                                                CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))
                                      ));
    }
  }
  uVar39 = 1;
switchD_0022a698_caseD_7:
  uVar42 = 3;
  local_280 = (ulong)uVar39;
  goto LAB_0022ba14;
LAB_0022b098:
  psVar64 = local_130;
  if (local_138 != (string *)0x0) {
    while (psVar67 = psVar64, psVar67 != psVar56) {
      psVar64 = psVar67 + -0x18;
      if (((byte)*psVar64 & 1) != 0) {
        operator_delete(*(void **)(psVar67 + -8));
      }
    }
    local_130 = psVar56;
    operator_delete(local_138);
  }
LAB_0022b0ec:
  uVar33 = uStack_bc;
  uVar37 = CONCAT13(cStack_bd,CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0)));
  psVar56 = (string *)CONCAT44(uStack_bc,uVar37);
  if (psVar56 != (string *)0x0) {
    psVar64 = uStack_b8;
    if (uStack_b8 != psVar56) {
      do {
        psVar67 = uStack_b8 + -0x18;
        if (((byte)*psVar67 & 1) != 0) {
          psVar1 = uStack_b8 + -8;
          uStack_b8 = psVar64;
          operator_delete(*(void **)psVar1);
          psVar64 = uStack_b8;
        }
        uStack_b8 = psVar67;
      } while (psVar67 != psVar56);
      psVar56 = (string *)
                CONCAT44(uStack_bc,
                         CONCAT13(cStack_bd,CONCAT12(cStack_be,CONCAT11(cStack_bf,local_c0))));
    }
    uStack_b8._4_4_ = uVar33;
    uStack_b8._0_4_ = uVar37;
    operator_delete(psVar56);
  }
  goto switchD_0022a698_caseD_2;
switchD_0022a698_caseD_4:
  (**(code **)(*(long *)this + 0x10))(this,1);
  (**(code **)(*(long *)this + 0x118))(this,4);
  uVar39 = prompt_and_wait_adb_install((RecoveryUI *)this,"/tmp/last_install");
  (**(code **)(*(long *)this + 0x10))(this,0);
  if (uVar39 == 0) {
LAB_0022b78c:
    uVar42 = 3;
  }
  else {
    uVar42 = 3;
    local_280 = (ulong)uVar39;
  }
  goto LAB_0022ba14;
switchD_0022920c_caseD_ffffffff:
  *(undefined4 *)PTR_optind_00429b10 = 1;
  Device::GetStage();
  uVar37 = uStack_10c;
  cVar32 = local_110;
  uVar31 = uStack_117;
  uVar39 = local_120;
  if ((char)pDStack_108 == '\0') {
    local_c0 = (string)0x0;
    cStack_bf = 0;
  }
  else {
    uVar7 = CONCAT71(uStack_117,cStack_118);
    uVar54 = CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120)));
    local_120 = 0;
    cStack_11c = '\0';
    cStack_11b = '\0';
    uStack_11a = 0;
    uVar8 = CONCAT44(uStack_10c,
                     CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
    cStack_118 = '\0';
    uStack_117 = 0;
    local_110 = '\0';
    uStack_10f = 0;
    uStack_10e = 0;
    uStack_10d = 0;
    uStack_10c = 0;
    uStack_b8._0_4_ = (undefined4)uVar7;
    uStack_b8._4_4_ = (uint)((uint7)uVar31 >> 0x18);
    local_c0 = SUB41(uVar39,0);
    cStack_bf = (char)(uVar39 >> 8);
    cStack_be = (char)(uVar39 >> 0x10);
    cStack_bd = (char)(uVar39 >> 0x18);
    uStack_bc = (undefined4)((ulong)uVar54 >> 0x20);
    local_b0 = cVar32;
    uStack_af = (undefined4)((ulong)uVar8 >> 8);
    uStack_ab = (undefined1)((uint)uVar37 >> 8);
    uStack_aa = (undefined2)((uint)uVar37 >> 0x10);
  }
  uVar42 = (ulong)&local_c0 | 1;
  if (((byte)local_c0 & 1) != 0) {
    uVar42 = CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)));
  }
  printf("stage is [%s]\n",uVar42);
  if (((byte)local_c0 & 1) != 0) {
    operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
  }
  if (((char)pDStack_108 != '\0') && ((local_120 & 1) != 0)) {
    operator_delete((void *)CONCAT44(uStack_10c,
                                     CONCAT13(uStack_10d,
                                              CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))))
    ;
  }
  Device::GetReason();
  uVar37 = uStack_10c;
  cVar32 = local_110;
  uVar31 = uStack_117;
  uVar39 = local_120;
  if ((char)pDStack_108 == '\0') {
    local_c0 = (string)0x0;
    cStack_bf = 0;
  }
  else {
    uVar7 = CONCAT71(uStack_117,cStack_118);
    uVar54 = CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120)));
    local_120 = 0;
    cStack_11c = '\0';
    cStack_11b = '\0';
    uStack_11a = 0;
    uVar8 = CONCAT44(uStack_10c,
                     CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
    cStack_118 = '\0';
    uStack_117 = 0;
    local_110 = '\0';
    uStack_10f = 0;
    uStack_10e = 0;
    uStack_10d = 0;
    uStack_10c = 0;
    uStack_b8._0_4_ = (undefined4)uVar7;
    uStack_b8._4_4_ = (uint)((uint7)uVar31 >> 0x18);
    local_c0 = SUB41(uVar39,0);
    cStack_bf = (char)(uVar39 >> 8);
    cStack_be = (char)(uVar39 >> 0x10);
    cStack_bd = (char)(uVar39 >> 0x18);
    uStack_bc = (undefined4)((ulong)uVar54 >> 0x20);
    local_b0 = cVar32;
    uStack_af = (undefined4)((ulong)uVar8 >> 8);
    uStack_ab = (undefined1)((uint)uVar37 >> 8);
    uStack_aa = (undefined2)((uint)uVar37 >> 0x10);
  }
  uVar42 = (ulong)&local_c0 | 1;
  if (((byte)local_c0 & 1) != 0) {
    uVar42 = CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)));
  }
  printf("reason is [%s]\n",uVar42);
  if (((byte)local_c0 & 1) != 0) {
    operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
  }
  if (((char)pDStack_108 != '\0') && ((local_120 & 1) != 0)) {
    operator_delete((void *)CONCAT44(uStack_10c,
                                     CONCAT13(uStack_10d,
                                              CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))))
    ;
  }
  pRVar45 = (RecoveryUI *)(**(code **)(*(long *)param_1 + 0x10))(param_1);
  (**(code **)(*(long *)pRVar45 + 0xb8))(pRVar45,param_1);
  debuggerd_init(pRVar45);
  pcVar61 = operator_new(0x20);
  pcVar61[0x1f] = '\0';
  uVar54 = s_vendor_wipecache_menu_confirmed_00143d93._0_8_;
  cStack_bd = '\0';
  local_120 = 0x21;
  cStack_11c = '\0';
  cStack_11b = '\0';
  uStack_11a = 0;
  cStack_118 = '\x1f';
  uStack_117 = 0;
  local_110 = (char)pcVar61;
  uStack_10f = (undefined1)((ulong)pcVar61 >> 8);
  uStack_10e = (undefined1)((ulong)pcVar61 >> 0x10);
  uStack_10d = (undefined1)((ulong)pcVar61 >> 0x18);
  uStack_10c = (undefined4)((ulong)pcVar61 >> 0x20);
  *(ulong *)(pcVar61 + 8) =
       CONCAT17(s_vendor_wipecache_menu_confirmed_00143d93[0xf],
                s_vendor_wipecache_menu_confirmed_00143d93._8_7_);
  *(undefined8 *)pcVar61 = uVar54;
  uVar54 = CONCAT71(s_vendor_wipecache_menu_confirmed_00143d93._16_7_,
                    s_vendor_wipecache_menu_confirmed_00143d93[0xf]);
  *(undefined8 *)(pcVar61 + 0x17) = s_vendor_wipecache_menu_confirmed_00143d93._23_8_;
  *(undefined8 *)(pcVar61 + 0xf) = uVar54;
  local_c0 = (string)0x4;
  cStack_bf = 0x6e;
  cStack_be = 'o';
  android::base::SetProperty((string *)&local_120,&local_c0);
  if (((byte)local_c0 & 1) != 0) {
    operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
  }
  if ((local_120 & 1) != 0) {
    operator_delete((void *)CONCAT44(uStack_10c,
                                     CONCAT13(uStack_10d,
                                              CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))))
    ;
  }
  local_190 = 0;
  local_194 = 0;
  Device::GetStage();
  if ((char)pDStack_108 == '\0') {
    Device::GetStage();
    if ((char)local_a8 != '\0') {
      pRVar41 = (RecoveryUI *)&local_190;
      pcVar61 = (char *)((ulong)&local_c0 | 1);
      if (((byte)local_c0 & 1) != 0) {
        pcVar61 = (char *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)));
      }
      iVar38 = sscanf(pcVar61,"%d/%d",pRVar41,&local_194);
      bVar35 = iVar38 == 2;
      if (((char)local_a8 != '\0') && (((byte)local_c0 & 1) != 0)) {
        operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)))
                       );
      }
      if ((char)pDStack_108 != '\0') goto LAB_00229b24;
      goto LAB_00229b2c;
    }
    if (*(long *)(lVar5 + 0x28) != local_88) goto LAB_0022cbd4;
    std::__throw_bad_optional_access_abi_ne180000_();
LAB_0022c180:
    if (*(long *)(lVar5 + 0x28) != local_88) goto LAB_0022cbd4;
    std::__throw_bad_optional_access_abi_ne180000_();
LAB_0022c198:
    chown("/data/miui",1000,1000);
    chmod("/data/miui",0x1f9);
    chown("/data/miui/app",1000,1000);
    chmod("/data/miui/app",0x1f9);
    _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
              (local_228,"/factoryreset_tmp/miui/app");
    _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
              (local_240,"/data/miui/app");
    copyFolder(local_228,local_240);
    if ((local_240[0] & 1) != 0) {
      operator_delete(local_230);
    }
    if ((local_228[0] & 1) != 0) {
      operator_delete(local_218);
    }
    uVar42 = 0;
    iVar38 = 0;
LAB_0022c0e8:
    psVar56 = local_138;
    local_138 = (string *)0x0;
    if (psVar56 != (string *)0x0) {
      (**(code **)(*(long *)psVar56 + 8))();
    }
    psVar56 = (string *)CONCAT44(uStack_b8._4_4_,(undefined4)uStack_b8);
    uStack_b8 = (string *)CONCAT44(uStack_b8._4_4_,(undefined4)uStack_b8);
    if ((iVar38 == 0) || (uStack_b8 = psVar56, iVar38 == 7)) goto LAB_0022ba14;
  }
  else {
    bVar35 = false;
LAB_00229b24:
    if ((local_120 & 1) == 0) {
LAB_00229b2c:
      if (bVar35) {
LAB_00229b40:
        pRVar41 = (RecoveryUI *)(ulong)local_194;
        (**(code **)(*(long *)pRVar45 + 0x110))(pRVar45,local_190);
      }
    }
    else {
      operator_delete((void *)CONCAT44(uStack_10c,
                                       CONCAT13(uStack_10d,
                                                CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))
                                      ));
      if (bVar35) goto LAB_00229b40;
    }
    local_1b0 = (byte *)0x0;
    local_1a8 = (byte *)0x0;
    local_c0 = (string)0x28;
    local_1a0 = 0;
    uStack_ab = 0;
    uStack_af = 0x746e6972;
    uStack_b8._1_3_ = (undefined3)s_ro_build_fingerprint_0014c624._8_8_;
    uStack_b8._4_4_ = SUB84(s_ro_build_fingerprint_0014c624._8_8_,3);
    local_b0 = SUB81(s_ro_build_fingerprint_0014c624._8_8_,7);
    cStack_bf = (char)s_ro_build_fingerprint_0014c624._0_8_;
    cStack_be = SUB81(s_ro_build_fingerprint_0014c624._0_8_,1);
    cStack_bd = SUB81(s_ro_build_fingerprint_0014c624._0_8_,2);
    uStack_bc = SUB84(s_ro_build_fingerprint_0014c624._0_8_,3);
    uStack_b8._0_1_ = SUB81(s_ro_build_fingerprint_0014c624._0_8_,7);
    local_138 = (string *)((ulong)local_138 & 0xffffffffffff0000);
    android::base::GetProperty((base *)&local_c0,(string *)&local_138,(string *)pRVar41);
    uVar42 = (ulong)local_170[0] >> 0x10;
    local_170[0] = (string *)CONCAT62((uint6)uVar42 & 0xffffffffff00,0x3a02);
    android::base::Split((base *)&local_120,(string *)local_170,(string *)pRVar41);
    if (((ulong)local_170[0] & 1) != 0) {
      operator_delete(local_160);
    }
    if ((local_120 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_10c,
                                       CONCAT13(uStack_10d,
                                                CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))
                                      ));
    }
    if (((ulong)local_138 & 1) != 0) {
      operator_delete(local_128);
    }
    if (((byte)local_c0 & 1) != 0) {
      operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
    }
    uStack_10f = 0;
    uStack_117 = (undefined7)s_Android_Recovery_0014a9da._8_8_;
    local_110 = SUB81(s_Android_Recovery_0014a9da._8_8_,7);
    local_120._1_3_ = (undefined3)s_Android_Recovery_0014a9da._0_8_;
    local_120 = CONCAT31(local_120._1_3_,0x20);
    cStack_11c = SUB81(s_Android_Recovery_0014a9da._0_8_,3);
    cStack_11b = SUB81(s_Android_Recovery_0014a9da._0_8_,4);
    uStack_11a = SUB82(s_Android_Recovery_0014a9da._0_8_,5);
    cStack_118 = SUB81(s_Android_Recovery_0014a9da._0_8_,7);
    std::vector<std::string,std::allocator<std::string>>::insert
              ((vector<std::string,std::allocator<std::string>> *)&local_1b0,local_1b0,&local_120);
    if ((local_120 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_10c,
                                       CONCAT13(uStack_10d,
                                                CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))
                                      ));
    }
    (**(code **)(*(long *)pRVar45 + 0x1c8))(pRVar45,&local_1b0);
    pRVar45[0xbc] = (RecoveryUI)0x0;
    (**(code **)(*(long *)param_1 + 0x28))(param_1);
    local_1c8 = 0;
    local_1c0 = 0;
    local_1b8 = (void *)0x0;
    Device::GetReason();
    uVar37 = uStack_10c;
    cVar32 = local_110;
    uVar31 = uStack_117;
    uVar39 = local_120;
    if ((char)pDStack_108 == '\0') {
      local_c0 = (string)0x0;
      cStack_bf = 0;
    }
    else {
      uVar7 = CONCAT71(uStack_117,cStack_118);
      uVar54 = CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120)));
      local_120 = 0;
      cStack_11c = '\0';
      cStack_11b = '\0';
      uStack_11a = 0;
      uVar8 = CONCAT44(uStack_10c,
                       CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
      cStack_118 = '\0';
      uStack_117 = 0;
      local_110 = '\0';
      uStack_10f = 0;
      uStack_10e = 0;
      uStack_10d = 0;
      uStack_10c = 0;
      uStack_b8._0_4_ = (undefined4)uVar7;
      uStack_b8._4_4_ = (uint)((uint7)uVar31 >> 0x18);
      local_c0 = SUB41(uVar39,0);
      cStack_bf = (char)(uVar39 >> 8);
      cStack_be = (char)(uVar39 >> 0x10);
      cStack_bd = (char)(uVar39 >> 0x18);
      uStack_bc = (undefined4)((ulong)uVar54 >> 0x20);
      local_b0 = cVar32;
      uStack_af = (undefined4)((ulong)uVar8 >> 8);
      uStack_ab = (undefined1)((uint)uVar37 >> 8);
      uStack_aa = (undefined2)((uint)uVar37 >> 0x10);
    }
    android::base::Trim<std::string&>((base *)&local_c0,extraout_x1);
    if (((byte)local_c0 & 1) != 0) {
      operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
    }
    if (((char)pDStack_108 != '\0') && ((local_120 & 1) != 0)) {
      operator_delete((void *)CONCAT44(uStack_10c,
                                       CONCAT13(uStack_10d,
                                                CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)))
                                      ));
    }
    uVar42 = local_1c8 >> 1 & 0x7f;
    pvVar4 = (void *)((ulong)&local_1c8 | 1);
    if ((local_1c8 & 1) != 0) {
      uVar42 = local_1c0;
      pvVar4 = local_1b8;
    }
    if (10 < (long)uVar42) {
      plVar51 = (long *)((long)pvVar4 + uVar42);
      pvVar48 = pvVar4;
      uVar57 = uVar42;
      while( true ) {
        psVar56 = (string *)(uVar57 - 10);
        plVar47 = memchr(pvVar48,0x42,(size_t)psVar56);
        if (plVar47 == (long *)0x0) break;
        if (*plVar47 == 0x696e6f4d746f6f42 && *(long *)((long)plVar47 + 3) == 0x726f74696e6f4d74) {
          if ((plVar47 != plVar51) && ((long)plVar47 - (long)pvVar4 != -1)) {
            lVar49 = 0x18;
            goto LAB_00229f90;
          }
          break;
        }
        pvVar48 = (void *)((long)plVar47 + 1);
        uVar57 = (long)plVar51 - (long)pvVar48;
        if ((long)uVar57 < 0xb) break;
      }
      pvVar48 = pvVar4;
      uVar57 = uVar42;
      if (0x16 < (long)uVar42) {
        while( true ) {
          psVar56 = (string *)(uVar57 - 0x16);
          plVar47 = memchr(pvVar48,0x65,(size_t)psVar56);
          if (plVar47 == (long *)0x0) break;
          if ((*plVar47 == 0x6966656c62616e65 && plVar47[1] == 0x6f7470797263656c) &&
              *(long *)((long)plVar47 + 0xf) == 0x64656c6961665f6f) {
            if ((plVar47 != plVar51) && ((long)plVar47 - (long)pvVar4 != -1)) {
              lVar49 = 0x19;
              goto LAB_00229f90;
            }
            break;
          }
          uVar57 = (long)plVar51 - ((long)plVar47 + 1);
          pvVar48 = (void *)((long)plVar47 + 1);
          if ((long)uVar57 < 0x17) break;
        }
      }
      pvVar48 = pvVar4;
      uVar57 = uVar42;
      if (0x10 < (long)uVar42) {
        while( true ) {
          psVar56 = (string *)(uVar57 - 0x10);
          plVar47 = memchr(pvVar48,0x69,(size_t)psVar56);
          if (plVar47 == (long *)0x0) break;
          if ((*plVar47 == 0x6573755f74696e69 && plVar47[1] == 0x656c6961665f3072) &&
              (char)plVar47[2] == 'd') {
            if ((plVar47 != plVar51) && ((long)plVar47 - (long)pvVar4 != -1)) {
              lVar49 = 0x1b;
              goto LAB_00229f90;
            }
            break;
          }
          uVar57 = (long)plVar51 - ((long)plVar47 + 1);
          pvVar48 = (void *)((long)plVar47 + 1);
          if ((long)uVar57 < 0x11) break;
        }
        pvVar48 = pvVar4;
        if (0x10 < (long)uVar42) {
          while( true ) {
            psVar56 = (string *)(uVar42 - 0x10);
            plVar47 = memchr(pvVar48,0x73,(size_t)psVar56);
            if (plVar47 == (long *)0x0) break;
            if ((*plVar47 == 0x696c6f705f746573 && plVar47[1] == 0x656c6961665f7963) &&
                (char)plVar47[2] == 'd') {
              if ((plVar47 != plVar51) && ((long)plVar47 - (long)pvVar4 != -1)) {
                lVar49 = 0x1d;
                goto LAB_00229f90;
              }
              break;
            }
            uVar42 = (long)plVar51 - ((long)plVar47 + 1);
            pvVar48 = (void *)((long)plVar47 + 1);
            if ((long)uVar42 < 0x11) break;
          }
        }
      }
    }
    psVar56 = (string *)0x0;
    lVar49 = std::string::find_abi_ne180000_((string *)&local_1c8,"fs_mgr_mount_all",0);
    if (lVar49 == -1) {
      psVar56 = (string *)0x0;
      lVar49 = std::string::find_abi_ne180000_((string *)&local_1c8,"RescueParty",0);
      if (lVar49 != -1) {
        lVar49 = 0x1c;
        goto LAB_00229f90;
      }
    }
    else {
      lVar49 = 0x1a;
LAB_00229f90:
      (**(code **)(*(long *)pRVar45 + lVar49 * 8))(pRVar45,1);
    }
    printf("Command:");
    pbVar63 = *(byte **)(param_2 + 8);
    for (pbVar62 = *(byte **)param_2; pbVar62 != pbVar63; pbVar62 = pbVar62 + 0x18) {
      pbVar59 = *(byte **)(pbVar62 + 0x10);
      if ((*pbVar62 & 1) == 0) {
        pbVar59 = pbVar62 + 1;
      }
      printf(" \"%s\"",pbVar59);
    }
    puts("\n");
    property_list(print_property,0);
    putchar(10);
    uVar39 = 8;
    if (!(bool)uVar10) {
      uVar39 = 1;
    }
    local_280 = (ulong)uVar39;
    if (pcVar66 != (char *)0x0) {
      psVar56 = (string *)0x7;
      iVar38 = strncmp("/sdcard",pcVar66,7);
      if (iVar38 == 0) {
        uStack_cc = 0;
        uStack_d0 = 0;
        pDStack_108 = (Device *)0x0;
        local_110 = '\0';
        uStack_10f = 0;
        uStack_10e = 0;
        uStack_10d = 0;
        uStack_10c = 0;
        local_f8 = (char *)0x0;
        local_100 = (uint *)0x0;
        puStack_e8 = (uint *)0x0;
        local_f0 = 0;
        uStack_d8 = 0;
        local_d4 = 0;
        local_e0 = (char *)0x0;
        cStack_118 = '\0';
        uStack_117 = 0;
        local_120 = 0;
        cStack_11c = '\0';
        cStack_11b = '\0';
        uStack_11a = 0;
        property_get("ro.boot.bootdevice",&local_120,&DAT_00150597);
        psVar56 = (string *)&local_120;
        (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"ro.boot.bootdevice is: %s\n");
        uVar42 = __strlen_chk(&local_120,0x5c);
        if (uVar42 < 7) {
LAB_0022a0c8:
          pcVar61 = "Update via sdcard on EMMC dev. Using path from fstab\n";
LAB_0022a0d0:
          (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,pcVar61);
          goto LAB_0022a0e0;
        }
        lVar49 = __strlen_chk(&local_120,0x5c);
        psVar56 = (string *)0x7;
        iVar38 = strncmp((char *)((long)&local_128 + lVar49 + 2),".ufshc",7);
        if (iVar38 != 0) goto LAB_0022a0c8;
        (**(code **)(*(long *)pRVar45 + 0x158))
                  (pRVar45,"Update via sdcard on UFS dev.Mounting card\n");
        cStack_118 = '\0';
        local_120 = 0x64732f0e;
        cStack_11c = 'c';
        cStack_11b = 'a';
        uStack_11a = 0x6472;
        lVar49 = volume_for_mount_point((string *)&local_120);
        if ((local_120 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110)))));
        }
        if (lVar49 != 0) {
          psVar56 = (string *)(lVar49 + 0x61);
          if ((*(byte *)(lVar49 + 0x60) & 1) != 0) {
            psVar56 = *(string **)(lVar49 + 0x70);
          }
          iVar38 = strncmp((char *)psVar56,"vfat",5);
          if (iVar38 != 0) {
            pcVar66 = "Unsupported format on the sdcard: %s\n";
LAB_0022b658:
            (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,pcVar66,psVar56);
            goto LAB_0022b66c;
          }
          pcVar61 = (char *)(lVar49 + 0x49);
          if ((*(byte *)(lVar49 + 0x48) & 1) != 0) {
            pcVar61 = *(char **)(lVar49 + 0x58);
          }
          pvVar4 = (void *)(lVar49 + 0x81);
          if ((*(byte *)(lVar49 + 0x80) & 1) != 0) {
            pvVar4 = *(void **)(lVar49 + 0x90);
          }
          iVar38 = mount("/dev/block/mmcblk0p1",pcVar61,(char *)psVar56,*(ulong *)(lVar49 + 0x78),
                         pvVar4);
          if (iVar38 != 0) {
            piVar55 = (int *)__errno();
            psVar56 = (string *)strerror(*piVar55);
            pcVar66 = "Failed to mount sdcard : %s\n";
            goto LAB_0022b658;
          }
          pcVar61 = "Done mounting sdcard\n";
          goto LAB_0022a0d0;
        }
        (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Unknown volume for /sdcard.Check fstab\n");
        goto LAB_0022b66c;
      }
LAB_0022a0e0:
      DAT_0042cf18 = 1;
      if (local_2a4 != 0) {
LAB_0022a320:
        delete_minidump();
        uVar39 = (**(code **)(*(long *)pRVar45 + 0x50))
                           (pRVar45,param_1,uVar68,pcVar66,uVar13,local_2a4,local_290);
        uVar42 = (ulong)uVar39;
        if ((uVar39 == 0) && (bVar34)) {
          uVar39 = (**(code **)(*(long *)pRVar45 + 0x68))(pRVar45,0);
          uVar42 = (ulong)uVar39;
          if (uVar39 != 0) {
            (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Data wipe failed.\n");
LAB_0022a398:
            (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Installation aborted.\n");
            copy_demsg();
            if (((int)uVar42 == 5) && (local_2a4 < 4)) {
              copy_logs((bool)DAT_0042cf18);
              set_retry_bootloader_message(local_2a4 + 1,param_2);
              psVar56 = (string *)(ulong)(local_2a4 + 1);
              (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Retry attempt %d\n");
              if (*(long *)(lVar5 + 0x28) != local_88) goto LAB_0022cbd4;
              Reboot("recovery",8);
              goto LAB_0022a424;
            }
            goto LAB_0022a488;
          }
        }
        else if (uVar39 != 0) goto LAB_0022a398;
        goto LAB_0022ba14;
      }
      local_c0 = (string)0x0;
      cStack_bf = 0;
      cStack_be = '\0';
      cStack_bd = '\0';
      uStack_bc = 0;
      uStack_b8._0_4_ = 0;
      uStack_b8._4_4_ = 0;
      local_b0 = '\0';
      uStack_af = 0;
      uStack_ab = 0;
      uStack_aa = 0;
      uStack_10d = 0;
      uStack_10f = 0x6f;
      uStack_10e = 0x6e;
      uStack_117 = (undefined7)s_ro_boot_bootreason_0014f8eb._8_8_;
      local_110 = SUB81(s_ro_boot_bootreason_0014f8eb._8_8_,7);
      local_120._1_3_ = (undefined3)s_ro_boot_bootreason_0014f8eb._0_8_;
      local_120 = CONCAT31(local_120._1_3_,0x24);
      cStack_11c = SUB81(s_ro_boot_bootreason_0014f8eb._0_8_,3);
      cStack_11b = SUB81(s_ro_boot_bootreason_0014f8eb._0_8_,4);
      uStack_11a = SUB82(s_ro_boot_bootreason_0014f8eb._0_8_,5);
      cStack_118 = SUB81(s_ro_boot_bootreason_0014f8eb._0_8_,7);
      local_138 = (string *)((ulong)local_138 & 0xffffffffffff0000);
      android::base::GetProperty((base *)&local_120,(string *)&local_138,psVar56);
      if (((ulong)local_138 & 1) != 0) {
        operator_delete(local_128);
      }
      if ((local_120 & 1) != 0) {
        operator_delete((void *)CONCAT44(uStack_10c,
                                         CONCAT13(uStack_10d,
                                                  CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)
                                                          ))));
      }
      uVar42 = (ulong)((byte)local_c0 >> 1);
      if (((byte)local_c0 & 1) != 0) {
        uVar42 = CONCAT44(uStack_b8._4_4_,(undefined4)uStack_b8);
      }
      if (uVar42 != 0) {
        if (((DAT_0042cf38 & 1) == 0) && (iVar38 = __cxa_guard_acquire(&DAT_0042cf38), iVar38 != 0))
        {
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_120,"kernel_panic");
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&pDStack_108,"Panic");
          std::vector<std::string,std::allocator<std::string>>::vector_abi_ne180000_
                    (&DAT_0042cf20,&local_120,2);
          if (((ulong)pDStack_108 & 1) != 0) {
            operator_delete(local_f8);
          }
          if ((local_120 & 1) != 0) {
            operator_delete((void *)CONCAT44(uStack_10c,
                                             CONCAT13(uStack_10d,
                                                      CONCAT12(uStack_10e,
                                                               CONCAT11(uStack_10f,local_110)))));
          }
          __cxa_atexit(std::vector<std::string,std::allocator<std::string>>::~vector_abi_ne180000_,
                       &DAT_0042cf20,&DAT_0042ce00);
          __cxa_guard_release(&DAT_0042cf38);
        }
        pbVar62 = DAT_0042cf28;
        if (DAT_0042cf20 != DAT_0042cf28) {
          pbVar63 = DAT_0042cf20;
          do {
            uVar42 = *(ulong *)(pbVar63 + 8);
            pbVar59 = *(byte **)(pbVar63 + 0x10);
            if ((*pbVar63 & 1) == 0) {
              pbVar59 = pbVar63 + 1;
              uVar42 = (ulong)(*pbVar63 >> 1);
            }
            uVar57 = (ulong)((byte)local_c0 >> 1);
            uVar6 = (ulong)&local_c0 | 1;
            if (((byte)local_c0 & 1) != 0) {
              uVar57 = CONCAT44(uStack_b8._4_4_,(undefined4)uStack_b8);
              uVar6 = CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)));
            }
            uVar42 = android::base::EqualsIgnoreCase(pbVar59,uVar42,uVar6,uVar57);
            if ((uVar42 & 1) != 0) {
              uVar10 = true;
              goto joined_r0x0022b4e0;
            }
            pbVar63 = pbVar63 + 0x18;
          } while (pbVar63 != pbVar62);
        }
      }
      uVar10 = false;
joined_r0x0022b4e0:
      if (((byte)local_c0 & 1) != 0) {
        operator_delete((void *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)))
                       );
      }
      if (!(bool)uVar10) {
        set_retry_bootloader_message(1,param_2);
        goto LAB_0022a320;
      }
      (**(code **)(*(long *)pRVar45 + 0x158))
                (pRVar45,"bootreason is in the blocklist; skip OTA installation\n");
      sVar50 = strlen(pcVar66);
      if (sVar50 < 0xfffffffffffffff0) {
        if (sVar50 < 0x17) {
          pcVar61 = (char *)((ulong)&local_1e0 | 1);
          local_1e0 = (undefined1 *)CONCAT71(local_1e0._1_7_,(char)((int)sVar50 << 1));
          if (sVar50 != 0) goto LAB_0022b184;
        }
        else {
          uVar42 = (sVar50 | 0xf) + 1;
          pcVar61 = operator_new(uVar42);
          local_1e0 = (undefined1 *)(uVar42 | 1);
          sStack_1d8 = sVar50;
          local_1d0 = pcVar61;
LAB_0022b184:
          memmove(pcVar61,pcVar66,sVar50);
        }
        pcVar61[sVar50] = '\0';
        local_138 = (string *)0x0;
        local_130 = (string *)0x0;
        local_128 = (string *)0x0;
        if (((ulong)local_1e0 & 1) == 0) {
          cStack_118 = (char)sStack_1d8;
          uStack_117 = (undefined7)(sStack_1d8 >> 8);
          local_120 = (uint)local_1e0;
          cStack_11c = (char)((ulong)local_1e0 >> 0x20);
          cStack_11b = (char)((ulong)local_1e0 >> 0x28);
          uStack_11a = (undefined2)((ulong)local_1e0 >> 0x30);
          local_110 = (char)local_1d0;
          uStack_10f = (undefined1)((ulong)local_1d0 >> 8);
          uStack_10e = (undefined1)((ulong)local_1d0 >> 0x10);
          uStack_10d = (undefined1)((ulong)local_1d0 >> 0x18);
          uStack_10c = (undefined4)((ulong)local_1d0 >> 0x20);
        }
        else {
          std::string::__init_copy_ctor_external((string *)&local_120,local_1d0,sStack_1d8);
        }
        uVar42 = (ulong)pDStack_108 >> 0x10;
        pDStack_108 = (Device *)CONCAT62((uint6)uVar42 & 0xffffffffff00,0x3002);
        std::to_string(0x17);
        pcVar66 = "error: ";
        puVar53 = (ulong *)std::string::insert((ulong)local_170,(char *)0x0,0x14305f);
        local_e0 = (char *)puVar53[2];
        puStack_e8 = (uint *)puVar53[1];
        local_f0 = *puVar53;
        local_148 = (Device *)&local_138;
        puVar53[1] = 0;
        puVar53[2] = 0;
        *puVar53 = 0;
        local_138 = (string *)0x0;
        local_130 = (string *)0x0;
        local_128 = (string *)0x0;
        uStack_140 = 0;
        local_158 = operator_new(0x48);
        local_128 = local_158 + 0x48;
        uStack_b8 = (string *)&local_158;
        local_c0 = SUB81(&local_128,0);
        cStack_bf = (char)((ulong)&local_128 >> 8);
        cStack_be = (char)((ulong)&local_128 >> 0x10);
        cStack_bd = (char)((ulong)&local_128 >> 0x18);
        uStack_bc = (undefined4)((ulong)&local_128 >> 0x20);
        local_b0 = (char)&local_150;
        uStack_af = (undefined4)((ulong)&local_150 >> 8);
        uStack_ab = (undefined1)((ulong)&local_150 >> 0x28);
        uStack_aa = (undefined2)((ulong)&local_150 >> 0x30);
        local_a8 = 0;
        local_138 = local_158;
        local_130 = local_158;
        if ((local_120 & 1) == 0) {
          *(ulong *)(local_158 + 8) = CONCAT71(uStack_117,cStack_118);
          *(ulong *)local_158 =
               CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120)));
          *(ulong *)(local_158 + 0x10) =
               CONCAT44(uStack_10c,
                        CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
          psVar56 = local_158;
        }
        else {
          pcVar66 = (char *)CONCAT71(uStack_117,cStack_118);
          local_150 = local_158;
          std::string::__init_copy_ctor_external
                    (local_158,
                     (char *)CONCAT44(uStack_10c,
                                      CONCAT13(uStack_10d,
                                               CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))))
                     ,(ulong)pcVar66);
          psVar56 = local_150;
        }
        local_150 = psVar56 + 0x18;
        if (((ulong)pDStack_108 & 1) == 0) {
          *(char **)(psVar56 + 0x28) = local_f8;
          *(uint **)(psVar56 + 0x20) = local_100;
          *(Device **)local_150 = pDStack_108;
        }
        else {
          pcVar66 = (char *)local_100;
          std::string::__init_copy_ctor_external(local_150,local_f8,(ulong)local_100);
        }
        psVar56 = local_150 + 0x18;
        if ((local_f0 & 1) == 0) {
          *(char **)(local_150 + 0x28) = local_e0;
          *(uint **)(local_150 + 0x20) = puStack_e8;
          *(ulong *)psVar56 = local_f0;
          local_150 = psVar56;
        }
        else {
          pcVar66 = (char *)puStack_e8;
          local_150 = psVar56;
          std::string::__init_copy_ctor_external(psVar56,local_e0,(ulong)puStack_e8);
        }
        local_130 = local_150 + 0x18;
        if ((local_f0 & 1) != 0) {
          operator_delete(local_e0);
        }
        if (((ulong)pDStack_108 & 1) != 0) {
          operator_delete(local_f8);
        }
        if ((local_120 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110)))));
        }
        if (((ulong)local_170[0] & 1) != 0) {
          operator_delete(local_160);
        }
        local_120 = 0;
        cStack_11c = '\0';
        cStack_11b = '\0';
        uStack_11a = 0;
        cStack_118 = '\0';
        uStack_117 = 0;
        local_110 = '\0';
        uStack_10f = 0;
        uStack_10e = 0;
        uStack_10d = 0;
        uStack_10c = 0;
        android::base::Join<std::vector<std::string,std::allocator<std::string>>,char_const*>
                  ((base *)&local_138,(vector *)&DAT_00152dff,pcVar66);
        lVar49 = Paths::Get();
        if ((*(byte *)(lVar49 + 0x78) & 1) == 0) {
          uStack_b8 = *(string **)(lVar49 + 0x80);
          uVar54 = *(undefined8 *)(lVar49 + 0x78);
          uVar68 = *(undefined8 *)(lVar49 + 0x88);
          local_c0 = SUB81(uVar54,0);
          cStack_bf = (char)((ulong)uVar54 >> 8);
          cStack_be = (char)((ulong)uVar54 >> 0x10);
          cStack_bd = (char)((ulong)uVar54 >> 0x18);
          uStack_bc = (undefined4)((ulong)uVar54 >> 0x20);
          local_b0 = (char)uVar68;
          uStack_af = (undefined4)((ulong)uVar68 >> 8);
          uStack_ab = (undefined1)((ulong)uVar68 >> 0x28);
          uStack_aa = (undefined2)((ulong)uVar68 >> 0x30);
        }
        else {
          std::string::__init_copy_ctor_external
                    (&local_c0,*(char **)(lVar49 + 0x88),*(ulong *)(lVar49 + 0x80));
        }
        uVar42 = android::base::WriteStringToFile((string *)&local_120,&local_c0,false);
        if ((uVar42 & 1) == 0) {
          uVar42 = android::base::ShouldLog(4,0);
          if ((uVar42 & 1) != 0) {
LAB_0022bb40:
            puVar43 = (undefined4 *)__errno();
            uVar37 = *puVar43;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)local_170,"bootable/recovery/recovery.cpp",0x330,4,0,uVar37);
            poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_170);
            poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar44,"Failed to write ",0x10);
            std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                      (poVar44,&local_c0);
            android::base::LogMessage::~LogMessage((LogMessage *)local_170);
            *puVar43 = uVar37;
          }
        }
        uVar42 = android::base::ShouldLog(2,0);
        if ((uVar42 & 1) != 0) {
          puVar43 = (undefined4 *)__errno();
          uVar37 = *puVar43;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)local_170,"bootable/recovery/recovery.cpp",0x334,2,0,0xffffffff);
          poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_170);
          std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (poVar44,(string *)&local_120);
          android::base::LogMessage::~LogMessage((LogMessage *)local_170);
          *puVar43 = uVar37;
        }
        if (((byte)local_c0 & 1) != 0) {
          operator_delete((void *)CONCAT26(uStack_aa,
                                           CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
        }
        psVar56 = local_138;
        if ((local_120 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110)))));
          psVar56 = local_138;
        }
        local_138 = psVar56;
        psVar64 = local_130;
        if (psVar56 != (string *)0x0) {
          while (psVar67 = psVar64, psVar67 != psVar56) {
            psVar64 = psVar67 + -0x18;
            if (((byte)*psVar64 & 1) != 0) {
              operator_delete(*(void **)(psVar67 + -8));
            }
          }
          local_130 = psVar56;
          operator_delete(local_138);
        }
        if (((ulong)local_1e0 & 1) != 0) {
          operator_delete(local_1d0);
        }
        uVar42 = 4;
        goto LAB_0022ba14;
      }
      if (*(long *)(lVar5 + 0x28) != local_88) goto LAB_0022cbd4;
      std::string::__throw_length_error_abi_ne180000_();
LAB_0022c2ec:
      piVar55 = (int *)__errno();
      iVar60 = *piVar55;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x51e,3,0,0xffffffff);
      poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
      poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar44,"Failed to memory map package ",0x1d);
      sVar50 = strlen(local_2c0);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar44,local_2c0,sVar50);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_120);
      uVar42 = 0;
LAB_0022c3fc:
      iVar38 = 7;
      *piVar55 = iVar60;
      goto LAB_0022c0e8;
    }
    if ((bool)uVar11) {
      DAT_0042cf18 = 1;
      Device::GetReason();
      if ((char)pDStack_108 == '\0') {
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_c0,"bootable/recovery/recovery.cpp",0x489,6,0,0xffffffff);
        poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_c0);
        poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar44,"Check failed: device->GetReason().has_value()",0x2d);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar44," ",1);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_c0);
        if ((char)pDStack_108 != '\0') goto LAB_0022a274;
      }
      else {
LAB_0022a274:
        if ((local_120 & 1) != 0) {
          operator_delete((void *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110)))));
        }
      }
      Device::GetReason();
      if ((char)pDStack_108 == '\0') goto LAB_0022c180;
      uVar42 = (ulong)((byte)local_120._0_1_ >> 1);
      if ((local_120 & 1) != 0) {
        uVar42 = CONCAT71(uStack_117,cStack_118);
      }
      if (uVar42 == 0xb) {
        plVar51 = (long *)((ulong)&local_120 | 1);
        if ((local_120 & 1) != 0) {
          plVar51 = (long *)CONCAT44(uStack_10c,
                                     CONCAT13(uStack_10d,
                                              CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))));
        }
        bVar34 = *plVar51 == 0x5f747265766e6f63 &&
                 *(long *)((long)plVar51 + 3) == 0x6562665f74726576;
      }
      else {
        bVar34 = false;
      }
      if ((local_120 & 1) != 0) {
        operator_delete((void *)CONCAT44(uStack_10c,
                                         CONCAT13(uStack_10d,
                                                  CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110)
                                                          ))));
      }
      uVar39 = (**(code **)(*(long *)pRVar45 + 0x68))(pRVar45,bVar34);
      uVar42 = (ulong)uVar39;
      if (uVar39 == 0) goto LAB_0022ba14;
      (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Data wipe failed.\n");
LAB_0022a488:
      if ((int)uVar42 == 3 && !(bool)uVar17) {
        copy_logs(true);
        uVar42 = disable_format();
        if ((uVar42 & 1) != 0) {
          (**(code **)(*(long *)param_1 + 0x58))(param_1,0x16);
        }
        (**(code **)(*(long *)param_1 + 0x58))(param_1,0x18);
        uVar42 = set_disable_systemrestore();
        if ((uVar42 & 1) != 0) {
          (**(code **)(*(long *)param_1 + 0x58))(param_1,0x19);
        }
        puts("status none, prompt_and_wait");
        local_148 = param_1;
        this = (MiScreenRecoveryUI *)(**(code **)(*(long *)param_1 + 0x10))(param_1);
        Device::GetReason();
        uVar37 = uStack_10c;
        cVar32 = local_110;
        uVar31 = uStack_117;
        uVar39 = local_120;
        if ((char)pDStack_108 == '\0') {
          uVar39 = 0;
          local_c0 = (string)0x0;
          cStack_bf = 0;
          uVar42 = extraout_x10;
        }
        else {
          uVar54 = CONCAT71(uStack_117,cStack_118);
          uVar68 = CONCAT26(uStack_11a,CONCAT15(cStack_11b,CONCAT14(cStack_11c,local_120)));
          local_120 = 0;
          cStack_11c = '\0';
          cStack_11b = '\0';
          uStack_11a = 0;
          uVar7 = CONCAT44(uStack_10c,
                           CONCAT13(uStack_10d,CONCAT12(uStack_10e,CONCAT11(uStack_10f,local_110))))
          ;
          cStack_118 = '\0';
          uStack_117 = 0;
          local_110 = '\0';
          uStack_10f = 0;
          uStack_10e = 0;
          uStack_10d = 0;
          uStack_10c = 0;
          uStack_b8._0_4_ = (undefined4)uVar54;
          uStack_b8._4_4_ = (uint)((uint7)uVar31 >> 0x18);
          local_c0 = SUB41(uVar39,0);
          cStack_bf = (char)(uVar39 >> 8);
          cStack_be = (char)(uVar39 >> 0x10);
          cStack_bd = (char)(uVar39 >> 0x18);
          uStack_bc = (undefined4)((ulong)uVar68 >> 0x20);
          local_b0 = cVar32;
          uStack_af = (undefined4)((ulong)uVar7 >> 8);
          uStack_ab = (undefined1)((uint)uVar37 >> 8);
          uStack_aa = (undefined2)((uint)uVar37 >> 0x10);
          uVar39 = uVar39 & 0xff;
          uVar42 = CONCAT44(uStack_b8._4_4_,(undefined4)uStack_b8);
        }
        uVar57 = (ulong)(uVar39 >> 1);
        if ((uVar39 & 1) != 0) {
          uVar57 = uVar42;
        }
        if (uVar57 == 0x12) {
          plVar51 = (long *)((ulong)&local_c0 | 1);
          if ((uVar39 & 1) != 0) {
            plVar51 = (long *)CONCAT26(uStack_aa,CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0)));
          }
          bVar34 = (*plVar51 == 0x695f657461647075 && plVar51[1] == 0x6572676f72705f6e) &&
                   (short)plVar51[2] == 0x7373;
        }
        else {
          bVar34 = false;
        }
        if ((uVar39 & 1) != 0) {
          operator_delete((void *)CONCAT26(uStack_aa,
                                           CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
        }
        if (((char)pDStack_108 != '\0') && ((local_120 & 1) != 0)) {
          operator_delete((void *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110)))));
        }
        dVar29 = DAT_00142038;
        pcVar66 = (char *)((ulong)&local_c0 | 1);
switchD_0022a698_caseD_2:
        puts("recovery: GetMenuSelection");
        iVar38 = (**(code **)(*(long *)this + 0x20))(this,"menu_home",0,0,local_148);
        if (iVar38 != -1) {
          if (iVar38 != -2) goto code_r0x0022a660;
          goto switchD_0022a698_caseD_d;
        }
        set_reboot_message(0);
        sync();
        goto switchD_0022a698_caseD_8;
      }
    }
    else {
      if ((bool)uVar12) {
LAB_0022a30c:
        uVar42 = 3;
        goto LAB_0022a488;
      }
LAB_0022a424:
      if ((bool)uVar13) {
        uVar42 = (**(code **)(*(long *)pRVar45 + 0x60))(pRVar45);
        uVar42 = uVar42 & 0xffffffff;
        goto LAB_0022a488;
      }
      if ((bool)uVar14) {
        uVar39 = WipeAbDevice(param_1,local_2c8);
        uVar42 = (ulong)((uVar39 ^ 0xffffffff) & 1);
        goto LAB_0022ba14;
      }
      if ((bool)uVar15) {
        (**(code **)(*(long *)pRVar45 + 0x10))(pRVar45,1);
        (**(code **)(*(long *)pRVar45 + 0x118))(pRVar45,4);
        prompt_and_wait_adb_install(pRVar45,"/tmp/last_install");
        goto LAB_0022b540;
      }
      if (bVar27) {
        uVar39 = (**(code **)(*(long *)pRVar45 + 0x70))(pRVar45);
        uVar42 = (ulong)uVar39;
        if (uVar39 != 0) {
          (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Format data failed.\n");
          goto LAB_0022a488;
        }
      }
      else if (bVar28) {
        uVar39 = (**(code **)(*(long *)pRVar45 + 0x78))(pRVar45);
        uVar42 = (ulong)uVar39;
        if (uVar39 != 0) {
          (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Format storage failed.\n");
          goto LAB_0022a488;
        }
      }
      else if (bVar18) {
        uVar39 = (**(code **)(*(long *)pRVar45 + 0x70))(pRVar45);
        uVar40 = (**(code **)(*(long *)pRVar45 + 0x78))(pRVar45);
        uVar42 = (ulong)(uVar40 | uVar39);
        if ((uVar40 | uVar39) != 0) {
          (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Format data and storage failed.\n");
          goto LAB_0022a488;
        }
      }
      else if (bVar19) {
        uVar39 = (**(code **)(*(long *)pRVar45 + 0x68))(pRVar45,0);
        uVar40 = (**(code **)(*(long *)pRVar45 + 0x78))(pRVar45);
        uVar42 = (ulong)(uVar40 | uVar39);
        if ((uVar40 | uVar39) != 0) {
          (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Wipe data and storage failed.\n");
          goto LAB_0022a488;
        }
      }
      else if (bVar20) {
        uVar39 = (**(code **)(*(long *)pRVar45 + 0x80))(pRVar45);
        uVar42 = (ulong)uVar39;
        if (uVar39 != 0) {
          (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Efs wipe failed.\n");
          goto LAB_0022a488;
        }
      }
      else {
        if (bVar21) {
          uVar42 = disable_format();
          if ((uVar42 & 1) != 0) {
            uVar42 = android::base::ShouldLog(4,0);
            if ((uVar42 & 1) == 0) {
LAB_0022b66c:
              uVar42 = 1;
            }
            else {
              puVar43 = (undefined4 *)__errno();
              uVar37 = *puVar43;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x4e3,4,0,
                         0xffffffff);
              poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar44,"Erase cache and data is already disable!1217 is already disable!",
                         0x40);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_120);
              uVar42 = 1;
              *puVar43 = uVar37;
            }
            goto LAB_0022ba14;
          }
          puts("trigger factoryreset");
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_120,"vendor.wipedata.savedataapp");
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_c0,&DAT_00150519);
          android::base::SetProperty((string *)&local_120,&local_c0);
          if (((byte)local_c0 & 1) != 0) {
            operator_delete((void *)CONCAT26(uStack_aa,
                                             CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
          }
          if ((local_120 & 1) != 0) {
            operator_delete((void *)CONCAT44(uStack_10c,
                                             CONCAT13(uStack_10d,
                                                      CONCAT12(uStack_10e,
                                                               CONCAT11(uStack_10f,local_110)))));
          }
          if (local_2c0 == (char *)0x0) {
            (**(code **)(*(long *)pRVar45 + 0x1c0))(pRVar45,0);
            property_set("vold.miui","factoryreset");
            sleep(0x708);
            (**(code **)(*(long *)pRVar45 + 0x1c0))(pRVar45,1);
            goto LAB_0022b540;
          }
          local_170[0] = (string *)((ulong)local_170[0] & 0xffffffffffffff00);
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_120,local_2c0);
          uVar42 = SetupPackageMount((string *)&local_120,(bool *)local_170);
          if ((local_120 & 1) != 0) {
            operator_delete((void *)CONCAT44(uStack_10c,
                                             CONCAT13(uStack_10d,
                                                      CONCAT12(uStack_10e,
                                                               CONCAT11(uStack_10f,local_110)))));
          }
          if ((uVar42 & 1) == 0) {
            uVar42 = android::base::ShouldLog(2,0);
            if ((uVar42 & 1) != 0) {
              puVar43 = (undefined4 *)__errno();
              uVar37 = *puVar43;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x4ec,2,0,
                         0xffffffff);
              poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar44,"Failed to set up the package access, skipping installation",0x3a);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_120);
              *puVar43 = uVar37;
            }
            goto LAB_0022b66c;
          }
          local_138 = (string *)0x0;
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_c0,local_2c0);
          local_100 = (uint *)0x0;
          Package::CreateMemoryPackage((Package *)&local_c0,(string *)&local_120,psVar56);
          if (local_100 == &local_120) {
            lVar49 = 4;
            puVar52 = &local_120;
LAB_0022bdac:
            (**(code **)(*(long *)puVar52 + lVar49 * 8))();
          }
          else if (local_100 != (uint *)0x0) {
            lVar49 = 5;
            puVar52 = local_100;
            goto LAB_0022bdac;
          }
          if (((byte)local_c0 & 1) != 0) {
            operator_delete((void *)CONCAT26(uStack_aa,
                                             CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
          }
          if (local_138 == (string *)0x0) {
            uVar42 = android::base::ShouldLog(3,0);
            if ((uVar42 & 1) == 0) {
              uVar42 = 0;
              iVar38 = 7;
              goto LAB_0022c0e8;
            }
            goto LAB_0022c2ec;
          }
          mount("tmpfs","/factoryreset_tmp","tmpfs",0,"size=4G");
          _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                    (&local_120,"/factoryreset_tmp/miui/app");
          iVar38 = mkdir_recursively((string *)&local_120,0x1ed,false,(selabel_handle *)0x0);
          if ((local_120 & 1) != 0) {
            operator_delete((void *)CONCAT44(uStack_10c,
                                             CONCAT13(uStack_10d,
                                                      CONCAT12(uStack_10e,
                                                               CONCAT11(uStack_10f,local_110)))));
          }
          if (iVar38 == 0) {
            uVar68 = (**(code **)(*(long *)local_138 + 0x30))();
            _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                      (local_1f8,"/factoryreset_tmp/miui/app");
            _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                      (local_210,"/data/miui/app");
            uVar54 = (**(code **)(*(long *)local_138 + 0x10))();
            uVar42 = unSplicing(uVar68,local_1f8,local_210,uVar54);
            if ((local_210[0] & 1) != 0) {
              operator_delete(local_200);
            }
            if ((local_1f8[0] & 1) != 0) {
              operator_delete(local_1e8);
            }
            if ((uVar42 & 1) != 0) {
              WipeData(param_1,false,true);
              _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                        (&local_120,"/data");
              iVar38 = ensure_path_mounted((string *)&local_120);
              if ((local_120 & 1) != 0) {
                operator_delete((void *)CONCAT44(uStack_10c,
                                                 CONCAT13(uStack_10d,
                                                          CONCAT12(uStack_10e,
                                                                   CONCAT11(uStack_10f,local_110))))
                               );
              }
              if (iVar38 == 0) {
                _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                          (&local_120,"/data/miui/app");
                iVar38 = mkdir_recursively((string *)&local_120,0x1fd,false,(selabel_handle *)0x0);
                if ((local_120 & 1) != 0) {
                  operator_delete((void *)CONCAT44(uStack_10c,
                                                   CONCAT13(uStack_10d,
                                                            CONCAT12(uStack_10e,
                                                                     CONCAT11(uStack_10f,local_110))
                                                           )));
                }
                if (iVar38 == 0) goto LAB_0022c198;
                uVar42 = android::base::ShouldLog(4,0);
                if ((uVar42 & 1) == 0) goto LAB_0022c15c;
                piVar55 = (int *)__errno();
                iVar60 = *piVar55;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x50e,4,0,
                           0xffffffff);
                poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
                poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                    (poVar44,"failed to mkdir /data/miui/app (",0x20);
                pcVar66 = strerror(*piVar55);
                sVar50 = strlen(pcVar66);
                poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                    (poVar44,pcVar66,sVar50);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar44,")",1);
              }
              else {
                uVar42 = android::base::ShouldLog(4,0);
                if ((uVar42 & 1) == 0) {
LAB_0022c15c:
                  iVar38 = 7;
                  uVar42 = 2;
                  goto LAB_0022c0e8;
                }
                piVar55 = (int *)__errno();
                iVar60 = *piVar55;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x508,4,0,
                           0xffffffff);
                poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar44,"failed to mount userdata",0x18);
              }
              android::base::LogMessage::~LogMessage((LogMessage *)&local_120);
              uVar42 = 2;
              goto LAB_0022c3fc;
            }
            uVar42 = android::base::ShouldLog(4,0);
            if ((uVar42 & 1) != 0) {
              piVar55 = (int *)__errno();
              iVar60 = *piVar55;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x4fe,4,0,
                         0xffffffff);
              poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
              poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar44,"failed to unSplicing apps block ",0x20);
              poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar44," (",2);
              pcVar66 = strerror(*piVar55);
              sVar50 = strlen(pcVar66);
              poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar44,pcVar66,sVar50);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar44,")",1);
              goto LAB_0022c3ec;
            }
          }
          else {
            uVar42 = android::base::ShouldLog(4,0);
            if ((uVar42 & 1) != 0) {
              piVar55 = (int *)__errno();
              iVar60 = *piVar55;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_120,"bootable/recovery/recovery.cpp",0x4f8,4,0,
                         0xffffffff);
              poVar44 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_120);
              poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar44,"failed to mkdir /factoryreset_tmp/miui/app",0x2a);
              poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar44," (",2);
              pcVar66 = strerror(*piVar55);
              sVar50 = strlen(pcVar66);
              poVar44 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                  (poVar44,pcVar66,sVar50);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar44,")",1);
LAB_0022c3ec:
              android::base::LogMessage::~LogMessage((LogMessage *)&local_120);
              uVar42 = 1;
              goto LAB_0022c3fc;
            }
          }
          iVar38 = 7;
          uVar42 = 1;
          goto LAB_0022c0e8;
        }
        if (bVar22) {
          puts("vold.miui SubMenuWipeCmdWipePartial");
          uVar39 = (**(code **)(*(long *)pRVar45 + 0x40))(pRVar45);
          uVar42 = (ulong)uVar39;
          if (uVar39 != 0) {
            (**(code **)(*(long *)pRVar45 + 0x158))(pRVar45,"Data wipe skip failed.\n");
            goto LAB_0022a488;
          }
        }
        else {
          if (!bVar16) {
            uVar42 = IsRoDebuggable();
            if ((uVar42 & 1) != 0) {
              (**(code **)(*(long *)pRVar45 + 0x140))(pRVar45,1);
            }
            local_120 = 0;
            cStack_11c = '\0';
            cStack_11b = '\0';
            uStack_11a = 0;
            cStack_118 = '\0';
            uStack_117 = 0;
            local_110 = '\0';
            uStack_10f = 0;
            uStack_10e = 0;
            uStack_10d = 0;
            uStack_10c = 0;
            _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                      (&local_c0,"ro.boot.verifiedbootstate");
            _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
                      (&local_138,&DAT_0014826c);
            android::base::GetProperty((base *)&local_c0,(string *)&local_138,psVar56);
            if (((ulong)local_138 & 1) == 0) {
              if (((byte)local_c0 & 1) == 0) goto LAB_0022bd34;
LAB_0022bef8:
              operator_delete((void *)CONCAT26(uStack_aa,
                                               CONCAT15(uStack_ab,CONCAT41(uStack_af,local_b0))));
              if (bVar23) goto LAB_0022bf08;
LAB_0022bf30:
              if (bVar24) {
                lVar49 = 0x13;
                goto LAB_0022c05c;
              }
              if (bVar25) {
                lVar49 = 0x15;
                goto LAB_0022c05c;
              }
            }
            else {
              operator_delete(local_128);
              if (((byte)local_c0 & 1) != 0) goto LAB_0022bef8;
LAB_0022bd34:
              if (!bVar23) goto LAB_0022bf30;
LAB_0022bf08:
              pcVar66 = (char *)((ulong)&local_120 | 1);
              if ((local_120 & 1) != 0) {
                pcVar66 = (char *)CONCAT44(uStack_10c,
                                           CONCAT13(uStack_10d,
                                                    CONCAT12(uStack_10e,
                                                             CONCAT11(uStack_10f,local_110))));
              }
              iVar38 = strcmp(pcVar66,"green");
              if (iVar38 != 0) goto LAB_0022bf30;
              lVar49 = 0x12;
LAB_0022c05c:
              (**(code **)(*(long *)pRVar45 + lVar49 * 8))(pRVar45,1);
            }
            if (bVar26) {
              (**(code **)(*(long *)pRVar45 + 0x90))(pRVar45,1);
            }
            (**(code **)(*(long *)pRVar45 + 0x118))(pRVar45,4);
            if ((local_120 & 1) != 0) {
              operator_delete((void *)CONCAT44(uStack_10c,
                                               CONCAT13(uStack_10d,
                                                        CONCAT12(uStack_10e,
                                                                 CONCAT11(uStack_10f,local_110)))));
            }
            goto LAB_0022a30c;
          }
LAB_0022b540:
          uVar42 = 0;
        }
      }
    }
LAB_0022ba14:
    save_install_status((int)uVar42);
    printf("enter finish_recovery status[%d]\n",uVar42);
    FinishRecovery(pRVar45);
    psVar56 = uStack_b8;
  }
  uStack_b8 = psVar56;
  if ((local_1c8 & 1) != 0) {
    operator_delete(local_1b8);
  }
  pbVar62 = local_1b0;
  pbVar63 = local_1a8;
  if (local_1b0 != (byte *)0x0) {
    while (pbVar59 = pbVar63, pbVar59 != pbVar62) {
      pbVar63 = pbVar59 + -0x18;
      if ((*pbVar63 & 1) != 0) {
        operator_delete(*(void **)(pbVar59 + -8));
      }
    }
    local_1a8 = pbVar62;
    operator_delete(local_1b0);
  }
  if (local_188 != (void *)0x0) {
    local_180 = local_188;
    operator_delete(local_188);
  }
  if (*(long *)(lVar5 + 0x28) == local_88) {
    return local_280 & 0xffffffff;
  }
LAB_0022cbd4:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
