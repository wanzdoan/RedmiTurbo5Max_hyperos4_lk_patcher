
uint FUN_ffff0000907b73ac(uint *param_1,uint param_2)

{
  ushort uVar1;
  ushort uVar2;
  undefined *puVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  char *pcVar7;
  undefined1 *puVar8;
  long lVar9;
  undefined8 uVar10;
  undefined4 uVar11;
  short *psVar12;
  uint *puVar13;
  short *psVar14;
  ushort *puVar15;
  ushort *puVar16;
  uint uVar17;
  ulong uVar18;
  long lVar19;
  ulong uVar20;
  undefined8 local_d40;
  undefined1 auStack_d38 [512];
  undefined1 auStack_b38 [512];
  undefined1 local_938 [512];
  undefined8 local_738;
  undefined8 local_730;
  undefined8 local_728;
  undefined8 local_720;
  undefined8 local_718;
  undefined8 local_710;
  undefined8 local_708;
  undefined8 local_700;
  undefined8 local_6f8;
  undefined8 local_6f0;
  undefined8 local_6e8;
  undefined8 local_6e0;
  undefined8 local_6d8;
  undefined8 local_6d0;
  undefined8 local_6c8;
  undefined8 local_6c0;
  undefined8 local_6b8;
  undefined8 local_6b0;
  undefined8 local_6a8;
  undefined8 local_6a0;
  undefined8 local_698;
  undefined8 local_690;
  undefined8 local_688;
  undefined8 local_680;
  undefined1 auStack_5ac [128];
  undefined1 auStack_52c [128];
  undefined1 auStack_4ac [516];
  undefined1 auStack_2a8 [512];
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  
  if (param_1 == (uint *)0x0) {
    uVar4 = 0x100006;
  }
  else {
    uVar4 = 0;
    puVar13 = param_1;
    do {
      if (((*puVar13 ^ 0xffffffff) & 0x4d4d4d) != 0) break;
      if (*(short *)((long)puVar13 + 6) == 0x60) {
        uVar4 = FUN_ffff0000907ad7fc();
        if (2 < uVar4) {
          FUN_ffff00009077e11c(s_GFH_MULTI_SEC_found_ffff0000907e58bb);
        }
        DAT_ffff00009090d8d4 = puVar13[2];
        DAT_ffff00009090d8d8 = puVar13[3];
        goto LAB_ffff0000907b7474;
      }
      puVar13 = (uint *)((long)puVar13 + (ulong)(ushort)puVar13[1]);
      uVar4 = uVar4 + (ushort)puVar13[1];
    } while (uVar4 <= param_2);
    uVar4 = FUN_ffff0000907ad7fc();
    if (2 < uVar4) {
      FUN_ffff00009077e11c(s_GFH_MULTI_SEC_not_found___Use_de_ffff0000907f5a72);
    }
    DAT_ffff00009090d8d8 = 0;
    DAT_ffff00009090d8d4 = 0;
LAB_ffff0000907b7474:
    uVar4 = FUN_ffff0000907ad7fc();
    if (2 < uVar4) {
      FUN_ffff00009077e11c(s_multi_sec_info__ffff0000907f5aa1);
    }
    uVar4 = FUN_ffff0000907ad7fc();
    if (2 < uVar4) {
      FUN_ffff00009077e11c(s_sec_level___0x_x_ffff0000907e58d1,DAT_ffff00009090d8d4);
    }
    uVar4 = FUN_ffff0000907ad7fc();
    if (2 < uVar4) {
      FUN_ffff00009077e11c(s_root_key_ver___0x_x_ffff0000907e8a5a,DAT_ffff00009090d8d8);
    }
    DAT_ffff00009090d8d4 = DAT_ffff00009090d8d4 & 0xf;
    if (DAT_ffff00009090d8d4 == 0) {
      DAT_ffff00009090d8dc = '\0';
      DAT_ffff000090838050 = 0x100;
    }
    else if (DAT_ffff00009090d8d4 == 2) {
      DAT_ffff00009090d8dc = '\x01';
      DAT_ffff000090838050 = 0x200;
    }
    else {
      if (DAT_ffff00009090d8d4 != 1) {
        return 0x101023;
      }
      DAT_ffff00009090d8dc = '\x01';
      DAT_ffff000090838050 = 0x180;
    }
    uVar4 = 0;
    local_680 = 0;
    local_688 = 0;
    local_690 = 0;
    local_698 = 0;
    local_6a0 = 0;
    local_6a8 = 0;
    local_6b0 = 0;
    local_6b8 = 0;
    puVar13 = param_1;
    while (DAT_ffff000090838054 = DAT_ffff000090838050, ((*puVar13 ^ 0xffffffff) & 0x4d4d4d) == 0) {
      if (*(short *)((long)puVar13 + 6) == 0) {
        DAT_ffff00009090d8d0 = puVar13[0xb];
        DAT_ffff00009090d8c8 = (uint)*(byte *)((long)puVar13 + 0x1b);
        DAT_ffff00009090d8cc = (uint)(*(short *)((long)puVar13 + 0x36) == -0x3da0);
        uVar4 = FUN_ffff0000907ad7fc();
        if (2 < uVar4) {
          FUN_ffff00009077e11c(s_sig_info__ffff00009080012b);
        }
        uVar4 = FUN_ffff0000907ad7fc();
        if (2 < uVar4) {
          FUN_ffff00009077e11c(s_sig_type___0x_x_ffff0000907ed836,DAT_ffff00009090d8c8);
        }
        uVar4 = FUN_ffff0000907ad7fc();
        if (2 < uVar4) {
          FUN_ffff00009077e11c(s_pad_type___0x_x_ffff0000907e8a6f,DAT_ffff00009090d8cc);
        }
        uVar4 = FUN_ffff0000907ad7fc();
        if (2 < uVar4) {
          FUN_ffff00009077e11c(s_sig_size___0x_x_ffff0000907da284,DAT_ffff00009090d8d0);
        }
        if (DAT_ffff00009090d8c8 != 5) {
          iVar5 = FUN_ffff0000907ad7fc();
          uVar4 = 0x101018;
          if (iVar5 == 0) {
            return 0x101018;
          }
          pcVar7 = s__SBC__Current_sig_type_is_not_su_ffff0000907d5451;
          goto LAB_ffff0000907b76a4;
        }
        lVar19 = (ulong)param_2 - (ulong)DAT_ffff00009090d8d0;
        if (DAT_ffff00009090d8d4 == 2) {
          lVar19 = (long)param_1 + lVar19 + 0xa4c;
LAB_ffff0000907b76b8:
          uVar4 = FUN_ffff0000907a28ec(param_1,param_2 - DAT_ffff00009090d8d0,&local_6b8);
          if (uVar4 != 0) {
            return uVar4;
          }
          uVar10 = 0x30;
          if (DAT_ffff00009090d8dc == '\0') {
            uVar10 = 0x20;
          }
          iVar5 = FUN_ffff0000907ad298(lVar19,&local_6b8,uVar10);
          if (iVar5 != 0) {
            return 0x101011;
          }
          local_78 = 0;
          uStack_70 = 0;
          local_88 = 0;
          uStack_80 = 0;
          local_98 = 0;
          uStack_90 = 0;
          local_a8 = 0;
          uStack_a0 = 0;
          FUN_ffff00009077ee50(auStack_2a8,0,0x200);
          FUN_ffff00009077ee50(&local_6b8,0,0x40c);
          local_6c0 = 0;
          local_6c8 = 0;
          local_6d0 = 0;
          local_6d8 = 0;
          local_6e0 = 0;
          local_6e8 = 0;
          local_6f0 = 0;
          local_6f8 = 0;
          local_700 = 0;
          local_708 = 0;
          local_710 = 0;
          local_718 = 0;
          local_720 = 0;
          local_728 = 0;
          local_730 = 0;
          local_738 = 0;
          FUN_ffff00009077ee50(local_938,0,0x200);
          FUN_ffff00009077ee50(auStack_b38,0,0x200);
          FUN_ffff00009077ee50(auStack_d38,0,0x200);
          local_d40 = 0;
          if (DAT_ffff00009090d8c8 != 5) {
            iVar5 = FUN_ffff0000907ad7fc();
            uVar4 = 0x101018;
            if (iVar5 == 0) {
              return 0x101018;
            }
            pcVar7 = s__SBC__Current_sig_type_is_not_su_ffff0000907da295;
            goto LAB_ffff0000907b76a4;
          }
          lVar19 = (ulong)param_2 - (ulong)DAT_ffff00009090d8d0;
          if (DAT_ffff00009090d8d4 == 2) {
            uVar10 = 0x40c;
          }
          else if (DAT_ffff00009090d8d4 == 1) {
            uVar10 = 0x30c;
          }
          else {
            if (DAT_ffff00009090d8d4 != 0) goto LAB_ffff0000907b7c54;
            uVar10 = 0x20c;
          }
          FUN_ffff0000907ad234(&local_6b8,(long)param_1 + lVar19 + 0x14,uVar10);
          iVar5 = FUN_ffff0000907aa2e4();
          iVar6 = FUN_ffff0000907ad7fc();
          if (iVar5 == 0) {
            if (iVar6 != 0) {
              FUN_ffff00009077e11c(s_sbc_en___0_ffff000090801b6f);
            }
            uVar4 = local_6b8._4_4_;
            if (DAT_ffff00009090d8d4 == 2) {
              puVar8 = auStack_4ac;
            }
            else if (DAT_ffff00009090d8d4 == 1) {
              puVar8 = auStack_52c;
            }
            else {
              if (DAT_ffff00009090d8d4 != 0) goto LAB_ffff0000907b7c54;
              puVar8 = auStack_5ac;
            }
            uVar18 = (ulong)local_6b8._4_4_;
            FUN_ffff0000907ad234(local_938,puVar8,uVar18);
            puVar3 = PTR_DAT_ffff000090839af8;
            iVar5 = FUN_ffff0000907ad298(*(undefined8 *)PTR_DAT_ffff000090839af8,local_938,uVar18);
            if (iVar5 != 0) {
              iVar5 = FUN_ffff0000907ad7fc();
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(s_g_sec_oemkey___ffff0000907e89f3);
              }
              if (uVar4 != 0) {
                uVar20 = 0;
                do {
                  iVar5 = FUN_ffff0000907ad7fc();
                  if (iVar5 != 0) {
                    FUN_ffff00009077e11c
                              (s_0x_x_ffff0000907fe651,*(undefined1 *)(*(long *)puVar3 + uVar20));
                  }
                  uVar20 = uVar20 + 1;
                  if (((uVar20 & 0xf) == 0) && (iVar5 = FUN_ffff0000907ad7fc(), iVar5 != 0)) {
                    FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
                  }
                } while (uVar18 != uVar20);
              }
              iVar5 = FUN_ffff0000907ad7fc();
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(s_pubk_>n___ffff0000907dbcf7);
              }
              if (uVar4 != 0) {
                uVar20 = 0;
                do {
                  iVar5 = FUN_ffff0000907ad7fc();
                  if (iVar5 != 0) {
                    FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,local_938[uVar20]);
                  }
                  uVar20 = uVar20 + 1;
                  if (((uVar20 & 0xf) == 0) && (iVar5 = FUN_ffff0000907ad7fc(), iVar5 != 0)) {
                    FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
                  }
                } while (uVar18 != uVar20);
              }
              iVar5 = FUN_ffff0000907ad7fc();
              uVar4 = 0x10101d;
              if (iVar5 == 0) {
                return 0x10101d;
              }
              pcVar7 = s_pubk_auth_fail_ffff0000908000f1;
LAB_ffff0000907b76a4:
              FUN_ffff00009077e11c(pcVar7);
              return uVar4;
            }
          }
          else {
            if (iVar6 != 0) {
              FUN_ffff00009077e11c(s_sbc_en___1_ffff000090800136);
            }
            if (2 < DAT_ffff00009090d8d4) goto LAB_ffff0000907b7c54;
            uVar4 = FUN_ffff0000907a28ec
                              (&local_6b8,(ulong)DAT_ffff00009090d8d4 * 0x100 + 0x20c,&local_6f8);
            if (uVar4 != 0) {
              return uVar4;
            }
            uVar11 = 0x30;
            if (DAT_ffff00009090d8dc == '\0') {
              uVar11 = 0x20;
            }
            uVar4 = FUN_ffff0000907aa328(&local_738,uVar11,DAT_ffff00009090d8d8);
            if (uVar4 != 0) {
              return uVar4;
            }
            uVar10 = 0x30;
            if (DAT_ffff00009090d8dc == '\0') {
              uVar10 = 0x20;
            }
            iVar5 = FUN_ffff00009077eae8(&local_6f8,&local_738,uVar10);
            if (iVar5 != 0) {
              iVar5 = FUN_ffff0000907ad7fc();
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(s_pl_key_cert_pubk_auth_fail_ffff0000907e58e3);
              }
              iVar5 = FUN_ffff0000907ad7fc();
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(s_pl_img_pubk_hash___ffff0000907ea41e);
              }
              uVar18 = 0;
              do {
                iVar5 = FUN_ffff0000907ad7fc();
                if (iVar5 != 0) {
                  FUN_ffff00009077e11c
                            (s_0x_x_ffff0000907fe651,*(undefined1 *)((long)&local_6f8 + uVar18));
                }
                uVar18 = uVar18 + 1;
                uVar20 = 0x30;
                if (DAT_ffff00009090d8dc == '\0') {
                  uVar20 = 0x20;
                }
              } while (uVar18 < uVar20);
              iVar5 = FUN_ffff0000907ad7fc();
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
              }
              iVar5 = FUN_ffff0000907ad7fc();
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(s_fuse_pubk_hash___ffff0000907d230d);
              }
              uVar18 = 0;
              do {
                iVar5 = FUN_ffff0000907ad7fc();
                if (iVar5 != 0) {
                  FUN_ffff00009077e11c
                            (s_0x_x_ffff0000907fe651,*(undefined1 *)((long)&local_738 + uVar18));
                }
                uVar18 = uVar18 + 1;
                uVar20 = 0x30;
                if (DAT_ffff00009090d8dc == '\0') {
                  uVar20 = 0x20;
                }
              } while (uVar18 < uVar20);
              iVar5 = FUN_ffff0000907ad7fc();
              uVar4 = 0x10101d;
              if (iVar5 == 0) {
                return 0x10101d;
              }
              pcVar7 = &DAT_ffff0000907f836a;
              goto LAB_ffff0000907b76a4;
            }
          }
          if (DAT_ffff00009090d8d4 == 2) {
            uVar4 = FUN_ffff0000907a28ec((long)param_1 + lVar19 + 4,0x82c,&local_a8,2);
            if (uVar4 != 0) {
              return uVar4;
            }
            FUN_ffff0000907ad234(auStack_2a8,auStack_4ac,0x200);
            lVar9 = (long)param_1 + lVar19 + 0x830;
            uVar10 = 0x200;
          }
          else if (DAT_ffff00009090d8d4 == 1) {
            uVar4 = FUN_ffff0000907a28ec((long)param_1 + lVar19 + 4,0x62c,&local_a8,1);
            if (uVar4 != 0) {
              return uVar4;
            }
            FUN_ffff0000907ad234(auStack_2a8,auStack_52c,0x180);
            lVar9 = (long)param_1 + lVar19 + 0x630;
            uVar10 = 0x180;
          }
          else {
            if (DAT_ffff00009090d8d4 != 0) goto LAB_ffff0000907b7c54;
            uVar4 = FUN_ffff0000907a28ec((long)param_1 + lVar19 + 4,0x42c,&local_a8,0);
            if (uVar4 != 0) {
              return uVar4;
            }
            FUN_ffff0000907ad234(auStack_2a8,auStack_5ac,0x100);
            lVar9 = (long)param_1 + lVar19 + 0x430;
            uVar10 = 0x100;
          }
          FUN_ffff0000907ad234(auStack_b38,lVar9,uVar10);
          FUN_ffff0000907adb8c(auStack_2a8,DAT_ffff000090838050);
          FUN_ffff0000907adb8c(auStack_b38,DAT_ffff000090838054);
          uVar4 = FUN_ffff0000907a2a34(auStack_b38,auStack_2a8,auStack_d38,DAT_ffff00009090d8d4);
          if (uVar4 != 0) {
            return uVar4;
          }
          FUN_ffff0000907adb8c(auStack_d38,DAT_ffff000090838054);
          iVar5 = FUN_ffff0000907a2a64
                            (&local_a8,auStack_d38,(long)&local_d40 + 4,DAT_ffff00009090d8d4);
          if (iVar5 != 0) {
            return 0x102005;
          }
          if (local_d40._4_4_ != 1) {
            return 0x102005;
          }
          if (DAT_ffff00009090d8d4 == 2) {
            FUN_ffff0000907ad234(&local_6b8,(long)param_1 + lVar19 + 0x420,0x40c);
            uVar4 = FUN_ffff0000907a28ec
                              ((long)param_1 + lVar19 + 0xa30,0x4c,&local_a8,DAT_ffff00009090d8d4);
            if (uVar4 != 0) {
              return uVar4;
            }
            FUN_ffff0000907ad234(auStack_2a8,auStack_4ac,0x200);
            lVar9 = (long)param_1 + lVar19 + 0xa7c;
            uVar10 = 0x200;
          }
          else if (DAT_ffff00009090d8d4 == 1) {
            FUN_ffff0000907ad234(&local_6b8,(long)param_1 + lVar19 + 800,0x30c);
            uVar4 = FUN_ffff0000907a28ec
                              ((long)param_1 + lVar19 + 0x7b0,0x4c,&local_a8,DAT_ffff00009090d8d4);
            if (uVar4 != 0) {
              return uVar4;
            }
            FUN_ffff0000907ad234(auStack_2a8,auStack_52c,0x180);
            lVar9 = (long)param_1 + lVar19 + 0x7fc;
            uVar10 = 0x180;
          }
          else {
            if (DAT_ffff00009090d8d4 != 0) goto LAB_ffff0000907b7c54;
            FUN_ffff0000907ad234(&local_6b8,(long)param_1 + lVar19 + 0x220,0x20c);
            uVar4 = FUN_ffff0000907a28ec
                              ((long)param_1 + lVar19 + 0x530,0x3c,&local_a8,DAT_ffff00009090d8d4);
            if (uVar4 != 0) {
              return uVar4;
            }
            FUN_ffff0000907ad234(auStack_2a8,auStack_5ac,0x100);
            lVar9 = (long)param_1 + lVar19 + 0x56c;
            uVar10 = 0x100;
          }
          FUN_ffff0000907ad234(auStack_b38,lVar9,uVar10);
          FUN_ffff0000907adb8c(auStack_2a8,DAT_ffff000090838050);
          FUN_ffff0000907adb8c(auStack_b38,DAT_ffff000090838054);
          uVar4 = FUN_ffff0000907a2a34(auStack_b38,auStack_2a8,auStack_d38,DAT_ffff00009090d8d4);
          if (uVar4 != 0) {
            return uVar4;
          }
          FUN_ffff0000907adb8c(auStack_d38,DAT_ffff000090838054);
          iVar5 = FUN_ffff0000907a2a64
                            (&local_a8,auStack_d38,(long)&local_d40 + 4,DAT_ffff00009090d8d4);
          if (iVar5 != 0) {
            return 0x102005;
          }
          if (local_d40._4_4_ != 1) {
            return 0x102005;
          }
          if (DAT_ffff00009090d8d4 == 2) {
            psVar12 = (short *)((long)param_1 + lVar19 + 0x82c);
            psVar14 = (short *)((long)param_1 + lVar19 + 0xa40);
            puVar15 = (ushort *)((long)param_1 + lVar19 + 0x82e);
            puVar16 = (ushort *)((long)param_1 + lVar19 + 0xa42);
          }
          else if (DAT_ffff00009090d8d4 == 1) {
            psVar12 = (short *)((long)param_1 + lVar19 + 0x62c);
            psVar14 = (short *)((long)param_1 + lVar19 + 0x7c0);
            puVar15 = (ushort *)((long)param_1 + lVar19 + 0x62e);
            puVar16 = (ushort *)((long)param_1 + lVar19 + 0x7c2);
          }
          else {
            if (DAT_ffff00009090d8d4 != 0) {
              return 0x101023;
            }
            psVar12 = (short *)((long)param_1 + lVar19 + 0x42c);
            psVar14 = (short *)((long)param_1 + lVar19 + 0x540);
            puVar15 = (ushort *)((long)param_1 + lVar19 + 0x42e);
            puVar16 = (ushort *)((long)param_1 + lVar19 + 0x542);
          }
          if (*psVar12 != *psVar14) {
            return 0x10400e;
          }
          uVar1 = *puVar16;
          uVar2 = *puVar15;
          if (uVar2 == uVar1) {
            uVar4 = FUN_ffff00009074a724(0,&local_d40);
            if (uVar4 != 0) {
              return uVar4;
            }
            uVar4 = FUN_ffff0000907aba1c(local_d40 & 0xffffffff);
            if (uVar2 < uVar4) {
              iVar5 = FUN_ffff0000907ad7fc();
              uVar17 = 0x104001;
              if (iVar5 != 0) {
                FUN_ffff00009077e11c
                          (s__SEC__img_is_rollbacked__img___d_ffff0000908035b4,uVar2,uVar4);
              }
            }
            else if (uVar1 < 0x20) {
              uVar17 = 0;
            }
            else {
              iVar5 = FUN_ffff0000907ad7fc();
              uVar17 = 0x104002;
              if (iVar5 != 0) {
                FUN_ffff00009077e11c(s__SEC__img_ver_is_too_large__img__ffff0000907d547b,uVar2);
              }
            }
            iVar5 = FUN_ffff0000907ad7fc();
            if (iVar5 == 0) {
              return uVar17;
            }
            FUN_ffff00009077e11c
                      (s__SBC__sig_type____d__img_ver_____ffff0000907d8a10,DAT_ffff00009090d8c8,
                       (uint)uVar2,uVar4);
            return uVar17;
          }
          iVar5 = FUN_ffff0000907ad7fc();
          if (iVar5 != 0) {
            FUN_ffff00009077e11c(s__SEC__img_ver_is_not_sync___key__ffff0000907e3e2f,uVar2,uVar1);
          }
          uVar4 = 0x4000;
        }
        else {
          if (DAT_ffff00009090d8d4 == 1) {
            lVar19 = (long)param_1 + lVar19 + 0x7cc;
            goto LAB_ffff0000907b76b8;
          }
          if (DAT_ffff00009090d8d4 == 0) {
            lVar19 = (long)param_1 + lVar19 + 0x54c;
            goto LAB_ffff0000907b76b8;
          }
LAB_ffff0000907b7c54:
          uVar4 = 0x1023;
        }
        goto LAB_ffff0000907b758c;
      }
      puVar13 = (uint *)((long)puVar13 + (ulong)(ushort)puVar13[1]);
      uVar4 = uVar4 + (ushort)puVar13[1];
      if (param_2 < uVar4) break;
    }
    uVar4 = 0x9001;
LAB_ffff0000907b758c:
    uVar4 = uVar4 | 0x100000;
  }
  return uVar4;
}


