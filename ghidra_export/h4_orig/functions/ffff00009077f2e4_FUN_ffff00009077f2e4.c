// ffff00009077f2e4  size=100  FUN_ffff00009077f2e4

void FUN_ffff00009077f2e4(ulong param_1,undefined8 param_2,undefined8 param_3,ulong param_4)

{
  long lVar1;
  ulong uVar2;
  undefined1 *in_x11;
  undefined1 *puVar3;
  ulong uVar4;
  ulong uVar5;
  
  if (param_1 < param_4) {
    uVar2 = param_4 - param_1;
    if (1 < uVar2) {
      uVar4 = uVar2 & 0xfffffffffffffffe;
      param_1 = param_1 + uVar4;
      puVar3 = in_x11 + 1;
      uVar5 = uVar4;
      do {
        *(undefined2 *)(puVar3 + -1) = 0;
        puVar3 = puVar3 + 2;
        uVar5 = uVar5 - 2;
      } while (uVar5 != 0);
      in_x11 = in_x11 + uVar4;
      if (uVar2 == uVar4) {
        return;
      }
    }
    lVar1 = param_4 - param_1;
    do {
      lVar1 = lVar1 + -1;
      *in_x11 = 0;
      in_x11 = in_x11 + 1;
    } while (lVar1 != 0);
  }
  return;
}


