// File offset entry: 0xa3dd0

void FUN_020a3cd0(undefined1 *param_1,undefined8 param_2)

{
  int iVar1;
  undefined1 *puVar2;
  
  iVar1 = func_0x020a0e20();
  puVar2 = (undefined1 *)func_0x020a3c60(3,iVar1,param_2);
  if ((iVar1 != 0) && (puVar2 != (undefined1 *)0x0)) {
    for (; iVar1 != 0; iVar1 = iVar1 + -1) {
      *puVar2 = *param_1;
      puVar2 = puVar2 + 1;
      param_1 = param_1 + 1;
    }
    return;
  }
  return;
}


