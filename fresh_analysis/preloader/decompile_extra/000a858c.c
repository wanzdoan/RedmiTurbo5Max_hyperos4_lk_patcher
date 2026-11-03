// File offset entry: 0xa858c

/* WARNING: Possible PIC construction at 0x020a91fc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a8538: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a9200) */
/* WARNING: Removing unreachable block (ram,0x020a853c) */
/* WARNING: Removing unreachable block (ram,0x020a8dd8) */

ulong FUN_020a848c(long param_1,long param_2,byte param_3,long param_4,long param_5,uint param_6)

{
  undefined8 *puVar1;
  long *plVar2;
  undefined1 uVar3;
  uint uVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  ulong uVar8;
  undefined8 *puVar9;
  undefined1 *puVar10;
  undefined1 *puVar11;
  undefined1 *puVar12;
  undefined1 *puVar13;
  undefined1 *puVar14;
  undefined1 *puVar15;
  undefined1 *puVar16;
  undefined1 *puVar17;
  undefined1 *puVar18;
  ulong uVar19;
  long lVar20;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  undefined1 *puVar21;
  uint uVar22;
  uint *puVar23;
  long lVar24;
  ulong uVar25;
  long unaff_x20;
  long unaff_x21;
  ulong uVar26;
  undefined8 unaff_x22;
  undefined8 unaff_x23;
  undefined1 *puVar27;
  undefined8 unaff_x24;
  ulong uVar28;
  undefined8 unaff_x25;
  undefined1 *puVar29;
  uint uVar30;
  undefined8 unaff_x26;
  ulong uVar31;
  undefined8 unaff_x27;
  undefined1 *puVar32;
  ulong unaff_x28;
  long *plVar33;
  undefined1 *puVar34;
  undefined1 auVar35 [16];
  undefined1 auVar36 [16];
  undefined1 auVar37 [12];
  undefined8 uStack_f0;
  long lStack_e8;
  ulong uStack_e0;
  long lStack_d8;
  undefined8 uStack_d0;
  long lStack_c8;
  undefined1 *puStack_c0;
  undefined1 *puStack_b8;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  long lStack_38;
  
  puVar9 = &uStack_80;
  puVar1 = &uStack_80;
  uVar25 = (ulong)(uint)param_3;
  lStack_38 = *plRam0000000002103280;
  uStack_50 = 0;
  uStack_48 = 0;
  uStack_60 = 0;
  uStack_58 = 0;
  uStack_70 = 0;
  uStack_68 = 0;
  uStack_80 = 0;
  uStack_78 = 0;
  if (param_3 != 0) {
    if (param_3 == 1) {
      uVar4 = func_0x0209f2d4(3,&uStack_80,0x20);
      unaff_x20 = param_1;
      unaff_x21 = param_2;
      if (uVar4 == 0) {
        uVar19 = 0x20;
        param_6 = 0;
        puVar34 = (undefined1 *)0x20a853c;
        param_5 = param_1;
        goto code_r0x020a858c;
      }
      uVar25 = (ulong)uVar4;
      FUN_0208ada0(0x20bf2bb,uVar25);
    }
    else {
      FUN_0208ada0(0x20bf298);
      uVar25 = 0;
    }
  }
  puVar9 = (undefined8 *)0x40;
  FUN_02091664(&uStack_80,0);
  if (lRam0000000002100880 == lStack_38) {
    return uVar25;
  }
  puVar34 = (undefined1 *)0x20a8588;
  auVar35 = FUN_02091090();
  uVar19 = auVar35._8_8_;
  param_1 = auVar35._0_8_;
  param_2 = param_4;
code_r0x020a858c:
  auVar35._8_8_ = uVar19 & 0xffffffff;
  auVar35._0_8_ = param_1;
  puStack_c0 = &stack0xffffffffffffffd0;
  puStack_b8 = puVar34;
  uVar6 = FUN_020a1ab0();
  if ((((((int)uVar6 != 0) || (uVar6 = 0x10201a, 1 < param_6)) || (param_2 == 0)) ||
      ((puVar9 == (undefined8 *)0x0 || (param_1 == 0)))) || (param_5 == 0)) {
    return uVar6;
  }
  if ((((int)uVar19 == 0) || (param_1 == 0)) ||
     ((puVar9 == (undefined8 *)0x0 || ((param_2 == 0 || (param_5 == 0)))))) {
    return 0x10201a;
  }
  puVar34 = puStack_c0;
  puVar32 = puStack_b8;
  if (param_6 != 0) {
    if (param_6 != 1) {
      return 0x10201a;
    }
    puVar1 = &uStack_f0;
    uVar25 = 2;
    lVar20 = *plRam0000000002103280;
    if (((param_1 != 0) && (puVar9 != (undefined8 *)0x0)) && (param_2 != 0)) {
      if (param_5 == 0) {
        uVar25 = 3;
      }
      else {
        uVar25 = 1;
        if ((int)uVar19 != 0) {
          uStack_f0 = 0x18005000;
          uStack_d0 = 0;
          puStack_c0 = (undefined1 *)0x0;
          lStack_e8 = param_1;
          uStack_e0 = uVar19 & 0xffffffff;
          lStack_d8 = param_2;
          lStack_c8 = param_5;
          puStack_b8 = (undefined1 *)puVar9;
          FUN_020abea8(0x10000000,5);
          uVar25 = FUN_020a94ac(&uStack_f0);
          uVar25 = uVar25 & 0xffffffff;
          FUN_020abea8(0x10000000,0);
        }
      }
    }
    if (lRam0000000002100880 == lVar20) {
      return uVar25;
    }
    auVar35 = FUN_02091090();
    puVar34 = &stack0xffffffffffffff60;
    puVar32 = (undefined1 *)0x209b760;
  }
  *(undefined1 **)((long)puVar1 + -0x20) = puVar34;
  *(undefined1 **)((long)puVar1 + -0x18) = puVar32;
  *(ulong *)((long)puVar1 + -0x10) = uVar25;
  uVar25 = 2;
  *(long *)((long)puVar1 + -0x28) = *plRam0000000002103280;
  if (((auVar35._0_8_ != 0) && (puVar9 != (undefined8 *)0x0)) && (param_2 != 0)) {
    if (param_5 == 0) {
      uVar25 = 3;
    }
    else {
      uVar25 = 1;
      if (auVar35._8_4_ != 0) {
        *(undefined8 **)((long)puVar1 + -0x38) = puVar9;
        *(undefined8 *)((long)puVar1 + -0x30) = 0;
        *(ulong *)((long)puVar1 + -0x60) = auVar35._8_8_ & 0xffffffff;
        *(long *)((long)puVar1 + -0x58) = param_2;
        *(undefined8 *)((long)puVar1 + -0x70) = 0x18005000;
        *(long *)((long)puVar1 + -0x68) = auVar35._0_8_;
        *(undefined8 *)((long)puVar1 + -0x50) = 0;
        *(long *)((long)puVar1 + -0x48) = param_5;
        *(undefined8 *)((long)puVar1 + -0x40) = 0;
        *(undefined4 *)((long)puVar1 + -0x2c) = 1;
        FUN_020abea8(0x10000000,3);
        uVar25 = FUN_020a94ac((undefined1 *)((long)puVar1 + -0x70));
        uVar25 = uVar25 & 0xffffffff;
        FUN_020abea8(0x10000000,0);
      }
    }
  }
  if (lRam0000000002100880 == *(long *)((long)puVar1 + -0x28)) {
    return uVar25;
  }
  auVar35 = FUN_02091090();
  *(undefined1 **)((long)puVar1 + -0x90) = (undefined1 *)((long)puVar1 + -0x20);
  *(undefined8 *)((long)puVar1 + -0x88) = 0x209b824;
  *(ulong *)((long)puVar1 + -0x80) = uVar25;
  *(long *)((long)puVar1 + -0x98) = *plRam0000000002103280;
  *(undefined8 *)((long)puVar1 + -0xe0) = 0x18005000;
  *(long *)((long)puVar1 + -0xd8) = auVar35._0_8_;
  *(undefined8 *)((long)puVar1 + -0xc0) = 0;
  *(long *)((long)puVar1 + -0xb8) = param_2;
  *(ulong *)((long)puVar1 + -0xd0) = auVar35._8_8_ & 0xffffffff;
  *(undefined8 *)((long)puVar1 + -200) = 0;
  *(undefined2 *)((long)puVar1 + -0xa0) = 0;
  *(undefined4 *)((long)puVar1 + -0x9c) = 0;
  *(undefined8 *)((long)puVar1 + -0xb0) = 0x10;
  *(undefined8 **)((long)puVar1 + -0xa8) = puVar9;
  if (auVar35._0_8_ == 0) {
    uVar25 = 2;
  }
  else if (param_2 == 0) {
    uVar25 = 3;
  }
  else if (auVar35._8_4_ == 0) {
    uVar25 = 1;
  }
  else {
    FUN_020abf40(0x10000000,1);
    FUN_020abea8(0x10000000,3);
    FUN_020abfa4(0x18000000,1);
    uVar4 = FUN_020a985c((undefined1 *)((long)puVar1 + -0xe0));
    uVar25 = (ulong)uVar4;
    if (uVar4 == 0x72d1) {
      FUN_020a9364(0x18005000);
      FUN_020a9388(0x18005000);
    }
    FUN_020abea8(0x10000000,0);
  }
  uVar4 = (uint)param_2;
  if (lRam0000000002100880 == *(long *)((long)puVar1 + -0x98)) {
    return uVar25;
  }
  auVar37 = FUN_02091090();
  puVar32 = puRam00000000020e4240;
  puVar34 = auVar37._0_8_;
  if (((puVar34 == (undefined1 *)0x0) || (auVar37._8_4_ == 0)) ||
     ((puVar9 == (undefined8 *)0x0 || (uVar4 == 0)))) {
    return 1;
  }
  puVar15 = (undefined1 *)(long)auVar37._8_4_;
  uVar19 = (ulong)uVar4;
  puVar12 = (undefined1 *)0x0;
  *(undefined1 **)((long)puVar1 + -0x140) = (undefined1 *)((long)puVar1 + -0x90);
  *(undefined8 *)((long)puVar1 + -0x138) = 0x209b928;
  *(ulong *)((long)puVar1 + -0x130) = unaff_x28;
  *(undefined8 *)((long)puVar1 + -0x128) = unaff_x27;
  *(undefined8 *)((long)puVar1 + -0x120) = unaff_x26;
  *(undefined8 *)((long)puVar1 + -0x118) = unaff_x25;
  *(undefined8 *)((long)puVar1 + -0x110) = unaff_x24;
  *(undefined8 *)((long)puVar1 + -0x108) = unaff_x23;
  *(undefined8 *)((long)puVar1 + -0x100) = unaff_x22;
  *(long *)((long)puVar1 + -0xf8) = unaff_x21;
  *(long *)((long)puVar1 + -0xf0) = unaff_x20;
  *(ulong *)((long)puVar1 + -0xe8) = uVar25;
  plVar33 = (long *)((long)puVar1 + -0x140);
  uVar3 = 0;
  *(long *)((long)puVar1 + -0x148) = *plRam0000000002103280;
  puVar21 = puVar34;
  puVar17 = puVar15;
  puVar27 = (undefined1 *)puVar9;
  uVar25 = uVar19;
  FUN_02091664((undefined1 *)((long)puVar1 + -400),0,0x48);
  *(undefined8 *)((long)puVar1 + -0x1d0) = 0;
  *(undefined8 *)((long)puVar1 + -0x1c8) = 0;
  *(undefined8 *)((long)puVar1 + -0x1c0) = 0;
  *(undefined8 *)((long)puVar1 + -0x1b8) = 0;
  *(undefined8 *)((long)puVar1 + -0x1b0) = 0;
  *(undefined8 *)((long)puVar1 + -0x1a8) = 0;
  *(undefined8 *)((long)puVar1 + -0x1a0) = 0;
  *(undefined8 *)((long)puVar1 + -0x198) = 0;
  FUN_02091664((undefined1 *)((long)puVar1 + -400),0,0x48);
  *(undefined8 *)((long)puVar1 + -400) = 0x18003000;
  *(undefined4 *)((long)puVar1 + -0x158) = 0;
  *(undefined4 *)((long)puVar1 + -0x184) = 0;
  *(undefined4 *)((long)puVar1 + -0x180) = 0;
  *(undefined4 *)((long)puVar1 + -0x17c) = 4;
  *(undefined4 *)((long)puVar1 + -0x174) = 0;
  FUN_02091664((undefined1 *)((long)puVar1 + -0x1d0),0,0x40);
  *(undefined1 **)((long)puVar1 + -0x1c0) = puVar34;
  *(undefined1 **)((long)puVar1 + -0x1b8) = puVar15;
  *(undefined8 **)((long)puVar1 + -0x1a0) = puVar9;
  *(ulong *)((long)puVar1 + -0x198) = uVar19;
  *(undefined8 *)((long)puVar1 + -0x1d0) = 0;
  *(undefined1 **)((long)puVar1 + -0x1c8) = puVar32;
  FUN_020915ac((undefined1 *)((long)puVar1 + -0x218),(undefined1 *)((long)puVar1 + -400),0x48);
  puVar10 = (undefined1 *)0x40;
  FUN_020915ac((undefined1 *)((long)puVar1 + -600),(undefined1 *)((long)puVar1 + -0x1d0));
  uVar7 = func_0x020a88f4((undefined1 *)((long)puVar1 + -0x218),(undefined1 *)((long)puVar1 + -600))
  ;
  uVar31 = 0x18003000;
  puVar29 = (undefined1 *)0x4;
  uVar28 = 0;
  uVar26 = 0;
  uVar6 = func_0x020a9218(uVar7);
  if ((bool)uVar3) {
    return uVar6;
  }
  lVar20 = 0x20a8e0c;
  auVar36 = FUN_02091090();
  plVar2 = (long *)((long)puVar1 + -0x260);
  do {
    plVar2[-0xc] = (long)plVar33;
    plVar2[-0xb] = lVar20;
    plVar2[-10] = unaff_x28;
    plVar2[-9] = (long)puVar32;
    plVar2[-8] = uVar31;
    plVar2[-7] = (long)puVar29;
    plVar2[-6] = uVar28;
    plVar2[-5] = (long)puVar34;
    plVar2[-4] = (long)puVar15;
    plVar2[-3] = uVar26;
    plVar2[-2] = (long)puVar9;
    plVar2[-1] = uVar19;
    uVar8 = 0x7247;
    plVar2[-0xe] = *plRam0000000002103280;
    puVar11 = puVar10;
    puVar13 = puVar12;
    puVar14 = puVar21;
    puVar16 = puVar17;
    puVar18 = puVar27;
    uVar6 = uVar25;
    if ((puVar10 != (undefined1 *)0x0) &&
       (puVar9 = (undefined8 *)puVar21, puVar21 != (undefined1 *)0x0)) {
      uVar28 = auVar36._8_8_ & 0xffffffff;
      lVar20 = *plVar2;
      puVar11 = (undefined1 *)0x30;
      uVar31 = uVar25 & 0xffffffff;
      uVar26 = (ulong)puVar17 & 0xffffffff;
      puVar34 = (undefined1 *)((ulong)puVar12 & 0xffffffff);
      plVar2[-0x1c] = 0;
      plVar2[-0x1b] = 0;
      plVar2[-0x1e] = 0;
      plVar2[-0x1d] = 0;
      plVar2[-0x20] = 0;
      plVar2[-0x1f] = 0;
      plVar2[-0x21] = auVar36._0_8_;
      FUN_02091664(plVar2 + -0x20,0);
      plVar2[-0x22] = lVar20;
      uVar4 = auVar36._8_4_;
      *(undefined4 *)(plVar2 + -0x20) = 1;
      *(uint *)((long)plVar2 + -0xfc) = (uint)(lVar20 == 0);
      plVar2[-0x1e] = plVar2[-0x21];
      plVar2[-0x1d] = (long)puVar34;
      plVar2[-0x1c] = uVar31;
      plVar2[-0x1b] = uVar26;
      puVar32 = puVar34;
      uVar19 = uVar31;
      puVar15 = puVar10;
      puVar29 = puVar27;
      unaff_x28 = uVar26;
      if (uVar4 < 8) {
        if (uVar4 == 0) {
          uVar8 = 0x7247;
          if ((puVar27 == (undefined1 *)0x0) || (uVar30 = (uint)uVar25, uVar30 == 0))
          goto code_r0x020a9040;
          puVar11 = (undefined1 *)0x60;
          FUN_02091664(plVar2 + -0x1a,0);
          if (uVar30 < 0x61) {
            uVar30 = uVar30 >> 2;
            if ((uVar25 & 3) != 0) {
              uVar30 = uVar30 + 1;
            }
            if (uVar30 < 0x19) {
              uVar22 = uVar30 * 4;
              uVar25 = uVar31;
              while (uVar25 != 0) {
                uVar22 = uVar22 - 1;
                uVar25 = uVar25 - 1;
                *(undefined1 *)((long)plVar2 + ((ulong)uVar22 - 0xd0)) =
                     puVar27[uVar25 & 0xffffffff];
              }
              uVar19 = 0;
              if (uVar30 != 0) {
                do {
                  func_0x020a9238();
                } while (extraout_x8_01 != 0);
              }
              goto LAB_020a8ed0;
            }
          }
        }
        else {
LAB_020a8ed0:
          plVar2[-0x14] = 0;
          plVar2[-0x13] = 0;
          *(uint *)(plVar2 + -0x1f) = uVar4;
          *(undefined4 *)((long)plVar2 + -0xf4) = 1;
          plVar2[-0x16] = 0;
          plVar2[-0x15] = 0;
          plVar2[-0x18] = 0;
          plVar2[-0x17] = 0;
          plVar2[-0x1a] = 0;
          plVar2[-0x19] = 0;
          if ((uint)puVar12 < 0x41) {
            uVar4 = (uint)puVar12 >> 2;
            if (((ulong)puVar12 & 3) != 0) {
              uVar4 = uVar4 + 1;
            }
            if (uVar4 < 0x11) {
              uVar22 = uVar4 * 4;
              uVar30 = *(uint *)(plVar2 + 1);
              puVar27 = (undefined1 *)(ulong)uVar30;
              while (puVar34 != (undefined1 *)0x0) {
                uVar22 = uVar22 - 1;
                puVar34 = puVar34 + -1;
                *(undefined1 *)((long)plVar2 + ((ulong)uVar22 - 0xd0)) =
                     puVar10[(ulong)puVar34 & 0xffffffff];
              }
              if (uVar4 != 0) {
                do {
                  func_0x020a9238();
                } while (extraout_x8 != 0);
              }
              plVar2[-0x16] = 0;
              plVar2[-0x15] = 0;
              plVar2[-0x18] = 0;
              plVar2[-0x17] = 0;
              plVar2[-0x1a] = 0;
              plVar2[-0x19] = 0;
              puVar32 = (undefined1 *)0x0;
              puVar34 = puVar27;
              if ((uint)puVar17 < 0x31) {
                uVar4 = (uint)puVar17 >> 2;
                if (((ulong)puVar17 & 3) != 0) {
                  uVar4 = uVar4 + 1;
                }
                if (uVar4 < 0xd) {
                  uVar22 = uVar4 * 4;
                  uVar25 = uVar26;
                  while (uVar25 != 0) {
                    uVar22 = uVar22 - 1;
                    uVar25 = uVar25 - 1;
                    *(undefined1 *)((long)plVar2 + ((ulong)uVar22 - 0xd0)) =
                         puVar21[uVar25 & 0xffffffff];
                  }
                  uVar19 = plVar2[-0x22];
                  if (uVar4 != 0) {
                    do {
                      func_0x020a9238();
                    } while (extraout_x8_00 != 0);
                  }
                  puVar11 = (undefined1 *)0x1;
                  puVar13 = (undefined1 *)0x1388;
                  lVar20 = plVar2[-0x1e];
                  *(uint *)(lVar20 + 0x19c) =
                       (*(uint *)(plVar2 + -0x20) & 1) << 1 |
                       (*(uint *)((long)plVar2 + -0xfc) & 3) << 2 |
                       (*(uint *)(plVar2 + -0x1f) & 7) << 4 |
                       (*(uint *)((long)plVar2 + -0xf4) & 1) << 7 |
                       (*(uint *)(plVar2 + -0x1d) & 0xff) << 8 |
                       (*(uint *)(plVar2 + -0x1c) & 0xff) << 0x10 | (int)plVar2[-0x1b] << 0x18 | 1;
                  iVar5 = func_0x020a9354(lVar20 + 0x19c,1);
                  unaff_x28 = 0;
                  if (iVar5 == 0) {
                    uVar4 = *(uint *)(plVar2[-0x21] + 0x198);
                    if ((uVar4 & 0x3f) == 0) {
LAB_020a9100:
                      uVar22 = 0x724d;
                      if ((uVar4 & 0x9f000000) == 0x80000000 || *(int *)((long)plVar2 + -0xfc) == 0)
                      {
                        uVar22 = 0;
                      }
                      uVar8 = (ulong)uVar22;
                      if (*(int *)((long)plVar2 + -0xfc) == 0) {
                        plVar2[-0x14] = 0;
                        plVar2[-0x13] = 0;
                        plVar2[-0x16] = 0;
                        plVar2[-0x15] = 0;
                        plVar2[-0x18] = 0;
                        plVar2[-0x17] = 0;
                        plVar2[-0x1a] = 0;
                        plVar2[-0x19] = 0;
                        if ((0x43 < uVar30) || ((uVar30 & 3) != 0)) goto LAB_020a9038;
                        puVar23 = (uint *)(plVar2[-0x21] + 0x1fc);
                        for (uVar25 = 0; (uVar30 & 0xfffffffc) != uVar25; uVar25 = uVar25 + 4) {
                          *(uint *)((long)plVar2 + (uVar25 - 0xd0)) =
                               (*puVar23 & 0xff00) << 8 | *puVar23 << 0x18 | *puVar23 >> 8 & 0xff00
                               | *puVar23 >> 0x18;
                          puVar23 = puVar23 + -1;
                        }
                        for (puVar21 = (undefined1 *)0x0; puVar27 != puVar21; puVar21 = puVar21 + 1)
                        {
                          puVar21[uVar19] = *(undefined1 *)((long)(plVar2 + -0x1a) + (long)puVar21);
                        }
                        uVar8 = 0;
                      }
                    }
                    else {
                      lVar20 = 0;
                      do {
                        lVar24 = lVar20;
                        if (lVar24 + 8 == 0x30) goto LAB_020a9100;
                        lVar20 = lVar24 + 8;
                      } while ((uVar4 >> (ulong)(*(uint *)(lVar24 + 0x20e4214) & 0x1f) & 1) == 0);
                      uVar8 = (ulong)*(uint *)(lVar24 + 0x20e4218);
                    }
                  }
                  else {
                    uVar8 = 0x7262;
                  }
                  goto code_r0x020a9040;
                }
              }
            }
          }
        }
LAB_020a9038:
        uVar8 = 0x724c;
      }
      else {
        uVar8 = 0x7246;
      }
    }
code_r0x020a9040:
    if (lRam0000000002100880 == plVar2[-0xe]) {
      return uVar8;
    }
    auVar35 = FUN_02091090(uVar8);
    puVar10 = auVar35._8_8_;
    if (uVar6 == 0) {
      return 0x7247;
    }
    plVar2[-0x24] = (long)(plVar2 + -0xc);
    plVar2[-0x23] = 0x20a91b4;
    plVar33 = plVar2 + -0x24;
    uVar25 = (ulong)puVar18 & 0xffffffff;
    puVar17 = (undefined1 *)((ulong)puVar14 & 0xffffffff);
    puVar12 = (undefined1 *)((ulong)puVar11 & 0xffffffff);
    auVar36[8] = puVar16 == (undefined1 *)0x0;
    auVar36._0_8_ = auVar35._0_8_;
    auVar36._9_7_ = 0;
    *(int *)(plVar2 + -0x25) = (int)plVar2[-0x22];
    plVar2[-0x26] = uVar6;
    lVar20 = 0x20a9200;
    plVar2 = plVar2 + -0x26;
    puVar21 = puVar13;
    puVar27 = puVar16;
  } while( true );
}


