// File offset entry: 0xaabe8

/* WARNING: Removing unreachable block (ram,0x020aa8c4) */
/* WARNING: Removing unreachable block (ram,0x020aaa38) */

void FUN_020aaae8(undefined8 param_1,undefined8 param_2,ulong param_3,undefined1 *param_4)

{
  uint uVar1;
  uint uVar2;
  undefined1 in_ZR;
  undefined8 uVar3;
  long lVar4;
  ulong uVar5;
  undefined1 *puVar6;
  undefined1 *puVar7;
  uint extraout_w8;
  uint uVar8;
  undefined4 uVar9;
  undefined4 extraout_w9;
  int iVar10;
  long *plVar12;
  uint extraout_w10;
  uint uVar13;
  long lVar14;
  undefined1 *unaff_x19;
  long unaff_x20;
  ulong unaff_x21;
  undefined1 *unaff_x22;
  ulong unaff_x23;
  int *unaff_x24;
  undefined1 *unaff_x29;
  code *unaff_x30;
  undefined1 auVar15 [16];
  ulong uVar11;
  
  auVar15._8_8_ = param_2;
  auVar15._0_8_ = param_1;
  while( true ) {
    lVar14 = auVar15._8_8_;
    lVar4 = auVar15._0_8_;
    puVar7 = (undefined1 *)(param_3 * 8);
    *(undefined1 **)((long)register0x00000008 + -0x40) = unaff_x29;
    *(code **)((long)register0x00000008 + -0x38) = unaff_x30;
    *(int **)((long)register0x00000008 + -0x30) = unaff_x24;
    *(ulong *)((long)register0x00000008 + -0x28) = unaff_x23;
    *(undefined1 **)((long)register0x00000008 + -0x20) = unaff_x22;
    *(ulong *)((long)register0x00000008 + -0x18) = unaff_x21;
    *(long *)((long)register0x00000008 + -0x10) = unaff_x20;
    *(undefined1 **)((long)register0x00000008 + -8) = unaff_x19;
    unaff_x29 = (undefined1 *)((long)register0x00000008 + -0x40);
    *(undefined8 *)((long)register0x00000008 + -0x48) = uRam0000000002100880;
    *(undefined8 *)((long)register0x00000008 + -0x58) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x50) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x68) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x60) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x78) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x70) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x88) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x80) = 0;
    puVar6 = puVar7;
    if (lVar14 == 0) {
      uVar3 = 0x7247;
    }
    else {
      *(undefined4 *)((long)register0x00000008 + -0x90) = 0;
      *(undefined4 *)((long)register0x00000008 + -0x8c) = 0;
      *(undefined4 *)((long)register0x00000008 + -0x94) = 0;
      func_0x020a9298(lVar14,param_3);
      FUN_02091664((undefined1 *)((long)register0x00000008 + -0x8c),0,4);
      FUN_02091664((undefined1 *)((long)register0x00000008 + -0x90),0,4);
      uVar5 = 4;
      FUN_02091664((undefined1 *)((long)register0x00000008 + -0x94),0);
      unaff_x23 = func_0x020a928c(lVar14);
      func_0x020a928c(0);
      do {
        uVar8 = *(uint *)(lVar4 + 0x100);
      } while ((uVar8 & 0xf8) == 0);
      uVar11 = (param_3 & 0x1fffffffffffffff) >> 0x1d;
      uVar9 = (undefined4)uVar11;
      uVar2 = 0x30000080;
      if (uVar11 == 0) {
        uVar2 = 0x30000000;
      }
      uVar1 = *(uint *)((long)register0x00000008 + -0x90);
      uVar13 = uVar2 & 0xf8000000 | 0x2000000 | uVar2 & 0x1ffffff | 0x800 |
               (uint)(unaff_x23 >> 0x20 != 0) << 8 |
               *(uint *)((long)register0x00000008 + -0x8c) & 0xc18ff67f;
      *(uint *)((long)register0x00000008 + -0x90) = uVar1 | 0x80000000;
      *(uint *)((long)register0x00000008 + -0x8c) = uVar13;
      unaff_x24 = (int *)(lVar4 + 0x2c);
      uVar2 = *unaff_x24 + 1U & 0xffff;
      in_ZR = (*unaff_x24 + 1U & 0xfffe) == 0;
      if ((bool)in_ZR) {
        uVar2 = 1;
      }
      *(uint *)((long)register0x00000008 + -0x90) = uVar2 | uVar1 & 0xffff0000 | 0x80000000;
      param_3 = uVar5;
      if (unaff_x23 >> 0x20 != 0) {
        func_0x020aac34();
        param_3 = uVar5;
        uVar13 = extraout_w10;
        uVar8 = extraout_w8;
        uVar9 = extraout_w9;
      }
      *(uint *)(lVar4 + 0x104) = uVar13;
      *(undefined4 *)(lVar4 + 0x104) = *(undefined4 *)((long)register0x00000008 + -0x90);
      *(undefined4 *)(lVar4 + 0x104) = *(undefined4 *)((long)register0x00000008 + -0x94);
      *(int *)(lVar4 + 0x104) = (int)puVar7;
      *(int *)(lVar4 + 0x104) = (int)unaff_x23;
      if (*(char *)((long)register0x00000008 + -0x8c) < '\0') {
        *(undefined4 *)(lVar4 + 0x104) = uVar9;
        iVar10 = -6;
      }
      else {
        iVar10 = -5;
      }
      *(uint *)(lVar4 + 0x100) = iVar10 + (uVar8 & 0xff);
      uVar3 = func_0x020aab00(unaff_x24,*(undefined2 *)((long)register0x00000008 + -0x90));
      uVar5 = uRam00000000020e43d0;
      unaff_x19 = param_4;
      unaff_x20 = lVar4;
      unaff_x21 = 5;
      unaff_x22 = puVar7;
      if ((int)uVar3 == 0) {
        if (*unaff_x24 < 0) {
          unaff_x22 = (undefined1 *)((long)register0x00000008 + -0x88);
          unaff_x23 = uRam00000000020e43d0 >> 2;
          FUN_02091664((undefined1 *)((long)register0x00000008 + -0x88),0,0x40);
          plVar12 = (long *)0x20e4310;
          uVar11 = unaff_x23;
          while( true ) {
            uVar11 = uVar11 - 1;
            in_ZR = uVar11 == 0xffffffffffffffff;
            if ((bool)in_ZR) break;
            lVar14 = *plVar12;
            *(uint *)(unaff_x22 + uVar11 * 4) =
                 (*(uint *)(lVar14 + lVar4) & 0xff00) << 8 | *(int *)(lVar14 + lVar4) << 0x18 |
                 *(uint *)(lVar14 + lVar4) >> 8 & 0xff00 | *(uint *)(lVar14 + lVar4) >> 0x18;
            plVar12 = plVar12 + 1;
          }
          param_3 = uVar5;
          FUN_020915ac(param_4,(undefined1 *)((long)register0x00000008 + -0x88));
          uVar3 = 0;
          unaff_x21 = uVar5;
        }
        else {
          uVar3 = 0x724e;
        }
      }
    }
    func_0x020aab34(uVar3);
    if ((bool)in_ZR) break;
    unaff_x30 = FUN_020aaae8;
    auVar15 = FUN_02091090();
    register0x00000008 = (BADSPACEBASE *)((long)register0x00000008 + -0xa0);
    param_4 = puVar6;
  }
  return;
}


