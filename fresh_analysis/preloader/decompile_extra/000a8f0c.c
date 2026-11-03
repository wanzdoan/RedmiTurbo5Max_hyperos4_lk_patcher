// File offset entry: 0xa8f0c

ulong FUN_020a8e0c(long param_1,uint param_2,ulong param_3,ulong param_4,ulong param_5,ulong param_6
                  ,ulong param_7,ulong param_8,long param_9,uint param_10)

{
  long lVar1;
  int iVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  uint uVar10;
  uint *puVar11;
  long lVar12;
  ulong uVar13;
  ulong uVar14;
  uint uVar15;
  ulong uVar16;
  undefined1 auVar17 [16];
  undefined4 uStack_110;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  long lStack_f0;
  ulong uStack_e8;
  ulong uStack_e0;
  ulong uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  long lStack_70;
  
  uVar3 = 0x7247;
  lStack_70 = *plRam0000000002103280;
  uVar4 = param_3;
  uVar5 = param_4;
  uVar6 = param_5;
  uVar7 = param_6;
  uVar8 = param_7;
  uVar9 = param_8;
  if ((param_3 == 0) || (param_5 == 0)) goto code_r0x020a9040;
  uVar4 = 0x30;
  uVar16 = param_8 & 0xffffffff;
  uVar13 = param_6 & 0xffffffff;
  uVar14 = param_4 & 0xffffffff;
  uStack_e0 = 0;
  uStack_d8 = 0;
  lStack_f0 = 0;
  uStack_e8 = 0;
  uStack_100 = 0;
  uStack_f8 = 0;
  FUN_02091664(&uStack_100,0,0x30);
  uStack_110 = (undefined4)param_9;
  uStack_100 = (ulong)CONCAT14(param_9 == 0,1);
  lStack_f0 = param_1;
  uStack_e8 = uVar14;
  uStack_e0 = uVar16;
  uStack_d8 = uVar13;
  if (7 < param_2) {
    uVar3 = 0x7246;
    goto code_r0x020a9040;
  }
  if (param_2 == 0) {
    uVar3 = 0x7247;
    if ((param_7 == 0) || (uVar15 = (uint)param_8, uVar15 == 0)) goto code_r0x020a9040;
    uVar4 = 0x60;
    FUN_02091664(&uStack_d0,0,0x60);
    if (uVar15 < 0x61) {
      uVar15 = uVar15 >> 2;
      if ((param_8 & 3) != 0) {
        uVar15 = uVar15 + 1;
      }
      if (uVar15 < 0x19) {
        uVar10 = uVar15 * 4;
        while (uVar16 != 0) {
          uVar10 = uVar10 - 1;
          uVar16 = uVar16 - 1;
          *(undefined1 *)((long)&uStack_d0 + (ulong)uVar10) =
               *(undefined1 *)(param_7 + (uVar16 & 0xffffffff));
        }
        if (uVar15 != 0) {
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
    uStack_a0 = 0;
    uStack_98 = 0;
    uStack_f8 = CONCAT44(1,param_2);
    uStack_b0 = 0;
    uStack_a8 = 0;
    uStack_c0 = 0;
    uStack_b8 = 0;
    uStack_d0 = 0;
    uStack_c8 = 0;
    if ((uint)param_4 < 0x41) {
      uVar15 = (uint)param_4 >> 2;
      if ((param_4 & 3) != 0) {
        uVar15 = uVar15 + 1;
      }
      if (uVar15 < 0x11) {
        uVar10 = uVar15 * 4;
        while (uVar14 != 0) {
          uVar10 = uVar10 - 1;
          uVar14 = uVar14 - 1;
          *(undefined1 *)((long)&uStack_d0 + (ulong)uVar10) =
               *(undefined1 *)(param_3 + (uVar14 & 0xffffffff));
        }
        if (uVar15 != 0) {
          do {
            FUN_020a9238();
          } while (extraout_x8 != 0);
        }
        uStack_b0 = 0;
        uStack_a8 = 0;
        uStack_c0 = 0;
        uStack_b8 = 0;
        uStack_d0 = 0;
        uStack_c8 = 0;
        if ((uint)param_6 < 0x31) {
          uVar15 = (uint)param_6 >> 2;
          if ((param_6 & 3) != 0) {
            uVar15 = uVar15 + 1;
          }
          if (uVar15 < 0xd) {
            uVar10 = uVar15 * 4;
            while (uVar13 != 0) {
              uVar10 = uVar10 - 1;
              uVar13 = uVar13 - 1;
              *(undefined1 *)((long)&uStack_d0 + (ulong)uVar10) =
                   *(undefined1 *)(param_5 + (uVar13 & 0xffffffff));
            }
            if (uVar15 != 0) {
              do {
                FUN_020a9238();
              } while (extraout_x8_00 != 0);
            }
            uVar4 = 1;
            uVar5 = 5000;
            *(uint *)(lStack_f0 + 0x19c) =
                 ((uint)uStack_100 & 1) << 1 | (uStack_100._4_4_ & 3) << 2 |
                 ((uint)uStack_f8 & 7) << 4 | (uStack_f8._4_4_ & 1) << 7 |
                 ((uint)uStack_e8 & 0xff) << 8 | ((uint)uStack_e0 & 0xff) << 0x10 |
                 (int)uStack_d8 << 0x18 | 1;
            iVar2 = FUN_020a9354(lStack_f0 + 0x19c,1,1,5000);
            if (iVar2 != 0) {
              uVar3 = 0x7262;
              goto code_r0x020a9040;
            }
            uVar15 = *(uint *)(param_1 + 0x198);
            if ((uVar15 & 0x3f) != 0) {
              lVar1 = 0;
              do {
                lVar12 = lVar1;
                if (lVar12 + 8 == 0x30) goto LAB_020a9100;
                lVar1 = lVar12 + 8;
              } while ((uVar15 >> (ulong)(*(uint *)(lVar12 + 0x20e4214) & 0x1f) & 1) == 0);
              uVar3 = (ulong)*(uint *)(lVar12 + 0x20e4218);
              goto code_r0x020a9040;
            }
LAB_020a9100:
            uVar10 = 0x724d;
            if ((uVar15 & 0x9f000000) == 0x80000000 || uStack_100._4_4_ == 0) {
              uVar10 = 0;
            }
            uVar3 = (ulong)uVar10;
            if (uStack_100._4_4_ != 0) goto code_r0x020a9040;
            uStack_a0 = 0;
            uStack_98 = 0;
            uStack_b0 = 0;
            uStack_a8 = 0;
            uStack_c0 = 0;
            uStack_b8 = 0;
            uStack_d0 = 0;
            uStack_c8 = 0;
            if ((param_10 < 0x44) && ((param_10 & 3) == 0)) {
              puVar11 = (uint *)(param_1 + 0x1fc);
              for (uVar3 = 0; (param_10 & 0xfffffffc) != uVar3; uVar3 = uVar3 + 4) {
                *(uint *)((long)&uStack_d0 + uVar3) =
                     (*puVar11 & 0xff00) << 8 | *puVar11 << 0x18 | *puVar11 >> 8 & 0xff00 |
                     *puVar11 >> 0x18;
                puVar11 = puVar11 + -1;
              }
              for (uVar3 = 0; param_10 != uVar3; uVar3 = uVar3 + 1) {
                *(undefined1 *)(param_9 + uVar3) = *(undefined1 *)((long)&uStack_d0 + uVar3);
              }
              uVar3 = 0;
              goto code_r0x020a9040;
            }
          }
        }
      }
    }
  }
  uVar3 = 0x724c;
code_r0x020a9040:
  if (lRam0000000002100880 == lStack_70) {
    return uVar3;
  }
  auVar17 = FUN_02091090(uVar3);
  if (uVar9 == 0) {
    return 0x7247;
  }
  uVar4 = FUN_020a8e0c(auVar17._0_8_,uVar7 == 0,auVar17._8_8_,uVar4 & 0xffffffff,uVar5,
                       uVar6 & 0xffffffff,uVar7,uVar8 & 0xffffffff,uVar9,uStack_110,
                       &stack0xffffffffffffffa0,0x20a91b4);
  return uVar4;
}


