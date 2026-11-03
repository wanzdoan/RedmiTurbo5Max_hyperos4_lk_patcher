// File offset entry: 0xa95ac

/* WARNING: Possible PIC construction at 0x020a9a78: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a9c4c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a9778: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a9c50) */
/* WARNING: Removing unreachable block (ram,0x020a9a7c) */
/* WARNING: Removing unreachable block (ram,0x020a977c) */
/* WARNING: Removing unreachable block (ram,0x020a9688) */
/* WARNING: Removing unreachable block (ram,0x020a9514) */
/* WARNING: Type propagation algorithm not settling */

ulong FUN_020a94ac(long *param_1)

{
  uint uVar1;
  undefined1 in_ZR;
  int iVar2;
  ulong uVar3;
  long *plVar4;
  ulong uVar5;
  uint *puVar6;
  long extraout_x8;
  uint *puVar7;
  long lVar8;
  uint uVar9;
  uint uVar10;
  uint extraout_w10;
  long extraout_x10;
  long extraout_x11;
  undefined4 extraout_w13;
  undefined8 uVar11;
  int iVar12;
  long lVar13;
  undefined1 auVar14 [16];
  uint uStack_194;
  undefined4 uStack_190;
  undefined4 uStack_18c;
  undefined4 uStack_188;
  uint uStack_184;
  undefined8 uStack_180;
  undefined8 uStack_178;
  undefined8 uStack_170;
  undefined8 uStack_168;
  undefined8 uStack_160;
  undefined8 uStack_158;
  undefined8 uStack_150;
  undefined8 uStack_148;
  undefined8 uStack_118;
  undefined8 uStack_110;
  long lStack_108;
  undefined1 *puStack_d0;
  undefined8 uStack_c8;
  uint auStack_68 [2];
  undefined4 uStack_60;
  uint uStack_5c;
  long lStack_58;
  
  puVar6 = (uint *)0x0;
  lStack_58 = lRam0000000002100880;
  if (param_1[2] == 0) {
LAB_020a982c:
    iVar2 = 0;
  }
  else {
    in_ZR = 1;
    if ((param_1[2] & 0xfU) == 0) {
      in_ZR = *(byte *)((long)param_1 + 0x41) == 1;
      if (*(byte *)((long)param_1 + 0x41) < 2) {
        uVar9 = *(uint *)((long)param_1 + 0x44);
        in_ZR = uVar9 == 2;
        if (uVar9 < 3) {
          uVar11 = *(undefined8 *)((long)(int)uVar9 * 8 + 0x20e4390);
          uStack_5c = 0;
          auStack_68[1] = 0;
          uStack_60 = 0;
          auStack_68[0] = 0;
          FUN_02091664(&uStack_5c,0,4);
          FUN_02091664(&uStack_60,0,4);
          FUN_02091664(auStack_68 + 1,0,4);
          FUN_02091664(auStack_68,0,4);
          uVar3 = func_0x020a928c(param_1[1]);
          uVar5 = func_0x020a928c(param_1[5]);
          uVar9 = 0x10000000;
          if (*(char *)((long)param_1 + 0x41) != '\x01') {
            uVar9 = 0x18000000;
          }
          uVar10 = uStack_5c & 0x7800000 | *(int *)((long)param_1 + 0x44) << 0x1e;
          lVar13 = *param_1;
          if (param_1[7] == 0) {
            uStack_5c = uVar10 | uStack_5c & 0x7fb0 | 0x8000 | uVar9 | 0x80040 |
                        *(byte *)(param_1 + 8) & 0xf;
          }
          else {
            uStack_5c = uVar10 | uStack_5c & 0x7fbf | 0x8000 | uVar9 | 0x80040;
            func_0x020a9298(param_1[7],uVar11);
            uStack_5c = uStack_5c | 0x80;
          }
          uVar3 = uVar3 >> 0x20 & 0xf;
          uVar9 = *(int *)(lVar13 + 0x54) + 1;
          uVar10 = uVar9 & 0xffff;
          if ((uVar9 & 0xfffe) == 0) {
            uVar10 = 1;
          }
          uStack_60 = uStack_60 & 0xf9ff0000 | 0x2000000 | uVar10;
          if ((uVar3 != 0) || (((param_1[2] | uVar5) & 0xf00000000) != 0)) {
            uStack_60 = uStack_60 | 0x400000;
            auStack_68[0] = auStack_68[0] | 0x20;
          }
          func_0x020a9298(param_1[1]);
          func_0x020a9298(param_1[5],param_1[2]);
          do {
            uVar10 = *(uint *)(*param_1 + 0x100);
            uVar9 = uVar10 & 0xff;
            in_ZR = uVar9 == 0x15;
          } while (uVar9 < 0x15);
          func_0x020aacbc();
          if ((extraout_w10 >> 6 & 1) == 0) {
            iVar2 = 7;
          }
          else {
            iVar2 = 8;
            *(uint *)(extraout_x8 + 0x104) = auStack_68[0];
          }
          func_0x020aac98();
          if ((char)uStack_60._2_1_ < '\0') {
            iVar2 = iVar2 + 1;
            *(undefined4 *)(*param_1 + 0x104) = 0;
          }
          if ((uStack_5c >> 6 & 1) != 0) {
            puVar6 = (uint *)param_1[3];
            lVar13 = *param_1;
            iVar2 = iVar2 + 4;
            uVar9 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
            *(uint *)(lVar13 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
            uVar9 = (puVar6[1] & 0xff00ff00) >> 8 | (puVar6[1] & 0xff00ff) << 8;
            *(uint *)(lVar13 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
            uVar9 = (puVar6[2] & 0xff00ff00) >> 8 | (puVar6[2] & 0xff00ff) << 8;
            *(uint *)(lVar13 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
            uVar9 = (puVar6[3] & 0xff00ff00) >> 8 | (puVar6[3] & 0xff00ff) << 8;
            *(uint *)(lVar13 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
          }
          if ((uStack_5c >> 7 & 1) != 0) {
            auVar14._8_8_ = param_1[7];
            auVar14._0_8_ = *param_1;
            puVar6 = &uStack_5c;
            goto code_r0x020a9d64;
          }
          if (((uStack_60._2_1_ >> 6 & 1) != 0) && (((byte)auStack_68[0] >> 5 & 1) != 0)) {
            lVar13 = param_1[2];
            iVar2 = iVar2 + 7;
            *(int *)(*param_1 + 0x104) = (int)uVar3;
            *(uint *)(*param_1 + 0x104) = *(uint *)((long)param_1 + 0x14) & 0xf;
            *(uint *)(*param_1 + 0x104) = (uint)(uVar5 >> 0x20) & 0xf;
            *(uint *)(*param_1 + 0x104) = (uint)(uVar5 + lVar13 + -1 >> 0x20) & 0xf;
            *(undefined4 *)(*param_1 + 0x104) = 0;
            *(undefined4 *)(*param_1 + 0x104) = 0;
            *(undefined4 *)(*param_1 + 0x104) = 0;
          }
          *(uint *)(*param_1 + 0x100) = (uVar10 & 0xff) - iVar2;
          puVar6 = (uint *)(ulong)(ushort)uStack_60;
          iVar2 = func_0x020a9e2c(*param_1,0);
          if (iVar2 == 0) {
            func_0x020a92a4(param_1[5],param_1[2]);
            goto LAB_020a982c;
          }
        }
        else {
          iVar2 = 0x726e;
        }
      }
      else {
        iVar2 = 0x7245;
      }
    }
    else {
      iVar2 = 0x724f;
    }
  }
  uVar3 = func_0x020aab34(iVar2);
  if ((bool)in_ZR) {
    return uVar3;
  }
  plVar4 = (long *)FUN_02091090();
  uStack_c8 = 0x20a985c;
  lStack_108 = lRam0000000002100880;
  uStack_150 = 0;
  uStack_148 = 0;
  uStack_160 = 0;
  uStack_158 = 0;
  uStack_170 = 0;
  uStack_168 = 0;
  uStack_180 = 0;
  uStack_178 = 0;
  puStack_d0 = &stack0xffffffffffffffb0;
  if (*(uint *)((long)plVar4 + 0x44) < 3) {
    puVar7 = (uint *)plVar4[6];
    if (puVar7 == (uint *)0x0) {
      uVar3 = 0x726f;
    }
    else {
      uVar11 = *(undefined8 *)((long)(int)*(uint *)((long)plVar4 + 0x44) * 8 + 0x20e4390);
      if ((uint *)0xf < puVar7) {
        puVar7 = (uint *)0x10;
      }
      uStack_188 = 0;
      uStack_184 = 0;
      uStack_190 = 0;
      uStack_18c = 0;
      FUN_02091664(&uStack_184,0,4);
      FUN_02091664(&uStack_188,0,4);
      FUN_02091664(&uStack_18c,0,4);
      FUN_02091664(&uStack_190,0,4);
      uVar3 = func_0x020a928c(&uStack_180);
      uVar5 = func_0x020a928c((ulong)&uStack_180 | 0x10);
      lVar13 = *plVar4;
      uVar9 = *(int *)((long)plVar4 + 0x44) << 0x1e;
      if (plVar4[7] == 0) {
        uStack_184 = uStack_184 & 0x7807ff0 | uVar9 | 0x100a8000 | *(byte *)(plVar4 + 8) & 0xf;
      }
      else {
        uStack_184 = uStack_184 & 0x7807fff | uVar9 | 0x100a8000;
        func_0x020a9298(plVar4[7],uVar11);
        uStack_184 = uStack_184 | 0x80;
      }
      uVar9 = *(int *)(lVar13 + 0x54) + 1;
      uVar10 = uVar9 & 0xffff;
      if ((uVar9 & 0xfffe) == 0) {
        uVar10 = 1;
      }
      uStack_188 = uVar10 | uStack_188 & 0xf8ff0000 | 0x3000000;
      if ((uVar5 & 0xf00000000) != 0 || (uVar3 >> 0x20 & 0xf) != 0) {
        func_0x020aac60();
      }
      func_0x020a9298(&uStack_180,0x40);
      lVar8 = *plVar4;
      do {
        uVar9 = *(uint *)(lVar8 + 0x100);
      } while ((uVar9 & 0xf0) == 0);
      *(uint *)(lVar8 + 0x104) = uStack_184;
      *(uint *)(lVar8 + 0x104) = uStack_188;
      *(undefined4 *)(lVar8 + 0x104) = uStack_18c;
      if ((uStack_188._2_1_ >> 6 & 1) == 0) {
        iVar2 = 7;
      }
      else {
        iVar2 = 8;
        *(undefined4 *)(lVar8 + 0x104) = uStack_190;
      }
      *(int *)(lVar8 + 0x104) = (int)uVar3;
      *(undefined4 *)(lVar8 + 0x104) = 0x10;
      *(int *)(lVar8 + 0x104) = (int)uVar5;
      *(int *)(lVar8 + 0x104) = (int)uVar5 + 0xf;
      if ((char)uStack_184 < '\0') {
        auVar14._8_8_ = plVar4[7];
        auVar14._0_8_ = *plVar4;
        puVar6 = &uStack_184;
        goto code_r0x020a9d64;
      }
      if (((uStack_188._2_1_ >> 6 & 1) != 0) && (((byte)uStack_190 >> 5 & 1) != 0)) {
        iVar2 = iVar2 + 7;
        *(uint *)(*plVar4 + 0x104) = (uint)(uVar3 >> 0x20) & 0xf;
        *(undefined4 *)(*plVar4 + 0x104) = 0;
        func_0x020aab54();
      }
      *(uint *)(*plVar4 + 0x100) = (uVar9 & 0xff) - iVar2;
      puVar6 = (uint *)(ulong)(ushort)uStack_188;
      uStack_194 = func_0x020a9e2c(*plVar4,0);
      if (uStack_194 == 0) {
        FUN_02091664(&uStack_184,0,4);
        FUN_02091664(&uStack_188,0,4);
        FUN_02091664(&uStack_18c,0,4);
        FUN_02091664(&uStack_190,0,4);
        uVar3 = func_0x020a928c(plVar4[1]);
        iVar2 = func_0x020a928c(plVar4[5]);
        uVar9 = *(int *)((long)plVar4 + 0x44) << 0x1e;
        if (plVar4[7] == 0) {
          uStack_184 = uStack_184 & 0x7807ff0 | uVar9 | 0x100b0000 | *(byte *)(plVar4 + 8) & 0xf;
        }
        else {
          uStack_184 = uStack_184 & 0x7807fff | uVar9 | 0x100b0000;
          func_0x020a9298(plVar4[7],uVar11);
          uStack_184 = uStack_184 | 0x80;
        }
        uVar9 = *(int *)(lVar13 + 0x54) + 1;
        uVar10 = uVar9 & 0xffff;
        if ((uVar9 & 0xfffe) == 0) {
          uVar10 = 1;
        }
        uStack_188 = uVar10 | uStack_188 & 0xf8ff0000 | 0x3000000;
        if ((plVar4[2] & 0xf00000000U) != 0 || (uVar3 >> 0x20 & 0xf) != 0) {
          func_0x020aac60();
        }
        func_0x020a9298(plVar4[1]);
        lVar13 = *plVar4;
        do {
          uVar9 = *(uint *)(lVar13 + 0x100);
        } while ((uVar9 & 0xf0) == 0);
        lVar8 = plVar4[2];
        *(uint *)(lVar13 + 0x104) = uStack_184;
        *(uint *)(lVar13 + 0x104) = uStack_188;
        *(undefined4 *)(lVar13 + 0x104) = uStack_18c;
        if ((uStack_188._2_1_ >> 6 & 1) == 0) {
          iVar12 = 7;
        }
        else {
          iVar12 = 8;
          *(undefined4 *)(lVar13 + 0x104) = uStack_190;
        }
        *(int *)(lVar13 + 0x104) = (int)uVar3;
        *(int *)(lVar13 + 0x104) = (int)lVar8;
        *(int *)(lVar13 + 0x104) = iVar2;
        *(int *)(lVar13 + 0x104) = iVar2 + (int)puVar7 + -1;
        if ((char)uStack_184 < '\0') {
          auVar14._8_8_ = plVar4[7];
          auVar14._0_8_ = *plVar4;
          puVar6 = &uStack_184;
          goto code_r0x020a9d64;
        }
        if (((uStack_188._2_1_ >> 6 & 1) != 0) && (((byte)uStack_190 >> 5 & 1) != 0)) {
          iVar12 = iVar12 + 7;
          *(uint *)(*plVar4 + 0x104) = (uint)(uVar3 >> 0x20) & 0xf;
          *(uint *)(*plVar4 + 0x104) = *(uint *)((long)plVar4 + 0x14) & 0xf;
          func_0x020aab54();
        }
        *(uint *)(*plVar4 + 0x100) = (uVar9 & 0xff) - iVar12;
        puVar6 = (uint *)(ulong)(ushort)uStack_188;
        uStack_194 = func_0x020a9e2c(*plVar4,0);
        if (uStack_194 == 0) {
          lVar13 = plVar4[5];
          uStack_110 = 0;
          uStack_118 = 0;
          lVar8 = 0xc;
          while (lVar8 != -4) {
            lVar13 = func_0x020aabe8();
            *(undefined4 *)(extraout_x11 + extraout_x10) = extraout_w13;
            lVar8 = extraout_x10 + -4;
          }
          FUN_020915ac(lVar13,&uStack_118);
          uVar3 = 0;
          puVar6 = puVar7;
          goto code_r0x020a9cc0;
        }
      }
      uVar3 = (ulong)uStack_194;
    }
  }
  else {
    uVar3 = 0x726e;
  }
code_r0x020a9cc0:
  if (lRam0000000002100880 == lStack_108) {
    return uVar3;
  }
  auVar14 = FUN_02091090(uVar3);
code_r0x020a9d64:
  puVar7 = auVar14._8_8_;
  lVar13 = auVar14._0_8_;
  uVar9 = *puVar6 >> 0x1e;
  if ((uVar9 == 0) || ((*puVar6 & 0x780000) != 0x80000)) {
    uVar9 = 0;
    uVar3 = 4;
  }
  else {
    if (uVar9 == 2) {
      uVar10 = 0;
      *(undefined4 *)(lVar13 + 0x104) = 0;
      *(undefined4 *)(lVar13 + 0x104) = 0;
    }
    else {
      uVar9 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
      *(uint *)(lVar13 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
      uVar9 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
      *(uint *)(lVar13 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
      uVar10 = 8;
    }
    uVar9 = uVar10 + 8;
    uVar3 = 8;
    uVar1 = (*(uint *)((long)puVar7 + (ulong)uVar10) & 0xff00ff00) >> 8 |
            (*(uint *)((long)puVar7 + (ulong)uVar10) & 0xff00ff) << 8;
    *(uint *)(lVar13 + 0x104) = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar10 = *(uint *)(((ulong)uVar10 | 4) + (long)puVar7);
    uVar10 = (uVar10 & 0xff00ff00) >> 8 | (uVar10 & 0xff00ff) << 8;
    *(uint *)(lVar13 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
  }
  puVar6 = (uint *)(lVar13 + 0x104);
  puVar7 = (uint *)((long)puVar7 + (ulong)uVar9);
  uVar9 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
  *puVar6 = uVar9 >> 0x10 | uVar9 << 0x10;
  uVar9 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
  *puVar6 = uVar9 >> 0x10 | uVar9 << 0x10;
  uVar9 = (puVar7[2] & 0xff00ff00) >> 8 | (puVar7[2] & 0xff00ff) << 8;
  *puVar6 = uVar9 >> 0x10 | uVar9 << 0x10;
  uVar9 = (puVar7[3] & 0xff00ff00) >> 8 | (puVar7[3] & 0xff00ff) << 8;
  *puVar6 = uVar9 >> 0x10 | uVar9 << 0x10;
  return uVar3;
}


