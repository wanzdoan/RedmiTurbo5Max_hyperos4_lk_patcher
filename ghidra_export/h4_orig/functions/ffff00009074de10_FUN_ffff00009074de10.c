// ffff00009074de10  size=44  FUN_ffff00009074de10

undefined8 FUN_ffff00009074de10(int param_1,undefined8 param_2,int *param_3)

{
  int iVar1;
  undefined8 uVar2;
  uint uVar3;
  long *plVar4;
  long lVar5;
  long lVar6;
  
  iVar1 = param_3[0x3a];
  if (param_3[5] - 1U < 4) {
    uVar2 = FUN_ffff00009074de3c
                      (*(undefined4 *)(&DAT_ffff000090804d10 + (long)(int)(param_3[5] - 1U) * 4));
    return uVar2;
  }
  uVar3 = 0;
  if (param_1 != 0x2b) {
    uVar3 = (uint)(param_1 != 0x2d);
  }
  lVar5 = 1;
  if (param_1 != 0x2b) {
    lVar5 = 2;
  }
  lVar5 = lVar5 - (ulong)uVar3;
  plVar4 = (long *)(s_0123456789abcdef_ffff000090804c70 + (ulong)uVar3 * 8 + 0x10);
  do {
    lVar6 = *plVar4;
    *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) & 0xfffffffc;
    *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) & 0xffffffbf;
    *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) & 0xffffff7f;
    *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) & 0xffff0fff;
    *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) & 0xfffeffff | (uint)(iVar1 == 0) << 0x10;
    *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) & 0xffffffc3;
    *(undefined4 *)(lVar6 + 0x48) = 0x3c;
    if ((*param_3 == 0) || (param_3[0x157] != 0)) {
      if (param_3[0x156] == 1) {
        *(uint *)(lVar6 + 0x38) = *(uint *)(lVar6 + 0x38) | 0x400;
      }
      uVar3 = *(uint *)(lVar6 + 0x38) | 0x200;
    }
    else {
      uVar3 = *(uint *)(lVar6 + 0x38) & 0xff0fffff | 0x100000;
    }
    lVar5 = lVar5 + -1;
    *(uint *)(lVar6 + 0x38) = uVar3;
    plVar4 = plVar4 + 1;
  } while (lVar5 != 0);
  return 0;
}


