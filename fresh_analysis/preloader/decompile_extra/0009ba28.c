// File offset entry: 0x9ba28

/* WARNING: Removing unreachable block (ram,0x020a8dd8) */

ulong FUN_0209b928(ulong param_1,int param_2,ulong param_3,uint param_4)

{
  long lVar1;
  undefined1 uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  ulong uVar7;
  ulong uVar8;
  uint uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  uint uVar20;
  uint uVar21;
  uint *puVar22;
  long lVar23;
  ulong uVar24;
  ulong uVar25;
  ulong uVar26;
  undefined1 auVar27 [16];
  undefined1 auVar28 [12];
  undefined4 uStack_290;
  undefined8 uStack_280;
  undefined8 uStack_278;
  long lStack_270;
  ulong uStack_268;
  ulong uStack_260;
  ulong uStack_258;
  undefined8 uStack_250;
  undefined8 uStack_248;
  undefined8 uStack_240;
  undefined8 uStack_238;
  undefined8 uStack_230;
  undefined8 uStack_228;
  undefined8 uStack_220;
  undefined8 uStack_218;
  long lStack_1f0;
  undefined1 *puStack_1e0;
  code *pcStack_1d8;
  long lStack_180;
  uint auStack_178 [16];
  undefined1 auStack_138 [72];
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  ulong uStack_e0;
  ulong uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  ulong uStack_c0;
  ulong uStack_b8;
  undefined8 uStack_b0;
  undefined4 uStack_a4;
  undefined4 uStack_a0;
  undefined4 uStack_9c;
  undefined4 uStack_94;
  undefined4 uStack_78;
  long lStack_68;
  
  uVar4 = uRam00000000020e4240;
  if ((((param_1 == 0) || (param_2 == 0)) || (param_3 == 0)) || (param_4 == 0)) {
    return 1;
  }
  uVar11 = 0;
  uVar2 = 0;
  lStack_68 = *plRam0000000002103280;
  uVar8 = param_1;
  uVar14 = (long)param_2;
  uVar16 = param_3;
  uVar18 = (ulong)param_4;
  FUN_02091664(&uStack_b0,0,0x48);
  uStack_f0 = 0;
  uStack_e8 = 0;
  uStack_e0 = 0;
  uStack_d8 = 0;
  uStack_d0 = 0;
  uStack_c8 = 0;
  uStack_c0 = 0;
  uStack_b8 = 0;
  FUN_02091664(&uStack_b0,0,0x48);
  uStack_b0 = 0x18003000;
  uStack_78 = 0;
  uStack_a4 = 0;
  uStack_a0 = 0;
  uStack_9c = 4;
  uStack_94 = 0;
  FUN_02091664(&uStack_f0,0,0x40);
  uStack_f0 = 0;
  uStack_e8 = uVar4;
  uStack_e0 = param_1;
  uStack_d8 = (long)param_2;
  uStack_c0 = param_3;
  uStack_b8 = (ulong)param_4;
  FUN_020915ac(auStack_138,&uStack_b0,0x48);
  uVar10 = 0x40;
  FUN_020915ac(auStack_178,&uStack_f0);
  uVar4 = FUN_020a88f4(auStack_138,auStack_178);
  uVar5 = FUN_020a9218(uVar4);
  if ((bool)uVar2) {
    return uVar5;
  }
  auVar28 = FUN_02091090();
  uVar9 = auVar28._8_4_;
  lVar6 = auVar28._0_8_;
  pcStack_1d8 = FUN_020a8e0c;
  uVar7 = 0x7247;
  lStack_1f0 = *plRam0000000002103280;
  uVar5 = uVar10;
  uVar12 = uVar11;
  uVar13 = uVar8;
  uVar15 = uVar14;
  uVar17 = uVar16;
  uVar19 = uVar18;
  puStack_1e0 = &stack0xffffffffffffffa0;
  if ((uVar10 == 0) || (uVar8 == 0)) goto code_r0x020a9040;
  uVar5 = 0x30;
  uVar26 = uVar18 & 0xffffffff;
  uVar24 = uVar14 & 0xffffffff;
  uVar25 = uVar11 & 0xffffffff;
  uStack_260 = 0;
  uStack_258 = 0;
  lStack_270 = 0;
  uStack_268 = 0;
  uStack_280 = 0;
  uStack_278 = 0;
  FUN_02091664(&uStack_280,0,0x30);
  uStack_290 = (undefined4)lStack_180;
  uStack_280 = (ulong)CONCAT14(lStack_180 == 0,1);
  lStack_270 = lVar6;
  uStack_268 = uVar25;
  uStack_260 = uVar26;
  uStack_258 = uVar24;
  if (7 < uVar9) {
    uVar7 = 0x7246;
    goto code_r0x020a9040;
  }
  if (uVar9 == 0) {
    uVar7 = 0x7247;
    if ((uVar16 == 0) || (uVar20 = (uint)uVar18, uVar20 == 0)) goto code_r0x020a9040;
    uVar5 = 0x60;
    FUN_02091664(&uStack_250,0,0x60);
    if (uVar20 < 0x61) {
      uVar20 = uVar20 >> 2;
      if ((uVar18 & 3) != 0) {
        uVar20 = uVar20 + 1;
      }
      if (uVar20 < 0x19) {
        uVar21 = uVar20 * 4;
        while (uVar26 != 0) {
          uVar21 = uVar21 - 1;
          uVar26 = uVar26 - 1;
          *(undefined1 *)((long)&uStack_250 + (ulong)uVar21) =
               *(undefined1 *)(uVar16 + (uVar26 & 0xffffffff));
        }
        if (uVar20 != 0) {
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
    uStack_220 = 0;
    uStack_218 = 0;
    uStack_278 = CONCAT44(1,uVar9);
    uStack_230 = 0;
    uStack_228 = 0;
    uStack_240 = 0;
    uStack_238 = 0;
    uStack_250 = 0;
    uStack_248 = 0;
    if ((uint)uVar11 < 0x41) {
      uVar9 = (uint)uVar11 >> 2;
      if ((uVar11 & 3) != 0) {
        uVar9 = uVar9 + 1;
      }
      if (uVar9 < 0x11) {
        uVar20 = uVar9 * 4;
        while (uVar25 != 0) {
          uVar20 = uVar20 - 1;
          uVar25 = uVar25 - 1;
          *(undefined1 *)((long)&uStack_250 + (ulong)uVar20) =
               *(undefined1 *)(uVar10 + (uVar25 & 0xffffffff));
        }
        if (uVar9 != 0) {
          do {
            FUN_020a9238();
          } while (extraout_x8 != 0);
        }
        uStack_230 = 0;
        uStack_228 = 0;
        uStack_240 = 0;
        uStack_238 = 0;
        uStack_250 = 0;
        uStack_248 = 0;
        if ((uint)uVar14 < 0x31) {
          uVar9 = (uint)uVar14 >> 2;
          if ((uVar14 & 3) != 0) {
            uVar9 = uVar9 + 1;
          }
          if (uVar9 < 0xd) {
            uVar20 = uVar9 * 4;
            while (uVar24 != 0) {
              uVar20 = uVar20 - 1;
              uVar24 = uVar24 - 1;
              *(undefined1 *)((long)&uStack_250 + (ulong)uVar20) =
                   *(undefined1 *)(uVar8 + (uVar24 & 0xffffffff));
            }
            if (uVar9 != 0) {
              do {
                FUN_020a9238();
              } while (extraout_x8_00 != 0);
            }
            uVar5 = 1;
            uVar12 = 5000;
            *(uint *)(lStack_270 + 0x19c) =
                 ((uint)uStack_280 & 1) << 1 | (uStack_280._4_4_ & 3) << 2 |
                 ((uint)uStack_278 & 7) << 4 | (uStack_278._4_4_ & 1) << 7 |
                 ((uint)uStack_268 & 0xff) << 8 | ((uint)uStack_260 & 0xff) << 0x10 |
                 (int)uStack_258 << 0x18 | 1;
            iVar3 = FUN_020a9354(lStack_270 + 0x19c,1,1,5000);
            if (iVar3 != 0) {
              uVar7 = 0x7262;
              goto code_r0x020a9040;
            }
            uVar9 = *(uint *)(lVar6 + 0x198);
            if ((uVar9 & 0x3f) != 0) {
              lVar1 = 0;
              do {
                lVar23 = lVar1;
                if (lVar23 + 8 == 0x30) goto LAB_020a9100;
                lVar1 = lVar23 + 8;
              } while ((uVar9 >> (ulong)(*(uint *)(lVar23 + 0x20e4214) & 0x1f) & 1) == 0);
              uVar7 = (ulong)*(uint *)(lVar23 + 0x20e4218);
              goto code_r0x020a9040;
            }
LAB_020a9100:
            uVar20 = 0x724d;
            if ((uVar9 & 0x9f000000) == 0x80000000 || uStack_280._4_4_ == 0) {
              uVar20 = 0;
            }
            uVar7 = (ulong)uVar20;
            if (uStack_280._4_4_ != 0) goto code_r0x020a9040;
            uStack_220 = 0;
            uStack_218 = 0;
            uStack_230 = 0;
            uStack_228 = 0;
            uStack_240 = 0;
            uStack_238 = 0;
            uStack_250 = 0;
            uStack_248 = 0;
            if ((auStack_178[0] < 0x44) && ((auStack_178[0] & 3) == 0)) {
              puVar22 = (uint *)(lVar6 + 0x1fc);
              for (uVar8 = 0; (auStack_178[0] & 0xfffffffc) != uVar8; uVar8 = uVar8 + 4) {
                *(uint *)((long)&uStack_250 + uVar8) =
                     (*puVar22 & 0xff00) << 8 | *puVar22 << 0x18 | *puVar22 >> 8 & 0xff00 |
                     *puVar22 >> 0x18;
                puVar22 = puVar22 + -1;
              }
              for (uVar8 = 0; auStack_178[0] != uVar8; uVar8 = uVar8 + 1) {
                *(undefined1 *)(lStack_180 + uVar8) = *(undefined1 *)((long)&uStack_250 + uVar8);
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
  if (lRam0000000002100880 == lStack_1f0) {
    return uVar7;
  }
  auVar27 = FUN_02091090(uVar7);
  if (uVar19 != 0) {
    uVar8 = FUN_020a8e0c(auVar27._0_8_,uVar15 == 0,auVar27._8_8_,uVar5 & 0xffffffff,uVar12,
                         uVar13 & 0xffffffff,uVar15,uVar17 & 0xffffffff,uVar19,uStack_290,
                         &puStack_1e0,FUN_020a91b4);
    return uVar8;
  }
  return 0x7247;
}


