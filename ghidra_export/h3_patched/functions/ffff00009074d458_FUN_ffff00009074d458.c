// ffff00009074d458  size=80  FUN_ffff00009074d458

undefined8
FUN_ffff00009074d458(int param_1,undefined8 param_2,long param_3,uint param_4,uint param_5)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  undefined8 uVar4;
  long *plVar5;
  long lVar6;
  uint uVar7;
  long lVar8;
  uint uVar9;
  uint uVar10;
  int iVar11;
  uint uVar12;
  
  uVar2 = *(uint *)(param_3 + 0x30);
  if (uVar2 < 8) {
    iVar11 = *(int *)(&DAT_ffff000090803da8 + (long)(int)uVar2 * 4);
  }
  else {
    iVar11 = 0;
  }
  uVar3 = *(int *)(param_3 + 0x34) - 8;
  if (uVar3 < 5) {
    if (uVar3 < 5) {
      uVar4 = FUN_ffff00009074d4a8();
      return uVar4;
    }
    iVar11 = 0;
  }
  uVar9 = param_4;
  if ((int)param_4 < 0) {
    uVar9 = param_4 + 1;
  }
  if (param_1 == 0x2d) {
    param_4 = (int)uVar9 >> 1;
  }
  uVar9 = param_4 & 3;
  if (-1 < (int)-param_4) {
    uVar9 = -(-param_4 & 3);
  }
  uVar10 = uVar9 + param_4;
  uVar1 = uVar10;
  if ((int)uVar10 < 0 != SCARRY4(uVar9,param_4)) {
    uVar1 = uVar10 + 1;
  }
  uVar7 = uVar10 / 3;
  uVar9 = uVar7 + 2;
  if ((uVar10 / 3 + 1 & 1) == 0) {
    uVar9 = uVar7 + 1;
  }
  uVar12 = 0;
  if (param_1 != 0x2b) {
    uVar12 = (uint)(param_1 != 0x2d);
  }
  lVar6 = 1;
  if (param_1 != 0x2b) {
    lVar6 = 2;
  }
  if (uVar10 != uVar7 + (uVar10 / 3) * 2) {
    uVar7 = uVar9;
  }
  lVar6 = lVar6 - (ulong)uVar12;
  plVar5 = (long *)(s_0123456789abcdef_ffff000090804a48 + (ulong)uVar12 * 8 + 0x10);
  do {
    lVar8 = *plVar5;
    *(uint *)(lVar8 + 0x6c) = *(uint *)(lVar8 + 0x6c) & 0xffff8000 | param_5 & 0x7fff;
    if ((*(int *)(param_3 + 0xec) == 0) || (*(int *)(param_3 + 0x10c) == 1)) {
      uVar9 = iVar11 * param_4;
      if (*(int *)(param_3 + 0xf0) != 0) {
        uVar9 = *(int *)(param_3 + 0x14c) + *(int *)(param_3 + 0x14c) * *(int *)(param_3 + 0x11c);
      }
    }
    else {
      uVar9 = uVar7;
      if (*(int *)(param_3 + 0x108) != 3) {
        uVar9 = (int)uVar1 >> 1;
      }
      uVar9 = uVar9 * iVar11;
    }
    *(uint *)(lVar8 + 0x3c) = *(uint *)(lVar8 + 0x3c) & 0xffff8000 | uVar9 & 0x7fff;
    if (*(int *)(param_3 + 0xf0) != 0) {
      uVar10 = 5;
      goto code_r0xffff00009074d678;
    }
    if (uVar2 - 1 < 7) {
      uVar12 = *(uint *)(&DAT_ffff00009080cd80 + (long)(int)(uVar2 - 1) * 4);
      if (uVar3 < 5) goto code_r0xffff00009074d620;
LAB_ffff00009074d5f8:
      uVar10 = uVar12;
      if ((uVar12 & 0xd) != 4) goto code_r0xffff00009074d678;
      goto code_r0xffff00009074d664;
    }
    uVar12 = 0;
    if (4 < uVar3) goto LAB_ffff00009074d5f8;
code_r0xffff00009074d620:
    uVar12 = 4;
    uVar10 = 0xd;
    switch(uVar3) {
    case 0:
      goto code_r0xffff00009074d664;
    case 1:
      uVar12 = 6;
      goto code_r0xffff00009074d664;
    case 2:
      uVar12 = 0xe;
code_r0xffff00009074d664:
      *(uint *)(lVar8 + 0x3c) = *(uint *)(lVar8 + 0x3c) | 0x100000;
      uVar10 = uVar12;
      break;
    case 3:
      break;
    case 4:
      uVar10 = 0xf;
      break;
    default:
      uVar10 = 0;
    }
code_r0xffff00009074d678:
    *(uint *)(lVar8 + 0x3c) = *(uint *)(lVar8 + 0x3c) & 0xfff0ffff | uVar10 << 0x10;
    if ((*(int *)(param_3 + 0xf8) != 0) && (*(int *)(param_3 + 0x21c) == 0)) {
      *(uint *)(lVar8 + 0x3c) =
           *(uint *)(lVar8 + 0x3c) & 0xffc00000 |
           *(uint *)(lVar8 + 0x3c) & 0x1fffff | (*(uint *)(param_3 + 0x250) & 1) << 0x15;
      *(uint *)(lVar8 + 0x3c) =
           *(uint *)(lVar8 + 0x3c) & 0xff000000 |
           *(uint *)(lVar8 + 0x3c) & 0x7fffff | (*(uint *)(param_3 + 0x244) & 1) << 0x17;
      *(uint *)(lVar8 + 0x3c) =
           *(uint *)(lVar8 + 0x3c) & 0x3ffffff | *(int *)(param_3 + 0x248) << 0x1a;
    }
    uVar10 = param_4;
    if (*(int *)(param_3 + 0xf0) != 0) {
      uVar10 = (uVar9 + 2) / 3;
    }
    lVar6 = lVar6 + -1;
    *(uint *)(lVar8 + 0x2c) = param_5 * 0x10000 + uVar10;
    plVar5 = plVar5 + 1;
    if (lVar6 == 0) {
      return 0;
    }
  } while( true );
}


