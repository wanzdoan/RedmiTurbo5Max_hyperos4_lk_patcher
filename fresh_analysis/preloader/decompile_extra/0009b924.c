// File offset entry: 0x9b924

/* WARNING: Removing unreachable block (ram,0x020a8dd8) */

ulong FUN_0209b824(long param_1,uint param_2,ulong param_3,long param_4)

{
  long lVar1;
  undefined1 uVar2;
  uint uVar3;
  int iVar4;
  undefined8 uVar5;
  long lVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  ulong uVar18;
  uint uVar19;
  uint uVar20;
  uint *puVar21;
  long lVar22;
  ulong uVar23;
  ulong uVar24;
  ulong uVar25;
  ulong uVar26;
  undefined1 auVar27 [16];
  undefined1 auVar28 [12];
  undefined4 uStack_300;
  undefined8 uStack_2f0;
  undefined8 uStack_2e8;
  long lStack_2e0;
  ulong uStack_2d8;
  ulong uStack_2d0;
  ulong uStack_2c8;
  undefined8 uStack_2c0;
  undefined8 uStack_2b8;
  undefined8 uStack_2b0;
  undefined8 uStack_2a8;
  undefined8 uStack_2a0;
  undefined8 uStack_298;
  undefined8 uStack_290;
  undefined8 uStack_288;
  long lStack_260;
  undefined1 **ppuStack_250;
  code *pcStack_248;
  long lStack_1f0;
  uint auStack_1e8 [16];
  undefined1 auStack_1a8 [72];
  undefined8 uStack_160;
  undefined8 uStack_158;
  ulong uStack_150;
  ulong uStack_148;
  undefined8 uStack_140;
  undefined8 uStack_138;
  ulong uStack_130;
  ulong uStack_128;
  undefined8 uStack_120;
  undefined4 uStack_114;
  undefined4 uStack_110;
  undefined4 uStack_10c;
  undefined4 uStack_104;
  undefined4 uStack_e8;
  long lStack_d8;
  undefined1 *puStack_d0;
  code *pcStack_c8;
  undefined8 uStack_70;
  long lStack_68;
  ulong uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  long lStack_48;
  undefined8 uStack_40;
  ulong uStack_38;
  undefined2 uStack_30;
  undefined4 uStack_2c;
  long lStack_28;
  
  lStack_28 = *plRam0000000002103280;
  uStack_60 = (ulong)param_2;
  uStack_70 = 0x18005000;
  uStack_50 = 0;
  uStack_58 = 0;
  uStack_30 = 0;
  uStack_2c = 0;
  uStack_40 = 0x10;
  lStack_68 = param_1;
  lStack_48 = param_4;
  uStack_38 = param_3;
  if (param_1 == 0) {
    uVar23 = 2;
  }
  else if (param_4 == 0) {
    uVar23 = 3;
  }
  else if (param_2 == 0) {
    uVar23 = 1;
  }
  else {
    FUN_020abf40(0x10000000,1);
    FUN_020abea8(0x10000000,3);
    FUN_020abfa4(0x18000000,1);
    uVar3 = FUN_020a985c(&uStack_70);
    uVar23 = (ulong)uVar3;
    if (uVar3 == 0x72d1) {
      FUN_020a9364(0x18005000);
      FUN_020a9388(0x18005000);
    }
    FUN_020abea8(0x10000000,0);
  }
  uVar3 = (uint)param_4;
  if (lRam0000000002100880 == lStack_28) {
    return uVar23;
  }
  auVar28 = FUN_02091090();
  uVar5 = uRam00000000020e4240;
  iVar4 = auVar28._8_4_;
  uVar23 = auVar28._0_8_;
  if ((((uVar23 == 0) || (iVar4 == 0)) || (param_3 == 0)) || (uVar3 == 0)) {
    return 1;
  }
  uVar9 = 0;
  pcStack_c8 = FUN_0209b928;
  uVar2 = 0;
  lStack_d8 = *plRam0000000002103280;
  uVar18 = uVar23;
  uVar12 = (long)iVar4;
  uVar14 = param_3;
  uVar16 = (ulong)uVar3;
  puStack_d0 = &stack0xffffffffffffffe0;
  FUN_02091664(&uStack_120,0,0x48);
  uStack_160 = 0;
  uStack_158 = 0;
  uStack_150 = 0;
  uStack_148 = 0;
  uStack_140 = 0;
  uStack_138 = 0;
  uStack_130 = 0;
  uStack_128 = 0;
  FUN_02091664(&uStack_120,0,0x48);
  uStack_120 = 0x18003000;
  uStack_e8 = 0;
  uStack_114 = 0;
  uStack_110 = 0;
  uStack_10c = 4;
  uStack_104 = 0;
  FUN_02091664(&uStack_160,0,0x40);
  uStack_160 = 0;
  uStack_158 = uVar5;
  uStack_150 = uVar23;
  uStack_148 = (long)iVar4;
  uStack_130 = param_3;
  uStack_128 = (ulong)uVar3;
  FUN_020915ac(auStack_1a8,&uStack_120,0x48);
  uVar8 = 0x40;
  FUN_020915ac(auStack_1e8,&uStack_160);
  uVar5 = FUN_020a88f4(auStack_1a8,auStack_1e8);
  uVar23 = FUN_020a9218(uVar5);
  if ((bool)uVar2) {
    return uVar23;
  }
  auVar28 = FUN_02091090();
  uVar3 = auVar28._8_4_;
  lVar6 = auVar28._0_8_;
  pcStack_248 = FUN_020a8e0c;
  uVar7 = 0x7247;
  lStack_260 = *plRam0000000002103280;
  uVar23 = uVar8;
  uVar10 = uVar9;
  uVar11 = uVar18;
  uVar13 = uVar12;
  uVar15 = uVar14;
  uVar17 = uVar16;
  ppuStack_250 = &puStack_d0;
  if ((uVar8 == 0) || (uVar18 == 0)) goto code_r0x020a9040;
  uVar23 = 0x30;
  uVar26 = uVar16 & 0xffffffff;
  uVar24 = uVar12 & 0xffffffff;
  uVar25 = uVar9 & 0xffffffff;
  uStack_2d0 = 0;
  uStack_2c8 = 0;
  lStack_2e0 = 0;
  uStack_2d8 = 0;
  uStack_2f0 = 0;
  uStack_2e8 = 0;
  FUN_02091664(&uStack_2f0,0,0x30);
  uStack_300 = (undefined4)lStack_1f0;
  uStack_2f0 = (ulong)CONCAT14(lStack_1f0 == 0,1);
  lStack_2e0 = lVar6;
  uStack_2d8 = uVar25;
  uStack_2d0 = uVar26;
  uStack_2c8 = uVar24;
  if (7 < uVar3) {
    uVar7 = 0x7246;
    goto code_r0x020a9040;
  }
  if (uVar3 == 0) {
    uVar7 = 0x7247;
    if ((uVar14 == 0) || (uVar19 = (uint)uVar16, uVar19 == 0)) goto code_r0x020a9040;
    uVar23 = 0x60;
    FUN_02091664(&uStack_2c0,0,0x60);
    if (uVar19 < 0x61) {
      uVar19 = uVar19 >> 2;
      if ((uVar16 & 3) != 0) {
        uVar19 = uVar19 + 1;
      }
      if (uVar19 < 0x19) {
        uVar20 = uVar19 * 4;
        while (uVar26 != 0) {
          uVar20 = uVar20 - 1;
          uVar26 = uVar26 - 1;
          *(undefined1 *)((long)&uStack_2c0 + (ulong)uVar20) =
               *(undefined1 *)(uVar14 + (uVar26 & 0xffffffff));
        }
        if (uVar19 != 0) {
          do {
            FUN_020a9238();
          } while (extraout_x8_01 != 0);
        }
        goto LAB_020a8ed0;
      }
    }
  }
  else {
LAB_020a8ed0:
    uStack_290 = 0;
    uStack_288 = 0;
    uStack_2e8 = CONCAT44(1,uVar3);
    uStack_2a0 = 0;
    uStack_298 = 0;
    uStack_2b0 = 0;
    uStack_2a8 = 0;
    uStack_2c0 = 0;
    uStack_2b8 = 0;
    if ((uint)uVar9 < 0x41) {
      uVar3 = (uint)uVar9 >> 2;
      if ((uVar9 & 3) != 0) {
        uVar3 = uVar3 + 1;
      }
      if (uVar3 < 0x11) {
        uVar19 = uVar3 * 4;
        while (uVar25 != 0) {
          uVar19 = uVar19 - 1;
          uVar25 = uVar25 - 1;
          *(undefined1 *)((long)&uStack_2c0 + (ulong)uVar19) =
               *(undefined1 *)(uVar8 + (uVar25 & 0xffffffff));
        }
        if (uVar3 != 0) {
          do {
            FUN_020a9238();
          } while (extraout_x8 != 0);
        }
        uStack_2a0 = 0;
        uStack_298 = 0;
        uStack_2b0 = 0;
        uStack_2a8 = 0;
        uStack_2c0 = 0;
        uStack_2b8 = 0;
        if ((uint)uVar12 < 0x31) {
          uVar3 = (uint)uVar12 >> 2;
          if ((uVar12 & 3) != 0) {
            uVar3 = uVar3 + 1;
          }
          if (uVar3 < 0xd) {
            uVar19 = uVar3 * 4;
            while (uVar24 != 0) {
              uVar19 = uVar19 - 1;
              uVar24 = uVar24 - 1;
              *(undefined1 *)((long)&uStack_2c0 + (ulong)uVar19) =
                   *(undefined1 *)(uVar18 + (uVar24 & 0xffffffff));
            }
            if (uVar3 != 0) {
              do {
                FUN_020a9238();
              } while (extraout_x8_00 != 0);
            }
            uVar23 = 1;
            uVar10 = 5000;
            *(uint *)(lStack_2e0 + 0x19c) =
                 ((uint)uStack_2f0 & 1) << 1 | (uStack_2f0._4_4_ & 3) << 2 |
                 ((uint)uStack_2e8 & 7) << 4 | (uStack_2e8._4_4_ & 1) << 7 |
                 ((uint)uStack_2d8 & 0xff) << 8 | ((uint)uStack_2d0 & 0xff) << 0x10 |
                 (int)uStack_2c8 << 0x18 | 1;
            iVar4 = FUN_020a9354(lStack_2e0 + 0x19c,1,1,5000);
            if (iVar4 != 0) {
              uVar7 = 0x7262;
              goto code_r0x020a9040;
            }
            uVar3 = *(uint *)(lVar6 + 0x198);
            if ((uVar3 & 0x3f) != 0) {
              lVar1 = 0;
              do {
                lVar22 = lVar1;
                if (lVar22 + 8 == 0x30) goto LAB_020a9100;
                lVar1 = lVar22 + 8;
              } while ((uVar3 >> (ulong)(*(uint *)(lVar22 + 0x20e4214) & 0x1f) & 1) == 0);
              uVar7 = (ulong)*(uint *)(lVar22 + 0x20e4218);
              goto code_r0x020a9040;
            }
LAB_020a9100:
            uVar19 = 0x724d;
            if ((uVar3 & 0x9f000000) == 0x80000000 || uStack_2f0._4_4_ == 0) {
              uVar19 = 0;
            }
            uVar7 = (ulong)uVar19;
            if (uStack_2f0._4_4_ != 0) goto code_r0x020a9040;
            uStack_290 = 0;
            uStack_288 = 0;
            uStack_2a0 = 0;
            uStack_298 = 0;
            uStack_2b0 = 0;
            uStack_2a8 = 0;
            uStack_2c0 = 0;
            uStack_2b8 = 0;
            if ((auStack_1e8[0] < 0x44) && ((auStack_1e8[0] & 3) == 0)) {
              puVar21 = (uint *)(lVar6 + 0x1fc);
              for (uVar18 = 0; (auStack_1e8[0] & 0xfffffffc) != uVar18; uVar18 = uVar18 + 4) {
                *(uint *)((long)&uStack_2c0 + uVar18) =
                     (*puVar21 & 0xff00) << 8 | *puVar21 << 0x18 | *puVar21 >> 8 & 0xff00 |
                     *puVar21 >> 0x18;
                puVar21 = puVar21 + -1;
              }
              for (uVar18 = 0; auStack_1e8[0] != uVar18; uVar18 = uVar18 + 1) {
                *(undefined1 *)(lStack_1f0 + uVar18) = *(undefined1 *)((long)&uStack_2c0 + uVar18);
              }
              uVar7 = 0;
              goto code_r0x020a9040;
            }
          }
        }
      }
    }
  }
  uVar7 = 0x724c;
code_r0x020a9040:
  if (lRam0000000002100880 == lStack_260) {
    return uVar7;
  }
  auVar27 = FUN_02091090(uVar7);
  if (uVar17 != 0) {
    uVar23 = FUN_020a8e0c(auVar27._0_8_,uVar13 == 0,auVar27._8_8_,uVar23 & 0xffffffff,uVar10,
                          uVar11 & 0xffffffff,uVar13,uVar15 & 0xffffffff,uVar17,uStack_300,
                          &ppuStack_250,FUN_020a91b4);
    return uVar23;
  }
  return 0x7247;
}


