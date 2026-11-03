// File offset entry: 0xaa960

/* WARNING: Removing unreachable block (ram,0x020aa8c4) */

void FUN_020aa860(undefined8 param_1,undefined8 param_2,ulong param_3,undefined1 *param_4)

{
  uint uVar1;
  uint uVar2;
  undefined1 *puVar3;
  undefined1 in_ZR;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  undefined1 *puVar7;
  undefined1 *puVar8;
  ulong uVar9;
  uint extraout_w8;
  uint uVar10;
  undefined4 uVar11;
  undefined4 extraout_w9;
  int iVar12;
  long *plVar14;
  uint extraout_w10;
  uint uVar15;
  long lVar16;
  undefined1 *unaff_x19;
  long unaff_x20;
  uint uVar17;
  ulong unaff_x21;
  undefined1 *unaff_x22;
  ulong unaff_x23;
  int *unaff_x24;
  undefined1 *unaff_x29;
  code *unaff_x30;
  undefined1 auVar18 [16];
  ulong uVar13;
  
  auVar18._8_8_ = param_2;
  auVar18._0_8_ = param_1;
  uVar9 = 0;
  puVar3 = (undefined1 *)register0x00000008;
  while( true ) {
    lVar16 = auVar18._8_8_;
    lVar4 = auVar18._0_8_;
    puVar7 = (undefined1 *)(param_3 * 8);
    *(undefined1 **)(puVar3 + -0x40) = unaff_x29;
    *(code **)(puVar3 + -0x38) = unaff_x30;
    *(int **)(puVar3 + -0x30) = unaff_x24;
    *(ulong *)(puVar3 + -0x28) = unaff_x23;
    *(undefined1 **)(puVar3 + -0x20) = unaff_x22;
    *(ulong *)(puVar3 + -0x18) = unaff_x21;
    *(long *)(puVar3 + -0x10) = unaff_x20;
    *(undefined1 **)(puVar3 + -8) = unaff_x19;
    unaff_x29 = puVar3 + -0x40;
    *(undefined8 *)(puVar3 + -0x48) = uRam0000000002100880;
    *(undefined8 *)(puVar3 + -0x58) = 0;
    *(undefined8 *)(puVar3 + -0x50) = 0;
    *(undefined8 *)(puVar3 + -0x68) = 0;
    *(undefined8 *)(puVar3 + -0x60) = 0;
    *(undefined8 *)(puVar3 + -0x78) = 0;
    *(undefined8 *)(puVar3 + -0x70) = 0;
    *(undefined8 *)(puVar3 + -0x88) = 0;
    *(undefined8 *)(puVar3 + -0x80) = 0;
    puVar8 = puVar7;
    if (lVar16 == 0) {
      uVar5 = 0x7247;
    }
    else {
      *(undefined4 *)(puVar3 + -0x90) = 0;
      *(undefined4 *)(puVar3 + -0x8c) = 0;
      *(undefined4 *)(puVar3 + -0x94) = 0;
      func_0x020a9298(lVar16,param_3);
      FUN_02091664(puVar3 + -0x8c,0,4);
      FUN_02091664(puVar3 + -0x90,0,4);
      uVar6 = 4;
      FUN_02091664(puVar3 + -0x94,0);
      unaff_x23 = func_0x020a928c(lVar16);
      func_0x020a928c(0);
      do {
        uVar10 = *(uint *)(lVar4 + 0x100);
      } while ((uVar10 & 0xf8) == 0);
      uVar13 = (param_3 & 0x1fffffffffffffff) >> 0x1d;
      uVar11 = (undefined4)uVar13;
      uVar2 = 0x30000080;
      if (uVar13 == 0) {
        uVar2 = 0x30000000;
      }
      uVar17 = (uint)uVar9;
      uVar1 = *(uint *)(puVar3 + -0x90);
      uVar15 = uVar2 & 0xf8000000 | (uVar17 & 3) << 0x19 |
               uVar2 & 0x1ffffff | (int)(uVar9 >> 2) << 0xb | (uint)(unaff_x23 >> 0x20 != 0) << 8 |
               *(uint *)(puVar3 + -0x8c) & 0xc18ff67f;
      *(uint *)(puVar3 + -0x90) = uVar1 | 0x80000000;
      *(uint *)(puVar3 + -0x8c) = uVar15;
      unaff_x24 = (int *)(lVar4 + 0x2c);
      uVar2 = *unaff_x24 + 1U & 0xffff;
      in_ZR = (*unaff_x24 + 1U & 0xfffe) == 0;
      if ((bool)in_ZR) {
        uVar2 = 1;
      }
      *(uint *)(puVar3 + -0x90) = uVar2 | uVar1 & 0xffff0000 | 0x80000000;
      param_3 = uVar6;
      if (unaff_x23 >> 0x20 != 0) {
        func_0x020aac34();
        param_3 = uVar6;
        uVar15 = extraout_w10;
        uVar10 = extraout_w8;
        uVar11 = extraout_w9;
      }
      *(uint *)(lVar4 + 0x104) = uVar15;
      *(undefined4 *)(lVar4 + 0x104) = *(undefined4 *)(puVar3 + -0x90);
      *(undefined4 *)(lVar4 + 0x104) = *(undefined4 *)(puVar3 + -0x94);
      *(int *)(lVar4 + 0x104) = (int)puVar7;
      *(int *)(lVar4 + 0x104) = (int)unaff_x23;
      if ((char)puVar3[-0x8c] < '\0') {
        *(undefined4 *)(lVar4 + 0x104) = uVar11;
        iVar12 = -6;
      }
      else {
        iVar12 = -5;
      }
      *(uint *)(lVar4 + 0x100) = iVar12 + (uVar10 & 0xff);
      uVar5 = func_0x020aab00(unaff_x24,*(undefined2 *)(puVar3 + -0x90));
      unaff_x19 = param_4;
      unaff_x20 = lVar4;
      unaff_x21 = uVar9;
      unaff_x22 = puVar7;
      if ((int)uVar5 == 0) {
        if (*unaff_x24 < 0) {
          in_ZR = uVar17 == 0xd;
          if ((uVar17 < 0xe) && ((0x3cfbU >> uVar9 & 1) != 0)) {
            unaff_x21 = *(ulong *)((long)(int)uVar17 * 8 + 0x20e43a8);
            unaff_x22 = puVar3 + -0x88;
            unaff_x23 = unaff_x21 >> 2;
            FUN_02091664(puVar3 + -0x88,0,0x40);
            plVar14 = (long *)0x20e4310;
            uVar9 = unaff_x23;
            while( true ) {
              uVar9 = uVar9 - 1;
              in_ZR = uVar9 == 0xffffffffffffffff;
              if ((bool)in_ZR) break;
              lVar16 = *plVar14;
              *(uint *)(unaff_x22 + uVar9 * 4) =
                   (*(uint *)(lVar16 + lVar4) & 0xff00) << 8 | *(int *)(lVar16 + lVar4) << 0x18 |
                   *(uint *)(lVar16 + lVar4) >> 8 & 0xff00 | *(uint *)(lVar16 + lVar4) >> 0x18;
              plVar14 = plVar14 + 1;
            }
            param_3 = unaff_x21;
            FUN_020915ac(param_4,puVar3 + -0x88);
            uVar5 = 0;
          }
          else {
            uVar5 = 0x7250;
          }
        }
        else {
          uVar5 = 0x724e;
        }
      }
    }
    func_0x020aab34(uVar5);
    if ((bool)in_ZR) break;
    unaff_x30 = FUN_020aaae8;
    auVar18 = FUN_02091090();
    uVar9 = 5;
    puVar3 = puVar3 + -0xa0;
    param_4 = puVar8;
  }
  return;
}


