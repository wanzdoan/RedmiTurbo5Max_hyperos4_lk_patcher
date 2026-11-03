// File offset entry: 0xa10ac

void FUN_020a0fac(undefined1 *param_1,uint param_2)

{
  undefined1 uVar1;
  ulong uVar2;
  undefined1 *puVar3;
  
  if ((param_1 != (undefined1 *)0x0) && (1 < param_2)) {
    uVar2 = (ulong)(param_2 >> 1);
    puVar3 = param_1;
    do {
      param_2 = param_2 - 1;
      uVar2 = uVar2 - 1;
      uVar1 = *puVar3;
      *puVar3 = param_1[param_2];
      param_1[param_2] = uVar1;
      puVar3 = puVar3 + 1;
    } while (uVar2 != 0);
  }
  return;
}


