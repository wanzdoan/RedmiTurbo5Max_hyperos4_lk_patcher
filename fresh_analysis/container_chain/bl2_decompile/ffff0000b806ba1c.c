
undefined8 FUN_ffff0000b806ba1c(undefined1 *param_1,uint param_2)

{
  undefined1 uVar1;
  int iVar2;
  undefined8 uVar3;
  ulong uVar4;
  undefined1 *puVar5;
  
  iVar2 = FUN_ffff0000b8061884(param_2);
  if (iVar2 == 0) {
    uVar3 = 0x101026;
  }
  else {
    FUN_ffff0000b8069a2c(param_1,&DAT_ffff0000b81b8ec0,param_2);
    uVar3 = 0;
    if ((param_1 != (undefined1 *)0x0) && (1 < param_2)) {
      uVar4 = (ulong)(param_2 >> 1);
      puVar5 = param_1;
      do {
        param_2 = param_2 - 1;
        uVar4 = uVar4 - 1;
        uVar1 = *puVar5;
        *puVar5 = param_1[param_2];
        param_1[param_2] = uVar1;
        puVar5 = puVar5 + 1;
      } while (uVar4 != 0);
      uVar3 = 0;
    }
  }
  return uVar3;
}


