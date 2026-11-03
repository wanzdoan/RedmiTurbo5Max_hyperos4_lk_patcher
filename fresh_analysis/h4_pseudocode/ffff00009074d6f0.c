
undefined8
FUN_ffff00009074d6f0(int param_1,undefined8 param_2,long param_3,uint param_4,uint param_5)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long *plVar4;
  long lVar5;
  uint uVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  int iVar10;
  uint uVar11;
  
  uVar2 = *(uint *)(param_3 + 0x30);
  if (uVar2 < 8) {
    iVar10 = *(int *)(&DAT_ffff000090803fd0 + (long)(int)uVar2 * 4);
  }
  else {
    iVar10 = 0;
  }
  uVar3 = *(int *)(param_3 + 0x34) - 8;
  if (uVar3 < 5) {
    if (uVar3 < 5) {
      iVar10 = *(int *)(&DAT_ffff00009080cfc4 + (long)(int)uVar3 * 4);
    }
    else {
      iVar10 = 0;
    }
  }
  uVar8 = param_4;
  if ((int)param_4 < 0) {
    uVar8 = param_4 + 1;
  }
  if (param_1 == 0x2d) {
    param_4 = (int)uVar8 >> 1;
  }
  uVar8 = param_4 & 3;
  if (-1 < (int)-param_4) {
    uVar8 = -(-param_4 & 3);
  }
  uVar9 = uVar8 + param_4;
  uVar1 = uVar9;
  if ((int)uVar9 < 0 != SCARRY4(uVar8,param_4)) {
    uVar1 = uVar9 + 1;
  }
  uVar6 = uVar9 / 3;
  uVar8 = uVar6 + 2;
  if ((uVar9 / 3 + 1 & 1) == 0) {
    uVar8 = uVar6 + 1;
  }
  uVar11 = 0;
  if (param_1 != 0x2b) {
    uVar11 = (uint)(param_1 != 0x2d);
  }
  lVar5 = 1;
  if (param_1 != 0x2b) {
    lVar5 = 2;
  }
  if (uVar9 != uVar6 + (uVar9 / 3) * 2) {
    uVar6 = uVar8;
  }
  lVar5 = lVar5 - (ulong)uVar11;
  plVar4 = (long *)(s_0123456789abcdef_ffff000090804c70 + (ulong)uVar11 * 8 + 0x10);
  do {
    lVar7 = *plVar4;
    *(uint *)(lVar7 + 0x6c) = *(uint *)(lVar7 + 0x6c) & 0xffff8000 | param_5 & 0x7fff;
    if ((*(int *)(param_3 + 0xec) == 0) || (*(int *)(param_3 + 0x10c) == 1)) {
      uVar8 = iVar10 * param_4;
      if (*(int *)(param_3 + 0xf0) != 0) {
        uVar8 = *(int *)(param_3 + 0x14c) + *(int *)(param_3 + 0x14c) * *(int *)(param_3 + 0x11c);
      }
    }
    else {
      uVar8 = uVar6;
      if (*(int *)(param_3 + 0x108) != 3) {
        uVar8 = (int)uVar1 >> 1;
      }
      uVar8 = uVar8 * iVar10;
    }
    *(uint *)(lVar7 + 0x3c) = *(uint *)(lVar7 + 0x3c) & 0xffff8000 | uVar8 & 0x7fff;
    if (*(int *)(param_3 + 0xf0) != 0) {
      uVar9 = 5;
      goto code_r0xffff00009074d910;
    }
    if (uVar2 - 1 < 7) {
      uVar11 = *(uint *)(&DAT_ffff00009080cfa8 + (long)(int)(uVar2 - 1) * 4);
      if (uVar3 < 5) goto code_r0xffff00009074d8b8;
LAB_ffff00009074d890:
      uVar9 = uVar11;
      if ((uVar11 & 0xd) != 4) goto code_r0xffff00009074d910;
      goto code_r0xffff00009074d8fc;
    }
    uVar11 = 0;
    if (4 < uVar3) goto LAB_ffff00009074d890;
code_r0xffff00009074d8b8:
    uVar11 = 4;
    uVar9 = 0xd;
    switch(uVar3) {
    case 0:
      goto code_r0xffff00009074d8fc;
    case 1:
      uVar11 = 6;
      goto code_r0xffff00009074d8fc;
    case 2:
      uVar11 = 0xe;
code_r0xffff00009074d8fc:
      *(uint *)(lVar7 + 0x3c) = *(uint *)(lVar7 + 0x3c) | 0x100000;
      uVar9 = uVar11;
      break;
    case 3:
      break;
    case 4:
      uVar9 = 0xf;
      break;
    default:
      uVar9 = 0;
    }
code_r0xffff00009074d910:
    *(uint *)(lVar7 + 0x3c) = *(uint *)(lVar7 + 0x3c) & 0xfff0ffff | uVar9 << 0x10;
    if ((*(int *)(param_3 + 0xf8) != 0) && (*(int *)(param_3 + 0x21c) == 0)) {
      *(uint *)(lVar7 + 0x3c) =
           *(uint *)(lVar7 + 0x3c) & 0xffc00000 |
           *(uint *)(lVar7 + 0x3c) & 0x1fffff | (*(uint *)(param_3 + 0x250) & 1) << 0x15;
      *(uint *)(lVar7 + 0x3c) =
           *(uint *)(lVar7 + 0x3c) & 0xff000000 |
           *(uint *)(lVar7 + 0x3c) & 0x7fffff | (*(uint *)(param_3 + 0x244) & 1) << 0x17;
      *(uint *)(lVar7 + 0x3c) =
           *(uint *)(lVar7 + 0x3c) & 0x3ffffff | *(int *)(param_3 + 0x248) << 0x1a;
    }
    uVar9 = param_4;
    if (*(int *)(param_3 + 0xf0) != 0) {
      uVar9 = (uVar8 + 2) / 3;
    }
    lVar5 = lVar5 + -1;
    *(uint *)(lVar7 + 0x2c) = param_5 * 0x10000 + uVar9;
    plVar4 = plVar4 + 1;
    if (lVar5 == 0) {
      return 0;
    }
  } while( true );
}


