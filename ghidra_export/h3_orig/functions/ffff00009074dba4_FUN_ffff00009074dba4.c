// ffff00009074dba4  size=244  FUN_ffff00009074dba4

undefined8 FUN_ffff00009074dba4(int param_1,undefined8 param_2,int *param_3)

{
  uint in_w8;
  int in_w9;
  uint uVar1;
  long *plVar2;
  long lVar3;
  long lVar4;
  
  uVar1 = 0;
  if (param_1 != 0x2b) {
    uVar1 = (uint)(param_1 != 0x2d);
  }
  lVar3 = 1;
  if (param_1 != 0x2b) {
    lVar3 = 2;
  }
  lVar3 = lVar3 - (ulong)uVar1;
  plVar2 = (long *)(s_0123456789abcdef_ffff000090804a48 + (ulong)uVar1 * 8 + 0x10);
  do {
    lVar4 = *plVar2;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xfffffffc;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xffffffbf;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xffffff7f;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xffff0fff;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xfffeffff | (uint)(in_w9 == 0) << 0x10;
    *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) & 0xffffffc3 | in_w8;
    *(undefined4 *)(lVar4 + 0x48) = 0x3c;
    if ((*param_3 == 0) || (param_3[0x157] != 0)) {
      if (param_3[0x156] == 1) {
        *(uint *)(lVar4 + 0x38) = *(uint *)(lVar4 + 0x38) | 0x400;
      }
      uVar1 = *(uint *)(lVar4 + 0x38) | 0x200;
    }
    else {
      uVar1 = *(uint *)(lVar4 + 0x38) & 0xff0fffff | 0x100000;
    }
    lVar3 = lVar3 + -1;
    *(uint *)(lVar4 + 0x38) = uVar1;
    plVar2 = plVar2 + 1;
  } while (lVar3 != 0);
  return 0;
}


