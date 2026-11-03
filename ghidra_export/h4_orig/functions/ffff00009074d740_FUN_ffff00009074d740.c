// ffff00009074d740  size=596  FUN_ffff00009074d740

undefined8
FUN_ffff00009074d740(int param_1,undefined8 param_2,long param_3,uint param_4,uint param_5)

{
  uint uVar1;
  long *plVar2;
  undefined8 uVar3;
  long lVar4;
  uint uVar5;
  long lVar6;
  uint uVar7;
  uint uVar8;
  uint in_w8;
  undefined4 in_register_00004044;
  int in_w9;
  int in_w13;
  uint uVar9;
  
  uVar7 = param_4;
  if ((int)param_4 < 0) {
    uVar7 = param_4 + 1;
  }
  if (param_1 == 0x2d) {
    param_4 = (int)uVar7 >> 1;
  }
  uVar7 = param_4 & 3;
  if (-1 < (int)-param_4) {
    uVar7 = -(-param_4 & 3);
  }
  uVar8 = uVar7 + param_4;
  uVar1 = uVar8;
  if ((int)uVar8 < 0 != SCARRY4(uVar7,param_4)) {
    uVar1 = uVar8 + 1;
  }
  uVar5 = uVar8 / 3;
  uVar7 = uVar5 + 2;
  if ((uVar8 / 3 + 1 & 1) == 0) {
    uVar7 = uVar5 + 1;
  }
  uVar9 = 0;
  if (param_1 != 0x2b) {
    uVar9 = (uint)(param_1 != 0x2d);
  }
  lVar4 = 1;
  if (param_1 != 0x2b) {
    lVar4 = 2;
  }
  if (uVar8 != uVar5 + (uVar8 / 3) * 2) {
    uVar5 = uVar7;
  }
  lVar4 = lVar4 - (ulong)uVar9;
  plVar2 = (long *)(s_0123456789abcdef_ffff000090804c70 + (ulong)uVar9 * 8 + 0x10);
  do {
    lVar6 = *plVar2;
    *(uint *)(lVar6 + 0x6c) = *(uint *)(lVar6 + 0x6c) & 0xffff8000 | param_5 & 0x7fff;
    if ((*(int *)(param_3 + 0xec) == 0) || (*(int *)(param_3 + 0x10c) == 1)) {
      uVar7 = in_w9 * param_4;
      if (*(int *)(param_3 + 0xf0) != 0) {
        uVar7 = *(int *)(param_3 + 0x14c) + *(int *)(param_3 + 0x14c) * *(int *)(param_3 + 0x11c);
      }
    }
    else {
      uVar7 = uVar5;
      if (*(int *)(param_3 + 0x108) != 3) {
        uVar7 = (int)uVar1 >> 1;
      }
      uVar7 = uVar7 * in_w9;
    }
    *(uint *)(lVar6 + 0x3c) = *(uint *)(lVar6 + 0x3c) & 0xffff8000 | uVar7 & 0x7fff;
    if (*(int *)(param_3 + 0xf0) == 0) {
      if (in_w13 - 1U < 7) {
        uVar8 = *(uint *)(&DAT_ffff00009080cfa8 + (long)(int)(in_w13 - 1U) * 4);
      }
      else {
        uVar8 = 0;
      }
      if (in_w8 < 5) {
        if (in_w8 < 5) {
                    /* WARNING: Could not recover jumptable at 0xffff00009074d8cc. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
          uVar3 = (*(code *)((ulong)(&switchD_ffff00009074d8cc::switchdataD_ffff00009080cf6e)
                                    [CONCAT44(in_register_00004044,in_w8)] * 4 + -0xffff6f8b2730))()
          ;
          return uVar3;
        }
        uVar8 = 0;
      }
      else if ((uVar8 & 0xd) == 4) {
        *(uint *)(lVar6 + 0x3c) = *(uint *)(lVar6 + 0x3c) | 0x100000;
      }
    }
    else {
      uVar8 = 5;
    }
    *(uint *)(lVar6 + 0x3c) = *(uint *)(lVar6 + 0x3c) & 0xfff0ffff | uVar8 << 0x10;
    if ((*(int *)(param_3 + 0xf8) != 0) && (*(int *)(param_3 + 0x21c) == 0)) {
      *(uint *)(lVar6 + 0x3c) =
           *(uint *)(lVar6 + 0x3c) & 0xffc00000 |
           *(uint *)(lVar6 + 0x3c) & 0x1fffff | (*(uint *)(param_3 + 0x250) & 1) << 0x15;
      *(uint *)(lVar6 + 0x3c) =
           *(uint *)(lVar6 + 0x3c) & 0xff000000 |
           *(uint *)(lVar6 + 0x3c) & 0x7fffff | (*(uint *)(param_3 + 0x244) & 1) << 0x17;
      *(uint *)(lVar6 + 0x3c) =
           *(uint *)(lVar6 + 0x3c) & 0x3ffffff | *(int *)(param_3 + 0x248) << 0x1a;
    }
    uVar8 = param_4;
    if (*(int *)(param_3 + 0xf0) != 0) {
      uVar8 = (uVar7 + 2) / 3;
    }
    lVar4 = lVar4 + -1;
    *(uint *)(lVar6 + 0x2c) = param_5 * 0x10000 + uVar8;
    plVar2 = plVar2 + 1;
  } while (lVar4 != 0);
  return 0;
}


