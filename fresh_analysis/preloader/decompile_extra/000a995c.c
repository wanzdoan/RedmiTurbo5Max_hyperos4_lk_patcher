// File offset entry: 0xa995c

/* WARNING: Possible PIC construction at 0x020a9a78: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a9c4c: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a9a7c) */
/* WARNING: Removing unreachable block (ram,0x020a9c50) */

int FUN_020a985c(long *param_1,undefined8 param_2,uint *param_3)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  ulong uVar4;
  uint *puVar5;
  long lVar6;
  uint *puVar7;
  uint uVar8;
  uint uVar9;
  long extraout_x10;
  long extraout_x11;
  undefined4 extraout_w13;
  undefined8 uVar10;
  int iVar11;
  long lVar12;
  undefined1 auVar13 [16];
  undefined4 uStack_110;
  undefined4 uStack_10c;
  undefined4 uStack_108;
  uint uStack_104;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_98;
  undefined8 uStack_90;
  long lStack_88;
  
  lStack_88 = lRam0000000002100880;
  uStack_d0 = 0;
  uStack_c8 = 0;
  uStack_e0 = 0;
  uStack_d8 = 0;
  uStack_f0 = 0;
  uStack_e8 = 0;
  uStack_100 = 0;
  uStack_f8 = 0;
  if (*(uint *)((long)param_1 + 0x44) < 3) {
    puVar5 = (uint *)param_1[6];
    if (puVar5 == (uint *)0x0) {
      iVar2 = 0x726f;
    }
    else {
      uVar10 = *(undefined8 *)((long)(int)*(uint *)((long)param_1 + 0x44) * 8 + 0x20e4390);
      if ((uint *)0xf < puVar5) {
        puVar5 = (uint *)0x10;
      }
      uStack_108 = 0;
      uStack_104 = 0;
      uStack_110 = 0;
      uStack_10c = 0;
      FUN_02091664(&uStack_104,0,4);
      FUN_02091664(&uStack_108,0,4);
      FUN_02091664(&uStack_10c,0,4);
      FUN_02091664(&uStack_110,0,4);
      uVar3 = func_0x020a928c(&uStack_100);
      uVar4 = func_0x020a928c((ulong)&uStack_100 | 0x10);
      lVar12 = *param_1;
      uVar9 = *(int *)((long)param_1 + 0x44) << 0x1e;
      if (param_1[7] == 0) {
        uStack_104 = uStack_104 & 0x7807ff0 | uVar9 | 0x100a8000 | *(byte *)(param_1 + 8) & 0xf;
      }
      else {
        uStack_104 = uStack_104 & 0x7807fff | uVar9 | 0x100a8000;
        func_0x020a9298(param_1[7],uVar10);
        uStack_104 = uStack_104 | 0x80;
      }
      uVar9 = *(int *)(lVar12 + 0x54) + 1;
      uVar8 = uVar9 & 0xffff;
      if ((uVar9 & 0xfffe) == 0) {
        uVar8 = 1;
      }
      uStack_108 = uVar8 | uStack_108 & 0xf8ff0000 | 0x3000000;
      if ((uVar4 & 0xf00000000) != 0 || (uVar3 >> 0x20 & 0xf) != 0) {
        func_0x020aac60();
      }
      func_0x020a9298(&uStack_100,0x40);
      lVar6 = *param_1;
      do {
        uVar9 = *(uint *)(lVar6 + 0x100);
      } while ((uVar9 & 0xf0) == 0);
      *(uint *)(lVar6 + 0x104) = uStack_104;
      *(uint *)(lVar6 + 0x104) = uStack_108;
      *(undefined4 *)(lVar6 + 0x104) = uStack_10c;
      if ((uStack_108._2_1_ >> 6 & 1) == 0) {
        iVar2 = 7;
      }
      else {
        iVar2 = 8;
        *(undefined4 *)(lVar6 + 0x104) = uStack_110;
      }
      *(int *)(lVar6 + 0x104) = (int)uVar3;
      *(undefined4 *)(lVar6 + 0x104) = 0x10;
      *(int *)(lVar6 + 0x104) = (int)uVar4;
      *(int *)(lVar6 + 0x104) = (int)uVar4 + 0xf;
      if ((char)uStack_104 < '\0') {
        auVar13._8_8_ = param_1[7];
        auVar13._0_8_ = *param_1;
        param_3 = &uStack_104;
        goto code_r0x020a9d64;
      }
      if (((uStack_108._2_1_ >> 6 & 1) != 0) && (((byte)uStack_110 >> 5 & 1) != 0)) {
        iVar2 = iVar2 + 7;
        *(uint *)(*param_1 + 0x104) = (uint)(uVar3 >> 0x20) & 0xf;
        *(undefined4 *)(*param_1 + 0x104) = 0;
        func_0x020aab54();
      }
      *(uint *)(*param_1 + 0x100) = (uVar9 & 0xff) - iVar2;
      param_3 = (uint *)(ulong)(ushort)uStack_108;
      iVar2 = func_0x020a9e2c(*param_1,0);
      if (iVar2 == 0) {
        FUN_02091664(&uStack_104,0,4);
        FUN_02091664(&uStack_108,0,4);
        FUN_02091664(&uStack_10c,0,4);
        FUN_02091664(&uStack_110,0,4);
        uVar3 = func_0x020a928c(param_1[1]);
        iVar2 = func_0x020a928c(param_1[5]);
        uVar9 = *(int *)((long)param_1 + 0x44) << 0x1e;
        if (param_1[7] == 0) {
          uStack_104 = uStack_104 & 0x7807ff0 | uVar9 | 0x100b0000 | *(byte *)(param_1 + 8) & 0xf;
        }
        else {
          uStack_104 = uStack_104 & 0x7807fff | uVar9 | 0x100b0000;
          func_0x020a9298(param_1[7],uVar10);
          uStack_104 = uStack_104 | 0x80;
        }
        uVar9 = *(int *)(lVar12 + 0x54) + 1;
        uVar8 = uVar9 & 0xffff;
        if ((uVar9 & 0xfffe) == 0) {
          uVar8 = 1;
        }
        uStack_108 = uVar8 | uStack_108 & 0xf8ff0000 | 0x3000000;
        if ((param_1[2] & 0xf00000000U) != 0 || (uVar3 >> 0x20 & 0xf) != 0) {
          func_0x020aac60();
        }
        func_0x020a9298(param_1[1]);
        lVar12 = *param_1;
        do {
          uVar9 = *(uint *)(lVar12 + 0x100);
        } while ((uVar9 & 0xf0) == 0);
        lVar6 = param_1[2];
        *(uint *)(lVar12 + 0x104) = uStack_104;
        *(uint *)(lVar12 + 0x104) = uStack_108;
        *(undefined4 *)(lVar12 + 0x104) = uStack_10c;
        if ((uStack_108._2_1_ >> 6 & 1) == 0) {
          iVar11 = 7;
        }
        else {
          iVar11 = 8;
          *(undefined4 *)(lVar12 + 0x104) = uStack_110;
        }
        *(int *)(lVar12 + 0x104) = (int)uVar3;
        *(int *)(lVar12 + 0x104) = (int)lVar6;
        *(int *)(lVar12 + 0x104) = iVar2;
        *(int *)(lVar12 + 0x104) = iVar2 + (int)puVar5 + -1;
        if ((char)uStack_104 < '\0') {
          auVar13._8_8_ = param_1[7];
          auVar13._0_8_ = *param_1;
          param_3 = &uStack_104;
          goto code_r0x020a9d64;
        }
        if (((uStack_108._2_1_ >> 6 & 1) != 0) && (((byte)uStack_110 >> 5 & 1) != 0)) {
          iVar11 = iVar11 + 7;
          *(uint *)(*param_1 + 0x104) = (uint)(uVar3 >> 0x20) & 0xf;
          *(uint *)(*param_1 + 0x104) = *(uint *)((long)param_1 + 0x14) & 0xf;
          func_0x020aab54();
        }
        *(uint *)(*param_1 + 0x100) = (uVar9 & 0xff) - iVar11;
        param_3 = (uint *)(ulong)(ushort)uStack_108;
        iVar2 = func_0x020a9e2c(*param_1,0);
        if (iVar2 == 0) {
          lVar12 = param_1[5];
          uStack_90 = 0;
          uStack_98 = 0;
          lVar6 = 0xc;
          while (lVar6 != -4) {
            lVar12 = func_0x020aabe8();
            *(undefined4 *)(extraout_x11 + extraout_x10) = extraout_w13;
            lVar6 = extraout_x10 + -4;
          }
          FUN_020915ac(lVar12,&uStack_98);
          iVar2 = 0;
          param_3 = puVar5;
        }
      }
    }
  }
  else {
    iVar2 = 0x726e;
  }
  if (lRam0000000002100880 == lStack_88) {
    return iVar2;
  }
  auVar13 = FUN_02091090(iVar2);
code_r0x020a9d64:
  puVar5 = auVar13._8_8_;
  lVar12 = auVar13._0_8_;
  uVar9 = *param_3 >> 0x1e;
  if ((uVar9 == 0) || ((*param_3 & 0x780000) != 0x80000)) {
    uVar9 = 0;
    iVar2 = 4;
  }
  else {
    if (uVar9 == 2) {
      uVar8 = 0;
      *(undefined4 *)(lVar12 + 0x104) = 0;
      *(undefined4 *)(lVar12 + 0x104) = 0;
    }
    else {
      uVar9 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      *(uint *)(lVar12 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
      uVar9 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
      *(uint *)(lVar12 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
      uVar8 = 8;
    }
    uVar9 = uVar8 + 8;
    iVar2 = 8;
    uVar1 = (*(uint *)((long)puVar5 + (ulong)uVar8) & 0xff00ff00) >> 8 |
            (*(uint *)((long)puVar5 + (ulong)uVar8) & 0xff00ff) << 8;
    *(uint *)(lVar12 + 0x104) = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar8 = *(uint *)(((ulong)uVar8 | 4) + (long)puVar5);
    uVar8 = (uVar8 & 0xff00ff00) >> 8 | (uVar8 & 0xff00ff) << 8;
    *(uint *)(lVar12 + 0x104) = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar7 = (uint *)(lVar12 + 0x104);
  puVar5 = (uint *)((long)puVar5 + (ulong)uVar9);
  uVar9 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
  *puVar7 = uVar9 >> 0x10 | uVar9 << 0x10;
  uVar9 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
  *puVar7 = uVar9 >> 0x10 | uVar9 << 0x10;
  uVar9 = (puVar5[2] & 0xff00ff00) >> 8 | (puVar5[2] & 0xff00ff) << 8;
  *puVar7 = uVar9 >> 0x10 | uVar9 << 0x10;
  uVar9 = (puVar5[3] & 0xff00ff00) >> 8 | (puVar5[3] & 0xff00ff) << 8;
  *puVar7 = uVar9 >> 0x10 | uVar9 << 0x10;
  return iVar2;
}


