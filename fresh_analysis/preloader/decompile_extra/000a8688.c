// File offset entry: 0xa8688

/* WARNING: Possible PIC construction at 0x020a91fc: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a9200) */
/* WARNING: Removing unreachable block (ram,0x020a8dd8) */

ulong FUN_020a8588(long param_1,uint param_2,ulong param_3,long param_4,long param_5,uint param_6)

{
  long *plVar1;
  undefined1 uVar2;
  uint uVar3;
  int iVar4;
  ulong uVar5;
  undefined8 uVar6;
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
  ulong uVar18;
  ulong uVar19;
  long lVar20;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  uint uVar21;
  uint *puVar22;
  long lVar23;
  ulong unaff_x19;
  undefined8 unaff_x20;
  undefined8 unaff_x21;
  ulong uVar24;
  undefined8 unaff_x22;
  undefined8 unaff_x23;
  ulong uVar25;
  ulong uVar26;
  undefined8 unaff_x24;
  ulong uVar27;
  undefined8 unaff_x25;
  ulong uVar28;
  uint uVar29;
  undefined8 unaff_x26;
  ulong uVar30;
  undefined8 unaff_x27;
  ulong uVar31;
  ulong unaff_x28;
  undefined1 *unaff_x29;
  long *plVar32;
  undefined8 unaff_x30;
  undefined1 auVar33 [16];
  undefined1 auVar34 [16];
  undefined1 auVar35 [12];
  undefined8 uStack_70;
  long lStack_68;
  ulong uStack_60;
  long lStack_58;
  undefined8 uStack_50;
  long lStack_48;
  
  uVar25 = (ulong)param_2;
  uVar5 = FUN_020a1ab0();
  if ((((((int)uVar5 != 0) || (uVar5 = 0x10201a, 1 < param_6)) || (param_4 == 0)) ||
      ((param_3 == 0 || (param_1 == 0)))) || (param_5 == 0)) {
    return uVar5;
  }
  if (((param_2 == 0) || (param_1 == 0)) || ((param_3 == 0 || ((param_4 == 0 || (param_5 == 0))))))
  {
    return 0x10201a;
  }
  if (param_6 != 0) {
    if (param_6 != 1) {
      return 0x10201a;
    }
    unaff_x29 = &stack0xffffffffffffffe0;
    unaff_x19 = 2;
    lVar20 = *plRam0000000002103280;
    if (((param_1 != 0) && (param_3 != 0)) && (param_4 != 0)) {
      if (param_5 == 0) {
        unaff_x19 = 3;
      }
      else {
        unaff_x19 = 1;
        if (param_2 != 0) {
          uStack_70 = 0x18005000;
          uStack_50 = 0;
          lStack_68 = param_1;
          uStack_60 = uVar25;
          lStack_58 = param_4;
          lStack_48 = param_5;
          FUN_020abea8(0x10000000,5);
          uVar5 = FUN_020a94ac(&uStack_70);
          unaff_x19 = uVar5 & 0xffffffff;
          FUN_020abea8(0x10000000,0);
        }
      }
    }
    if (lRam0000000002100880 == lVar20) {
      return unaff_x19;
    }
    unaff_x30 = 0x209b760;
    auVar33 = FUN_02091090();
    uVar25 = auVar33._8_8_;
    param_1 = auVar33._0_8_;
    register0x00000008 = (BADSPACEBASE *)&uStack_70;
  }
  *(undefined1 **)((long)register0x00000008 + -0x20) = unaff_x29;
  *(undefined8 *)((long)register0x00000008 + -0x18) = unaff_x30;
  *(ulong *)((long)register0x00000008 + -0x10) = unaff_x19;
  uVar5 = 2;
  *(long *)((long)register0x00000008 + -0x28) = *plRam0000000002103280;
  if (((param_1 != 0) && (param_3 != 0)) && (param_4 != 0)) {
    if (param_5 == 0) {
      uVar5 = 3;
    }
    else {
      uVar5 = 1;
      if ((int)uVar25 != 0) {
        *(ulong *)((long)register0x00000008 + -0x38) = param_3;
        *(undefined8 *)((long)register0x00000008 + -0x30) = 0;
        *(ulong *)((long)register0x00000008 + -0x60) = uVar25 & 0xffffffff;
        *(long *)((long)register0x00000008 + -0x58) = param_4;
        *(undefined8 *)((long)register0x00000008 + -0x70) = 0x18005000;
        *(long *)((long)register0x00000008 + -0x68) = param_1;
        *(undefined8 *)((long)register0x00000008 + -0x50) = 0;
        *(long *)((long)register0x00000008 + -0x48) = param_5;
        *(undefined8 *)((long)register0x00000008 + -0x40) = 0;
        *(undefined4 *)((long)register0x00000008 + -0x2c) = 1;
        FUN_020abea8(0x10000000,3);
        uVar5 = FUN_020a94ac((undefined1 *)((long)register0x00000008 + -0x70));
        uVar5 = uVar5 & 0xffffffff;
        FUN_020abea8(0x10000000,0);
      }
    }
  }
  if (lRam0000000002100880 == *(long *)((long)register0x00000008 + -0x28)) {
    return uVar5;
  }
  auVar33 = FUN_02091090();
  *(undefined1 **)((long)register0x00000008 + -0x90) =
       (undefined1 *)((long)register0x00000008 + -0x20);
  *(undefined8 *)((long)register0x00000008 + -0x88) = 0x209b824;
  *(ulong *)((long)register0x00000008 + -0x80) = uVar5;
  *(long *)((long)register0x00000008 + -0x98) = *plRam0000000002103280;
  *(undefined8 *)((long)register0x00000008 + -0xe0) = 0x18005000;
  *(long *)((long)register0x00000008 + -0xd8) = auVar33._0_8_;
  *(undefined8 *)((long)register0x00000008 + -0xc0) = 0;
  *(long *)((long)register0x00000008 + -0xb8) = param_4;
  *(ulong *)((long)register0x00000008 + -0xd0) = auVar33._8_8_ & 0xffffffff;
  *(undefined8 *)((long)register0x00000008 + -200) = 0;
  *(undefined2 *)((long)register0x00000008 + -0xa0) = 0;
  *(undefined4 *)((long)register0x00000008 + -0x9c) = 0;
  *(undefined8 *)((long)register0x00000008 + -0xb0) = 0x10;
  *(ulong *)((long)register0x00000008 + -0xa8) = param_3;
  if (auVar33._0_8_ == 0) {
    uVar5 = 2;
  }
  else if (param_4 == 0) {
    uVar5 = 3;
  }
  else if (auVar33._8_4_ == 0) {
    uVar5 = 1;
  }
  else {
    FUN_020abf40(0x10000000,1);
    FUN_020abea8(0x10000000,3);
    FUN_020abfa4(0x18000000,1);
    uVar3 = FUN_020a985c((undefined1 *)((long)register0x00000008 + -0xe0));
    uVar5 = (ulong)uVar3;
    if (uVar3 == 0x72d1) {
      FUN_020a9364(0x18005000);
      FUN_020a9388(0x18005000);
    }
    FUN_020abea8(0x10000000,0);
  }
  uVar3 = (uint)param_4;
  if (lRam0000000002100880 == *(long *)((long)register0x00000008 + -0x98)) {
    return uVar5;
  }
  auVar35 = FUN_02091090();
  uVar31 = uRam00000000020e4240;
  uVar25 = auVar35._0_8_;
  if (((uVar25 == 0) || (auVar35._8_4_ == 0)) || ((param_3 == 0 || (uVar3 == 0)))) {
    return 1;
  }
  uVar13 = (ulong)auVar35._8_4_;
  uVar17 = (ulong)uVar3;
  uVar10 = 0;
  *(undefined1 **)((long)register0x00000008 + -0x140) =
       (undefined1 *)((long)register0x00000008 + -0x90);
  *(undefined8 *)((long)register0x00000008 + -0x138) = 0x209b928;
  *(ulong *)((long)register0x00000008 + -0x130) = unaff_x28;
  *(undefined8 *)((long)register0x00000008 + -0x128) = unaff_x27;
  *(undefined8 *)((long)register0x00000008 + -0x120) = unaff_x26;
  *(undefined8 *)((long)register0x00000008 + -0x118) = unaff_x25;
  *(undefined8 *)((long)register0x00000008 + -0x110) = unaff_x24;
  *(undefined8 *)((long)register0x00000008 + -0x108) = unaff_x23;
  *(undefined8 *)((long)register0x00000008 + -0x100) = unaff_x22;
  *(undefined8 *)((long)register0x00000008 + -0xf8) = unaff_x21;
  *(undefined8 *)((long)register0x00000008 + -0xf0) = unaff_x20;
  *(ulong *)((long)register0x00000008 + -0xe8) = uVar5;
  plVar32 = (long *)((long)register0x00000008 + -0x140);
  uVar2 = 0;
  *(long *)((long)register0x00000008 + -0x148) = *plRam0000000002103280;
  uVar5 = uVar25;
  uVar15 = uVar13;
  uVar26 = param_3;
  uVar19 = uVar17;
  FUN_02091664((undefined1 *)((long)register0x00000008 + -400),0,0x48);
  *(undefined8 *)((long)register0x00000008 + -0x1d0) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x1c8) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x1c0) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x1b8) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x1b0) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x1a8) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x1a0) = 0;
  *(undefined8 *)((long)register0x00000008 + -0x198) = 0;
  FUN_02091664((undefined1 *)((long)register0x00000008 + -400),0,0x48);
  *(undefined8 *)((long)register0x00000008 + -400) = 0x18003000;
  *(undefined4 *)((long)register0x00000008 + -0x158) = 0;
  *(undefined4 *)((long)register0x00000008 + -0x184) = 0;
  *(undefined4 *)((long)register0x00000008 + -0x180) = 0;
  *(undefined4 *)((long)register0x00000008 + -0x17c) = 4;
  *(undefined4 *)((long)register0x00000008 + -0x174) = 0;
  FUN_02091664((undefined1 *)((long)register0x00000008 + -0x1d0),0,0x40);
  *(ulong *)((long)register0x00000008 + -0x1c0) = uVar25;
  *(ulong *)((long)register0x00000008 + -0x1b8) = uVar13;
  *(ulong *)((long)register0x00000008 + -0x1a0) = param_3;
  *(ulong *)((long)register0x00000008 + -0x198) = uVar17;
  *(undefined8 *)((long)register0x00000008 + -0x1d0) = 0;
  *(ulong *)((long)register0x00000008 + -0x1c8) = uVar31;
  FUN_020915ac((undefined1 *)((long)register0x00000008 + -0x218),
               (undefined1 *)((long)register0x00000008 + -400),0x48);
  uVar9 = 0x40;
  FUN_020915ac((undefined1 *)((long)register0x00000008 + -600),
               (undefined1 *)((long)register0x00000008 + -0x1d0));
  uVar6 = func_0x020a88f4((undefined1 *)((long)register0x00000008 + -0x218),
                          (undefined1 *)((long)register0x00000008 + -600));
  uVar30 = 0x18003000;
  uVar28 = 4;
  uVar27 = 0;
  uVar24 = 0;
  uVar7 = func_0x020a9218(uVar6);
  if ((bool)uVar2) {
    return uVar7;
  }
  lVar20 = 0x20a8e0c;
  auVar33 = FUN_02091090();
  plVar1 = (long *)((long)register0x00000008 + -0x260);
  do {
    plVar1[-0xc] = (long)plVar32;
    plVar1[-0xb] = lVar20;
    plVar1[-10] = unaff_x28;
    plVar1[-9] = uVar31;
    plVar1[-8] = uVar30;
    plVar1[-7] = uVar28;
    plVar1[-6] = uVar27;
    plVar1[-5] = uVar25;
    plVar1[-4] = uVar13;
    plVar1[-3] = uVar24;
    plVar1[-2] = param_3;
    plVar1[-1] = uVar17;
    uVar8 = 0x7247;
    plVar1[-0xe] = *plRam0000000002103280;
    uVar7 = uVar9;
    uVar11 = uVar10;
    uVar12 = uVar5;
    uVar14 = uVar15;
    uVar16 = uVar26;
    uVar18 = uVar19;
    if ((uVar9 != 0) && (param_3 = uVar5, uVar5 != 0)) {
      uVar27 = auVar33._8_8_ & 0xffffffff;
      lVar20 = *plVar1;
      uVar7 = 0x30;
      uVar30 = uVar19 & 0xffffffff;
      uVar24 = uVar15 & 0xffffffff;
      uVar25 = uVar10 & 0xffffffff;
      plVar1[-0x1c] = 0;
      plVar1[-0x1b] = 0;
      plVar1[-0x1e] = 0;
      plVar1[-0x1d] = 0;
      plVar1[-0x20] = 0;
      plVar1[-0x1f] = 0;
      plVar1[-0x21] = auVar33._0_8_;
      FUN_02091664(plVar1 + -0x20,0);
      plVar1[-0x22] = lVar20;
      uVar3 = auVar33._8_4_;
      *(undefined4 *)(plVar1 + -0x20) = 1;
      *(uint *)((long)plVar1 + -0xfc) = (uint)(lVar20 == 0);
      plVar1[-0x1e] = plVar1[-0x21];
      plVar1[-0x1d] = uVar25;
      plVar1[-0x1c] = uVar30;
      plVar1[-0x1b] = uVar24;
      uVar31 = uVar25;
      uVar17 = uVar30;
      uVar13 = uVar9;
      uVar28 = uVar26;
      unaff_x28 = uVar24;
      if (uVar3 < 8) {
        if (uVar3 == 0) {
          uVar8 = 0x7247;
          if ((uVar26 == 0) || (uVar29 = (uint)uVar19, uVar29 == 0)) goto code_r0x020a9040;
          uVar7 = 0x60;
          FUN_02091664(plVar1 + -0x1a,0);
          if (uVar29 < 0x61) {
            uVar29 = uVar29 >> 2;
            if ((uVar19 & 3) != 0) {
              uVar29 = uVar29 + 1;
            }
            if (uVar29 < 0x19) {
              uVar21 = uVar29 * 4;
              while (uVar17 != 0) {
                uVar21 = uVar21 - 1;
                uVar17 = uVar17 - 1;
                *(undefined1 *)((long)plVar1 + ((ulong)uVar21 - 0xd0)) =
                     *(undefined1 *)(uVar26 + (uVar17 & 0xffffffff));
              }
              uVar17 = 0;
              if (uVar29 != 0) {
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
          plVar1[-0x14] = 0;
          plVar1[-0x13] = 0;
          *(uint *)(plVar1 + -0x1f) = uVar3;
          *(undefined4 *)((long)plVar1 + -0xf4) = 1;
          plVar1[-0x16] = 0;
          plVar1[-0x15] = 0;
          plVar1[-0x18] = 0;
          plVar1[-0x17] = 0;
          plVar1[-0x1a] = 0;
          plVar1[-0x19] = 0;
          if ((uint)uVar10 < 0x41) {
            uVar3 = (uint)uVar10 >> 2;
            if ((uVar10 & 3) != 0) {
              uVar3 = uVar3 + 1;
            }
            if (uVar3 < 0x11) {
              uVar21 = uVar3 * 4;
              uVar29 = *(uint *)(plVar1 + 1);
              uVar26 = (ulong)uVar29;
              while (uVar25 != 0) {
                uVar21 = uVar21 - 1;
                uVar25 = uVar25 - 1;
                *(undefined1 *)((long)plVar1 + ((ulong)uVar21 - 0xd0)) =
                     *(undefined1 *)(uVar9 + (uVar25 & 0xffffffff));
              }
              if (uVar3 != 0) {
                do {
                  func_0x020a9238();
                } while (extraout_x8 != 0);
              }
              plVar1[-0x16] = 0;
              plVar1[-0x15] = 0;
              plVar1[-0x18] = 0;
              plVar1[-0x17] = 0;
              plVar1[-0x1a] = 0;
              plVar1[-0x19] = 0;
              uVar31 = 0;
              uVar25 = uVar26;
              if ((uint)uVar15 < 0x31) {
                uVar3 = (uint)uVar15 >> 2;
                if ((uVar15 & 3) != 0) {
                  uVar3 = uVar3 + 1;
                }
                if (uVar3 < 0xd) {
                  uVar21 = uVar3 * 4;
                  uVar17 = uVar24;
                  while (uVar17 != 0) {
                    uVar21 = uVar21 - 1;
                    uVar17 = uVar17 - 1;
                    *(undefined1 *)((long)plVar1 + ((ulong)uVar21 - 0xd0)) =
                         *(undefined1 *)(uVar5 + (uVar17 & 0xffffffff));
                  }
                  uVar17 = plVar1[-0x22];
                  if (uVar3 != 0) {
                    do {
                      func_0x020a9238();
                    } while (extraout_x8_00 != 0);
                  }
                  uVar7 = 1;
                  uVar11 = 5000;
                  lVar20 = plVar1[-0x1e];
                  *(uint *)(lVar20 + 0x19c) =
                       (*(uint *)(plVar1 + -0x20) & 1) << 1 |
                       (*(uint *)((long)plVar1 + -0xfc) & 3) << 2 |
                       (*(uint *)(plVar1 + -0x1f) & 7) << 4 |
                       (*(uint *)((long)plVar1 + -0xf4) & 1) << 7 |
                       (*(uint *)(plVar1 + -0x1d) & 0xff) << 8 |
                       (*(uint *)(plVar1 + -0x1c) & 0xff) << 0x10 | (int)plVar1[-0x1b] << 0x18 | 1;
                  iVar4 = func_0x020a9354(lVar20 + 0x19c,1);
                  unaff_x28 = 0;
                  if (iVar4 == 0) {
                    uVar3 = *(uint *)(plVar1[-0x21] + 0x198);
                    if ((uVar3 & 0x3f) == 0) {
LAB_020a9100:
                      uVar21 = 0x724d;
                      if ((uVar3 & 0x9f000000) == 0x80000000 || *(int *)((long)plVar1 + -0xfc) == 0)
                      {
                        uVar21 = 0;
                      }
                      uVar8 = (ulong)uVar21;
                      if (*(int *)((long)plVar1 + -0xfc) == 0) {
                        plVar1[-0x14] = 0;
                        plVar1[-0x13] = 0;
                        plVar1[-0x16] = 0;
                        plVar1[-0x15] = 0;
                        plVar1[-0x18] = 0;
                        plVar1[-0x17] = 0;
                        plVar1[-0x1a] = 0;
                        plVar1[-0x19] = 0;
                        if ((0x43 < uVar29) || ((uVar29 & 3) != 0)) goto LAB_020a9038;
                        puVar22 = (uint *)(plVar1[-0x21] + 0x1fc);
                        for (uVar5 = 0; (uVar29 & 0xfffffffc) != uVar5; uVar5 = uVar5 + 4) {
                          *(uint *)((long)plVar1 + (uVar5 - 0xd0)) =
                               (*puVar22 & 0xff00) << 8 | *puVar22 << 0x18 | *puVar22 >> 8 & 0xff00
                               | *puVar22 >> 0x18;
                          puVar22 = puVar22 + -1;
                        }
                        for (uVar5 = 0; uVar26 != uVar5; uVar5 = uVar5 + 1) {
                          *(undefined1 *)(uVar17 + uVar5) =
                               *(undefined1 *)((long)plVar1 + (uVar5 - 0xd0));
                        }
                        uVar8 = 0;
                      }
                    }
                    else {
                      lVar20 = 0;
                      do {
                        lVar23 = lVar20;
                        if (lVar23 + 8 == 0x30) goto LAB_020a9100;
                        lVar20 = lVar23 + 8;
                      } while ((uVar3 >> (ulong)(*(uint *)(lVar23 + 0x20e4214) & 0x1f) & 1) == 0);
                      uVar8 = (ulong)*(uint *)(lVar23 + 0x20e4218);
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
    if (lRam0000000002100880 == plVar1[-0xe]) {
      return uVar8;
    }
    auVar34 = FUN_02091090(uVar8);
    uVar9 = auVar34._8_8_;
    if (uVar18 == 0) {
      return 0x7247;
    }
    plVar1[-0x24] = (long)(plVar1 + -0xc);
    plVar1[-0x23] = 0x20a91b4;
    plVar32 = plVar1 + -0x24;
    uVar19 = uVar16 & 0xffffffff;
    uVar15 = uVar12 & 0xffffffff;
    uVar10 = uVar7 & 0xffffffff;
    auVar33[8] = uVar14 == 0;
    auVar33._0_8_ = auVar34._0_8_;
    auVar33._9_7_ = 0;
    *(int *)(plVar1 + -0x25) = (int)plVar1[-0x22];
    plVar1[-0x26] = uVar18;
    lVar20 = 0x20a9200;
    plVar1 = plVar1 + -0x26;
    uVar5 = uVar11;
    uVar26 = uVar14;
  } while( true );
}


